package com.sekusarisu.mdpings.vpings.presentation.server_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RunningWithErrors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sekusarisu.mdpings.R
import com.sekusarisu.mdpings.ui.theme.MDPingsTheme
import com.sekusarisu.mdpings.vpings.domain.AppSettings
import com.sekusarisu.mdpings.vpings.domain.ServerOrder
import com.sekusarisu.mdpings.vpings.domain.ServerSortField
import com.sekusarisu.mdpings.vpings.presentation.app_settings.AppSettingsAction
import com.sekusarisu.mdpings.vpings.presentation.app_settings.AppSettingsState
import com.sekusarisu.mdpings.vpings.presentation.models.WSServerUi
import com.sekusarisu.mdpings.vpings.presentation.server_list.components.ServerListCard
import com.sekusarisu.mdpings.vpings.presentation.server_list.components.ServerSummaryCard
import com.sekusarisu.mdpings.vpings.presentation.server_list.components.previewListServers
import kotlinx.coroutines.delay

private fun List<WSServerUi>.sortByField(serverSortField: ServerSortField, serverOrder: ServerOrder): List<WSServerUi> {
    return if (serverOrder.ordinal == 0) {
        when (serverSortField) {
            ServerSortField.ID -> sortedBy { it.id }
            ServerSortField.ONLINE -> sortedBy { it.isOnline }
            else -> sortedBy { it.id }
        }
    } else {
        when (serverSortField) {
            ServerSortField.ID -> sortedByDescending { it.id }
            ServerSortField.ONLINE -> sortedByDescending { it.isOnline }
            else -> sortedBy { it.id }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerListScreen(
    onNavigateToDetail: (Int) -> Unit,
    onLoad: (AppSettingsAction) -> Unit,
    onAction: (ServerListAction) -> Unit,
    appSettingsState: AppSettingsState,
    state: ServerListState,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(appSettingsState.appSettings.activeInstance) {
        delay(500)
        if (appSettingsState.appSettings.instances.isNotEmpty()) {
            val baseUrl = appSettingsState.appSettings.instances[appSettingsState.appSettings.activeInstance].baseUrl
            onAction(
                ServerListAction.OnLoadWSServer(baseUrl = baseUrl)
            )
        }
    }

    AnimatedVisibility(
        visible = appSettingsState.appSettings.instances.isEmpty()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.RunningWithErrors,
                contentDescription = null
            )
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.server_list_no_available_credentials))
        }
    }

    AnimatedVisibility(
        visible = state.wsServers == emptyList<WSServerUi>() && state.isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.server_list_loading_servers))
        }
    }

    AnimatedVisibility(
        visible = state.wsServers != emptyList<WSServerUi>(),
        enter = slideInVertically(
            spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = Spring.DampingRatioMediumBouncy,
                visibilityThreshold = IntOffset.VisibilityThreshold
            )
        ) + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(
            initialAlpha = 0.3f
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            item(
                key = "Summary"
            ) {
                ServerSummaryCard(
                    isExpanded = true,
                    servers = state.wsServers,
                    onAction = {},
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            items(
                items = state.wsServers.sortByField(
                    appSettingsState.appSettings.serverSortField,
                    appSettingsState.appSettings.serverOrder
                ),
                key = { it.id }
            ) { serverUi ->
                ServerListCard(
                    isExpanded = appSettingsState.appSettings.expandedServerListCard,
                    onNavigateToDetail = { onNavigateToDetail(serverUi.id) },
                    serverUi = serverUi,
                    onAction = onAction,
                    modifier = Modifier
                        .animateItem(
                            placementSpec = spring(
                                stiffness = Spring.StiffnessLow,
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                visibilityThreshold = IntOffset.VisibilityThreshold
                            ),
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .fillMaxWidth()
                )
            }
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
            appSettingsState = AppSettingsState(AppSettings())
        )
    }
}
