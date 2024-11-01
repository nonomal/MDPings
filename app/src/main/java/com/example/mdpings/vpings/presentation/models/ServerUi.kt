package com.example.mdpings.vpings.presentation.models

import com.example.mdpings.vpings.domain.Host
import com.example.mdpings.vpings.domain.Server
import com.example.mdpings.vpings.domain.Status

data class ServerUi(
    val id: Int,
    val name: String,
    val tag: String,
    val lastActive: Int,
    val ipv4: String,
    val ipv6: String,
    val validIp: String,
    val displayIndex: Int,
    val hideForGuest: Boolean,
    val host: HostUi,
    val status: StatusUi
)

data class HostUi(
    val platform: String,
    val platformVersion: String,
    val cpu: List<String>,
    val memTotal: Long,
    val diskTotal: Long,
    val swapTotal: Int,
    val arch: String,
    val virtualization: String,
    val bootTime: Int,
    val countryCode: String,
    val version: String,
//    val GPU: Any
)

data class StatusUi(
    val cpu: Double,
    val memUsed: Long,
    val swapUsed: Int,
    val diskUsed: Long,
    val netInTransfer: Long,
    val netOutTransfer: Long,
    val netInSpeed: Int,
    val netOutSpeed: Int,
    val uptime: Int,
    val load1: Double,
    val load5: Double,
    val load15: Double,
    val tcpConnCount: Int,
    val udpConnCount: Int,
    val processCount: Int,
//    val Temperatures: Any,
    val gpu: Int
)

data class DisplayableNumber(
    val value: Double,
    val formatted: String
)

fun Server.toServerUi(): ServerUi {
    return ServerUi(
        id = id,
        name = name,
        tag = tag,
        lastActive = lastActive,
        ipv4 = ipv4,
        ipv6 = ipv6,
        validIp = validIp,
        displayIndex = displayIndex,
        hideForGuest = hideForGuest,
        host = host.toHostUi(),
        status = status.toStatusUi()
    )
}

private fun Host.toHostUi(): HostUi {
    return HostUi(
        platform = platform,
        platformVersion = platformVersion,
        cpu = cpu,
        memTotal = memTotal,
        diskTotal = diskTotal,
        swapTotal = swapTotal,
        arch = arch,
        virtualization = virtualization,
        bootTime = bootTime,
        countryCode = countryCode,
        version = version
    )
}

private fun Status.toStatusUi(): StatusUi {
    return StatusUi(
        cpu = cpu,
        memUsed = memUsed,
        swapUsed = swapUsed,
        diskUsed = diskUsed,
        netInTransfer = netInTransfer,
        netOutTransfer = netOutTransfer,
        netInSpeed = netInSpeed,
        netOutSpeed = netOutSpeed,
        uptime = uptime,
        load1 = load1,
        load5 = load5,
        load15 = load15,
        tcpConnCount = tcpConnCount,
        udpConnCount = udpConnCount,
        processCount = processCount,
        gpu = gpu
    )
}

