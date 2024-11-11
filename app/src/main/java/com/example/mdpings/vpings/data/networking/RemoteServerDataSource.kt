package com.example.mdpings.vpings.data.networking

import com.example.mdpings.core.data.networking.constructUrl
import com.example.mdpings.core.data.networking.safeCall
import com.example.mdpings.core.domain.util.NetworkError
import com.example.mdpings.core.domain.util.Result
import com.example.mdpings.core.domain.util.map
import com.example.mdpings.vpings.data.mappers.toIpAPI
import com.example.mdpings.vpings.data.mappers.toMonitor
import com.example.mdpings.vpings.data.mappers.toServer
import com.example.mdpings.vpings.data.networking.dto.IpAPIDto
import com.example.mdpings.vpings.data.networking.dto.MonitorsResponsesDto
import com.example.mdpings.vpings.data.networking.dto.ServerDto
import com.example.mdpings.vpings.data.networking.dto.ServersResponsesDto
import com.example.mdpings.vpings.domain.IpAPI
import com.example.mdpings.vpings.domain.Monitor
import com.example.mdpings.vpings.domain.Server
import com.example.mdpings.vpings.domain.ServerDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header

class RemoteServerDataSource(
    private val httpClient: HttpClient
): ServerDataSource {

    override suspend fun getServers(apiUrl: String, token: String): Result<List<Server>, NetworkError> {
        return safeCall<ServersResponsesDto> {
            httpClient.get(
                urlString = constructUrl(
                    baseURL = apiUrl,
                    url = "/api/v1/server/details"
                )
            ) {
                header("Authorization", token)
            }
        }.map { response ->
            response.result
                .sortedBy { it.id }
                .map { it.toServer() }
        }
    }

    override suspend fun getSingleServer(apiUrl: String, token: String, serverId: String): Result<Server, NetworkError> {
        return safeCall<ServersResponsesDto> {
            httpClient.get(
                urlString = constructUrl(
                    baseURL = apiUrl,
                    url = "/api/v1/server/details?id=${serverId}"
                )
            ) {
                header("Authorization", token)
            }
        }.map { response ->
            response.result.first().toServer()
        }
    }

    override suspend fun getMonitors(apiUrl: String, serverId: Int): Result<List<Monitor>, NetworkError> {
        return safeCall<MonitorsResponsesDto> {
            httpClient.get(
                urlString = constructUrl(
                    baseURL = apiUrl,
                    url = "/api/v1/monitor/$serverId"
                )
            )
        }.map { response ->
            response.result
                .sortedBy { it.monitorId }
                .map { it.toMonitor() }
        }
    }

    override suspend fun getIpAPI(serverIp: String): Result<IpAPI, NetworkError> {
        return safeCall<IpAPIDto> {
            httpClient.get(
                urlString = constructUrl(
                    baseURL = "http://ip-api.com/json/",
                    url = "/${serverIp}"
                )
            )
        }.map { response ->
            response.toIpAPI()
        }
    }

}