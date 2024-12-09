package com.sekusarisu.mdpings.vpings.domain

data class WSServer(
    val id: Int,
    val name: String,
    val host: WSHost,
    val displayIndex: Int?,
    val status: WSState,
    val countryCode: String?,
    val lastActive: String?
)

data class WSHost(
    val platform: String?,
    val platformVersion: String?,
    val cpu: List<String>?,
    val memTotal: Long?,
    val diskTotal: Long?,
    val swapTotal: Long?,
    val arch: String?,
    val virtualization: String?,
    val bootTime: Long?,
    val version: String?
)

data class WSState(
    val cpu: Float?,
    val memUsed: Long?,
    val diskUsed: Long?,
    val swapUsed: Long?,
    val load1: Double?,
    val load5: Double?,
    val load15: Double?,
    val netInTransfer: Long?,
    val netOutTransfer: Long?,
    val netInSpeed: Long?,
    val netOutSpeed: Long?,
    val uptime: Long?,
    val tcpConnCount: Int?,
    val udpConnCount: Int?,
    val processCount: Int?
)