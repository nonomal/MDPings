package com.example.mdpings.vpings.presentation.server_list.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.NetworkPing
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.domain.Monitor
import com.example.mdpings.vpings.presentation.delay_chart.MonitorsChart
import com.example.mdpings.vpings.presentation.delay_chart.delayList1
import com.example.mdpings.vpings.presentation.delay_chart.delayList2
import com.example.mdpings.vpings.presentation.delay_chart.delayList3
import com.example.mdpings.vpings.presentation.delay_chart.delayListDate1
import com.example.mdpings.vpings.presentation.delay_chart.delayListDate2
import com.example.mdpings.vpings.presentation.delay_chart.delayListDate3
import com.example.mdpings.vpings.presentation.delay_chart.mediumLineModel
import com.example.mdpings.vpings.presentation.models.HostUi
import com.example.mdpings.vpings.presentation.models.MonitorUi
import com.example.mdpings.vpings.presentation.models.ServerUi
import com.example.mdpings.vpings.presentation.models.StatusUi
import com.example.mdpings.vpings.presentation.models.toCountryCodeToEmojiFlag
import com.example.mdpings.vpings.presentation.models.toDisplayableNumber
import com.example.mdpings.vpings.presentation.models.toLongDisplayableString
import com.example.mdpings.vpings.presentation.models.toNetIOSpeedDisplayableString
import com.example.mdpings.vpings.presentation.server_list.ServerListAction
import com.example.mdpings.vpings.presentation.user_login.LoginAction
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

private fun String.toLetterSpacing() = when(this) {
    "CPU" -> 5.5.sp
    "RAM" -> 4.5.sp
    "SWAP" -> 0.7.sp
    "DISK" -> 3.sp
    "NetIO" -> 1.2.sp
    "NetTR" -> 0.sp
    "LOAD" -> 1.7.sp
    else -> 0.sp
}

@Composable
fun ServerListItem(
    monitors: List<MonitorUi>,
    serverUi: ServerUi,
    onAction: (ServerListAction) -> Unit,
    onClick: (ServerUi) -> Unit,
    modifier: Modifier = Modifier
) {

    var isExtended by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.wrapContentHeight(),
        shape = ShapeDefaults.Medium,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
// OnClickSample
//        onClick = {
//            onAction(
//                LoginAction.OnTestClick(api, token)
//            )
//        },
    ) {
        ServerTitle(serverUi)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.MonitorHeart,
                    contentDescription = null,
                    modifier = Modifier
                        .alpha(0.7f)
                )
                Text(
                    text = "Status",
                    modifier = Modifier
                        .alpha(0.7f)
                        .padding(horizontal = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Status(serverUi)
            Spacer(modifier = Modifier.height(2.dp))
            NetworkIO(serverUi)
            Spacer(modifier = Modifier.height(2.dp))
            NetworkTransfer(serverUi)
            Spacer(modifier = Modifier.height(2.dp))
            LoadAndUptime(serverUi)
            Spacer(modifier = Modifier.height(8.dp))
            Monitor(
                monitors = monitors,
                isExtended = isExtended
            )
            IconButton(
                onClick = {
                    onAction(
                        ServerListAction.OnExpandClick(id = serverUi.id)
                    )
                    isExtended = !isExtended
                },
                enabled = true
            ) {
                Icon(
                    imageVector = if (isExtended) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier
                        .alpha(0.7f)
                )
            }
        }
    }
}

//@PreviewLightDark
//@Composable
//fun ServerCardPreview() {
//    MDPingsTheme {
//        ServerListItem(
//            serverUi = previewServerUi0,
//            onClick = {},
//            onAction = {},
//            modifier = Modifier
//        )
//    }
//}

@Composable
fun Monitor(
    monitors: List<MonitorUi> = emptyList(),
    isExtended: Boolean
) {

    val modelProducer = remember { CartesianChartModelProducer() }

    if (isExtended && monitors.isNotEmpty()) {
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                modelProducer.runTransaction {
                    lineSeries {
                        monitors.map { monitor ->
                            series(
                                x = monitor.createdAt,
                                y = monitor.avgDelay
                            )
                        }
                    }
                }
            }
        }

        Column {
            HorizontalDivider()
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.NetworkPing,
                    contentDescription = null,
                    modifier = Modifier
                        .alpha(0.7f)
                )
                Text(
                    text = "Network",
                    modifier = Modifier
                        .alpha(0.7f)
                        .padding(horizontal = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            MonitorsChart(
//                model = mediumLineModel,
                modelProducer = modelProducer,
                modifier = Modifier
                    .sizeIn(maxHeight = 240.dp)
            )
        }
    }
}

