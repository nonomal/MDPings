package com.sekusarisu.mdpings.vpings.presentation.server_terminal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekusarisu.mdpings.core.domain.util.Result
import com.sekusarisu.mdpings.core.domain.util.onError
import com.sekusarisu.mdpings.core.domain.util.onSuccess
import com.sekusarisu.mdpings.vpings.domain.RealtimeServerDataClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServerTerminalViewModel(
    private val realtimeServerDataClient: RealtimeServerDataClient
): ViewModel() {
    private val _state = MutableStateFlow(ServerTerminalState())
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ServerTerminalState()
        )

    private var webSocketJob: Job? = null

    fun onAction(action: ServerTerminalAction) {
        when(action) {
            is ServerTerminalAction.OnInitConnection -> {
                refreshToken(action.baseUrl)
                initConnection(action.baseUrl, action.selectedServerId, action.connectTo)
            }
            is ServerTerminalAction.OnConnectToTerminal -> {
                connectToTerminal(action.baseUrl, action.sessionId, action.connectTo)
            }
            is ServerTerminalAction.OnSendCommand -> {
                sendCommand(action.command)
            }
            is ServerTerminalAction.OnDisconnect -> {
                closeSession()
            }
            is ServerTerminalAction.OnCleanScreen -> {
                cleanScreen()
            }
        }
    }

    private fun cleanScreen() {
        _state.update {
            it.copy(
                terminal = ""
            )
        }
        sendCommand("\n")
    }

    private fun refreshToken(baseUrl: String) {
        viewModelScope.launch{
            realtimeServerDataClient
                .refreshToken(baseUrl)
        }
    }

    private fun initConnection(baseUrl: String, selectedServerId: Int, connectTo: String) {
        viewModelScope.launch{
            println("${baseUrl}, ${selectedServerId}")
            realtimeServerDataClient
                .getSession(baseUrl, selectedServerId)
                .onSuccess { result ->
                    connectToTerminal(baseUrl, result.sessionId, connectTo)
                }
                .onError { error ->
                    println(error)
                }
        }
    }

    private fun connectToTerminal(baseUrl: String, sessionId: String, connectTo: String) {
        webSocketJob?.cancel()
        _state.update {
            it.copy(
                terminal = "已和" + connectTo + "建立终端连接\n"
            )
        }
        webSocketJob = viewModelScope.launch{
            realtimeServerDataClient
                .getServerTerminalStream(baseUrl, sessionId)
                .onEach {
                    _state.update { it.copy(
                        isLoading = false
                    ) }
                }
                .collect { newTerminalMessage ->
                    _state.update { currentState ->
                        currentState.copy(
                            terminal = currentState.terminal + newTerminalMessage
                        )
                    }
                }
        }
    }

    private fun sendCommand(command: String) {
        viewModelScope.launch{
            realtimeServerDataClient.sendCommand(command)
        }
    }

    private fun closeSession() {
        webSocketJob?.cancel()
        _state.update { currentState ->
            currentState.copy(
                terminal = currentState.terminal + "\n" + "已断开终端连接"
            )
        }
        viewModelScope.launch{
            realtimeServerDataClient
                .disconnect()
        }
    }

}