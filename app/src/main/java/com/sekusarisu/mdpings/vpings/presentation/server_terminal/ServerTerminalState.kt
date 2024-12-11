package com.sekusarisu.mdpings.vpings.presentation.server_terminal

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString


@Immutable
data class ServerTerminalState(
    val isLoading: Boolean = false,
    val selectedServerId: Int = 0,
    val terminal: List<AnnotatedString> = emptyList<AnnotatedString>()
)
