package com.sekusarisu.mdpings.vpings.data.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardServerDto(
    @SerialName(value = "created_at") val createdAt: String,
    @SerialName(value = "display_index") val displayIndex: Int,
    val geoip: Geoip,
    val host: Host,
    val id: Int,
    @SerialName(value = "last_active") val lastActive: String,
    val name: String,
    val note: String = "N/A",
    @SerialName(value = "public_note") val publicNote: String = "N/A",
    val state: State,
    @SerialName(value = "updated_at") val updatedAt: String,
    val uuid: String,
)

@Serializable
data class Host(
    val arch: String = "unknown",
    @SerialName(value = "boot_time") val bootTime: Long = 0,
    val cpu: List<String> = listOf<String>("unknown"),
    @SerialName(value = "disk_total") val diskTotal: Long = 0,
    val gpu: List<String> = listOf<String>("N/A"),
    @SerialName(value = "mem_total") val memTotal: Long = 0,
    val platform: String = "unknown",
    @SerialName(value = "platform_version") val platformVersion: String = "unknown",
    @SerialName(value = "swap_total") val swapTotal: Long = 0,
    val version: String = "unknown",
    val virtualization: String = "unknown",
)

@Serializable
data class State(
    val cpu: Double = 0.0,
    @SerialName(value = "disk_used") val diskUsed: Long = 0,
    val gpu: List<Float> = listOf<Float>(0F),
    @SerialName(value = "load_1") val load1: Double = 0.0,
    @SerialName(value = "load_5") val load5: Double = 0.0,
    @SerialName(value = "load_15") val load15: Double = 0.0,
    @SerialName(value = "mem_used") val memUsed: Long = 0,
    @SerialName(value = "net_in_transfer") val netInTransfer: Long = 0,
    @SerialName(value = "net_out_transfer") val netOutTransfer: Long = 0,
    @SerialName(value = "net_in_speed") val netInSpeed: Long = 0,
    @SerialName(value = "net_out_speed") val netOutSpeed: Long = 0,
    @SerialName(value = "process_count") val processCount: Long = 0,
    @SerialName(value = "swap_used") val swapUsed: Long = 0,
    @SerialName(value = "tcp_conn_count") val tcpConnCount: Long = 0,
    @SerialName(value = "udp_conn_count") val udpConnCount: Long = 0,
    val temperatures: List<Temperature> = emptyList(),
    val uptime: Long = 0,
)

@Serializable
data class Temperature(
    @SerialName(value = "Name") val name: String = "N/A",
    @SerialName(value = "Temperature") val temperature: Float = 0F
)

@Serializable
data class Geoip(
    val ip: Ip,
    @SerialName(value = "country_code") val countryCode: String = "un"
)

@Serializable
data class Ip(
    @SerialName(value = "ipv4_addr") val ipv4Addr: String = "1.1.1.1",
    @SerialName(value = "ipv6_addr") val ipv6Addr: String = "N/A"
)