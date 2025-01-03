package com.sekusarisu.mdpings.vpings.presentation.server_list

import com.sekusarisu.mdpings.vpings.presentation.models.ServerUi


interface ServerListAction {
    data class OnLoadServer(val apiURL: String, val apiTOKEN: String): ServerListAction
    data class OnServerClick(val serverUi: ServerUi): ServerListAction
    data class OnInitCleanSelectedServer(val isLoading: Boolean): ServerListAction
}