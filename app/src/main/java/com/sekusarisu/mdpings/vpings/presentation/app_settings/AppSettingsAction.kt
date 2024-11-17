package com.sekusarisu.mdpings.vpings.presentation.app_settings

sealed interface AppSettingsAction {
    object OnInitLoadAppSettings: AppSettingsAction
    data class OnChangeActiveInstance(val index: Int): AppSettingsAction
    data class OnDeleteInstance(val index: Int): AppSettingsAction
    data class OnSaveInstanceClicked(val name: String, val apiUrl: String, val apiToken: String): AppSettingsAction
    data class OnSaveIntervalClicked(val interval: Int): AppSettingsAction
}