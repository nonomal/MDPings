package com.example.mdpings.vpings.presentation.server_list

import androidx.compose.runtime.Immutable
import com.example.mdpings.vpings.presentation.models.ServerUi

@Immutable
data class ServerListState(
    val isLoading: Boolean = false,
    val previousData: List<ServerUi> = emptyList(),
    val servers: List<ServerUi> = emptyList()
)