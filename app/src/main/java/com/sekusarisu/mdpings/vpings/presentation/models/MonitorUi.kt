package com.sekusarisu.mdpings.vpings.presentation.models

import com.sekusarisu.mdpings.vpings.domain.Monitor

data class MonitorUi(
    val monitorId: Int,
    val serverId: Int,
    val monitorName: String,
    val serverName: String,
    val createdAt: List<Long>,
    val avgDelay: List<Double>,
    val pktLoss24h: String,
    val avgDelay24h: String,
    val avgDelay6h: String,
    val avgDelay3h: String,
    val avgDelay1h: String,
    val avgDelay30mins: String
)

//fun Monitor.toMonitorUi(): MonitorUi {
//
//    // pktLoss24h
//    val timeoutCount = avgDelay.count { it == 1000.0 }
//    val pktLoss24h = timeoutCount.toDouble() / avgDelay.size * 100
//    val formattedPktLoss24h = "%.2f".format(pktLoss24h)
//
//    // filtered timeout avgDelay(1000ms)
//    val filtered = createdAt.zip(avgDelay) { createdAt, avgDelay ->
//        createdAt to avgDelay
//    }.filter { (_, avgDelay) -> avgDelay.toInt() != 1000 }
//
//    // avgDelay24h
//    val avgDelay24h = if (filtered.map { it.second }.isNotEmpty()) {
//        "${filtered.map { it.second }.average()}ms"
//    } else "N/A"
//
//    return MonitorUi(
//        monitorId = monitorId,
//        serverId = serverId,
//        monitorName = monitorName,
//        serverName = serverName,
//        createdAt = filtered.map { it.first },
//        avgDelay = filtered.map { it.second },
//        pktLoss24h = formattedPktLoss24h,
//        avgDelay24h = avgDelay24h,
//        avgDelay6h = getSliceAvgDelay("6 hours", filtered).map { it.second }.average().toString() + "ms",
//        avgDelay3h = getSliceAvgDelay("3 hours", filtered).map { it.second }.average().toString() + "ms",
//        avgDelay1h = getSliceAvgDelay("1 hour", filtered).map { it.second }.average().toString() + "ms",
//        avgDelay30mins = getSliceAvgDelay("30 mins", filtered).map { it.second }.average().toString() + "ms"
//    )
//}
//
//fun getSliceAvgDelay(
//    timeSlice: String,
//    filtered: List<Pair<Long, Double>>
//):List<Pair<Long, Double>> {
//    val cutoffTime = System.currentTimeMillis() - timeSlice.toMilliseconds()
//    return filtered
//        .filter { (createdAt, avgDelay) -> createdAt < cutoffTime }
//}
//
//fun String.toMilliseconds(): Int {
//    return when (this) {
//        "30 mins" -> 30 * 60 * 1000
//        "1 hour" -> 60 * 60 * 1000
//        "3 hours" -> 3 * 60 * 60 * 1000
//        "6 hours" -> 6 * 60 * 60 * 1000
//        else -> 0
//    }
//}

fun Monitor.toMonitorUi(): MonitorUi {
    // 计算丢包率
    val timeoutCount = avgDelay.count { it == 1000.0 }
    val pktLoss24h = "%.2f".format(timeoutCount.toDouble() / avgDelay.size * 100) + "%"

    // 过滤超时数据并将时间戳和延迟配对
    val filtered = createdAt.zip(avgDelay)
        .filter { (_, delay) -> delay != 1000.0 }
        .takeIf { it.isNotEmpty() } // 确保有数据

    // 如果没有有效数据，返回所有统计数据为N/A的对象
    if (filtered == null) {
        return MonitorUi(
            monitorId = monitorId,
            serverId = serverId,
            monitorName = monitorName,
            serverName = serverName,
            createdAt = emptyList(),
            avgDelay = emptyList(),
            pktLoss24h = pktLoss24h,
            avgDelay24h = "N/A",
            avgDelay6h = "N/A",
            avgDelay3h = "N/A",
            avgDelay1h = "N/A",
            avgDelay30mins = "N/A"
        )
    }

    val currentTime = System.currentTimeMillis()

    // 计算不同时间段的平均延迟
    fun calculateAverageDelay(timeMillis: Long): String {
        return filtered
            .filter { (timestamp, _) -> timestamp >= currentTime - timeMillis }
            .map { it.second }
            .takeIf { it.isNotEmpty() }
            ?.average()
            ?.let { "%.2f".format(it) + "ms" }
            ?: "N/A"
    }

    return MonitorUi(
        monitorId = monitorId,
        serverId = serverId,
        monitorName = monitorName,
        serverName = serverName,
        createdAt = filtered.map { it.first },
        avgDelay = filtered.map { it.second },
        pktLoss24h = pktLoss24h,
        avgDelay24h = calculateAverageDelay(TimeConstants.HOURS_24),
        avgDelay6h = calculateAverageDelay(TimeConstants.HOURS_6),
        avgDelay3h = calculateAverageDelay(TimeConstants.HOURS_3),
        avgDelay1h = calculateAverageDelay(TimeConstants.HOURS_1),
        avgDelay30mins = calculateAverageDelay(TimeConstants.MINS_30)
    )
}

object TimeConstants {
    const val MINS_30 = 30 * 60 * 1000L
    const val HOURS_1 = 60 * 60 * 1000L
    const val HOURS_3 = 3 * 60 * 60 * 1000L
    const val HOURS_6 = 6 * 60 * 60 * 1000L
    const val HOURS_24 = 24 * 60 * 60 * 1000L
}




















