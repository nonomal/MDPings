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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mdpings.core.presentation.util.ObserveAsEvents
import com.example.mdpings.core.presentation.util.toString
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.data.StoreSettings
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

                // Nav
                val navController = rememberNavController()

                ModalNavigationDrawer(
                    gesturesEnabled = true,
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            DrawerContent(
                                modifier = Modifier.requiredWidth(280.dp),
                                navController = navController,
                                drawerState = drawerState
                            )
                        }
                    }
                ) {
                    Scaffold(
                        modifier = Modifier
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = {
                            MDAppTopBar(
                                // TODO 这个topPadding可以自动判断不用hardcode的？
                                modifier = Modifier.wrapContentHeight(),
                                scrollBehavior = scrollBehavior,
                                title = if (serverDetailState.serverUi == null) "MDPings"
                                    else "${serverDetailState.serverUi!!.host.countryCode} ${serverDetailState.serverUi!!.name}",
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
}

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    drawerState: DrawerState
) {

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        Text(
            text = "MDPings",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(16.dp)
        )
        HorizontalDivider()
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = Icons.Rounded.Home.name
                )
            },
            label = {
                Text(
                    text = "Home",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                    navController.navigate(
                        route = ServerListScreen
                    ) {
                        popUpTo(0)
                    }
                }
            }
        )
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = Icons.Rounded.Settings.name
                )
            },
            label = {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            },
            selected = false,
            onClick = {}
        )
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = Icons.Rounded.Info.name
                )
            },
            label = {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            },
            selected = false,
            onClick = {}
        )
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