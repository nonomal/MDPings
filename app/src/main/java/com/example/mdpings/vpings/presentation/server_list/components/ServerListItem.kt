package com.example.mdpings.vpings.presentation.server_list.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.presentation.models.HostUi
import com.example.mdpings.vpings.presentation.models.ServerUi
import com.example.mdpings.vpings.presentation.models.StatusUi
import com.example.mdpings.vpings.presentation.models.toCountryCodeToEmojiFlag
import com.example.mdpings.vpings.presentation.models.toDisplayableNumber
import com.example.mdpings.vpings.presentation.models.toLongDisplayableString
import com.example.mdpings.vpings.presentation.models.toNetIOSpeedDisplayableString
import kotlin.random.Random

private fun String.toLetterSpacing() = when(this) {
    "CPU" -> 4.7.sp
    "RAM" -> 3.8.sp
    "SWAP" -> 0.5.sp
    "DISK" -> 2.5.sp
    "NetIO" -> 1.sp
    "NetTR" -> 0.sp
    "LOAD" -> 1.5.sp
    else -> 0.sp
}

@Composable
fun ServerListItem(
    serverUi: ServerUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
        modifier = modifier.wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = modifier
                .padding(12.dp)
        ) {
            ServerTitle(serverUi)
            HorizontalDivider()
            Spacer(modifier = Modifier.height(2.dp))
            Status(serverUi)
            Spacer(modifier = Modifier.height(2.dp))
            NetworkIO(serverUi)
            Spacer(modifier = Modifier.height(2.dp))
            NetworkTransfer(serverUi)
            Spacer(modifier = Modifier.height(2.dp))
            LoadAndUptime(serverUi)
        }
    }
}

@PreviewLightDark
@Composable
fun ServerCardPreview() {
    MDPingsTheme {
        ServerListItem(
            serverUi = previewServerUi0,
            onClick = {},
            modifier = Modifier
        )
    }
}

@Composable
private fun ServerTitle(server: ServerUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "${server.host.countryCode} ${server.name}",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${server.host.platform}${server.host.platformVersion}",
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.End,
            modifier = Modifier
                .weight(1f)
                .alpha(0.7f)
        )
    }
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium.copy(
                letterSpacing = text.toLetterSpacing()
            ),
            modifier = Modifier
                .weight(1f)
                .alpha(0.8f)
        )

        LinearProgressIndicator(
            progress = animatedProgress,
            color = if (animatedProgress <= 0.25f) MaterialTheme.colorScheme.tertiary
                else if (animatedProgress <= 0.75f) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.error,
            modifier = Modifier
                .weight(4f)
                .height(4.5.dp)
        )

        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.End
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun LoadAndUptime(server: ServerUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(4f)
        ) {
            Text(
                text = "LOAD",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium.copy(
                    letterSpacing = "LOAD".toLetterSpacing()
                ),
                modifier = Modifier.alpha(0.8f)
            )
            Spacer(Modifier.weight(0.8f))
            AnimatedContent(
                targetState = server.status,
                label = "AnimatedNetworkIO",
                transitionSpec = {
                    fadeIn() + slideInVertically(animationSpec = tween(1000),
                        initialOffsetY = { fullHeight -> fullHeight }) with
                            fadeOut(animationSpec = tween(200))
                },
                modifier = Modifier
                    .weight(3.2f)
            ) { it ->
                Text(
                    text = "${it.load1.formatted} | ${it.load5.formatted} | ${it.load15.formatted}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Row(
            modifier = Modifier.weight(2f)
        ) {
            Text(
                text = "UPTIME",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alpha(0.8f)
            )
            Spacer(Modifier.weight(0.5f))
            Text(
                text = "${server.status.uptime / 86400} D",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1.5f)
            )
        }
    }
}

