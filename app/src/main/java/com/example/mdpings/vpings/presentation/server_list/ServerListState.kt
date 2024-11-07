package com.example.mdpings.vpings.presentation.server_list

import androidx.compose.runtime.Immutable
import com.example.mdpings.vpings.presentation.models.IpAPIUi
import com.example.mdpings.vpings.presentation.models.MonitorUi
import com.example.mdpings.vpings.presentation.models.ServerUi

@Immutable
data class ServerListState(
    val isLoading: Boolean = false,
    val selectedServer: ServerUi? = null,
    val servers: List<ServerUi> = emptyList(),
    val ipAPIUi: IpAPIUi? = null,
    val monitors: List<MonitorUi> = emptyList()
)