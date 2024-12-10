package com.sekusarisu.mdpings.vpings.presentation.server_detail

import androidx.compose.runtime.Immutable
import com.sekusarisu.mdpings.vpings.presentation.models.IpAPIUi
import com.sekusarisu.mdpings.vpings.presentation.models.MonitorUi
import com.sekusarisu.mdpings.vpings.presentation.models.ServerUi
import com.sekusarisu.mdpings.vpings.presentation.models.WSServerUi

@Immutable
data class ServerDetailState(
    val isLoading: Boolean = false,
    val isChartLoading: Boolean = false,
    val serverUi: ServerUi? = null,
    val wsServerUi: WSServerUi? = null,
    val ipAPIUi: IpAPIUi? = null,
    val monitors: List<MonitorUi> = emptyList(),
    val monitorsOrigin: List<MonitorUi> = emptyList(),
    val monitorsTimeSlice: String = ""
)
