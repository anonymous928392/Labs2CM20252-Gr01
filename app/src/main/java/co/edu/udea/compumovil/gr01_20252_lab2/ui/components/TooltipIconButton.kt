package co.edu.udea.compumovil.gr01_20252_lab2.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TooltipIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    tooltipText: String,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    var showTooltip by remember { mutableStateOf(false) }

    // Auto-hide tooltip after 2 seconds
    LaunchedEffect(showTooltip) {
        if (showTooltip) {
            delay(2000)
            showTooltip = false
        }
    }

    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                Text(tooltipText)
            }
        },
        state = rememberTooltipState(
            isPersistent = false
        ).apply {
            LaunchedEffect(showTooltip) {
                if (showTooltip) {
                    show()
                } else {
                    dismiss()
                }
            }
        }
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier.combinedClickable(
                onClick = onClick,
                onLongClick = {
                    showTooltip = true
                }
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TooltipButton(
    onClick: () -> Unit,
    tooltipText: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    var showTooltip by remember { mutableStateOf(false) }

    // Auto-hide tooltip after 2 seconds
    LaunchedEffect(showTooltip) {
        if (showTooltip) {
            delay(2000)
            showTooltip = false
        }
    }

    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                Text(tooltipText)
            }
        },
        state = rememberTooltipState(
            isPersistent = false
        ).apply {
            LaunchedEffect(showTooltip) {
                if (showTooltip) {
                    show()
                } else {
                    dismiss()
                }
            }
        }
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier.combinedClickable(
                onClick = onClick,
                onLongClick = {
                    showTooltip = true
                },
                enabled = enabled
            ),
            content = content
        )
    }
}
