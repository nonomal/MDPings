package com.example.mdpings.vpings.presentation.user_login

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.data.StoreSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToServer: () -> Unit,
) {
    // context
    val context = LocalContext.current
    // a coroutine scope
    val scope = rememberCoroutineScope()
    // we instantiate the saveEmail class
    val dataStore = StoreSettings(context)

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        var api by rememberSaveable { mutableStateOf("") }
        var token by rememberSaveable { mutableStateOf("") }

        Text(
            modifier = Modifier
                .padding(16.dp, 0.dp)
                .alpha(0.6f),
            text = "Login",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp
        )

        Text(
            modifier = Modifier
                .padding(16.dp, 0.dp)
                .alpha(0.6f),
            text = "API BACKEND",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 12.sp
        )
        OutlinedTextField(
            value = api,
            shape = ShapeDefaults.Medium,
            onValueChange = { api = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType
                = KeyboardType.Uri
            ),
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                .fillMaxWidth(),
        )

        Text(
            modifier = Modifier
                .padding(16.dp, 0.dp)
                .alpha(0.6f),
            text = "TOKEN",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 12.sp
        )
        OutlinedTextField(
            value = token,
            shape = ShapeDefaults.Medium,
            onValueChange = { token = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType
                = KeyboardType.Text
            ),
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp)
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
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = "Test"
                    )
                    Spacer(Modifier.width(4.dp))
                    if (!state.servers.isEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Check"
                        )
                    } else if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = LocalContentColor.current
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Login,
                            contentDescription = "Login"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.2f))
            Button(
                enabled = !state.servers.isEmpty(),
                onClick = {
                    scope.launch {
                        dataStore.saveApi(api)
                        dataStore.saveToken(token)
                        delay(1000)
                        onNavigateToServer()
                    }
                },
                modifier = Modifier
                    .height(60.dp)
                    .weight(1f)
            ) {
                Row {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = "Save"
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = "Save"
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun LoginScreenPreview() {
    MDPingsTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {},
            onNavigateToServer = {}
        )
    }
}