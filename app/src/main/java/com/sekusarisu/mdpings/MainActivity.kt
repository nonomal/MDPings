package com.sekusarisu.mdpings

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sekusarisu.mdpings.core.presentation.util.ObserveAsEvents
import com.sekusarisu.mdpings.core.presentation.util.toString
import com.sekusarisu.mdpings.ui.theme.MDPingsTheme
import com.sekusarisu.mdpings.vpings.presentation.AboutScreen
import com.sekusarisu.mdpings.vpings.presentation.app_settings.AppSettingsScreen
import com.sekusarisu.mdpings.vpings.presentation.app_settings.AppSettingsViewModel
import com.sekusarisu.mdpings.vpings.presentation.components.DrawerContent
import com.sekusarisu.mdpings.vpings.presentation.server_detail.ServerDetailScreen
import com.sekusarisu.mdpings.vpings.presentation.server_detail.ServerDetailViewModel
import com.sekusarisu.mdpings.vpings.presentation.server_list.ServerListScreen
import com.sekusarisu.mdpings.vpings.presentation.server_list.ServerListViewModel
import com.sekusarisu.mdpings.vpings.presentation.components.MDAppTopBar
import com.sekusarisu.mdpings.vpings.presentation.user_login.LoginEvent
import com.sekusarisu.mdpings.vpings.presentation.user_login.LoginScreen
import com.sekusarisu.mdpings.vpings.presentation.user_login.LoginViewModel
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

                // AppSettings
                val apiURL = appSettingsViewModel.getApiURL()
                val apiTOKEN = appSettingsViewModel.getApiTOKEN()

                // Nav && currentRoute -> topAppBarTitle
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val topAppBarTitle =
                    if (currentRoute == "com.sekusarisu.mdpings.LoginScreen") "Login"
                    else if (currentRoute == "com.sekusarisu.mdpings.AppSettingsScreen") "Settings"
                    else if (currentRoute == "com.sekusarisu.mdpings.AboutScreen") "About"
                    else if (currentRoute == "com.sekusarisu.mdpings.ServerDetailScreen") "${serverListState.selectedServer!!.host.countryCode} ${serverListState.selectedServer!!.name}"
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
                                modifier = Modifier,
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

                        LaunchedEffect(apiURL, apiTOKEN) {
//                            delay(1000)
                            val isSettingsIsnull = apiURL == null || apiTOKEN == null
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
                                    appSettingsState = appSettingsState,
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
                                    },
                                    onLoad = appSettingsViewModel::onAction,
                                    appSettingsState = appSettingsState
                                )
                            }
                            composable<ServerDetailScreen> {
                                ServerDetailScreen(
                                    state = serverDetailState,
                                    selectedServerUi = serverListState.selectedServer!!,
                                    onAction = serverDetailViewModel::onAction,
                                    appSettingsState = appSettingsState,
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