package com.sekusarisu.mdpings.vpings.presentation.models

import com.sekusarisu.mdpings.vpings.domain.WSHost
import com.sekusarisu.mdpings.vpings.domain.WSServer
import com.sekusarisu.mdpings.vpings.domain.WSState

data class WSServerUi(
    val id: Int,
    val name: String,
    val host: WSHostUi,
    val status: WSStateUi,
    val countryCode: String,
    val lastActive: String,
    val isOnline: Boolean
)

data class WSHostUi(
    val platform: String,
    val platformVersion: String,
    val cpu: String,
    val memTotal: Long,
    val diskTotal: Long,
    val swapTotal: Long,
    val arch: String,
    val virtualization: String,
    val bootTime: Long,
    val version: String
)

data class WSStateUi(
    val cpu: Float,
    val memUsed: Long,
    val diskUsed: Long,
    val swapUsed: Long,
    val load1: DoubleDisplayableNumber,
    val load5: DoubleDisplayableNumber,
    val load15: DoubleDisplayableNumber,
    val netInTransfer: LongDisplayableString,
    val netOutTransfer: LongDisplayableString,
    val netInSpeed: NetIOSpeedDisplayableString,
    val netOutSpeed: NetIOSpeedDisplayableString,
    val uptime: Long,
    val tcpConnCount: Int,
    val udpConnCount: Int,
    val processCount: Int
)

fun WSServer.toWSServerUi(): WSServerUi {
    return WSServerUi(
        id = id,
        name = name,
        host = host.toWSHostUi(),
        status = status.toWSStatusUi(),
        countryCode = countryCode.countryCodeCheck().toCountryCodeToEmojiFlag(),
        lastActive = lastActive,
        isOnline = true
    )
}

private fun WSHost.toWSHostUi(): WSHostUi {
    return WSHostUi(
        platform = platform,
        platformVersion = platformVersion,
        cpu = cpu?.joinToString("\n") ?: "N/A",
        memTotal = memTotal,
        diskTotal = diskTotal,
        arch = arch,
        bootTime = bootTime,
        version = version,
        swapTotal = swapTotal ?: 0L,
        virtualization = virtualization
    )
}

private fun WSState.toWSStatusUi(): WSStateUi {
    return WSStateUi(
        memUsed = memUsed,
        diskUsed = diskUsed,
        swapUsed = swapUsed ?: 0L,
        netInTransfer = netInTransfer.toNetTRLongDisplayableString(),
        netOutTransfer = netOutTransfer.toNetTRLongDisplayableString(),
        netInSpeed = netInSpeed.toNetIOSpeedDisplayableString(),
        netOutSpeed = netOutSpeed.toNetIOSpeedDisplayableString(),
        uptime = uptime,
        tcpConnCount = tcpConnCount,
        udpConnCount = udpConnCount,
        processCount = processCount,
        cpu = cpu ?: 0f,
        load1 = load1?.toDisplayableNumber() ?: 0.0.toDisplayableNumber(),
        load5 = load5?.toDisplayableNumber() ?: 0.0.toDisplayableNumber(),
        load15 = load15?.toDisplayableNumber() ?: 0.0.toDisplayableNumber()
    )
}

//fun String.countryCodeCheck(): String {
//    return if (this.lowercase() == "tw") "cn" else this
//}
//
//fun String.toCountryCodeToEmojiFlag(): String {
//    return this
//        .uppercase(Locale.US)
//        .map { char ->
//            Character.codePointAt("$char", 0) - 0x41 + 0x1F1E6
//        }
//        .map { codePoint ->
//            Character.toChars(codePoint)
//        }
//        .joinToString(separator = "") { charArray ->
//            String(charArray)
//        }
//}
//
//data class DoubleDisplayableNumber(
//    val value: Double,
//    val formatted: String
//)
//
//fun Double.toDisplayableNumber(): DoubleDisplayableNumber {
//    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
//        minimumFractionDigits = 2
//        maximumFractionDigits = 2
//    }
//    return DoubleDisplayableNumber(
//        value = this,
//        formatted = formatter.format(this)
//    )
//}
//
//data class NetIOSpeedDisplayableString(
//    val value: Long,
//    val formatted: String
//)
//
//fun Long.toNetIOSpeedDisplayableString(): NetIOSpeedDisplayableString {
//    val speed = when {
//        this / 1024 < 1024 -> "${String.format(Locale.US, "%.2f", this / 1024.0)} K/s"
//        this / 1024 / 1024 < 1024 -> "${String.format(Locale.US, "%.2f", this / 1024.0.pow(2.0))} M/s"
//        else -> "${String.format(Locale.US, "%.2f", this / 1024.0.pow(3.0))} G/s"
//    }
//    return NetIOSpeedDisplayableString(
//        value = this,
//        formatted = speed
//    )
//}
//
//data class LongDisplayableString(
//    val value: Long,
//    val formatted: String
//)
//
//fun Long.toNetTRLongDisplayableString(): LongDisplayableString {
//    val transfer = when {
//        this / 1024 < 1024 -> "${String.format(Locale.US, "%.2f", this / 1024.0)} K"
//        this / 1024.0.pow(2.0) < 1024 -> "${String.format(Locale.US, "%.2f", this / 1024.0.pow(2.0))} M"
//        this / 1024.0.pow(3.0) < 1024 -> "${String.format(Locale.US, "%.2f", this / 1024.0.pow(3.0))} G"
//        this / 1024.0.pow(4.0) < 1024 -> "${String.format(Locale.US, "%.2f", this / 1024.0.pow(4.0))} T"
//        else -> "${String.format(Locale.US, "%.2f", this / 1024.0.pow(5.0))} P"
//    }
//    return LongDisplayableString(
//        value = this,
//        formatted = transfer
//    )
//}
//
//fun Long.toMemDiskLongDisplayableString(): String {
//    val transfer = when {
//        this / 1024 < 1024 -> "${String.format(Locale.US, "%.2f", this / 1024.0)} KB"
//        this / 1024.0.pow(2.0) < 1024 -> "${String.format(Locale.US, "%.2f", this / 1024.0.pow(2.0))} MB"
//        this / 1024.0.pow(3.0) < 1024 -> "${String.format(Locale.US, "%.2f", this / 1024.0.pow(3.0))} GB"
//        this / 1024.0.pow(4.0) < 1024 -> "${String.format(Locale.US, "%.2f", this / 1024.0.pow(4.0))} TB"
//        else -> "${String.format(Locale.US, "%.2f", this / 1024.0.pow(5.0))} PB"
//    }
//    return transfer
//}
//
//fun Long.toISOnline(): Boolean {
//    val current = System.currentTimeMillis() / 1000
//    val timeDifference = abs(current - this)
//    return timeDifference <= 600
//}