package com.sekusarisu.mdpings.vpings.data.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WSServerDto(
    val id: Int,
    val name: String,
    @SerialName(value = "display_index") val displayIndex: Int = 0,
    val host: WSHostDto,
    val state: WSStateDto,
    @SerialName(value = "country_code") val countryCode: String = "un",
    @SerialName(value = "last_active") val lastActive: String
)

@Serializable
data class WSHostDto(
    val platform: String = "unknown",
    @SerialName(value = "platform_version") val platformVersion: String = "unknown",
    val cpu: List<String> = listOf<String>("unknown"),
    @SerialName(value = "mem_total") val memTotal: Long = 0L,
    @SerialName(value = "disk_total") val diskTotal: Long = 0L,
    @SerialName(value = "swap_total") val swapTotal: Long = 0L,
    val arch: String = "unknown",
    val virtualization: String = "unknown",
    @SerialName(value = "boot_time") val bootTime: Long = 0L,
    val version: String = "unknown"
)

@Serializable
data class WSStateDto(
    val cpu: Float = 0F,
    @SerialName(value = "mem_used") val memUsed: Long = 0L,
    @SerialName(value = "disk_used") val diskUsed: Long = 0L,
    @SerialName(value = "swap_used") val swapUsed: Long = 0L,
    @SerialName(value = "load_1") val load1: Double = 0.0,
    @SerialName(value = "load_5") val load5: Double = 0.0,
    @SerialName(value = "load_15") val load15: Double = 0.0,
    @SerialName(value = "net_in_transfer") val netInTransfer: Long = 0L,
    @SerialName(value = "net_out_transfer") val netOutTransfer: Long = 0L,
    @SerialName(value = "net_in_speed") val netInSpeed: Long = 0L,
    @SerialName(value = "net_out_speed") val netOutSpeed: Long = 0L,
    val uptime: Long = 0L,
    @SerialName(value = "tcp_conn_count") val tcpConnCount: Int = 0,
    @SerialName(value = "udp_conn_count") val udpConnCount: Int = 0,
    @SerialName(value = "process_count") val processCount: Int = 0
)

