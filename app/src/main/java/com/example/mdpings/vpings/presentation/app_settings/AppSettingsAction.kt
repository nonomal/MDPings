package com.example.mdpings.vpings.presentation.app_settings

sealed interface AppSettingsAction {
    object OnInitLoadAppSettings: AppSettingsAction
    data class OnSaveClicked(val dialogTitle: String, val value: String): AppSettingsAction
}