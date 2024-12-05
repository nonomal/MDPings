package com.sekusarisu.mdpings.vpings.domain

import com.sekusarisu.mdpings.vpings.presentation.server_list.ServerListState
import kotlinx.coroutines.flow.Flow

interface RealtimeServerDataClient {
    suspend fun getServerListStateStream(baseUrl: String): Flow<List<WSServer>>
    suspend fun close()
}