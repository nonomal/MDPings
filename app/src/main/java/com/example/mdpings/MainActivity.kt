package com.example.mdpings

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mdpings.core.presentation.util.ObserveAsEvents
import com.example.mdpings.core.presentation.util.toString
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.data.StoreSettings
import com.example.mdpings.vpings.presentation.server_detail.ServerDetailScreen
import com.example.mdpings.vpings.presentation.server_detail.ServerDetailState
import com.example.mdpings.vpings.presentation.server_detail.ServerDetailViewModel
import com.example.mdpings.vpings.presentation.server_list.ServerListScreen
import com.example.mdpings.vpings.presentation.server_list.ServerListViewModel
import com.example.mdpings.vpings.presentation.user_login.LoginEvent
import com.example.mdpings.vpings.presentation.user_login.LoginScreen
import com.example.mdpings.vpings.presentation.user_login.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MDPingsTheme {

                Scaffold { innerPadding ->

                    val context = LocalContext.current

                    // Error Handle
                    val loginViewModel = koinViewModel<LoginViewModel>()
                    val loginState by loginViewModel.state.collectAsStateWithLifecycle()
                    val serverListViewModel = koinViewModel<ServerListViewModel>()
                    val serverListState by serverListViewModel.state.collectAsStateWithLifecycle()
                    val serverDetailViewModel = koinViewModel<ServerDetailViewModel>()
                    val serverDetailState by serverDetailViewModel.state.collectAsStateWithLifecycle()

                    ObserveAsEvents(events = loginViewModel.events) { event ->
                        when (event) {
                            is LoginEvent.Error -> {
                                Toast.makeText(
                                    context,
                                    event.error.toString(context),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    // DataStore
                    val dataStore = StoreSettings(context)
                    val stateApi by dataStore.getApi.collectAsState(initial = "")
                    val stateToken by dataStore.getToken.collectAsState(initial = "")

                    // Nav
                    val navController = rememberNavController()

                    LaunchedEffect(stateApi, stateToken) {
                        delay(1000)
                        val isSettingsIsnull = stateApi!!.isEmpty() || stateToken!!.isEmpty()
                        navController.navigate(if (isSettingsIsnull) LoginScreen else ServerListScreen) {
                            popUpTo(0)
                        }
                    }

                    NavHost(
                        navController = navController,
                        startDestination = LoadingScreen,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<LoadingScreen> {
                            Column(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                                Spacer(Modifier.height(8.dp))
                                Text("Initializing MDPings...")
                            }
                        }
                        composable<LoginScreen> {
                            LoginScreen(
                                modifier = Modifier.padding(innerPadding),
                                state = loginState,
                                onAction = loginViewModel::onAction,
                                onNavigateToServer = {
                                    navController.navigate(
                                        route = ServerListScreen
                                    )
                                }
                            )
                        }
                        composable<ServerListScreen> {
                            ServerListScreen(
                                state = serverListState,
                                onAction = serverListViewModel::onAction,
                                onNavigateToDetail = {
                                    navController.navigate(
                                        route = ServerDetailScreen
                                    )
                                }
                            )
                        }
                        composable<ServerDetailScreen> {
                            ServerDetailScreen(
                                state = serverDetailState,
                                selectedServerUi = serverListState.selectedServer!!,
                                onAction = serverDetailViewModel::onAction
                            )
                        }
                    }

                }
            }
        }
    }
}

@Serializable
object LoadingScreen

@Serializable
object LoginScreen

@Serializable
object ServerListScreen

@Serializable
object ServerDetailScreen