package com.sekusarisu.mdpings.vpings.domain

import com.sekusarisu.mdpings.core.domain.util.NetworkError
import com.sekusarisu.mdpings.core.domain.util.Result

interface ServerDataSource {
    suspend fun getLoginData(baseUrl: String, username: String, password: String): Result<LoginData, NetworkError>
    suspend fun getSingleServerIP(baseUrl: String, serverId: Int): Result<String, NetworkError>
    suspend fun getServers(apiUrl: String, token: String): Result<List<Server>, NetworkError>
    suspend fun getSingleServer(apiUrl: String, token: String, serverId: String): Result<Server, NetworkError>
    suspend fun getMonitors(apiUrl: String, token: String, id: Int): Result<List<Monitor>, NetworkError>
    suspend fun getIpAPI(serverIp: String): Result<IpAPI, NetworkError>
}