package com.sekusarisu.mdpings.vpings.presentation.server_terminal

import androidx.compose.runtime.Immutable


@Immutable
data class ServerTerminalState(
    val isLoading: Boolean = false,
    val selectedServerId: Int = 0,
    val terminal: String = ""
)
