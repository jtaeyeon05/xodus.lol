package com.taeyeon.xoduslol.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.hypot


enum class DragValue { Down, Normal, Up }

@Composable
fun MainScreen(navController: NavController = rememberNavController()) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val density = LocalDensity.current
        val colorScheme = MaterialTheme.colorScheme

        val normalBallSize = if (maxHeight >= maxWidth * 2) maxWidth * 1.2f else maxHeight * 0.6f
        val changeBallSize = hypot(maxWidth.value, maxHeight.value).dp * 1.2f - normalBallSize

        val downPosition = with (density) { (maxHeight + normalBallSize * 0.2f).toPx() }
        val normalPosition = with (density) { (maxHeight - normalBallSize * 0.2f).toPx() }
        val upPosition = with (density) { (maxHeight * 0.5f).toPx() }

        var isDragEnabled by remember { mutableStateOf(true) }
        var dragProgress by remember { mutableStateOf(0.0f) }
        var ballSize by remember { mutableStateOf(normalBallSize) }

        val anchoredDraggableState = remember {
            AnchoredDraggableState(
                anchors = DraggableAnchors {
                    DragValue.Down at downPosition
                    DragValue.Normal at normalPosition
                    DragValue.Up at upPosition
                },
                initialValue = DragValue.Normal
            )
        }

        LaunchedEffect(anchoredDraggableState) {
            snapshotFlow { anchoredDraggableState.progress(DragValue.Normal, DragValue.Up) }
                .distinctUntilChanged()
                .collect { newValue ->
                    dragProgress = newValue
                    ballSize = normalBallSize + changeBallSize * newValue
                }
        }

        LaunchedEffect(anchoredDraggableState) {
            snapshotFlow { anchoredDraggableState.settledValue }
                .distinctUntilChanged()
                .collect { newValue ->
                    if (newValue == DragValue.Up) {
                        isDragEnabled = false
                    } else if (newValue == DragValue.Down) {
                        navController.popBackStack()
                    }
                }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .anchoredDraggable(
                    state = anchoredDraggableState,
                    orientation = Orientation.Vertical,
                    enabled = isDragEnabled,
                )
        ) {
            rotate(
                degrees = 180 * dragProgress,
                pivot = Offset(
                    x = center.x,
                    y = anchoredDraggableState.requireOffset()
                ),
            ) {
                drawCircle(
                    brush = Brush.linearGradient(
                        0.0f to colorScheme.primaryContainer,
                        1.0f to colorScheme.surface.copy(alpha = 1 - dragProgress).compositeOver(colorScheme.primaryContainer),
                        start = Offset(x = 0f, y = size.height),
                        end = Offset(x = size.width, y = 0f)
                    ),
                    radius = ballSize.toPx() * 0.5f,
                    center = Offset(
                        x = center.x,
                        y = anchoredDraggableState.requireOffset()
                    ),
                )
            }
        }
    }
}