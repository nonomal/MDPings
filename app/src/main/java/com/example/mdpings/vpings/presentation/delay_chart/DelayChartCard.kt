package com.example.mdpings.vpings.presentation.delay_chart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.mdpings.ui.theme.MDPingsTheme
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.data.rememberExtraLambda
import com.patrykandpatrick.vico.compose.common.dimensions
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.core.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Legend
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.component.Shadow
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Line的颜色
private val chartColors = listOf(Color(0xffb983ff), Color(0xff91b1fd), Color(0xff8fdaff))

// x轴时间
private val bottomAxisValueFormatter = CartesianValueFormatter { _, x, _ ->
    formatEpochTimeToHHMM(x)
}

// Epoch -> HH:MM
private fun formatEpochTimeToHHMM(epochTime: Double): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val time = Instant.ofEpochMilli(epochTime.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
    return time.format(formatter)
}

@Composable
//fun MonitorsChart(model: CartesianChartModel, modelProducer: CartesianChartModelProducer, modifier: Modifier) {
fun MonitorsChart(modelProducer: CartesianChartModelProducer, modifier: Modifier) {
    CartesianChartHost(
//        model = model,
        modelProducer = modelProducer,
        chart =
            rememberCartesianChart(
                rememberLineCartesianLayer(
                    // 线段样式调整
                    lineProvider = LineCartesianLayer.LineProvider.series(
                        chartColors.map { color ->
                            LineCartesianLayer.rememberLine(
                                fill = remember { LineCartesianLayer.LineFill.single(fill(color)) }
                            )
                        }
                    )
                ),
                // Y轴
                startAxis =
                    VerticalAxis.rememberStart(
                        label = rememberStartAxisLabel(),
                        // 标签（暂时没用）
                        titleComponent =
                            rememberTextComponent(
                                color = Color.Black,
                                margins = dimensions(end = 4.dp),
                                padding = dimensions(8.dp, 2.dp),
                                background = rememberShapeComponent(),
                            ),
                        horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                    ),
                // X轴
                bottomAxis =
                    HorizontalAxis.rememberBottom(
                        valueFormatter = bottomAxisValueFormatter,
                        itemPlacer =
                            remember {
                                HorizontalAxis.ItemPlacer.aligned(spacing = 3, addExtremeLabelPadding = true)
                            },
                    ),
                marker = rememberMarker(DefaultCartesianMarker.LabelPosition.AroundPoint),
                layerPadding = cartesianLayerPadding(scalableStart = 16.dp, scalableEnd = 16.dp),
                // 名字标注
//                legend = rememberLegend(),
            ),
        modifier = modifier.fillMaxHeight(),
        zoomState = rememberVicoZoomState(
            // 初始最小放大
            zoomEnabled = true,
            initialZoom =  remember { Zoom.min(Zoom.static(), Zoom.Content) },
        ),
    )
}

// y轴坐标标签样式
@Composable
private fun rememberStartAxisLabel() =
    rememberAxisLabelComponent(
        lineCount = 1,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        margins = dimensions(4.dp),
        padding = dimensions(4.dp, 0.dp),
        background = rememberShapeComponent(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = CorneredShape.rounded(4.dp),
            shadow = Shadow(
                radiusDp = 8f,
                dxDp = 0f,
                dyDp = 0f
            )
        ),
    )

// 每项的label
@Composable
private fun rememberLegend(): Legend<CartesianMeasuringContext, CartesianDrawingContext> {
    val labelComponent = rememberTextComponent(vicoTheme.textColor)
    val resources = LocalContext.current.resources
    return rememberHorizontalLegend(
        items =
            rememberExtraLambda {
                // TODO 根据ChartColor来foreach，不对，直接放弃掉？
                chartColors.forEachIndexed { index, color ->
                    add(
                        LegendItem(
                            icon = shapeComponent(color, CorneredShape.Pill),
                            labelComponent = labelComponent,
                            label = monitor_name[index],
                        )
                    )
                }
            },
        padding = dimensions(top = 8.dp, start = 16.dp, end = 16.dp)
    )
}

// 模拟数据
public val mediumLineModel =
    CartesianChartModel(
        LineCartesianLayerModel.build {
            series(x = delayListDate1.takeLast(120), y = delayList1.takeLast(120))
            series(x = delayListDate2.takeLast(120), y = delayList2.takeLast(120))
            series(x = delayListDate3.takeLast(120), y = delayList3.takeLast(120))
        }
    )

//@PreviewLightDark
//@Composable
//fun ChartPreview() {
//
//    val cartesianChartModelProducer = remember { CartesianChartModelProducer() }
//
//    MDPingsTheme {
//        Card(
//            modifier = Modifier,
//            shape = ShapeDefaults.Medium,
//            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
//            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .padding(horizontal = 16.dp)
//                    .padding(top = 16.dp, bottom = 16.dp)
//            ) {
//                Column {
//                    Text(
//                        text = "Network Status"
//                    )
//                    MonitorsChart(
//                        model = mediumLineModel,
//                        modelProducer = cartesianChartModelProducer,
//                        modifier = Modifier
//                            .sizeIn(maxHeight = 240.dp)
//                    )
//                }
//            }
//        }
//    }
//}