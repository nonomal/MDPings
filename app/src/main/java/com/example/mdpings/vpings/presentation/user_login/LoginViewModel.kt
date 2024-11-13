package com.example.mdpings.vpings.presentation.user_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mdpings.core.domain.util.onError
import com.example.mdpings.core.domain.util.onSuccess
import com.example.mdpings.vpings.domain.AppSettingsDataSource
import com.example.mdpings.vpings.domain.ServerDataSource
import com.example.mdpings.vpings.presentation.app_settings.USER_API_BACKEND
import com.example.mdpings.vpings.presentation.app_settings.USER_API_TOKEN
import com.example.mdpings.vpings.presentation.models.toServerUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val serverDataSource: ServerDataSource,
    private val appSettingsDataSource: AppSettingsDataSource
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
                    apiUrl = action.apiURL,
                    token = action.apiTOKEN
                )
            }
            is LoginAction.OnCredentialsChange -> {
                _state.update { it.copy(
                    servers = emptyList()
                ) }
            }
            is LoginAction.OnSaveClicked -> {
                saveApiURL(action.apiURL)
                saveApiTOKEN(action.apiTOKEN)
            }
        }
    }

    fun testConnection(apiUrl: String, token: String) {
        viewModelScope.launch{
            _state.update { it.copy(
                isLoading = true,
                servers = emptyList()
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

    fun saveApiURL(value: String) {
        viewModelScope.launch{
            appSettingsDataSource.putString(USER_API_BACKEND, value)
        }
    }

    fun saveApiTOKEN(value: String) {
        viewModelScope.launch{
            appSettingsDataSource.putString(USER_API_TOKEN, value)
        }
    }
}