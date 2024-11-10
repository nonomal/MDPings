package com.example.mdpings.vpings.presentation.server_detail

import androidx.compose.runtime.Immutable
import com.example.mdpings.vpings.presentation.models.IpAPIUi
import com.example.mdpings.vpings.presentation.models.MonitorUi
import com.example.mdpings.vpings.presentation.models.ServerUi

@Immutable
data class ServerDetailState(
    val isLoading: Boolean = false,
    val isChartLoading: Boolean = false,
    val serverUi: ServerUi? = null,
    val ipAPIUi: IpAPIUi? = null,
    val monitors: List<MonitorUi> = emptyList(),
    val monitorsOrigin: List<MonitorUi> = emptyList(),
    val monitorsTimeSlice: String = ""
)
