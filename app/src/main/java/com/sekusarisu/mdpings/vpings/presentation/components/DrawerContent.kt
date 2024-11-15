package com.sekusarisu.mdpings.vpings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Monitor
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sekusarisu.mdpings.AboutScreen
import com.sekusarisu.mdpings.AppSettingsScreen
import com.sekusarisu.mdpings.ServerListScreen
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    currentRoute: String? = "",
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {

    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .padding(start = 16.dp)
                .alpha(0.8f)
        ) {
            Icon(
                imageVector = Icons.Rounded.Monitor,
                contentDescription = Icons.Rounded.Monitor.name
            )
            Text(
                text = "MDPings",
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(16.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .alpha(0.9f)
        ) {
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = Icons.Rounded.Home.name
                    )
                },
                label = {
                    Text(
                        text = "Home",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                selected = currentRoute?.contains("ServerListScreen") == true,
                onClick = {
                    if (currentRoute?.contains("ServerListScreen") == false) {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                            navController.navigate(
                                route = ServerListScreen
                            ) {
                                popUpTo(0)
                            }
                        }
                    } else {
                        scope.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                    }
                }
            )
            Spacer(Modifier.height(4.dp))
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = Icons.Rounded.Settings.name
                    )
                },
                label = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                selected = currentRoute?.contains("AppSettingsScreen") == true,
                onClick = {
                    if (currentRoute?.contains("AppSettingsScreen") == false) {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                            navController.navigate(route = AppSettingsScreen)
                        }
                    } else {
                        scope.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                    }
                }
            )
            Spacer(Modifier.height(4.dp))
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = Icons.Rounded.Info.name
                    )
                },
                label = {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                selected = currentRoute?.contains("AboutScreen") == true,
                onClick = {
                    if (currentRoute?.contains("AboutScreen") == false) {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                            navController.navigate(route = AboutScreen)
                        }
                    } else {
                        scope.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                    }
                }
            )
        }
    }
}