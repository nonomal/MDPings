package com.sekusarisu.mdpings.vpings.presentation.server_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekusarisu.mdpings.core.domain.util.onError
import com.sekusarisu.mdpings.core.domain.util.onSuccess
import com.sekusarisu.mdpings.vpings.domain.Server
import com.sekusarisu.mdpings.vpings.domain.ServerDataSource
import com.sekusarisu.mdpings.vpings.domain.ServerOrder
import com.sekusarisu.mdpings.vpings.domain.ServerSortField
import com.sekusarisu.mdpings.vpings.presentation.models.ServerUi
import com.sekusarisu.mdpings.vpings.presentation.models.toServerUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//data class AppSettings(
//    val baseUrl: String,
//    val token: String,
//    val interval: Long
//)

class ServerListViewModel(
    private val serverDataSource: ServerDataSource,
): ViewModel() {

    private val _state = MutableStateFlow(ServerListState())
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ServerListState()
        )

    fun onAction(action: ServerListAction) {
        when(action) {
            is ServerListAction.OnServerClick -> {
                _state.update { it.copy(selectedServer = action.serverUi) }
            }
            is ServerListAction.OnLoadServer -> {
                loadServers(
                    apiUrl = action.apiURL,
                    token = action.apiTOKEN,
                    sort = action.sortField,
                    order = action.order,
                )
            }
            is ServerListAction.OnInitCleanSelectedServer -> {
                _state.update { it.copy(selectedServer = null) }
            }
        }
    }

    private fun loadServers(apiUrl: String, token: String, sort: ServerSortField, order: ServerOrder) {
        viewModelScope.launch{
            _state.update { it.copy(
                isLoading = true
            ) }

            serverDataSource
                .getServers(apiUrl, token)
                .onSuccess { servers ->
                    _state.update { it.copy(
                        isLoading = false,
                        servers = servers
                            .map { it.toServerUi() }
                            .sortByField(sort, order)
                    ) }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    fun List<ServerUi>.sortByField(serverSortField: ServerSortField, serverOrder: ServerOrder): List<ServerUi> {
        return if (serverOrder.ordinal == 0) {
            when (serverSortField) {
                ServerSortField.ID -> sortedBy { it.id }
                ServerSortField.ONLINE -> sortedBy { it.isOnline }
            }
        } else {
            when (serverSortField) {
                ServerSortField.ID -> sortedByDescending { it.id }
                ServerSortField.ONLINE -> sortedByDescending { it.isOnline }
            }
        }
    }

}