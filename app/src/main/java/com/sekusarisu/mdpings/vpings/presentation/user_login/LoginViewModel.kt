package com.sekusarisu.mdpings.vpings.presentation.user_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekusarisu.mdpings.core.domain.util.Error
import com.sekusarisu.mdpings.core.domain.util.NetworkError
import com.sekusarisu.mdpings.core.domain.util.Result
import com.sekusarisu.mdpings.core.domain.util.onError
import com.sekusarisu.mdpings.core.domain.util.onSuccess
import com.sekusarisu.mdpings.vpings.domain.AppSettingsDataSource
import com.sekusarisu.mdpings.vpings.domain.LoginData
import com.sekusarisu.mdpings.vpings.domain.ServerDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.fold

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
            is LoginAction.OnCredentialsChange -> {
                _state.update { it.copy(
                    servers = emptyList(),
                    isTestSucceed = false
                ) }
            }
            is LoginAction.OnTestClick -> {
                testConnection(action.baseUrl, action.username, action.password)
            }
            is LoginAction.OnSaveClicked -> {
                saveInstance(action.name, action.baseUrl, action.username, action.password)
            }
            is LoginAction.OnEditSaveClicked -> {
                editInstance(action.index, action.name, action.baseUrl, action.username, action.password)
            }
            is LoginAction.OnDeleteClick -> {
                deleteInstance(action.index)
            }
        }
    }

    fun testConnection(baseUrl: String, username: String, password: String) {
        viewModelScope.launch{
            _state.update { it.copy(
                isLoading = true,
                isTestSucceed = false,
            ) }
            serverDataSource
                .getLoginData(baseUrl, username, password)
                .onSuccess { servers ->
                    if (servers.token.isNotEmpty()) {
                        _state.update { it.copy(
                            isLoading = false,
                            isTestSucceed = true
                        ) }
                    }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(LoginEvent.Error(error))
                }
        }
    }

    suspend fun testConnectionToBoolean(baseUrl: String, username: String, password: String): Boolean {
        _state.update { it.copy(
            isLoading = true,
            isTestSucceed = false,
        ) }
        return serverDataSource
            .getLoginData(baseUrl, username, password)
            .let { result ->
                when (result) {
                    is Result.Success -> {
                        val loginData = result.data
                        val isSuccess = loginData.token.isNotEmpty()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isTestSucceed = isSuccess
                            )
                        }
                        isSuccess
                    }
                    is Result.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        _events.send(LoginEvent.Error(result.error))
                        false
                    }
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

    fun saveInstance(name: String, baseUrl: String, username: String, password: String) {
        viewModelScope.launch{
            _state.update { it.copy(
                isLoading = true
            ) }
            appSettingsDataSource.putInstance(name, baseUrl, username, password)
            _state.update { it.copy(
                isLoading = false
            ) }
        }
    }

    fun editInstance(index: Int, name: String, baseUrl: String, username: String, password: String) {
        viewModelScope.launch{
            _state.update { it.copy(
                isLoading = true
            ) }
            appSettingsDataSource.editInstance(index, name, baseUrl, username, password)
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