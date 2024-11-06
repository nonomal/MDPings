package com.example.mdpings.vpings.presentation.server_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.mdpings.ui.theme.MDPingsTheme
import com.example.mdpings.vpings.presentation.server_list.components.MDAppTopBar
import com.example.mdpings.vpings.presentation.server_list.components.ServerListItem
import com.example.mdpings.vpings.presentation.server_list.components.previewListServers
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerListScreen(
    onAction: (ServerListAction) -> Unit,
    state: ServerListState,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MDAppTopBar(
                scrollBehavior = scrollBehavior,
                onMenuClick = { },
                title = "MDPings",
                isLoading = state.isLoading
            )
        }
    ) { innerPadding ->

        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() - 4.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(
                items = state.servers,
                key = { it.id }
            ) { serverUi ->
                ServerListItem(
                    monitors = state.monitors,
                    serverUi = serverUi,
                    onClick = { },
                    onAction = onAction,
                    modifier = Modifier
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
            state = ServerListState(
                isLoading = false,
                servers = previewListServers,
            )
        )
    }
}
