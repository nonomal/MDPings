package com.sekusarisu.mdpings.vpings.presentation.app_settings

sealed interface AppSettingsAction {
    object OnInitLoadAppSettings: AppSettingsAction
//    data class OnSaveClicked(val dialogTitle: String, val value: String): AppSettingsAction
    data class OnSaveInstanceClicked(val name: String, val apiUrl: String, val apiToken: String): AppSettingsAction
    data class OnSaveIntervalClicked(val interval: Int): AppSettingsAction
}