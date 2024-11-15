package com.sekusarisu.mdpings.vpings.presentation.server_detail.components

import com.sekusarisu.mdpings.vpings.presentation.models.IpAPIUi
import com.sekusarisu.mdpings.vpings.presentation.models.MonitorUi
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import kotlin.random.Random

// 模拟数据
public fun mockLineModel(): CartesianChartModel {
    return CartesianChartModel(
        LineCartesianLayerModel.build {
            series(x = mockEpoch, y = mockDelayList())
            series(x = mockEpoch, y = mockDelayList())
            series(x = mockEpoch, y = mockDelayList())
            series(x = mockEpoch, y = mockDelayList())
            series(x = mockEpoch, y = mockDelayList())
            series(x = mockEpoch, y = mockDelayList())
            series(x = mockEpoch, y = mockDelayList())
            series(x = mockEpoch, y = mockDelayList())
            series(x = mockEpoch, y = mockDelayList())
            series(x = mockEpoch, y = mockDelayList())
            series(x = mockEpoch, y = mockDelayList())
            series(x = mockEpoch, y = mockDelayList())
        }
    )
}

// mock monitor_name
public val monitor_name = listOf<String>(
    "重庆电信","重庆联通","重庆移动",
    "北京电信","北京联通","北京移动",
    "上海电信","上海联通","上海移动",
    "广东电信","广东联通","广东移动",
)

// mock delay
public fun mockDelayList(): List<Number> {
    return List(360) {
        Random.nextDouble(100.0, 1000.0)
    }
}

// mock time Epoch
public val mockEpoch: List<Number> = List(360) { index ->
        1730711640000 + 240000 * (index + 1)
    }

public val mockMonitors: List<MonitorUi> = List(12) { index ->
    MonitorUi(
        monitorId = index,
        serverId = 6,
        monitorName = monitor_name[index],
        serverName = "WAP.AC",
        createdAt = mockEpoch.map { it.toLong() },
        avgDelay = mockDelayList().map { it.toDouble() },
        pktLoss24h = "12.10%",
        avgDelay24h = "111.4ms",
        avgDelay30mins = "111.2ms",
        pktLoss30mins = "6.78%",
    )
}

public val mockIpAPIUi = IpAPIUi(
    query = "24.48.0.1",
    status = "success",
    country = "Canada",
    countryCode = "CA",
    region = "QC",
    regionName = "Quebec",
    city = "Montreal",
    zip = "H1L",
    lat = 45.6026,
    lon = -73.5167,
    timezone = "America/Toronto",
    isp = "Le Groupe Videotron Ltee",
    org = "Videotron Ltee",
    `as` = "AS5769 Videotron Ltee"
)