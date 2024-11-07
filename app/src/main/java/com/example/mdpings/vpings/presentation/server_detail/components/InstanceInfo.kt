package com.example.mdpings.vpings.presentation.server_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.presentation.models.IpAPIUi
import com.example.mdpings.vpings.presentation.models.ServerUi
import com.example.mdpings.vpings.presentation.models.toMemDiskLongDisplayableString
import com.example.mdpings.vpings.presentation.server_list.components.previewServerUi0

@Composable
fun InstanceInfo(
    ipAPIUi: IpAPIUi,
    serverUi: ServerUi,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier.wrapContentHeight(),
        shape = ShapeDefaults.Medium,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
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
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null,
                    modifier = Modifier
                )
                Text(
                    text = "Instance Info",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                )
            }

            Column {
                InfoRow(
                    title = "System:",
                    content = "${serverUi.host.platform} ${serverUi.host.platformVersion}"
                )
                InfoRow(
                    title = "CPU:",
                    content = serverUi.host.cpu[0]
                )
                InfoRow(
                    title = "RAM:",
                    content = serverUi.host.memTotal.toMemDiskLongDisplayableString()
                )
                InfoRow(
                    title = "Storage:",
                    content = serverUi.host.diskTotal.toMemDiskLongDisplayableString()
                )

                // TODO：向https://ip-api.com/docs/api:json
                InfoRow(
                    title = "ISP:",
                    content = if (ipAPIUi.isp.isNotEmpty()) ipAPIUi.isp else "N/A"
                )
                InfoRow(
                    title = "ORG:",
                    content = if (ipAPIUi.org.isNotEmpty()) ipAPIUi.org else "N/A"
                )
                InfoRow(
                    title = "Location:",
                    content = "${ipAPIUi.lat}, ${ipAPIUi.lon}"
                )
                InfoRow(
                    title = "Nezha Version:",
                    content = serverUi.host.version
                )
            }
        }
    }
}

@Composable
fun InfoRow(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier
                .alpha(0.8f)
        )
        Spacer(
            modifier = modifier.size(4.dp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            modifier = modifier
                .alpha(0.7f)
        )
    }
}


@Composable
@PreviewLightDark
private fun InstanceInfoPreview() {
    MDPingsTheme {
        InstanceInfo(
            serverUi = previewServerUi0,
            ipAPIUi = mockIpAPIUi ,
            modifier = Modifier.padding(16.dp)
        )
    }
}