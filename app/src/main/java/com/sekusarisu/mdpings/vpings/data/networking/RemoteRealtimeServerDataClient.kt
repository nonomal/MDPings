package com.sekusarisu.mdpings.vpings.data.networking

import com.sekusarisu.mdpings.core.data.networking.constructUrl
import com.sekusarisu.mdpings.core.data.networking.constructWSUrl
import com.sekusarisu.mdpings.vpings.data.mappers.toWSServer
import com.sekusarisu.mdpings.vpings.data.networking.dto.WSServersResponsesDto
import com.sekusarisu.mdpings.vpings.data.networking.dto.WSStateDto
import com.sekusarisu.mdpings.vpings.domain.RealtimeServerDataClient
import com.sekusarisu.mdpings.vpings.domain.Server
import com.sekusarisu.mdpings.vpings.domain.WSServer
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.Json

class RemoteRealtimeServerDataClient(
    private val client: HttpClient
): RealtimeServerDataClient {

    private var session: WebSocketSession? = null

    override suspend fun getServerListStateStream(
        baseUrl: String
    ): Flow<List<WSServer>> {
        return flow {
            session = client.webSocketSession {
                url(constructWSUrl(
                    baseURL = baseUrl,
                    url = "/api/v1/ws/server"
                ))
//                header("Cookie", jwt)
            }
            val servers = session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull { frame ->
                    try {
                        val frameText = frame.readText()
//                        println("Received WebSocket frame: $frameText")

                        val decoded = Json.decodeFromString<WSServersResponsesDto>(frameText)
                        println("Decoded servers count: ${decoded.servers.size}")

                        decoded.servers
                            .sortedBy { it.id }
                            .map { it.toWSServer() }
                            .also {
                                println("Mapped servers: $it")
                            }
                    } catch (e: Exception) {
                        println("Error processing WebSocket frame: ${e.message}")
                        e.printStackTrace()
                        null
                    }
                }
            emitAll(servers)
        }
    }

    override suspend fun close() {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "User requested close"))
        session = null
    }

}