@Composable
fun ServerTitle(server: ServerUi) {
    FilterChip(
// TODO 根据服务器LastActive更改chip的颜色
//        colors = FilterChipDefaults.filterChipColors().copy(
//            if (server.lastActive)
//        ),
        modifier = Modifier
            .padding(horizontal = 12.dp),
        leadingIcon = {
            Text(text = server.host.countryCode)
        },
        trailingIcon = {
            Text(
                text = "${server.host.platform}${server.host.platformVersion}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .alpha(0.7f)
            )
        },
        shape = ShapeDefaults.Large,
        selected = true,
        onClick = {},
        label = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = server.name,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    )
}

@Composable
private fun Status(server: ServerUi) {
    ProgressBar(
        text = "CPU",
        total = 100F,
        used = server.status.cpu.toFloat()*10
    )
    Spacer(modifier = Modifier.height(2.dp))
    ProgressBar(
        text = "RAM",
        total = server.host.memTotal.toFloat(),
        used = server.status.memUsed.toFloat()
    )
    Spacer(modifier = Modifier.height(2.dp))
    ProgressBar(
        text = "SWAP",
        total = server.host.swapTotal.toFloat(),
        used = server.status.swapUsed.toFloat()
    )
    Spacer(modifier = Modifier.height(2.dp))
    ProgressBar(
        text = "DISK",
        total = server.host.diskTotal.toFloat(),
        used = server.status.diskUsed.toFloat()
    )
}

