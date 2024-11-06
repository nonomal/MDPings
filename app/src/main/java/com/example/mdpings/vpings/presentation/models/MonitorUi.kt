package com.example.mdpings.vpings.presentation.models

import com.example.mdpings.vpings.domain.Monitor
import com.example.mdpings.vpings.domain.Server

data class MonitorUi(
    val monitorId: Int,
    val serverId: Int,
    val monitorName: String,
    val serverName: String,
    val createdAt: List<Long>,
    val avgDelay: List<Double>
)

fun Monitor.toMonitorUi(): MonitorUi {
    return MonitorUi(
        monitorId = monitorId,
        serverId = serverId,
        monitorName = monitorName,
        serverName = serverName,
        createdAt = createdAt,
        avgDelay = avgDelay
    )
}