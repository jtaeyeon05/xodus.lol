package com.taeyeon.xoduslol.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.roundToInt


enum class DragValue { Start, End }

@Composable
fun MainScreen(navController: NavController = rememberNavController()) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (maxHeight >= maxWidth * 2) {
            val ballSize = maxWidth * 1.2f
            val density = LocalDensity.current
            val colorScheme = MaterialTheme.colorScheme

            val initialY = with (density) { (ballSize * 0.3f).toPx() }
            val targetY = with (density) { (ballSize * 0.5f - maxHeight * 0.5f).toPx() }
            val anchoredDraggableState = remember {
                AnchoredDraggableState(
                    anchors = DraggableAnchors {
                        DragValue.Start at initialY
                        DragValue.End at targetY
                    },
                    initialValue = DragValue.Start
                )
            }

            var dragProgress by remember { mutableStateOf(0.0f) }
            LaunchedEffect(anchoredDraggableState) {
                snapshotFlow { anchoredDraggableState.progress(DragValue.Start, DragValue.End) }
                    .distinctUntilChanged()
                    .collect { newValue ->
                        dragProgress = newValue
                    }
            }

            Canvas(
                modifier = Modifier
                    .requiredSize(ballSize)
                    .align(Alignment.BottomCenter)
                    .offset {
                        IntOffset(
                            x = 0,
                            y = anchoredDraggableState.requireOffset().roundToInt()
                        )
                    }
                    .anchoredDraggable(anchoredDraggableState, Orientation.Vertical)
            ) {
                rotate(180 * dragProgress) {
                    drawCircle(
                        brush = Brush.linearGradient(
                            0.0f to colorScheme.primaryContainer,
                            1.0f to colorScheme.surface,
                            start = Offset(x = 0f, y = size.height),
                            end = Offset(x = size.width, y = 0f)
                        ),
                    )
                }
            }
        } else {
            val ballSize = maxHeight * 0.4f

            Text("H", modifier = Modifier.align(Alignment.TopStart))
        }
    }
}