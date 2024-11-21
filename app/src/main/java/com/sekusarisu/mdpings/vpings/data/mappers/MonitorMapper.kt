package com.sekusarisu.mdpings.vpings.data.mappers

import com.sekusarisu.mdpings.vpings.data.networking.dto.MonitorDto
import com.sekusarisu.mdpings.vpings.domain.Monitor

fun MonitorDto.toMonitor(): Monitor {
    return Monitor(
        monitorId = monitorId,
        serverId = serverId,
        monitorName = monitorName,
        serverName = serverName,
        createdAt = createdAt,
        avgDelay = avgDelay
    )
}