package com.sekusarisu.mdpings.vpings.data.mappers

import com.sekusarisu.mdpings.vpings.data.networking.dto.WSHostDto
import com.sekusarisu.mdpings.vpings.data.networking.dto.WSServerDto
import com.sekusarisu.mdpings.vpings.data.networking.dto.WSStateDto
import com.sekusarisu.mdpings.vpings.data.networking.dto.WSTemperatureDto
import com.sekusarisu.mdpings.vpings.domain.WSHost
import com.sekusarisu.mdpings.vpings.domain.WSServer
import com.sekusarisu.mdpings.vpings.domain.WSState
import com.sekusarisu.mdpings.vpings.domain.WSTemperatures

fun WSServerDto.toWSServer(): WSServer {
    return WSServer(
        id = id,
        name = name,
        displayIndex = displayIndex,
        host = host.toWSHost(),
        status = state.toWSState(),
        countryCode = countryCode,
        lastActive = lastActive,
        publicNote = publicNote
    )
}

fun WSHostDto.toWSHost(): WSHost {
    return WSHost(
        platform = platform,
        platformVersion = platformVersion,
        cpu = cpu,
        memTotal = memTotal,
        diskTotal = diskTotal,
        swapTotal = swapTotal,
        arch = arch,
        virtualization = virtualization,
        bootTime = bootTime,
        version = version,
        gpu = gpu
    )
}

fun WSStateDto.toWSState(): WSState {
    return WSState(
        cpu = cpu,
        memUsed = memUsed,
        diskUsed = diskUsed,
        swapUsed = swapUsed,
        load1 = load1,
        load5 = load5,
        load15 = load15,
        netInTransfer = netInTransfer,
        netOutTransfer = netOutTransfer,
        netInSpeed = netInSpeed,
        netOutSpeed = netOutSpeed,
        uptime = uptime,
        tcpConnCount = tcpConnCount,
        udpConnCount = udpConnCount,
        processCount = processCount,
        gpu = gpu,
        temperatures = temperatures.map { it.toWSTemperatures() }
    )
}

fun WSTemperatureDto.toWSTemperatures(): WSTemperatures {
    return WSTemperatures(
        name = name,
        temperature = temperature
    )
}