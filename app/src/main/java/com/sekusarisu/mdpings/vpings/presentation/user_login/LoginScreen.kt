package com.sekusarisu.mdpings.vpings.presentation.user_login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.NetworkCheck
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekusarisu.mdpings.ui.theme.MDPingsTheme
import com.sekusarisu.mdpings.vpings.presentation.app_settings.AppSettingsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    appSettingsState: AppSettingsState,
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToServer: () -> Unit,
) {
//    // context
//    val context = LocalContext.current
//    // a coroutine scope
    val scope = rememberCoroutineScope()
//    // we instantiate the saveEmail class
//    val dataStore = StoreSettings(context)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .verticalScroll(rememberScrollState())
    ) {

        var api by rememberSaveable { mutableStateOf("") }
        var token by rememberSaveable { mutableStateOf("") }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(2f)
        ) {
            Text(
                modifier = Modifier
                    .alpha(0.8f),
                text = "Login",
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .alpha(0.7f),
                text = "请输入哪吒监控的访问地址和在管理后台创建的TOKEN，并点击Test测试服务器连接状况。",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                modifier = Modifier
                    .alpha(0.6f),
                text = "API BACKEND",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp
            )
            OutlinedTextField(
                value = api,
                maxLines = 1,
                placeholder = {
                    Text(
                        text = "https://your-api.example.com/",
                        modifier = Modifier.alpha(0.4f)
                    )
                },
                shape = ShapeDefaults.ExtraLarge,
                onValueChange = {
                    scope.launch {
                        api = it
                        if (state.servers.isNotEmpty()) {
                            onAction(
                                LoginAction.OnCredentialsChange
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType
                    = KeyboardType.Uri
                ),
                modifier = Modifier
                    .fillMaxWidth(),
            )

            Text(
                modifier = Modifier
                    .alpha(0.6f),
                text = "TOKEN",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp
            )
            OutlinedTextField(
                value = token,
                maxLines = 1,
                shape = ShapeDefaults.ExtraLarge,
                onValueChange = {
                    scope.launch {
                        token = it
                        if (state.servers.isNotEmpty()) {
                            onAction(
                                LoginAction.OnCredentialsChange
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType
                    = KeyboardType.Text
                ),
                modifier = Modifier
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        onAction(
                            LoginAction.OnTestClick(api, token)
                        )
                    },
                    modifier = Modifier
                        .height(60.dp)
                        .weight(1f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (!state.servers.isEmpty()) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "Check"
                            )
                        } else if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = LocalContentColor.current
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.NetworkCheck,
                                contentDescription = "Login"
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                        Text(
                            style = MaterialTheme.typography.titleMedium,
                            text = "Test"
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(0.2f))
                Button(
                    enabled = !state.servers.isEmpty(),
                    onClick = {
                        scope.launch {
                            onAction(
                                LoginAction.OnSaveClicked(api, token)
                            )
                            delay(1000)
                            onNavigateToServer()
                        }
                    },
                    modifier = Modifier
                        .height(60.dp)
                        .weight(1f)
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Rounded.Save,
                            contentDescription = "Save"
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            style = MaterialTheme.typography.titleMedium,
                            text = "Save"
                        )
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun LoginScreenPreview() {
    MDPingsTheme {
        LoginScreen(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            state = LoginState(),
            onAction = {},
            onNavigateToServer = {},
            appSettingsState = AppSettingsState()
        )
    }
}