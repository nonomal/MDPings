package com.sekusarisu.mdpings.vpings.data.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TerminalSessionRequest(
    val protocol: String,
    @SerialName("server_id") val serverId: Int
)

@Serializable
data class SessionDataDto(
    @SerialName(value = "session_id") val sessionId: String,
    @SerialName(value = "server_id") val serverId: Int,
    @SerialName(value = "server_name") val serverName: String
)