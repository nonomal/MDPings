package com.sekusarisu.mdpings.vpings.data.mappers

import com.sekusarisu.mdpings.vpings.data.networking.dto.HostDto
import com.sekusarisu.mdpings.vpings.data.networking.dto.LoginDataDto
import com.sekusarisu.mdpings.vpings.data.networking.dto.ServerDto
import com.sekusarisu.mdpings.vpings.data.networking.dto.StatusDto
import com.sekusarisu.mdpings.vpings.domain.Host
import com.sekusarisu.mdpings.vpings.domain.LoginData
import com.sekusarisu.mdpings.vpings.domain.Server
import com.sekusarisu.mdpings.vpings.domain.Status

fun LoginDataDto.toLoginData(): LoginData {
    return LoginData(
        token = token,
        expire = expire
    )
}

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