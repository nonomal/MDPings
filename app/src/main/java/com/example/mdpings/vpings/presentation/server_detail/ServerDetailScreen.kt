package com.example.mdpings.vpings.presentation.server_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.domain.Monitor
import com.example.mdpings.vpings.presentation.models.IpAPIUi
import com.example.mdpings.vpings.presentation.models.MonitorUi
import com.example.mdpings.vpings.presentation.models.ServerUi
import com.example.mdpings.vpings.presentation.server_detail.components.InstanceInfo
import com.example.mdpings.vpings.presentation.server_detail.components.NetworkMonitor
import com.example.mdpings.vpings.presentation.server_detail.components.mockIpAPIUi
import com.example.mdpings.vpings.presentation.server_detail.components.mockMonitors
import com.example.mdpings.vpings.presentation.server_list.ServerListScreen
import com.example.mdpings.vpings.presentation.server_list.ServerListState
import com.example.mdpings.vpings.presentation.server_list.components.LoadAndUptime
import com.example.mdpings.vpings.presentation.server_list.components.MDAppTopBar
import com.example.mdpings.vpings.presentation.server_list.components.NetworkIO
import com.example.mdpings.vpings.presentation.server_list.components.NetworkTransfer
import com.example.mdpings.vpings.presentation.server_list.components.Status
import com.example.mdpings.vpings.presentation.server_list.components.previewListServers
import com.example.mdpings.vpings.presentation.server_list.components.previewServerUi0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerDetailScreen(
    serverUi: ServerUi,
    ipAPIUi: IpAPIUi,
    monitors: List<MonitorUi>,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MDAppTopBar(
                scrollBehavior = scrollBehavior,
                onMenuClick = { },
                title = "${serverUi.host.countryCode}  ${serverUi.name}",
                isLoading = false
            )
        }
    ) { innerPadding ->

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = innerPadding.calculateTopPadding() - 4.dp)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {

            InstanceInfo(
                serverUi = serverUi,
                ipAPIUi = ipAPIUi,
                modifier = Modifier
            )

            Spacer(Modifier.height(8.dp))
//            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            NetworkMonitor(monitors = monitors)

            Spacer(Modifier.height(8.dp))
//            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            ServerStatus(
                serverUi = serverUi,
                modifier = Modifier
            )

        }
    }
}

@Composable
fun ServerStatus(
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
            Row(
                modifier = Modifier
                    .alpha(0.8f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.MonitorHeart,
                    contentDescription = null,
                    modifier = Modifier
                )
                Text(
                    text = "Server Status",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
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
fun ServerDetailScreenPreview() {
    MDPingsTheme {
        ServerDetailScreen(
            serverUi = previewServerUi0,
            ipAPIUi = mockIpAPIUi,
            monitors = mockMonitors,
            modifier = Modifier
        )
    }
}