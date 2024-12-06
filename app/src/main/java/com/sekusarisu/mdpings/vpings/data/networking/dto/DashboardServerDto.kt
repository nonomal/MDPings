package com.sekusarisu.mdpings.vpings.data.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardServerDto(
    val id: Int,
    @SerialName(value = "created_at") val createdAt: String,
    @SerialName(value = "updated_at") val updatedAt: String,
    val name: String,
    val uuid: String,
    @SerialName(value = "display_index") val displayIndex: Int,
    val host: Host,
    val state: State,
    val geoip: Geoip,
    @SerialName(value = "last_active") val lastActive: String
)

@Serializable
data class Host(
    val platform: String = "unknown",
    @SerialName(value = "platform_version") val platformVersion: String,
    val cpu: List<String>,
    @SerialName(value = "mem_total") val memTotal: Long,
    @SerialName(value = "disk_total") val diskTotal: Long,
    @SerialName(value = "swap_total") val swapTotal: Long = 0,
    val arch: String = "unknown",
    val virtualization: String = "unknown",
    @SerialName(value = "boot_time") val bootTime: Long,
    val version: String = "unknown"
)

@Serializable
data class State(
    val cpu: Double = 0.0,
    @SerialName(value = "mem_used") val memUsed: Long,
    @SerialName(value = "disk_used") val diskUsed: Long,
    @SerialName(value = "net_in_transfer") val netInTransfer: Long,
    @SerialName(value = "net_out_transfer") val netOutTransfer: Long,
    @SerialName(value = "net_in_speed") val netInSpeed: Long,
    @SerialName(value = "net_out_speed") val netOutSpeed: Long,
    val uptime: Long,
    @SerialName(value = "tcp_conn_count") val tcpConnCount: Long,
    @SerialName(value = "udp_conn_count") val udpConnCount: Long,
    @SerialName(value = "process_count") val processCount: Long
)

@Serializable
data class Geoip(
    val ip: Ip,
    @SerialName(value = "country_code") val countryCode: String
)

@Serializable
data class Ip(
    @SerialName(value = "ipv4_addr") val ipv4Addr: String,
    @SerialName(value = "ipv6_addr") val ipv6Addr: String = "N/A"
)