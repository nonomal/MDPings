package com.example.mdpings.vpings.presentation.app_settings

import androidx.compose.runtime.Immutable

@Immutable
data class AppSettingsState(
    val apiURL: String = "",
    val apiTOKEN: String = "",
    val refreshInterval: Int = 5000
)