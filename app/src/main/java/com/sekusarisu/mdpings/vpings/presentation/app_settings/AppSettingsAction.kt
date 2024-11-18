package com.sekusarisu.mdpings.vpings.presentation.app_settings

import com.sekusarisu.mdpings.vpings.domain.ServerOrder
import com.sekusarisu.mdpings.vpings.domain.ServerSortField

sealed interface AppSettingsAction {
    object OnInitLoadAppSettings: AppSettingsAction
    data class OnChangeActiveInstance(val index: Int): AppSettingsAction
    data class OnDeleteInstance(val index: Int): AppSettingsAction
    data class OnSaveInstanceClicked(val name: String, val apiUrl: String, val apiToken: String): AppSettingsAction
    data class OnSaveIntervalClicked(val interval: Int): AppSettingsAction
    data class OnSaveServerSortField(val serverSortField: ServerSortField): AppSettingsAction
    data class OnSaveServerOrder(val serverOrder: ServerOrder): AppSettingsAction
}