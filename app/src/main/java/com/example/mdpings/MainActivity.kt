package com.example.mdpings

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mdpings.core.presentation.util.ObserveAsEvents
import com.example.mdpings.core.presentation.util.toString
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.data.StoreSettings
import com.example.mdpings.vpings.presentation.server_list.ServerListScreen
import com.example.mdpings.vpings.presentation.server_list.ServerListViewModel
import com.example.mdpings.vpings.presentation.user_login.LoginEvent
import com.example.mdpings.vpings.presentation.user_login.LoginScreen
import com.example.mdpings.vpings.presentation.user_login.LoginViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MDPingsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Error Handle
                    val loginViewModel = koinViewModel<LoginViewModel>()
                    val loginState by loginViewModel.state.collectAsStateWithLifecycle()
                    val serverListViewModel = koinViewModel<ServerListViewModel>()
                    val serverListState by serverListViewModel.state.collectAsStateWithLifecycle()
                    val context = LocalContext.current

                    // DataStore
                    val dataStore = StoreSettings(context)
                    val stateApi = dataStore.getApi.collectAsState("").value
                    val stateToken = dataStore.getToken.collectAsState("").value
                    val isSettingsIsnull = stateApi!!.isEmpty() || stateToken!!.isEmpty()

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

                    when(isSettingsIsnull) {
                        true -> {
                            LoginScreen(
                                state = loginState,
                                modifier = Modifier
                                    .padding(innerPadding),
                                onAction = loginViewModel::onAction
                            )
                        }
                        false -> {
                            ServerListScreen(
                                state = serverListState,
                                modifier = Modifier
                                    .padding(innerPadding)
                            )
                        } else -> {
                            LoginScreen(
                                state = loginState,
                                modifier = Modifier
                                    .padding(innerPadding),
                                onAction = loginViewModel::onAction
                            )
                        }
                    }

                }
            }
        }
    }
}