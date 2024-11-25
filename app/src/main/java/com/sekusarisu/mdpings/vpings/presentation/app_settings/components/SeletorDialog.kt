package com.sekusarisu.mdpings.vpings.presentation.app_settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sekusarisu.mdpings.vpings.presentation.app_settings.AppSettingsAction
import kotlinx.coroutines.launch

@Composable
fun SelectorDialog(
    content: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: (AppSettingsAction) -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            content()
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {},
        dismissButton = {}
    )
}