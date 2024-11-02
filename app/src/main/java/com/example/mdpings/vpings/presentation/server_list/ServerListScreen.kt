package com.example.mdpings.vpings.presentation.server_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mdpings.vpings.presentation.server_list.components.ServerListItem
import java.util.Locale

fun countryCodeToEmojiFlag(countryCode: String): String {
    return countryCode
        .uppercase(Locale.US)
        .map { char ->
            Character.codePointAt("$char", 0) - 0x41 + 0x1F1E6
        }
        .map { codePoint ->
            Character.toChars(codePoint)
        }
        .joinToString(separator = "") { charArray ->
            String(charArray)
        }
}

@Composable
fun ServerListScreen(
    state: ServerListState,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    if (state.isLoading) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
        )
    }

    LazyColumn(
        state = scrollState,
        modifier = modifier
            .padding(PaddingValues(4.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(
            items = state.servers,
            key = { it.id }
        ) { serverUi ->
            ServerListItem(
                serverUi = serverUi,
                onClick = { },
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .fillMaxWidth()
            )
//            ServerListItem(
//                serverUi = serverUi,
//                onClick = { },
//                modifier = Modifier
//                    .padding(horizontal = 8.dp, vertical = 4.dp)
//                    .fillMaxWidth()
//            )
        }
    }

}
