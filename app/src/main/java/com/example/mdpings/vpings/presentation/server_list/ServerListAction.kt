package com.example.mdpings.vpings.presentation.server_list

import com.example.mdpings.vpings.presentation.models.ServerUi


interface ServerListAction {
    data class OnServerClick(val serverUi: ServerUi): ServerListAction
}