package com.example.mdpings.vpings.presentation.user_login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mdpings.core.domain.util.onError
import com.example.mdpings.core.domain.util.onSuccess
import com.example.mdpings.vpings.domain.ServerDataSource
import com.example.mdpings.vpings.presentation.models.toServerUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.onSuccess

class LoginViewModel(
    private val serverDataSource: ServerDataSource
): ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state

    // 报错用的channel
    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    // 点击coinList coin操作
    fun onAction(action: LoginAction) {
        when(action) {
            is LoginAction.OnTestClick -> {
                testConnection(
                    apiUrl = action.apiUrl,
                    token = action.token
                )
            }
        }
    }

    fun testConnection(apiUrl: String, token: String) {

        viewModelScope.launch{
            _state.update { it.copy(
                isLoading = true
            ) }
        serverDataSource
            .getServers(apiUrl, token)
            .onSuccess { servers ->
                _state.update { it.copy(
                    isLoading = false,
                    servers = servers.map { it.toServerUi() }
                ) }
            }
            .onError { error ->
                _state.update { it.copy(isLoading = false) }
                _events.send(LoginEvent.Error(error))
            }
        }

    }
}