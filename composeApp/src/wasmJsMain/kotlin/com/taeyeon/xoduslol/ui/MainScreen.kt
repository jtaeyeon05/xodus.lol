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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.imageResource
import xoduslol.composeapp.generated.resources.Res
import xoduslol.composeapp.generated.resources.SquaredCircle
import xoduslol.composeapp.generated.resources.SquaredUp
import kotlin.math.hypot
import kotlin.math.roundToInt


enum class DragValue { Down, Normal, Up }

@Composable
fun MainScreen(navController: NavController = rememberNavController()) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val density = LocalDensity.current
        val colorScheme = MaterialTheme.colorScheme

        val normalBallSize = if (maxHeight >= maxWidth * 1.5f) maxWidth * 1.2f else maxHeight * 0.75f
        val changeBallSize = hypot(maxWidth.value, maxHeight.value).dp * 1.2f - normalBallSize

        val downPosition = with (density) { (maxHeight + normalBallSize * 0.2f).toPx() }
        val normalPosition = with (density) { (maxHeight - normalBallSize * 0.2f).toPx() }
        val upPosition = with (density) { (maxHeight * 0.5f).toPx() }

        var isDragEnabled by remember { mutableStateOf(true) }
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
            snapshotFlow {
                Triple(
                    anchoredDraggableState.progress(DragValue.Normal, DragValue.Down),
                    anchoredDraggableState.progress(DragValue.Normal, DragValue.Up),
                    anchoredDraggableState.settledValue
                )
            }
                .distinctUntilChanged()
                .collect { (downProgress, upProgress, settleValue) ->
                    ballSize = normalBallSize * (1f - downProgress * 0.5f) + changeBallSize * upProgress
                    println("$downProgress, $upProgress")

                    if (settleValue == DragValue.Up) {
                        isDragEnabled = false
                    } else if (settleValue == DragValue.Down) {
                        navController.popBackStack()
                    }
                }
        }

        val squaredUpImage = imageResource(Res.drawable.SquaredUp)
        val squaredCircleImage = imageResource(Res.drawable.SquaredCircle)

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .anchoredDraggable(
                    state = anchoredDraggableState,
                    orientation = Orientation.Vertical,
                    enabled = isDragEnabled,
                )
        ) {
            drawIntoCanvas { canvas ->
                canvas.saveLayer(Rect(0f, 0f, size.width, size.height), Paint())

                drawImage(
                    image = squaredUpImage,
                    dstOffset = IntOffset(
                        x = (maxWidth.toPx() * 0.5f - normalBallSize.toPx() * 5f / 34f).roundToInt(),
                        y = (maxHeight.toPx() - normalBallSize.toPx()).roundToInt()
                    ),
                    dstSize = IntSize(
                        width = (normalBallSize.toPx() * 5f / 17f).roundToInt(),
                        height = (normalBallSize.toPx() * 5f / 17f).roundToInt()
                    ),
                    filterQuality = FilterQuality.None
                )

                drawRect(
                    color = colorScheme.onSurface.copy(alpha = 0.5f),
                    topLeft = Offset(
                        x = maxWidth.toPx() * 0.5f - normalBallSize.toPx() * 5f / 34f,
                        y = maxHeight.toPx() - normalBallSize.toPx()
                    ),
                    size = Size(
                        width = normalBallSize.toPx() * 5f / 17f,
                        height = normalBallSize.toPx() * 5f / 17f
                    ),
                    blendMode = BlendMode.SrcIn
                )

                canvas.restore()

                canvas.saveLayer(Rect(0f, 0f, size.width, size.height), Paint())

                drawImage(
                    image = squaredCircleImage,
                    dstOffset = IntOffset(
                        x = (maxWidth.toPx() * 0.5f - ballSize.toPx() * 0.5f).roundToInt(),
                        y = (anchoredDraggableState.requireOffset() - ballSize.toPx() * 0.5f).roundToInt()
                    ),
                    dstSize = IntSize(
                        width = ballSize.toPx().roundToInt(),
                        height = ballSize.toPx().roundToInt()
                    ),
                    filterQuality = FilterQuality.None
                )

                drawRect(
                    color = colorScheme.primaryContainer,
                    topLeft = Offset(
                        x = maxWidth.toPx() * 0.5f - ballSize.toPx() * 0.5f,
                        y = anchoredDraggableState.requireOffset() - ballSize.toPx() * 0.5f
                    ),
                    size = Size(
                        width = ballSize.toPx(),
                        height = ballSize.toPx()
                    ),
                    blendMode = BlendMode.SrcIn
                )

                canvas.restore()
            }
        }
    }
}
