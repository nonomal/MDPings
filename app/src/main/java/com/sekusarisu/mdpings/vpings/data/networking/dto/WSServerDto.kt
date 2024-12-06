package com.sekusarisu.mdpings.vpings.data.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WSServerDto(
    val id: Int,
    val name: String,
    val host: WSHostDto,
    val state: WSStateDto,
    @SerialName(value = "country_code") val countryCode: String = "un",
    @SerialName(value = "last_active") val lastActive: String
)

@Serializable
data class WSHostDto(
    val platform: String,
    @SerialName(value = "platform_version") val platformVersion: String = "unknown",
    val cpu: List<String>,
    @SerialName(value = "mem_total") val memTotal: Long,
    @SerialName(value = "disk_total") val diskTotal: Long,
    @SerialName(value = "swap_total") val swapTotal: Long = 0L,
    val arch: String,
    val virtualization: String = "unknown",
    @SerialName(value = "boot_time") val bootTime: Long,
    val version: String = "unknown"
)

@Serializable
data class WSStateDto(
    val cpu: Float = 0F,
    @SerialName(value = "mem_used") val memUsed: Long,
    @SerialName(value = "disk_used") val diskUsed: Long,
    @SerialName(value = "swap_used") val swapUsed: Long = 0L,
    @SerialName(value = "load_1") val load1: Double = 0.0,
    @SerialName(value = "load_5") val load5: Double = 0.0,
    @SerialName(value = "load_15") val load15: Double = 0.0,
    @SerialName(value = "net_in_transfer") val netInTransfer: Long,
    @SerialName(value = "net_out_transfer") val netOutTransfer: Long,
    @SerialName(value = "net_in_speed") val netInSpeed: Long,
    @SerialName(value = "net_out_speed") val netOutSpeed: Long,
    val uptime: Long,
    @SerialName(value = "tcp_conn_count") val tcpConnCount: Int,
    @SerialName(value = "udp_conn_count") val udpConnCount: Int,
    @SerialName(value = "process_count") val processCount: Int
)

