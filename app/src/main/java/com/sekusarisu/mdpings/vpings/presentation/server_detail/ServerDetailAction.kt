package com.sekusarisu.mdpings.vpings.presentation.server_detail

import com.sekusarisu.mdpings.vpings.presentation.models.ServerUi

interface ServerDetailAction {
    data class OnLoadSingleServer(val serverUi: ServerUi, val apiURL: String, val apiTOKEN: String, val interval: Int): ServerDetailAction
    data class OnLoadInfoAndMonitors(val serverUi: ServerUi, val monitorsTimeSlice: String, val apiURL: String, val apiTOKEN: String, val interval: Int): ServerDetailAction
    data class OnMonitorsRefresh(val serverId: Int, val monitorsTimeSlice: String, val apiURL: String): ServerDetailAction
    data class OnSliceMonitorsTime(val time: String): ServerDetailAction
    object OnDisposeCleanUp: ServerDetailAction
}