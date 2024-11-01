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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mdpings.core.presentation.util.ObserveAsEvents
import com.example.mdpings.core.presentation.util.toString
import com.example.mdpings.ui.theme.MDPingsTheme
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

                    // 报错框架
                    val viewModel = koinViewModel<LoginViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    val context = LocalContext.current
                    ObserveAsEvents(events = viewModel.events) { event ->
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

                    LoginScreen(
                        state = state,
                        modifier = Modifier
                            .padding(innerPadding),
                        onAction = viewModel::onAction
                    )
                }
            }
        }
    }
}