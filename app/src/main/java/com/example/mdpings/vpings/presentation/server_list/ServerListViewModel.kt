package com.example.mdpings.vpings.presentation.server_list

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mdpings.VpingsApp
import com.example.mdpings.core.domain.util.onError
import com.example.mdpings.core.domain.util.onSuccess
import com.example.mdpings.di.appModule
import com.example.mdpings.vpings.data.StoreSettings
import com.example.mdpings.vpings.domain.ServerDataSource
import com.example.mdpings.vpings.presentation.models.toServerUi
import com.example.mdpings.vpings.presentation.user_login.LoginEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServerListViewModel(
    private val serverDataSource: ServerDataSource,
    private val storeSettings: StoreSettings
): ViewModel() {

    private val _state = MutableStateFlow(ServerListState())
    val state = _state
        .onStart { observeSettingsAndLoadServers() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ServerListState()
        )

    private fun observeSettingsAndLoadServers() {
        viewModelScope.launch {
            combine(storeSettings.getApi, storeSettings.getToken) { api, token ->
                Pair(api ?: "", token ?: "")
            }.collect { (api, token) ->
                if (!(api.isEmpty() or token.isEmpty())) {
                    while (true) {
                        loadServers(api, token)
                        delay(5000)
                    }
                }
            }
        }
    }

    fun loadServers(apiUrl: String, token: String) {
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
                }
        }
    }
}