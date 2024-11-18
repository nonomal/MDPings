package com.sekusarisu.mdpings.vpings.presentation.server_list

import com.sekusarisu.mdpings.vpings.domain.ServerOrder
import com.sekusarisu.mdpings.vpings.domain.ServerSortField
import com.sekusarisu.mdpings.vpings.presentation.models.ServerUi


interface ServerListAction {
    data class OnLoadServer(
        val apiURL: String, val apiTOKEN: String, val sortField: ServerSortField, val order: ServerOrder
    ): ServerListAction
    data class OnServerClick(val serverUi: ServerUi): ServerListAction
    data class OnInitCleanSelectedServer(val isLoading: Boolean): ServerListAction
}