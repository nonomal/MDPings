package com.sekusarisu.mdpings.vpings.domain

//data class WSServer(
//    val id: Int,
//    val name: String,
//    val host: WSHost,
//    val state: WSState,
//    val countryCode: String,
//    val lastActive: String
//)
//
//data class WSHost(
//    val platform: String,
//    val platformVersion: String,
//    val cpu: List<String>,
//    val memTotal: Long,
//    val diskTotal: Long,
//    val arch: String,
//    val bootTime: Long,
//    val version: String
//)
//
//data class WSState(
//    val memUsed: Long,
//    val diskUsed: Long,
//    val netInTransfer: Long,
//    val netOutTransfer: Long,
//    val netInSpeed: Long,
//    val netOutSpeed: Long,
//    val uptime: Long,
//    val tcpConnCount: Int,
//    val udpConnCount: Int,
//    val processCount: Int
//)

data class Server(
    val id: Int,
    val name: String,
    val tag: String,
    val lastActive: Long,
    val ipv4: String,
    val ipv6: String,
    val validIp: String,
    val displayIndex: Int,
    val hideForGuest: Boolean,
    val host: Host,
    val status: Status
)

data class Host(
    val platform: String,
    val platformVersion: String,
    val cpu: List<String>?,
    val memTotal: Long,
    val diskTotal: Long,
    val swapTotal: Long,
    val arch: String,
    val virtualization: String,
    val bootTime: Int,
    val countryCode: String,
    val version: String,
//    @SerializedName(value = "GPU") val gpu: Any
)

data class Status(
    val cpu: Double,
    val memUsed: Long,
    val swapUsed: Long,
    val diskUsed: Long,
    val netInTransfer: Long,
    val netOutTransfer: Long,
    val netInSpeed: Long,
    val netOutSpeed: Long,
    val uptime: Long,
    val load1: Double,
    val load5: Double,
    val load15: Double,
    val tcpConnCount: Int,
    val udpConnCount: Int,
    val processCount: Int,
//    @SerializedName(value = "Temperatures") val temperatures: Any,
    val gpu: Int
)