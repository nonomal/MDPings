package com.sekusarisu.mdpings.vpings.presentation.user_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekusarisu.mdpings.core.domain.util.onError
import com.sekusarisu.mdpings.core.domain.util.onSuccess
import com.sekusarisu.mdpings.vpings.domain.AppSettingsDataSource
import com.sekusarisu.mdpings.vpings.domain.ServerDataSource
import com.sekusarisu.mdpings.vpings.presentation.app_settings.USER_API_BACKEND
import com.sekusarisu.mdpings.vpings.presentation.app_settings.USER_API_TOKEN
import com.sekusarisu.mdpings.vpings.presentation.models.toServerUi
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
            is LoginAction.OnInitLoadInstances -> {
                getInstances()
            }
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
                saveInstance(action.name, action.apiURL, action.apiTOKEN)
            }
            is LoginAction.OnDeleteClick -> {
                deleteInstance(action.index)
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

    fun getInstances() {
        viewModelScope.launch{
            _state.update { it.copy(
                isLoading = true
            ) }
            appSettingsDataSource
                .getInstances()
            _state.update { it.copy(
                isLoading = false
            ) }
        }
    }

    fun saveInstance(name: String, apiUrl: String, apiToken: String) {
        viewModelScope.launch{
            _state.update { it.copy(
                isLoading = true
            ) }
            appSettingsDataSource.putInstance(name, apiUrl, apiToken)
            _state.update { it.copy(
                isLoading = false
            ) }
        }
    }

    fun deleteInstance(index: Int) {
        viewModelScope.launch{
            _state.update { it.copy(
                isLoading = true
            ) }
            appSettingsDataSource.removeInstance(index)
            _state.update { it.copy(
                isLoading = false
            ) }
        }
    }
}