package com.example.mdpings.vpings.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mdpings.ui.theme.MDPingsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MDAppTopBar(
    title: String = "MDPings",
    navigationIcon: ImageVector = Icons.Rounded.Menu,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigationIconClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onUserClick: () -> Unit = {}
) {

    TopAppBar(
        expandedHeight = 56.dp,
        modifier = modifier
//            .padding(bottom = 8.dp)
            .padding(top = 24.dp)
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(100.dp)),
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        windowInsets = WindowInsets(top = 0.dp),
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
//            IconButton(
//                onClick = { }
//            ) {
//                Icon(
//                    imageVector = Icons.Rounded.Search,
//                    contentDescription = "Search"
//                )
//            }
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
            isLoading = true
        )
    }
}