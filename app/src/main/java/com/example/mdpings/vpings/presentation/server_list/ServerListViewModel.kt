package com.example.mdpings.vpings.presentation.server_list

import android.app.Application
import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mdpings.VpingsApp
import com.example.mdpings.core.domain.util.NetworkError
import com.example.mdpings.core.domain.util.Result
import com.example.mdpings.core.domain.util.onError
import com.example.mdpings.core.domain.util.onSuccess
import com.example.mdpings.di.appModule
import com.example.mdpings.vpings.data.StoreSettings
import com.example.mdpings.vpings.domain.Server
import com.example.mdpings.vpings.domain.ServerDataSource
import com.example.mdpings.vpings.presentation.models.toServerUi
import com.example.mdpings.vpings.presentation.user_login.LoginEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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
        .onStart {
            observeSettingsAndLoadServers(5000)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ServerListState()
        )

    fun observeSettingsAndLoadServers(interval: Long) {
        viewModelScope.launch {
            combine(storeSettings.getApi, storeSettings.getToken) { api, token ->
                Pair(api ?: "", token ?: "")
            }.collect { (api, token) ->
                if (!(api.isEmpty() or token.isEmpty())) {
                    while (true) {
                        loadServers(api, token)
                        delay(interval)
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

//class ServerListViewModel(
//    private val serverDataSource: ServerDataSource,
//    private val storeSettings: StoreSettings
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(ServerListState())
//    val state: StateFlow<ServerListState> = _state
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000L),
//            ServerListState()
//        )
//
//    init {
//        startPollingServers()
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private fun startPollingServers() {
//        viewModelScope.launch {
//            combine(storeSettings.getApi, storeSettings.getToken) { api, token ->
//                Pair(api ?: "", token ?: "")
//            }
//                .flatMapLatest { (api, token) ->
//                    if (api.isNotEmpty() && token.isNotEmpty()) {
//                        tickerFlow(api, token, interval = 5000L)
//                    } else {
//                        emptyFlow()
//                    }
//                }
//                .collect { result ->
//                    result.onSuccess { servers ->
//                        _state.update { it.copy(
//                            isLoading = false,
//                            servers = servers.map { it.toServerUi() }
//                        ) }
//                    }
//                    result.onError { error ->
//                        _state.update { it.copy(isLoading = false) }
//                    }
//                }
//        }
//    }
//
//    private fun tickerFlow(api: String, token: String, interval: Long): Flow<Result<List<Server>, NetworkError>> = flow {
//        while (true) {
//            emit(loadServers(api, token))
//            delay(interval)
//        }
//    }
//
//    private suspend fun loadServers(apiUrl: String, token: String):  Result<List<Server>, NetworkError> {
//        _state.update { it.copy(isLoading = true) }
//        return serverDataSource
//            .getServers(apiUrl, token)
//    }
//}