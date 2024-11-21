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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

                // AppSettings json
//                val appSettings by context.dataStore.data.collectAsStateWithLifecycle(AppSettings())

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
                    if (currentRoute == Screen.Login.route) "Instances List"
                    else if (currentRoute == Screen.AppSettings.route) "Settings"
                    else if (currentRoute == Screen.About.route) "About"
                    else if (currentRoute == Screen.ServerDetail.route)
                        "${serverListState.selectedServer!!.host.countryCode} ${serverListState.selectedServer!!.name}"
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
                                    if (currentRoute != Screen.Login.route) {
                                        navController.navigate(Screen.Login.route)
                                    }
                                },
                                onAction = appSettingsViewModel::onAction,
                                appSettingsState = appSettingsState
                            )
                        }
                    ) { innerPadding ->

                        LaunchedEffect(Unit) {
                            val isSettingsNull = apiURL == null || apiTOKEN == null
                            navController.navigate(
                                if (isSettingsNull) Screen.Login.route else Screen.ServerList.route
                            ) {
                                popUpTo(Screen.Loading.route) {
                                    inclusive = true  // 这会移除 Loading 屏幕
                                }
                            }
                        }

                        NavHost(
                            navController = navController,
                            startDestination = Screen.Loading.route,
                            modifier = Modifier.padding(top = innerPadding.calculateTopPadding() - 4.dp)
                        ) {
                            // Loading screen
                            composable(Screen.Loading.route) {
                                LoadingScreen()
                            }

                            // Auth navigation graph
                            navigation(
                                startDestination = Screen.Login.route,
                                route = Screen.Auth.route
                            ) {
                                composable(Screen.Login.route) {
                                    LoginScreen(
                                        modifier = Modifier.padding(innerPadding),
                                        state = loginState,
                                        appSettingsState = appSettingsState,
                                        onAction = loginViewModel::onAction,
                                        onNavigateToServer = {
                                            navController.navigate(Screen.ServerList.route) {
                                                popUpTo(Screen.Auth.route) { inclusive = true }
                                            }
                                        },
                                        onLoad = appSettingsViewModel::onAction
                                    )
                                }
                            }

                            // Main navigation graph
                            navigation(
                                startDestination = Screen.ServerList.route,
                                route = Screen.Main.route
                            ) {
                                composable(Screen.ServerList.route) {
                                    ServerListScreen(
                                        state = serverListState,
                                        onAction = serverListViewModel::onAction,
                                        onNavigateToDetail = { serverId ->
                                            navController.navigate(Screen.ServerDetail.createRoute(
                                                serverId.toString()
                                            ))
                                        },
                                        onLoad = appSettingsViewModel::onAction,
                                        appSettingsState = appSettingsState,
                                    )
                                }

                                composable(
                                    route = Screen.ServerDetail.route,
                                    arguments = listOf(navArgument("serverId") { type = NavType.StringType })
                                ) { backStackEntry ->
                                    val serverId = backStackEntry.arguments?.getString("serverId")?.toInt()
                                    val selectedServer = serverListState.servers.first { it.id == serverId }
                                    ServerDetailScreen(
                                        state = serverDetailState,
                                        selectedServerUi = selectedServer,
                                        onAction = serverDetailViewModel::onAction,
                                        appSettingsState = appSettingsState,
                                    )
                                }
                            }

                            // Settings navigation graph
                            navigation(
                                startDestination = Screen.AppSettings.route,
                                route = Screen.Settings.route
                            ) {
                                composable(Screen.AppSettings.route) {
                                    AppSettingsScreen(
                                        state = appSettingsState,
                                        onAction = appSettingsViewModel::onAction,
                                        onNavigateToLogin = {
                                            navController.navigate(Screen.Login.route)
                                        }
                                    )
                                }

                                composable(Screen.About.route) {
                                    AboutScreen()
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Loading : Screen("loading")

    // Auth graph
    object Auth : Screen("auth")
    object Login : Screen("auth/login")

    // Main graph
    object Main : Screen("main")
    object ServerList : Screen("main/servers")
    object ServerDetail : Screen("main/servers/{serverId}") {
        fun createRoute(serverId: String) = "main/servers/$serverId"
    }

    // Settings graph
    object Settings : Screen("settings")
    object AppSettings : Screen("settings/app")
    object About : Screen("settings/about")
}

@Composable
private fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(8.dp))
        Text("Initializing MDPings...")
    }
}