package com.sekusarisu.mdpings.vpings.data.mappers

import com.sekusarisu.mdpings.vpings.data.networking.dto.SessionDataDto
import com.sekusarisu.mdpings.vpings.domain.SessionData

fun SessionDataDto.toSessionData(): SessionData {
    return SessionData(
        sessionId = sessionId,
        serverId = serverId,
        serverName = serverName
    )
}