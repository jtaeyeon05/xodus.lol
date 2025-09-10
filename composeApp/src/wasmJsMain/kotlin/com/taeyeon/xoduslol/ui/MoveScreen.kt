package com.taeyeon.xoduslol.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.imageResource
import xoduslol.composeapp.generated.resources.Res
import xoduslol.composeapp.generated.resources.SquaredCircle
import xoduslol.composeapp.generated.resources.SquaredUp
import kotlin.math.hypot
import kotlin.math.roundToInt


enum class DragValue { Down, Normal, Up }

@Composable
fun MoveScreen(navController: NavController = rememberNavController()) {
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

        val squaredUpImage = imageResource(Res.drawable.SquaredUp)
        val squaredCircleImage = imageResource(Res.drawable.SquaredCircle)
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

        var ballDownProgress by remember { mutableStateOf(0f) }
        var ballUpProgress by remember { mutableStateOf(0f) }
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

                    ballDownProgress = downProgress
                    ballUpProgress = upProgress

                    if (settleValue == DragValue.Up) {
                        window.open("https://jtaeyeon05.github.io/", "_self")
                    } else if (settleValue == DragValue.Down) {
                        navController.popBackStack()
                    }
                }
        }

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(maxHeight - normalBallSize * 0.92f - 48.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 12.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val messageList = listOf(
                "다른 사이트로 이동하려고 하는구나",
                "그래, 초록 원을 위로 올리면\n" +
                        "https://jtaeyeon05.github.io/\n" +
                        "로 이동할거야",
                "다시 원래 화면으로 돌아가고 싶으면\n" +
                        "초록 원을 아래로 내려줘",
                "그러면 나는 너의 선택을 기다릴게",
                "• • •",
            )
            var message by rememberSaveable { mutableStateOf("") }
            LaunchedEffect(true) {
                for (i in 0 ..< 5) {
                    message = ""
                    for (j in 0 ..< messageList[i].length) {
                        message += messageList[i][j]
                        delay(50)
                    }
                    delay(2500)
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Filled.EmojiEmotions,
                    contentDescription = "버튼버튼",
                    tint = LocalContentColor.current.copy(alpha = 0.8f),
                )
                Text(
                    text = "버튼버튼",
                    color = LocalContentColor.current.copy(alpha = 0.8f),
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                )
            }
            Text(
                text = message,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
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
            drawIntoCanvas { canvas ->
                canvas.saveLayer(Rect(0f, 0f, size.width, size.height), Paint())

                drawImage(
                    image = squaredUpImage,
                    dstOffset = IntOffset(
                        x = (maxWidth.toPx() * 0.5f - normalBallSize.toPx() * 5f / 34f).roundToInt(),
                        y = (maxHeight.toPx() - normalBallSize.toPx() * 0.92f).roundToInt()
                    ),
                    dstSize = IntSize(
                        width = (normalBallSize.toPx() * 5f / 17f).roundToInt(),
                        height = (normalBallSize.toPx() * 3f / 17f).roundToInt()
                    ),
                    filterQuality = FilterQuality.None
                )

                drawRect(
                    color = colorScheme.onSurface
                        .copy(alpha = 0.5f * (1f - ballDownProgress)),
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
                    color = colorScheme.primaryContainer
                        .copy(alpha = 1f - ballUpProgress)
                        .compositeOver(colorScheme.surface),
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
