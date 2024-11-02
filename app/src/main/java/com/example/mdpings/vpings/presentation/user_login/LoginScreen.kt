package com.example.mdpings.vpings.presentation.user_login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.data.StoreSettings
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier
) {
    // context
    val context = LocalContext.current
    // a coroutine scope
    val scope = rememberCoroutineScope()
    // we instantiate the saveEmail class
    val dataStore = StoreSettings(context)

    val stateApi = dataStore.getApi.collectAsState("")
    val stateToken = dataStore.getToken.collectAsState("")
    val isSettingsIsnull = stateApi.value!!.isEmpty() && stateToken.value!!.isEmpty()

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        var api by rememberSaveable { mutableStateOf("") }
        var token by rememberSaveable { mutableStateOf("") }
        var isTestSuccess by remember { mutableStateOf(false) }

        if (!isSettingsIsnull) {
            Text(
                modifier = Modifier
                    .padding(16.dp, 0.dp)
                    .alpha(0.6f),
                text = "API: ${stateApi.value}",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp
            )
            Text(
                modifier = Modifier
                    .padding(16.dp, 0.dp)
                    .alpha(0.6f),
                text = "Token: ${stateToken.value}",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp
            )
        }

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
            enabled = !isTestSuccess,
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
            enabled = !isTestSuccess,
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
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    text = "Test"
                )
            }
            Spacer(modifier = Modifier.weight(0.2f))
            Button(
                enabled = isTestSuccess,
                onClick = {
                    //launch the class in a coroutine scope
                    scope.launch {
                        dataStore.saveApi(api)
                        dataStore.saveToken(token)
                    }
                },
                modifier = Modifier
                    .height(60.dp)
                    .weight(1f)
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    text = "Save Settings"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            }
            if (!state.servers.isEmpty()) {
                isTestSuccess = true
                Text(
                    modifier = Modifier
                        .padding(16.dp, 0.dp)
                        .alpha(0.6f),
                    text = "Test Success!\nClick Save Settings to get into homepage.",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .padding(16.dp, 0.dp)
                        .alpha(0.6f),
                    text = state.servers.last().toString(),
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


//@PreviewLightDark
//@Composable
//private fun LoginScreenPreview() {
//    MDPingsTheme {
//        LoginScreen()
//    }
//}