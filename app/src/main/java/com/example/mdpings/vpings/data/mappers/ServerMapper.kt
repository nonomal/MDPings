package com.example.mdpings.vpings.data.mappers

import com.example.mdpings.vpings.data.networking.dto.HostDto
import com.example.mdpings.vpings.data.networking.dto.ServerDto
import com.example.mdpings.vpings.data.networking.dto.StatusDto
import com.example.mdpings.vpings.domain.Host
import com.example.mdpings.vpings.domain.Server
import com.example.mdpings.vpings.domain.Status

fun ServerDto.toServer(): Server {
    return Server(
        id = id,
        name = name,
        tag = tag,
        lastActive = lastActive,
        ipv4 = ipv4,
        ipv6 = ipv6,
        validIp = validIp,
        displayIndex = displayIndex,
        hideForGuest = hideForGuest,
        host = host.toHost(),
        status = status.toStatus()
    )
}

private fun HostDto.toHost(): Host {
     return Host(
         platform = platform,
         platformVersion = platformVersion,
         cpu = cpu ?: listOf("N/A"),
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

private fun StatusDto.toStatus(): Status {
    return Status(
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