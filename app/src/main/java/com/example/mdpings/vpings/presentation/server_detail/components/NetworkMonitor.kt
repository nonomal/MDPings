package com.example.mdpings.vpings.presentation.server_detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Commit
import androidx.compose.material.icons.rounded.NetworkPing
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.presentation.app_settings.AppSettingsState
import com.example.mdpings.vpings.presentation.server_detail.ServerDetailAction
import com.example.mdpings.vpings.presentation.server_detail.ServerDetailState
import com.example.mdpings.vpings.presentation.server_list.components.previewServerUi0
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun NetworkMonitor(
    appSettingsState: AppSettingsState,
    state: ServerDetailState,
    onAction: (ServerDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val modelProducer = remember { CartesianChartModelProducer() }
    val monitors = state.monitors
    val serverId = state.serverUi!!.id
    val isChartLoading = state.isChartLoading

    if (monitors.isNotEmpty()) {
        LaunchedEffect(monitors) {
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
    }

    Card(
        modifier = Modifier
            .wrapContentHeight(),
//            .animateContentSize(),
        shape = ShapeDefaults.Medium,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Row(
                modifier = Modifier
                    .alpha(0.8f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.NetworkPing,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
                Text(
                    text = "Network Delay",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                )
                IconButton(
                    onClick = {
                        onAction(
                            ServerDetailAction.OnMonitorsRefresh(serverId, state.monitorsTimeSlice, appSettingsState.apiURL)
                        )
                    },
                    modifier = Modifier
                        .size(24.dp)
                ) {
                    if (isChartLoading) {
                        CircularProgressIndicator()
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = "Refresh",
                            modifier = Modifier
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // FilterChips
            DateFilterChipGroup(
                state = state,
                modifier = Modifier,
                onAction = onAction
            )

            AnimatedContent(
                targetState = monitors.isEmpty(),
                label = "AnimatedChart"
            ) { it ->
                // Loading or Chart
                if (it && state.isChartLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(8.dp))
                            Text("Loading Chart...")
                        }
                    }
                } else if (it) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No monitor data available.")
                        }
                    }
                } else {
                    MonitorsChart(
                        // TODO model = mediumLineModel Preview时解除注释，使用手动build的model测试
//                    model = mockLineModel(),
                        modelProducer = modelProducer,
                        modifier = Modifier
//                            .sizeIn(maxHeight = 240.dp)
                            .height(240.dp)
                    )
                }
            }

            AnimatedContent(
                targetState = monitors,
                contentKey = { it -> it.size },
                label = "AnimatedTags"
            ) { it ->
                // TAG
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(80.dp),
                    verticalArrangement = Arrangement.Center,
                    // 指定高度不然LazyGrid会爆炸
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .requiredHeightIn(max = (18*4).dp)
                ) {
                    itemsIndexed(
                        items = it
                    ) { index, monitor ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                tint = chartColors[index],
                                imageVector = Icons.Rounded.Commit,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = monitor.monitorName,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 12.sp,
                                modifier = Modifier.alpha(0.7f)
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun DateFilterChipGroup(
    state: ServerDetailState,
    modifier: Modifier = Modifier,
    onAction: (ServerDetailAction) -> Unit = { }
) {
    Row(
        modifier = Modifier.heightIn(max = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        listOf<String>("30 mins", "1 hour", "3 hours", "6 hours").map { text ->
            FilterChip(
                selected = state.monitorsTimeSlice == text,
                onClick = {
                    if (state.monitorsTimeSlice == text) {
                        onAction(
                            ServerDetailAction.OnSliceMonitorsTime("")
                        )
                    } else {
                        onAction(
                            ServerDetailAction.OnSliceMonitorsTime(text)
                        )
                    }
                },
                label = {
                    Text(
                        textAlign = TextAlign.Center,
                        text = text,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.width(64.dp)
                    )
                },
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 4.dp)
                    .weight(1f),
                shape = ShapeDefaults.Large,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun NetworkMonitorPreview() {
    MDPingsTheme {
        Card(
            modifier = Modifier,
            shape = ShapeDefaults.Medium,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            NetworkMonitor(
//                monitors = mockMonitors,
                state = ServerDetailState(
                    isLoading = false,
                    serverUi = previewServerUi0,
                    ipAPIUi = mockIpAPIUi,
                    monitors = mockMonitors
                ),
                onAction = {},
                appSettingsState = AppSettingsState()
            )
        }
    }
}