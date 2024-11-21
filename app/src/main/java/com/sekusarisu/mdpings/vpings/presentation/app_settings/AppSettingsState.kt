package com.sekusarisu.mdpings.vpings.presentation.app_settings

import androidx.compose.runtime.Immutable
import com.sekusarisu.mdpings.vpings.domain.AppSettings

@Immutable
data class AppSettingsState(
    val appSettings: AppSettings = AppSettings()
)