package com.sekusarisu.mdpings.vpings.presentation.server_list

import com.sekusarisu.mdpings.vpings.presentation.models.ServerUi
import com.sekusarisu.mdpings.vpings.presentation.models.WSServerUi


interface ServerListAction {
    data class OnLoadServer(val apiURL: String, val apiTOKEN: String): ServerListAction
    data class OnLoadWSServer(val baseUrl: String): ServerListAction
    data class OnLoadServerGroup(val baseUrl: String): ServerListAction
    object OnCloseSession: ServerListAction
    data class OnServerClick(val serverUi: WSServerUi): ServerListAction
    data class OnWSServerClick(val serverUi: WSServerUi): ServerListAction
    data class OnInitCleanSelectedServer(val isLoading: Boolean): ServerListAction
}