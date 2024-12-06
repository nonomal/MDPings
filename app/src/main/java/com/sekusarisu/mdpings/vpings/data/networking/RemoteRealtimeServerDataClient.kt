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
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
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
    ): Flow<List<WSServer>> = channelFlow {
        val wsUrl = constructWSUrl(
            baseURL = baseUrl,
            url = "/api/v1/ws/server"
        )

        try {
            client.webSocket(wsUrl) {
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            try {
                                val frameText = frame.readText()
                                println("ReadText: $frameText")
                                val decoded = Json.decodeFromString<WSServersResponsesDto>(frameText)
                                val servers = decoded.servers
                                    .sortedBy { it.id }
                                    .map { it.toWSServer() }

                                send(servers)
                            } catch (e: Exception) {
                                println("Error processing WebSocket frame: ${e.message}")
                                e.printStackTrace()
                            }
                        }
                        else -> {}
                    }
                }
            }
        } catch (e: Exception) {
            println("WebSocket connection error: ${e.message}")
            e.printStackTrace()
        }
    }

    override suspend fun close() {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "User requested close"))
        session = null
    }

}