@Composable
private fun NetworkTransfer(server: ServerUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "NetTR",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .alpha(0.8f)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier
                .weight(0.5f)
        )
        Text(
            text = server.status.netInTransfer.formatted,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = null,
            modifier = Modifier
                .weight(0.5f)
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
        Text(
            text = "NetIO",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium.copy(
                letterSpacing = "NetIO".toLetterSpacing()
            ),
            modifier = Modifier
                .weight(1f)
                .alpha(0.8f)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier
                .weight(0.5f)
        )
        AnimatedContent(
            targetState = server.status.netInSpeed,
            label = "AnimatedNetworkIO",
            transitionSpec = {
                fadeIn() + slideInVertically(animationSpec = tween(1000),
                    initialOffsetY = { fullHeight -> fullHeight }) with
                        fadeOut(animationSpec = tween(200))
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
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = null,
            modifier = Modifier
                .weight(0.5f)
        )
        AnimatedContent(
            targetState = server.status.netOutSpeed,
            label = "AnimatedNetworkIO",
            transitionSpec = {
                fadeIn() + slideInVertically(animationSpec = tween(1000),
                    initialOffsetY = { fullHeight -> fullHeight }) with
                        fadeOut(animationSpec = tween(200))
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
internal val previewHostUi = HostUi(
    platform = "ubuntu",
    platformVersion = "22.04",
    cpu = listOf("AMD EPYC 7543P 32-Core Processor 4 Virtual Core"),
    memTotal = 8323002368,
    diskTotal = 2164154892288,
    swapTotal = 267362304,
    arch = "x86_64",
    virtualization = "kvm",
    bootTime = 1725353936,
    countryCode = "nl".toCountryCodeToEmojiFlag(),
    version = "0.20.3"
)

internal val previewStatusUi = StatusUi(
    cpu = 1.5970223416354114,
    memUsed = Random.nextLong(until = 8323002368),
    swapUsed = Random.nextInt(until = 267362304),
    diskUsed = Random.nextLong(until = 2164154892288),
    netInTransfer = 1976106961615.toLongDisplayableString(),
    netOutTransfer = 520080984594.toLongDisplayableString(),
    netInSpeed = Random.nextInt(until = 102400000).toNetIOSpeedDisplayableString(),
    netOutSpeed = Random.nextInt(until = 102400000).toNetIOSpeedDisplayableString(),
    uptime = 5201009,
    load1 = Random.nextDouble(until = 200.0).toDisplayableNumber(),
    load5 = Random.nextDouble(until = 200.0).toDisplayableNumber(),
    load15 = Random.nextDouble(until = 200.0).toDisplayableNumber(),
    tcpConnCount = 63,
    udpConnCount = 97,
    processCount = 296,
    gpu = 0
)

internal val previewServerUi0 = ServerUi(
    id = 0,
    name = "Preview Server0",
    tag = "",
    lastActive = 1730554945,
    ipv4 = "1.1.1.1",
    ipv6 = "2001:0000:130F:0000:0000:09C0:876A:130B",
    validIp = "1.1.1.1",
    displayIndex = 0,
    hideForGuest = false,
    host = previewHostUi,
    status = previewStatusUi
)

internal val previewServerUi1 = ServerUi(
    id = 1,
    name = "Preview Server1",
    tag = "",
    lastActive = 1730554945,
    ipv4 = "1.1.1.1",
    ipv6 = "2001:0000:130F:0000:0000:09C0:876A:130B",
    validIp = "1.1.1.1",
    displayIndex = 0,
    hideForGuest = false,
    host = previewHostUi,
    status = previewStatusUi
)

internal val previewServerUi2 = ServerUi(
    id = 2,
    name = "Preview Server2",
    tag = "",
    lastActive = 1730554945,
    ipv4 = "1.1.1.1",
    ipv6 = "2001:0000:130F:0000:0000:09C0:876A:130B",
    validIp = "1.1.1.1",
    displayIndex = 0,
    hideForGuest = false,
    host = previewHostUi,
    status = previewStatusUi
)

internal val previewServerUi3 = ServerUi(
    id = 3,
    name = "Preview Server3",
    tag = "",
    lastActive = 1730554945,
    ipv4 = "1.1.1.1",
    ipv6 = "2001:0000:130F:0000:0000:09C0:876A:130B",
    validIp = "1.1.1.1",
    displayIndex = 0,
    hideForGuest = false,
    host = previewHostUi,
    status = previewStatusUi
)

internal val previewListServers = listOf<ServerUi>(
    previewServerUi0, previewServerUi1, previewServerUi2, previewServerUi3
)