package com.example.mdpings.vpings.presentation.server_detail

import com.example.mdpings.vpings.presentation.models.ServerUi

interface ServerDetailAction {
    data class OnLoadSingleServer(val serverUi: ServerUi): ServerDetailAction
    data class OnLoadInfoAndMonitors(val serverUi: ServerUi): ServerDetailAction
    data class OnSliceMonitorsTime(val time: String): ServerDetailAction
}