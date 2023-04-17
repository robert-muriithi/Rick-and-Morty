package dev.robert.rickandmorty.core.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt


@Composable
fun CollapsingToolbar(
    height: Dp,
    content: @Composable () -> Unit
) {
    BoxWithConstraints {
        val maxHeight = with(LocalDensity.current) { height.toPx() }
        val scrollState = rememberLazyListState()
        val maxOffset = remember { mutableStateOf(0f) }
        val alpha = remember { mutableStateOf(1f) }
        var toolbarOffset by remember { mutableStateOf(0f) }


        // Calculate the max offset of the toolbar
        LaunchedEffect(maxHeight) {
            maxOffset.value = maxHeight - height.value
        }

        // Observe the scroll state and update the offset and alpha values
        LaunchedEffect(scrollState) {
            snapshotFlow { scrollState.firstVisibleItemScrollOffset }
                .collect {
                    toolbarOffset = it.coerceIn(0, maxOffset.value.toInt()).toFloat()
                    alpha.value = 1 - (toolbarOffset / maxOffset.value)
                }
        }

        // Draw the toolbar and content
        Column {
            Spacer(
                modifier = Modifier
                    .height(height)
                    .offset { IntOffset(0, -toolbarOffset.roundToInt()) }
                    .alpha(alpha.value)
            )
            content()
        }
    }
}

