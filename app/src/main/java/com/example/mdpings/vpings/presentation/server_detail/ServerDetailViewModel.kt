package com.example.mdpings.vpings.presentation.server_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mdpings.core.domain.util.onError
import com.example.mdpings.core.domain.util.onSuccess
import com.example.mdpings.vpings.data.StoreSettings
import com.example.mdpings.vpings.domain.ServerDataSource
import com.example.mdpings.vpings.presentation.models.MonitorUi
import com.example.mdpings.vpings.presentation.models.ServerUi
import com.example.mdpings.vpings.presentation.models.toIpAPIUi
import com.example.mdpings.vpings.presentation.models.toMonitorUi
import com.example.mdpings.vpings.presentation.models.toServerUi
import com.example.mdpings.vpings.presentation.server_list.ServerListAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppSettings(
    val baseUrl: String,
    val token: String,
    val interval: Long
)

class ServerDetailViewModel(
    private val serverDataSource: ServerDataSource,
    private val storeSettings: StoreSettings
): ViewModel() {

    private val _state = MutableStateFlow(ServerDetailState())
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ServerDetailState()
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

    fun onAction(action: ServerDetailAction) {
        when(action) {
            is ServerDetailAction.OnLoadInfoAndMonitors -> {
                loadSelectServerInfoAndMonitors(action.serverUi)
            }
            is ServerDetailAction.OnMonitorsRefresh -> {
                reloadMonitors(action.serverId, action.monitorsTimeSlice)
            }
            is ServerDetailAction.OnLoadSingleServer -> {
                loadSingleServerDetail(action.serverUi)
            }
            is ServerDetailAction.OnSliceMonitorsTime -> {
                sliceMonitors(action.time)
            }
        }
    }

    private fun reloadMonitors(serverId: Int, monitorsTimeSlice: String) {
        _state.update { it.copy(isChartLoading = true) }

        viewModelScope.launch {
            appSettings.collect { (baseUrl) ->
                if (baseUrl.isNotEmpty()) {
                    serverDataSource
                        .getMonitors(baseUrl, serverId)
                        .onSuccess { monitors ->
                            val monitorUi = monitors.map { it.toMonitorUi() }
                            _state.update { it.copy(
                                monitors = monitorUi,
                                monitorsOrigin = monitorUi,
                                isChartLoading = false
                            ) }
                            sliceMonitors(monitorsTimeSlice)
                        }
                        .onError { error ->
                            _state.update { it.copy(isChartLoading = false) }
                        }
                }
            }
        }
    }

    private fun loadSelectServerInfoAndMonitors(serverUi: ServerUi) {
        _state.update { it.copy(isLoading = true, isChartLoading = true) }

        viewModelScope.launch {
            appSettings.collect { (baseUrl) ->
                if (baseUrl.isNotEmpty()) {
                    // getIpAPI && Monitors
                    serverDataSource
                        .getIpAPI(
                            serverIp = serverUi.ipv4
                        )
                        .onSuccess { result ->
                            _state.update {
                                it.copy(
                                    ipAPIUi = result.toIpAPIUi(),
                                    isLoading = false
                                )
                            }
                        }
                        .onError { error ->
                            _state.update { it.copy(isLoading = false) }
                        }

                    serverDataSource
                        .getMonitors(baseUrl, serverUi.id)
                        .onSuccess { monitors ->
                            val monitorUi = monitors.map { it.toMonitorUi() }
                            _state.update { it.copy(
                                monitors = monitorUi,
                                monitorsOrigin = monitorUi,
                                isChartLoading = false
                            ) }
                        }
                        .onError { error ->
                            _state.update { it.copy(isChartLoading = false) }
                        }
                }
            }
        }
    }

    private fun loadSingleServerDetail(serverUi: ServerUi) {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            appSettings.collect { (baseUrl, token) ->
                if (baseUrl.isNotEmpty() && token.isNotEmpty()) {
                    serverDataSource
                        .getSingleServer(baseUrl, token, serverUi.id.toString())
                        .onSuccess { server ->
                            _state.update { it.copy(
                                serverUi = server.toServerUi(),
                                isLoading = false
                            ) }
                        }
                        .onError { error ->
                            _state.update { it.copy(isLoading = false) }
                        }
                }
            }
        }
    }

    private fun sliceMonitors(time: String) {
        when(time) {
            "30 mins" -> {
                _state.update {
                    val newMonitors = it.monitorsOrigin.map { monitor ->
                        monitor.copy(
                            createdAt = monitor.createdAt.takeLast(10),
                            avgDelay = monitor.avgDelay.takeLast(10)
                        )
                    }
                    it.copy(
                        monitorsTimeSlice = time,
                        monitors = newMonitors
                    )
                }
            }
            "1 hour" -> {
                _state.update {
                    val newMonitors = it.monitorsOrigin.map { monitor ->
                        monitor.copy(
                            createdAt = monitor.createdAt.takeLast(15),
                            avgDelay = monitor.avgDelay.takeLast(15)
                        )
                    }
                    it.copy(
                        monitorsTimeSlice = time,
                        monitors = newMonitors
                    )
                }
            }
            "3 hours" -> {
                _state.update {
                    val newMonitors = it.monitorsOrigin.map { monitor ->
                        monitor.copy(
                            createdAt = monitor.createdAt.takeLast(45),
                            avgDelay = monitor.avgDelay.takeLast(45)
                        )
                    }
                    it.copy(
                        monitorsTimeSlice = time,
                        monitors = newMonitors
                    )
                }
            }
            "6 hours" -> {
                _state.update {
                    val newMonitors = it.monitorsOrigin.map { monitor ->
                        monitor.copy(
                            createdAt = monitor.createdAt.takeLast(90),
                            avgDelay = monitor.avgDelay.takeLast(90)
                        )
                    }
                    it.copy(
                        monitorsTimeSlice = time,
                        monitors = newMonitors
                    )
                }
            }
            else -> {
                _state.update { it.copy(
                    monitorsTimeSlice = "",
                    monitors = it.monitorsOrigin
                ) }
            }
        }
    }

}