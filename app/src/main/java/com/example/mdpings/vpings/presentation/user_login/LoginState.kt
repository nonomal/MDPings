package com.example.mdpings.vpings.presentation.user_login

import androidx.compose.runtime.Immutable
import com.example.mdpings.vpings.presentation.models.ServerUi

@Immutable
data class LoginState(
    val isLoading: Boolean = false,
    val servers: List<ServerUi> = emptyList()
)