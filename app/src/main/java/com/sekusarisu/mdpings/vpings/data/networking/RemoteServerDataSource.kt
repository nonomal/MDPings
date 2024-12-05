package com.sekusarisu.mdpings.vpings.data.networking

import com.sekusarisu.mdpings.core.data.networking.constructUrl
import com.sekusarisu.mdpings.core.data.networking.safeCall
import com.sekusarisu.mdpings.core.domain.util.NetworkError
import com.sekusarisu.mdpings.core.domain.util.Result
import com.sekusarisu.mdpings.core.domain.util.map
import com.sekusarisu.mdpings.vpings.data.mappers.toIpAPI
import com.sekusarisu.mdpings.vpings.data.mappers.toLoginData
import com.sekusarisu.mdpings.vpings.data.mappers.toMonitor
import com.sekusarisu.mdpings.vpings.data.mappers.toServer
import com.sekusarisu.mdpings.vpings.data.networking.dto.IpAPIDto
import com.sekusarisu.mdpings.vpings.data.networking.dto.LoginResponsesDto
import com.sekusarisu.mdpings.vpings.data.networking.dto.MonitorsResponsesDto
import com.sekusarisu.mdpings.vpings.data.networking.dto.ServersResponsesDto
import com.sekusarisu.mdpings.vpings.domain.IpAPI
import com.sekusarisu.mdpings.vpings.domain.LoginData
import com.sekusarisu.mdpings.vpings.domain.LoginRequestData
import com.sekusarisu.mdpings.vpings.domain.Monitor
import com.sekusarisu.mdpings.vpings.domain.Server
import com.sekusarisu.mdpings.vpings.domain.ServerDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.*

class RemoteServerDataSource(
    private val httpClient: HttpClient
): ServerDataSource {

    override suspend fun getLoginData(
        baseUrl: String,
        username: String,
        password: String
    ): Result<LoginData, NetworkError> {
        val url = constructUrl(
            baseURL = baseUrl,
            url = "/api/v1/login"
        )
        println("url: $url")
        return safeCall<LoginResponsesDto> {
            httpClient.post(
                urlString = url
            ) {
                contentType(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "username" to username,
                        "password" to password
                    )
                )
            }
        }.map { response ->
            response.data
                ?.toLoginData() ?: LoginData("","")
        }
    }

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

    override suspend fun getMonitors(apiUrl: String, token: String, serverId: Int): Result<List<Monitor>, NetworkError> {
        return safeCall<MonitorsResponsesDto> {
            httpClient.get(
                urlString = constructUrl(
                    baseURL = apiUrl,
                    url = "/api/v1/monitor/$serverId"
                )
            ) {
                header("Authorization", token)
            }
        }.map { response ->
            response.result
                ?.sortedBy { it.monitorId }
                ?.map { it.toMonitor() } ?: emptyList()
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