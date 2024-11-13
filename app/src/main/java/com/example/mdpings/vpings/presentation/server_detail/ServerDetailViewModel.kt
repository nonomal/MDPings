package com.example.mdpings.vpings.presentation.server_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mdpings.core.domain.util.onError
import com.example.mdpings.core.domain.util.onSuccess
import com.example.mdpings.vpings.domain.ServerDataSource
import com.example.mdpings.vpings.presentation.models.ServerUi
import com.example.mdpings.vpings.presentation.models.toIpAPIUi
import com.example.mdpings.vpings.presentation.models.toMonitorUi
import com.example.mdpings.vpings.presentation.models.toServerUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServerDetailViewModel(
    private val serverDataSource: ServerDataSource
): ViewModel() {

    private val _state = MutableStateFlow(ServerDetailState())
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ServerDetailState()
        )

    fun onAction(action: ServerDetailAction) {
        when(action) {
            is ServerDetailAction.OnLoadInfoAndMonitors -> {
                loadSelectServerInfoAndMonitors(action.serverUi, action.monitorsTimeSlice, action.apiURL)
            }
            is ServerDetailAction.OnMonitorsRefresh -> {
                reloadMonitors(action.serverId, action.monitorsTimeSlice, apiURL = action.apiURL)
            }
            is ServerDetailAction.OnLoadSingleServer -> {
                loadSingleServerDetail(action.serverUi, action.apiURL, action.apiTOKEN)
            }
            is ServerDetailAction.OnSliceMonitorsTime -> {
                sliceMonitors(action.time)
            }
            is ServerDetailAction.OnDisposeCleanUp -> {
                cleanUp()
            }
        }
    }

    private fun cleanUp() {
        _state.update { it.copy(
            isLoading = false,
            isChartLoading = false,
            serverUi = null,
            ipAPIUi = null,
            monitors = emptyList(),
            monitorsOrigin = emptyList()
        ) }
    }

    private fun loadSelectServerInfoAndMonitors(serverUi: ServerUi, monitorsTimeSlice: String, apiURL: String) {
        _state.update { it.copy(isLoading = true, isChartLoading = true) }
        val sliceCount = when(monitorsTimeSlice) {
            "30 mins" -> 10
            "1 hour" -> 15
            "3 hours" -> 45
            "6 hours" -> 90
            else -> 0
        }

        viewModelScope.launch {
            if (apiURL.isNotEmpty()) {
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
                    .getMonitors(apiURL, serverUi.id)
                    .onSuccess { monitors ->
                        val monitorsOrigin = monitors.map { it.toMonitorUi() }
                        val monitors = if (sliceCount > 0) {
                            monitorsOrigin.map { monitor ->
                                monitor.copy(
                                    createdAt = monitor.createdAt.takeLast(sliceCount),
                                    avgDelay = monitor.avgDelay.takeLast(sliceCount)
                                )
                            }
                        } else {
                            monitorsOrigin
                        }
                        _state.update { it.copy(
                            monitors = monitors,
                            monitorsOrigin = monitorsOrigin,
                            isChartLoading = false
                        ) }
                    }
                    .onError { error ->
                        _state.update { it.copy(
                            monitors = emptyList(),
                            monitorsOrigin = emptyList(),
                            isChartLoading = false
                        ) }
                    }
            }
        }
    }

    private fun loadSingleServerDetail(serverUi: ServerUi, apiURL: String, apiTOKEN: String) {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            if (apiURL.isNotEmpty() && apiTOKEN.isNotEmpty()) {
                serverDataSource
                    .getSingleServer(apiURL, apiTOKEN, serverUi.id.toString())
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

    private fun reloadMonitors(serverId: Int, monitorsTimeSlice: String, apiURL: String) {
        _state.update { it.copy(isChartLoading = true) }

        viewModelScope.launch {
            if (apiURL.isNotEmpty()) {
                serverDataSource
                    .getMonitors(apiURL, serverId)
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
                        _state.update { it.copy(
                            monitors = emptyList(),
                            monitorsOrigin = emptyList(),
                            isChartLoading = false,
                        ) }
                    }
            }
        }
    }

    private fun sliceMonitors(time: String) {
        val sliceCount = when(time) {
            "30 mins" -> 10
            "1 hour" -> 15
            "3 hours" -> 45
            "6 hours" -> 90
            else -> 0
        }
        _state.update {
            val newMonitors = if (sliceCount > 0) {
                it.monitorsOrigin.map { monitor ->
                    monitor.copy(
                        createdAt = monitor.createdAt.takeLast(sliceCount),
                        avgDelay = monitor.avgDelay.takeLast(sliceCount)
                    )
                }
            } else {
                it.monitorsOrigin
            }

            it.copy(
                monitorsTimeSlice = time,
                monitors = newMonitors
            )
        }
    }

}