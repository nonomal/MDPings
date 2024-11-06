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
import com.example.mdpings.vpings.presentation.models.toMonitorUi
import com.example.mdpings.vpings.presentation.models.toServerUi
import com.example.mdpings.vpings.presentation.user_login.LoginAction
import com.example.mdpings.vpings.presentation.user_login.LoginEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class AppSettings(
    val baseUrl: String,
    val token: String,
    val interval: Long
)

class ServerListViewModel(
    private val serverDataSource: ServerDataSource,
    private val storeSettings: StoreSettings
): ViewModel() {

    private val _state = MutableStateFlow(ServerListState())
    val state = _state
// 前后台切换会导致唤起多个observeSettingsAndLoadServers()
//        .onStart {
//            observeSettingsAndLoadServers()
//        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ServerListState()
        )

    private val appSettings: Flow<AppSettings> = combine(
        storeSettings.getApi,
        storeSettings.getToken,
        storeSettings.getInterval
    ) { baseUrl, token, interval ->
        AppSettings(baseUrl, token, interval)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        AppSettings("", "", 5000)
    )

    init {
        observeSettingsAndLoadServers()
    }

    fun observeSettingsAndLoadServers() {
        viewModelScope.launch {
            appSettings.collectLatest { (baseUrl, token, interval) ->
                if (!(baseUrl.isEmpty() or token.isEmpty())) {
                    while (isActive) {
                        loadServers(baseUrl, token)
                        delay(interval)
                    }
                }
            }
        }
    }

//            val baseUrl = storeSettings.readApi()
//            val token = storeSettings.readToken()
//            if (!(baseUrl.isEmpty() or token.isEmpty())) {
//                while (true) {
//                    loadServers(baseUrl, token)
//                    delay(5000)
//                }
//            }

    fun onAction(action: ServerListAction) {
        when(action) {
            is ServerListAction.OnExpandClick -> {
                viewModelScope.launch {
                    appSettings.collectLatest { (baseUrl) ->
                        if (!(baseUrl.isEmpty())) {
                            loadMonitors(baseUrl, action.id)
                        }
                    }
                }
            }
            is ServerListAction.OnShrinkClick -> {
                viewModelScope.launch {
                    cleanMonitors()
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

    fun loadMonitors(apiUrl: String, id: Int) {
        viewModelScope.launch{
            _state.update { it.copy(
                isLoading = true
            ) }

            serverDataSource
                .getMonitors(apiUrl, id)
                .onSuccess { monitors ->
                    _state.update { it.copy(
                        isLoading = false,
                        monitors = monitors.map { it.toMonitorUi() }
                    ) }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    fun cleanMonitors() {
        viewModelScope.launch{
            _state.update { it.copy(
                monitors = emptyList()
            ) }
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