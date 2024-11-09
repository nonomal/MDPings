package com.example.mdpings.vpings.domain

import com.example.mdpings.core.domain.util.NetworkError
import com.example.mdpings.core.domain.util.Result

interface ServerDataSource {
    suspend fun getServers(apiUrl: String, token: String): Result<List<Server>, NetworkError>
    suspend fun getSingleServer(apiUrl: String, token: String, serverId: String): Result<Server, NetworkError>
    suspend fun getMonitors(apiUrl: String, id: Int): Result<List<Monitor>, NetworkError>
    suspend fun getIpAPI(serverIp: String): Result<IpAPI, NetworkError>
}