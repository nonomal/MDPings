package com.example.mdpings.vpings.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.ReportProblem
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .alpha(0.8f)
            .padding(18.dp)
            .fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.alpha(0.9f)
        ) {
            Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = "Info",
            )
            Text(
                text = "About",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
        HorizontalDivider()
        Text(
            text = body,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.alpha(0.9f)
        ) {
            Icon(
                imageVector = Icons.Rounded.MonitorHeart,
                contentDescription = "MonitorHeart",
            )
            Text(
                text = "MDPings",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
        HorizontalDivider()
        ListItem(
            headlineContent = { Text("MDPings") },
            supportingContent = { Text("查看MDPings GitHub 项目地址和应用说明") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.MonitorHeart,
                    contentDescription = "MonitorHeart",
                )
            },
        )
        ListItem(
            headlineContent = { Text("GitHub 议题") },
            supportingContent = { Text("提交错误报告或改进建议") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.ReportProblem,
                    contentDescription = "ReportProblem",
                )
            },
        )
        ListItem(
            headlineContent = { Text("赞助") },
            supportingContent = { Text("在 GitHub 上赞助以支持本应用") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.AttachMoney,
                    contentDescription = "AttachMoney",
                )
            },
        )
        ListItem(
            headlineContent = { Text("Telegram 频道") },
            supportingContent = { Text("https://t.me/mdpings_app") },
            leadingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Send,
                    contentDescription = "Send",
                )
            },
        )
        ListItem(
            headlineContent = { Text("当前版本") },
            supportingContent = { Text("0.0.1-Alpha") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = "Info",
                )
            },
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.alpha(0.9f)
        ) {
            Icon(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = "Favorite",
            )
            Text(
                text = "鸣谢项目",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
        HorizontalDivider()
        ListItem(
            headlineContent = { Text("哪吒监控") },
            supportingContent = { Text("查看哪吒监控 GitHub 项目地址和应用说明") },
//            supportingContent = { Text("查看哪吒监控 GitHub 项目地址和应用说明") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = "Favorite",
                )
            },
//            trailingContent = { Text("meta") }
        )
        ListItem(
            headlineContent = { Text("Vico") },
            supportingContent = { Text("图表绘制库") },
//            supportingContent = { Text("查看Vico GitHub 项目地址和应用说明") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = "Favorite",
                )
            },
//            trailingContent = { Text("meta") }
        )
    }
}

private const val body: String =
    "MDPings是一个基于哪吒监控开发的MD3风格Android客户端，支持同时监控多个服务器的状态，提供历史网络状态和延迟图表。"
