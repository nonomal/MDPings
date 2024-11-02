package com.example.mdpings.vpings.data.networking

import com.example.mdpings.core.data.networking.constructUrl
import com.example.mdpings.core.data.networking.safeCall
import com.example.mdpings.core.domain.util.NetworkError
import com.example.mdpings.core.domain.util.Result
import com.example.mdpings.core.domain.util.map
import com.example.mdpings.vpings.data.mappers.toServer
import com.example.mdpings.vpings.data.networking.dto.ServersResponsesDto
import com.example.mdpings.vpings.domain.Server
import com.example.mdpings.vpings.domain.ServerDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.headers

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

}