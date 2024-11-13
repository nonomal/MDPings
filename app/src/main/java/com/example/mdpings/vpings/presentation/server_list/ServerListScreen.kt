package com.example.mdpings.vpings.presentation.server_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.presentation.app_settings.AppSettingsAction
import com.example.mdpings.vpings.presentation.app_settings.AppSettingsState
import com.example.mdpings.vpings.presentation.server_detail.ServerDetailAction
import com.example.mdpings.vpings.presentation.server_list.components.ServerListItem
import com.example.mdpings.vpings.presentation.server_list.components.previewListServers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerListScreen(
    onNavigateToDetail: () -> Unit,
    onLoad: (AppSettingsAction) -> Unit,
    onAction: (ServerListAction) -> Unit,
    appSettingsState: AppSettingsState,
    state: ServerListState,
    modifier: Modifier = Modifier
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(appSettingsState) {
        onLoad(
            AppSettingsAction.OnInitLoadAppSettings
        )
    }
    LaunchedEffect(appSettingsState) {
        while (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            onAction(
                ServerListAction.OnLoadServer(
                    apiURL = appSettingsState.apiURL,
                    apiTOKEN = appSettingsState.apiTOKEN
                )
            )
            delay(appSettingsState.refreshInterval.toLong())
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(
            items = state.servers,
            key = { it.id }
        ) { serverUi ->
            ServerListItem(
                onNavigateToDetail = onNavigateToDetail,
                serverUi = serverUi,
                ipAPI = state.ipAPIUi,
                monitors = state.monitors,
                onAction = onAction,
                onClick = { },
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun ServerListScreenPreview() {
    MDPingsTheme {
        ServerListScreen(
            onAction = {},
            onNavigateToDetail = {},
            state = ServerListState(
                isLoading = false,
                servers = previewListServers,
            ),
            onLoad = {},
            appSettingsState = AppSettingsState()
        )
    }
}
