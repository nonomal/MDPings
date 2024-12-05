package com.sekusarisu.mdpings.vpings.presentation.server_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekusarisu.mdpings.core.domain.util.onError
import com.sekusarisu.mdpings.core.domain.util.onSuccess
import com.sekusarisu.mdpings.vpings.domain.RealtimeServerDataClient
import com.sekusarisu.mdpings.vpings.domain.Server
import com.sekusarisu.mdpings.vpings.domain.ServerDataSource
import com.sekusarisu.mdpings.vpings.domain.ServerOrder
import com.sekusarisu.mdpings.vpings.domain.ServerSortField
import com.sekusarisu.mdpings.vpings.presentation.models.ServerUi
import com.sekusarisu.mdpings.vpings.presentation.models.toServerUi
import com.sekusarisu.mdpings.vpings.presentation.models.toWSServerUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//data class AppSettings(
//    val baseUrl: String,
//    val token: String,
//    val interval: Long
//)

class ServerListViewModel(
    private val serverDataSource: ServerDataSource,
    private val realtimeServerDataClient: RealtimeServerDataClient
): ViewModel() {

    private val _state = MutableStateFlow(ServerListState())
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ServerListState()
        )

    private var webSocketJob: Job? = null

//    init {
//        // 在 ViewModel 中订阅数据变化
//        viewModelScope.launch {
//            realtimeServerDataClient.appSettingsFlow.collect { appSettings ->
//                Log.d("AppSettingsViewModel", "Received new settings: $appSettings")
//                _state.update { it.copy(appSettings = appSettings) }
//            }
//        }
//    }

    fun onSwitchInstanceCleanUp() {
        viewModelScope.launch{
            _state.update { it.copy(
                selectedServer = null,
                servers = emptyList(),
                ipAPIUi = null,
                monitors = emptyList()
            ) }
        }
    }

    fun onAction(action: ServerListAction) {
        when(action) {
            is ServerListAction.OnServerClick -> {
                _state.update { it.copy(selectedServer = action.serverUi) }
            }
            is ServerListAction.OnWSServerClick -> {
                _state.update { it.copy(selectedServer = action.serverUi) }
            }
            is ServerListAction.OnCloseSession -> {
                closeSession()
            }
            is ServerListAction.OnLoadServer -> {
                loadServers(
                    apiUrl = action.apiURL,
                    token = action.apiTOKEN
                )
            }
            is ServerListAction.OnLoadWSServer -> {
                loadWSServers(baseUrl = action.baseUrl)
            }
            is ServerListAction.OnInitCleanSelectedServer -> {
                _state.update { it.copy(selectedServer = null) }
            }
        }
    }

    private fun loadServers(apiUrl: String, token: String) {
        viewModelScope.launch{
            _state.update { it.copy(
                isLoading = true
            ) }

            serverDataSource
                .getServers(apiUrl, token)
                .onSuccess { servers ->
                    _state.update { it.copy(
                        isLoading = false,
                        servers = servers
                            .map { it.toServerUi() }
                    ) }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun loadWSServers(baseUrl: String) {
        webSocketJob?.cancel()
        webSocketJob = viewModelScope.launch{
            realtimeServerDataClient
                .getServerListStateStream(baseUrl)
                .onStart {
                    _state.update { it.copy(
                        isLoading = true
                    ) }
                }
                .onEach {
                    _state.update { it.copy(
                        isLoading = false
                    ) }
                }
                .collect { servers ->
                    _state.update { it.copy(
                        wsServers = servers
                            .map { it.toWSServerUi() }
                    ) }
                }
        }
    }

    private fun closeSession() {
        webSocketJob?.cancel()
        viewModelScope.launch{
            realtimeServerDataClient
                .close()
        }
    }

}



