@Composable
fun ProgressBar(text: String, total: Float, used: Float) {

    val progress = if (total > 0) (used / total).toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000),
        label = "AnimationProgressBar"
    )
    val progressBarColor =
        if (animatedProgress <= 0.25f) MaterialTheme.colorScheme.tertiary
        else if (animatedProgress <= 0.75f) MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.error
    val animatedColor by animateColorAsState(
        animationSpec = tween(durationMillis = 1000),
        targetValue = progressBarColor,
        label = "AnimationColorProgressBar"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium.copy(
                    letterSpacing = text.toLetterSpacing()
                ),
                modifier = Modifier
                    .width(48.dp)
                    .alpha(0.8f)
            )
        }
        LinearProgressIndicator(
            progress = { animatedProgress },
            color = animatedColor,
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f)
                .height(4.5.dp)
        )
        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            modifier = Modifier
                .width(48.dp),
            textAlign = TextAlign.End
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun LoadAndUptime(server: ServerUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
        ) {
            Text(
                text = "LOAD",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium.copy(
                    letterSpacing = "LOAD".toLetterSpacing()
                ),
                modifier = Modifier
                    .width(48.dp)
                    .alpha(0.8f)
            )
        }
        AnimatedContent(
            targetState = server.status,
            label = "AnimatedNetworkIO",
            transitionSpec = {
                fadeIn() + slideInVertically(
                    animationSpec = tween(1000),
                    initialOffsetY = { fullHeight -> fullHeight }
                ) togetherWith fadeOut(animationSpec = tween(200))
            },
            modifier = Modifier
                .weight(3.2f)
        ) { it ->
            Text(
                textAlign = TextAlign.Center,
                text = "${it.load1.formatted} | ${it.load5.formatted} | ${it.load15.formatted}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = "UPTIME",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .alpha(0.8f)
        )
        Text(
            textAlign = TextAlign.Center,
            text = "${server.status.uptime / 86400} D",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
private fun NetworkTransfer(server: ServerUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = "NetTR",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .width(48.dp)
                    .alpha(0.8f)
            )
        }
        Icon(
            imageVector = Icons.Rounded.Download,
            contentDescription = null,
            modifier = Modifier
                .weight(0.5f)
                .alpha(0.7f)
        )
        Text(
            text = server.status.netInTransfer.formatted,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
        )
        Icon(
            imageVector = Icons.Rounded.Upload,
            contentDescription = null,
            modifier = Modifier
                .weight(0.5f)
                .alpha(0.7f)
        )
        Text(
            text = server.status.netOutTransfer.formatted,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun NetworkIO(server: ServerUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = "NetIO",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium.copy(
                    letterSpacing = "NetIO".toLetterSpacing()
                ),
                modifier = Modifier
                    .width(48.dp)
                    .alpha(0.8f)
            )
        }
        Icon(
            imageVector = Icons.Rounded.ArrowDownward,
            contentDescription = null,
            modifier = Modifier
                .weight(0.5f)
                .alpha(0.7f)
        )
        AnimatedContent(
            targetState = server.status.netInSpeed,
            label = "AnimatedNetworkIn",
            transitionSpec = {
                fadeIn() + slideInVertically(
                    animationSpec = tween(1000),
                    initialOffsetY = { fullHeight -> fullHeight }
                ) togetherWith fadeOut(animationSpec = tween(200))
            },
            modifier = Modifier
                .weight(1f)
        ) { it ->
            Text(
                text = it.formatted,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Icon(
            imageVector = Icons.Rounded.ArrowUpward,
            contentDescription = null,
            modifier = Modifier
                .weight(0.5f)
                .alpha(0.7f)
        )
        AnimatedContent(
            targetState = server.status.netOutSpeed,
            label = "AnimatedNetworkOut",
            transitionSpec = {
                fadeIn() + slideInVertically(
                    animationSpec = tween(1000),
                    initialOffsetY = { fullHeight -> fullHeight }
                ) togetherWith fadeOut(animationSpec = tween(200))
            },
            modifier = Modifier
                .weight(1f)
        ) { it ->
            Text(
                text = it.formatted,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

// PreviewData
private fun previewHostUi(): HostUi {
    return HostUi(
        platform = "ubuntu",
        platformVersion = "22.04",
        cpu = listOf("AMD EPYC 7543P 32-Core Processor 4 Virtual Core"),
        memTotal = 8323002368,
        diskTotal = 2164154892288,
        swapTotal = 267362304,
        arch = "x86_64",
        virtualization = "kvm",
        bootTime = 1725353936,
        countryCode = listOf<String>(
            "hk", "nl", "us", "cn", "ru", "jp"
        )[Random.nextInt(until = 6)]
            .toCountryCodeToEmojiFlag(),
        version = "0.20.3"
    )
}

private fun previewStatusUi(): StatusUi {
    return StatusUi(
        cpu = Random.nextDouble(from = 0.0, until = 10.0),
        memUsed = Random.nextLong(until = 8323002368),
        swapUsed = Random.nextInt(until = 267362304),
        diskUsed = Random.nextLong(until = 2164154892288),
        netInTransfer = Random.nextLong(from = 0, until = 1024000000000000).toLongDisplayableString(),
        netOutTransfer = Random.nextLong(from = 0, until = 1024000000000000000).toLongDisplayableString(),
        netInSpeed = Random.nextInt(until = 1024000000).toNetIOSpeedDisplayableString(),
        netOutSpeed = Random.nextInt(until = 1024000000).toNetIOSpeedDisplayableString(),
        uptime = Random.nextInt(until = 102400000),
        load1 = Random.nextDouble(until = 200.0).toDisplayableNumber(),
        load5 = Random.nextDouble(until = 200.0).toDisplayableNumber(),
        load15 = Random.nextDouble(until = 200.0).toDisplayableNumber(),
        tcpConnCount = 63,
        udpConnCount = 97,
        processCount = 296,
        gpu = 0
    )
}

internal val previewServerUi0 = ServerUi(
    id = 0,
    name = "Server0",
    tag = "",
    lastActive = 1730554945,
    ipv4 = "1.1.1.1",
    ipv6 = "2001:0000:130F:0000:0000:09C0:876A:130B",
    validIp = "1.1.1.1",
    displayIndex = 0,
    hideForGuest = false,
    host = previewHostUi(),
    status = previewStatusUi()
)

internal val previewServerUi1 = ServerUi(
    id = 1,
    name = "Server1",
    tag = "",
    lastActive = 1730554945,
    ipv4 = "1.1.1.1",
    ipv6 = "2001:0000:130F:0000:0000:09C0:876A:130B",
    validIp = "1.1.1.1",
    displayIndex = 0,
    hideForGuest = false,
    host = previewHostUi(),
    status = previewStatusUi()
)

internal val previewServerUi2 = ServerUi(
    id = 2,
    name = "Server2",
    tag = "",
    lastActive = 1730554945,
    ipv4 = "1.1.1.1",
    ipv6 = "2001:0000:130F:0000:0000:09C0:876A:130B",
    validIp = "1.1.1.1",
    displayIndex = 0,
    hideForGuest = false,
    host = previewHostUi(),
    status = previewStatusUi()
)

internal val previewServerUi3 = ServerUi(
    id = 3,
    name = "Server3",
    tag = "",
    lastActive = 1730554945,
    ipv4 = "1.1.1.1",
    ipv6 = "2001:0000:130F:0000:0000:09C0:876A:130B",
    validIp = "1.1.1.1",
    displayIndex = 0,
    hideForGuest = false,
    host = previewHostUi(),
    status = previewStatusUi()
)

internal val previewListServers = listOf<ServerUi>(
    previewServerUi0, previewServerUi1, previewServerUi2, previewServerUi3
)