package com.example.mdpings.vpings.presentation.server_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Commit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.NetworkPing
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.presentation.models.MonitorUi
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun NetworkMonitor(
    monitors: List<MonitorUi> = emptyList()
) {

    val modelProducer = remember { CartesianChartModelProducer() }

    if (monitors.isNotEmpty()) {
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

        Card(
            modifier = Modifier.wrapContentHeight(),
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
                    )
                    Text(
                        text = "Network Delay",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                MonitorsChart(
                    // model = mediumLineModel Preview时解除注释，使用手动build的model测试
//                    model = mockLineModel(),
                    modelProducer = modelProducer,
                    modifier = Modifier
                        .sizeIn(maxHeight = 240.dp)
                )

                // TAG
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(80.dp),
                    verticalArrangement = Arrangement.Center,
                    // 指定高度不然LazyGrid会爆炸
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeightIn(max = (18*4).dp)
                ) {
                    itemsIndexed(
                        items = monitors
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
@PreviewLightDark
private fun NetworkMonitorPreview() {
    MDPingsTheme {
        Card(
            modifier = Modifier,
            shape = ShapeDefaults.Medium,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            NetworkMonitor(
                monitors = mockMonitors
            )
        }
    }
}