package com.sekusarisu.mdpings.vpings.presentation.server_terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.sekusarisu.mdpings.R
import com.sekusarisu.mdpings.ui.theme.MDPingsTheme
import com.sekusarisu.mdpings.vpings.domain.AppSettings
import com.sekusarisu.mdpings.vpings.domain.Instance
import com.sekusarisu.mdpings.vpings.presentation.app_settings.AppSettingsState
import com.sekusarisu.mdpings.vpings.presentation.server_list.ServerListState
import com.sekusarisu.mdpings.vpings.presentation.server_list.components.previewListWSServers
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ServerTerminalScreen(
    state: ServerTerminalState,
    serverListState: ServerListState,
    selectedServerId: Int,
    appSettingsState: AppSettingsState,
    onAction: (ServerTerminalAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val baseUrl = appSettingsState.appSettings.instances[appSettingsState.appSettings.activeInstance].baseUrl
    val wsServer = serverListState.wsServers.first { it -> it.id == selectedServerId}
    val connectTo = "${wsServer.countryCode} ${wsServer.name}"
    var command by remember { mutableStateOf("") }
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        onAction(
            ServerTerminalAction.OnInitConnection(baseUrl, selectedServerId, connectTo)
        )
    }
    LaunchedEffect(state.terminal) {
        verticalScrollState.scrollTo(verticalScrollState.maxValue)
    }
    DisposableEffect(lifecycleOwner) {
        onDispose{
            onAction(
                ServerTerminalAction.OnDisconnect
            )
        }
    }

    // 终端输出显示区
    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp)
        ) {
            Box(
                contentAlignment = Alignment.BottomStart,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                SelectionContainer {
                    Text(
                        text = state.terminal,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(4.dp)
                            .horizontalScroll(horizontalScrollState)
                            .verticalScroll(verticalScrollState)
                            .alpha(0.7f)
                    )
                }
            }
        }

        // 命令输入区
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = command,
                onValueChange = { command = it },
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(R.string.terminal_command_input)) }
            )
            Button(
                onClick = {
                    onAction(
                        ServerTerminalAction.OnSendCommand(command = command)
                    )
                    onAction(
                        ServerTerminalAction.OnSendCommand(command = "\n")
                    )
                    command = ""
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(stringResource(R.string.terminal_command_send))
            }
        }

        // 连接控制按钮
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Button(onClick = {
                onAction(
                    ServerTerminalAction.OnInitConnection(baseUrl, selectedServerId, connectTo)
                )
            }) {
                Text(stringResource(R.string.terminal_connect))
            }

            Button(onClick = {
                onAction(
                    ServerTerminalAction.OnDisconnect
                )
            }) {
                Text(stringResource(R.string.terminal_disconnect))
            }

            Button(onClick = {
                onAction(
                    ServerTerminalAction.OnCleanScreen
                )
            }) {
                Text(stringResource(R.string.terminal_clean_screen))
            }

            Button(onClick = {
                onAction(
                    ServerTerminalAction.OnSendCommand(
                        command = "\u0003"
                    )
                )
            }) {
                Text(stringResource(R.string.terminal_command_ctrl_c))
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun ServerTerminalScreenPreview() {
    MDPingsTheme {
        ServerTerminalScreen(
            state = ServerTerminalState(
                isLoading = false,
                selectedServerId = 0,
                terminal = "已和 \uD83C\uDDFA\uD83C\uDDF8五角大楼 建立终端连接\nroot@s123445 /opt/nezha/agent #"
            ),
            serverListState = ServerListState(
                isLoading = false,
                wsServers = previewListWSServers,
            ),
            appSettingsState = AppSettingsState(
                AppSettings().copy(
                    instances = persistentListOf(
                        Instance(
                            name = "TODO()",
                            baseUrl = "TODO()",
                            username = "TODO()",
                            password = "TODO()",
                            token = "TODO()"
                        )
                    )
                )
            ),
            onAction = {},
            modifier = Modifier,
            selectedServerId = 0
        )
    }
}