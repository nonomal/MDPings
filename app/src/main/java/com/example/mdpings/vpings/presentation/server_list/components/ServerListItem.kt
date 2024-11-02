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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mdpings.vpings.presentation.models.ServerUi
import com.example.mdpings.vpings.presentation.server_list.countryCodeToEmojiFlag
import java.util.Locale
import kotlin.math.pow

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
            Status(serverUi)
            NetworkIO(serverUi)
            NetworkTransfer(serverUi)
            LoadAndUptime(serverUi)
        }
    }
}

@Composable
private fun ServerTitle(server: ServerUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "${countryCodeToEmojiFlag(server.host.countryCode)} ${server.name}",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${server.host.platform}${server.host.platformVersion}",
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
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
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.weight(0.8f))
            AnimatedContent(
                targetState = server.status,
                label = "AnimatedNetworkIO",
                transitionSpec = {
                    fadeIn() + slideInVertically(animationSpec = tween(400),
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
                style = MaterialTheme.typography.bodyMedium
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
private fun Status(server: ServerUi) {
    ProgressBar(
        text = "CPU",
        total = 100F,
        used = server.status.cpu.toFloat()*10
    )
    ProgressBar(
        text = "RAM",
        total = server.host.memTotal.toFloat(),
        used = server.status.memUsed.toFloat()
    )
    ProgressBar(
        text = "SWAP",
        total = server.host.swapTotal.toFloat(),
        used = server.status.swapUsed.toFloat()
    )
    ProgressBar(
        text = "DISK",
        total = server.host.diskTotal.toFloat(),
        used = server.status.diskUsed.toFloat()
    )
}

@Composable
private fun NetworkTransfer(server: ServerUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "NetTR",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier
                .weight(0.5f)
        )
        Text(
            text = "${String.format(Locale.US, "%.2f", server.status.netInTransfer / 1024.0.pow(3.0))} G",
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
            text = "${String.format(Locale.US, "%.2f", server.status.netOutTransfer / 1024.0.pow(3.0))} G",
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
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
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
                fadeIn() + slideInVertically(animationSpec = tween(400),
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
                fadeIn() + slideInVertically(animationSpec = tween(400),
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
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
        )

        LinearProgressIndicator(
            progress = animatedProgress,
            color = if (animatedProgress <= 0.25f) MaterialTheme.colorScheme.tertiary
            else if (animatedProgress <= 0.75f) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.error,
            modifier = Modifier
                .weight(4f)
        )

        Text(
            text = "${((used/total) * 100).toInt()}%",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.End
        )
    }
}