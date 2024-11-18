package com.sekusarisu.mdpings.vpings.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sekusarisu.mdpings.ui.theme.MDPingsTheme
import com.sekusarisu.mdpings.vpings.domain.ServerOrder
import com.sekusarisu.mdpings.vpings.domain.ServerSortField
import com.sekusarisu.mdpings.vpings.presentation.app_settings.AppSettingsAction
import com.sekusarisu.mdpings.vpings.presentation.app_settings.AppSettingsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MDAppTopBar(
    appSettingsState: AppSettingsState,
    title: String,
    navigationIcon: ImageVector = Icons.Rounded.Menu,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigationIconClick: () -> Unit = {},
    onUserClick: () -> Unit,
    onAction: (AppSettingsAction) -> Unit
) {
    var isDropDownExpanded by remember { mutableStateOf(false) }
    val serverSortFieldString = listOf("ServerID", "Online")
    val scope = rememberCoroutineScope()
    val selectedServerSortField = appSettingsState.appSettings.serverSortField.ordinal
    val serverOrder = appSettingsState.appSettings.serverOrder.ordinal

    TopAppBar(
        modifier = modifier
            .padding(bottom = 8.dp),
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedContent(
                    targetState = title,
                    label = "AnimatedTitle",
                    transitionSpec = {
                        fadeIn() + slideInVertically(
                            animationSpec = tween(1000),
                            initialOffsetY = { fullHeight -> fullHeight }
                        ) togetherWith fadeOut(animationSpec = tween(200))
                    }
                ) { it ->
                    Text(
                        text = it,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(Modifier.width(8.dp))
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = LocalContentColor.current
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick
            ) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationIcon.name
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    isDropDownExpanded = true
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Sort,
                    contentDescription = "Sort"
                )
            }
            DropdownMenu(
                expanded = isDropDownExpanded,
                onDismissRequest = {
                    isDropDownExpanded = false
                }
            ) {
                serverSortFieldString.forEachIndexed { index, serverSortFieldString ->
                    DropdownMenuItem(
                        trailingIcon = {
                            if (selectedServerSortField == index) {
                                if (serverOrder == 0) {
                                    Icon(
                                        imageVector = Icons.Rounded.ArrowDownward,
                                        contentDescription = null
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Rounded.ArrowUpward,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        text = { Text(serverSortFieldString) },
                        onClick = {
                            scope.launch {
                                if (selectedServerSortField == index) {
                                    onAction(
                                        AppSettingsAction.OnSaveServerOrder(
                                            serverOrder = ServerOrder.entries[if (serverOrder==0) 1 else 0]
                                        )
                                    )
                                } else {
                                    onAction(
                                        AppSettingsAction.OnSaveServerSortField(
                                            serverSortField = ServerSortField.entries[index]
                                        )
                                    )
                                }
                                isDropDownExpanded = false
                            }
                        }
                    )
                }
            }
            IconButton(
                onClick = onUserClick
            ) {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Account"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun AppBarPreview() {
    MDPingsTheme {

        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
            state = rememberTopAppBarState()
        )

        MDAppTopBar(
            title = "MDPings",
            modifier = Modifier,
            scrollBehavior = scrollBehavior,
            onNavigationIconClick = { },
            isLoading = true,
            onUserClick = {},
            onAction = {},
            appSettingsState = AppSettingsState()
        )
    }
}