package com.example.mdpings

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Monitor
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mdpings.AppSettingsScreen
import com.example.mdpings.core.presentation.util.ObserveAsEvents
import com.example.mdpings.core.presentation.util.toString
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.data.StoreSettings
import com.example.mdpings.vpings.presentation.AboutScreen
import com.example.mdpings.vpings.presentation.app_settings.AppSettingsScreen
import com.example.mdpings.vpings.presentation.app_settings.AppSettingsViewModel
import com.example.mdpings.vpings.presentation.components.DrawerContent
import com.example.mdpings.vpings.presentation.server_detail.ServerDetailScreen
import com.example.mdpings.vpings.presentation.server_detail.ServerDetailViewModel
import com.example.mdpings.vpings.presentation.server_list.ServerListScreen
import com.example.mdpings.vpings.presentation.server_list.ServerListViewModel
import com.example.mdpings.vpings.presentation.components.MDAppTopBar
import com.example.mdpings.vpings.presentation.user_login.LoginEvent
import com.example.mdpings.vpings.presentation.user_login.LoginScreen
import com.example.mdpings.vpings.presentation.user_login.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MDPingsTheme {

                // TopAppBar scrollBehavior
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
                    state = rememberTopAppBarState()
                )

                // Drawer
                val drawerState = rememberDrawerState(
                    initialValue = DrawerValue.Closed
                )
                val scope = rememberCoroutineScope()

                // States
                val loginViewModel = koinViewModel<LoginViewModel>()
                val loginState by loginViewModel.state.collectAsStateWithLifecycle()
                val serverListViewModel = koinViewModel<ServerListViewModel>()
                val serverListState by serverListViewModel.state.collectAsStateWithLifecycle()
                val serverDetailViewModel = koinViewModel<ServerDetailViewModel>()
                val serverDetailState by serverDetailViewModel.state.collectAsStateWithLifecycle()
                val appSettingsViewModel = koinViewModel<AppSettingsViewModel>()
                val appSettingsState by appSettingsViewModel.state.collectAsStateWithLifecycle()

                // Error Handle
                val context = LocalContext.current

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

                // Nav && currentRoute -> topAppBarTitle
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val topAppBarTitle =
                    if (currentRoute == "com.example.mdpings.LoginScreen") "Login"
                    else if (currentRoute == "com.example.mdpings.AppSettingsScreen") "Settings"
                    else if (currentRoute == "com.example.mdpings.AboutScreen") "About"
                    else if (currentRoute == "com.example.mdpings.ServerDetailScreen") "${serverListState.selectedServer!!.host.countryCode} ${serverListState.selectedServer!!.name}"
                    else "MDPings"

                ModalNavigationDrawer(
                    gesturesEnabled = true,
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            DrawerContent(
                                modifier = Modifier.requiredWidth(280.dp),
                                navController = navController,
                                currentRoute = currentRoute,
                                drawerState = drawerState
                            )
                        }
                    }
                ) {
                    Scaffold(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = {
                            MDAppTopBar(
                                // TODO 这个topPadding可以自动判断不用hardcode的？
                                modifier = Modifier.wrapContentHeight(),
                                scrollBehavior = scrollBehavior,
                                title = topAppBarTitle,
                                isLoading = serverListState.isLoading,
                                onNavigationIconClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                },
                                onUserClick = {
                                    navController.navigate(
                                        route = LoginScreen
                                    )
                                }
                            )
                        }
                    ) { innerPadding ->

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
                            modifier = Modifier.padding(top = innerPadding.calculateTopPadding() - 4.dp)
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
                                        navController.navigate(ServerDetailScreen)
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
                            composable<AppSettingsScreen> {
                                AppSettingsScreen(
                                    state = appSettingsState,
                                    onAction = appSettingsViewModel::onAction
                                )
                            }
                            composable<AboutScreen> {
                                AboutScreen()
                            }
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

@Serializable
object AppSettingsScreen

@Serializable
object AboutScreen