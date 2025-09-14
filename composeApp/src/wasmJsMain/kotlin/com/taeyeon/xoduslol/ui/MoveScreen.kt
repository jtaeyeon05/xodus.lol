package com.taeyeon.xoduslol.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.taeyeon.xoduslol.navigation.Screen
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.imageResource
import xoduslol.composeapp.generated.resources.Res
import xoduslol.composeapp.generated.resources.SquaredCircle
import xoduslol.composeapp.generated.resources.SquaredUp
import kotlin.math.hypot
import kotlin.math.roundToInt


private const val SQUARED_CIRCLE_SIZE = 17f
private const val SQUARED_UP_WIDTH = 5f
private const val SQUARED_UP_HEIGHT = 3f
private const val DRAW_MARGIN_PX = 10f


@Composable
fun MoveScreen(
    navController: NavController = rememberNavController(),
    screen: Screen.Move = Screen.Move(),
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val urlState by rememberUpdatedState(screen.target)
        val newTabState by rememberUpdatedState(screen.newTab)

        val density = LocalDensity.current
        val colorScheme = MaterialTheme.colorScheme

        val normalBallSize = if (maxHeight >= maxWidth * 1.5f) maxWidth * 1.2f else maxHeight * 0.75f
        val changeBallSize = hypot(maxWidth.value, maxHeight.value).dp * 1.2f - normalBallSize

        val squaredUpImage = imageResource(Res.drawable.SquaredUp)
        val squaredCircleImage = imageResource(Res.drawable.SquaredCircle)

        val anchoredDraggableState = remember(maxWidth, maxHeight) {
            AnchoredDraggableState(
                anchors = DraggableAnchors {
                    with (density) {
                        "down" at (maxHeight + normalBallSize * 0.2f).toPx()
                        "normal" at (maxHeight - normalBallSize * 0.2f).toPx()
                        "up" at (maxHeight * 0.5f).toPx()
                    }
                },
                initialValue = "normal"
            )
        }

        var ballSize by remember(maxWidth, maxHeight) { mutableStateOf(normalBallSize) }
        var ballDownProgress by remember(maxWidth, maxHeight) { mutableStateOf(0f) }
        var ballUpProgress by remember(maxWidth, maxHeight) { mutableStateOf(0f) }
        LaunchedEffect(anchoredDraggableState) {
            snapshotFlow {
                Triple(
                    anchoredDraggableState.progress("normal", "down"),
                    anchoredDraggableState.progress("normal", "up"),
                    anchoredDraggableState.settledValue
                )
            }
                .distinctUntilChanged()
                .collect { (downProgress, upProgress, settleValue) ->
                    ballSize = normalBallSize * (1f - downProgress * 0.5f) + changeBallSize * upProgress

                    ballDownProgress = downProgress
                    ballUpProgress = upProgress

                    if (settleValue == "up") {
                        if (newTabState) {
                            window.open(urlState ?: "https://github.com/jtaeyeon05", "_blank")?.focus()
                            anchoredDraggableState.animateTo("normal")
                        } else {
                            window.open(urlState ?: "https://github.com/jtaeyeon05", "_self")
                        }
                    } else if (settleValue == "down") {
                        navController.popBackStack()
                    }
                }
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(maxHeight - normalBallSize * (SQUARED_CIRCLE_SIZE - 1f) / SQUARED_CIRCLE_SIZE - 32.dp)
                .zIndex(1f),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val messageList = if (urlState != null) listOf(
                "다른 사이트로 이동하려고 하는구나",
                "초록 원을 위로 올리면 아래 사이트로 이동할거야\n$urlState",
                "다시 원래 화면으로 돌아가고 싶으면\n초록 원을 아래로 내려줘",
                "그러면 나는 너의 선택을 기다릴게",
                "• • •",
            ) else listOf(
                "다른 사이트로 이동하려고 하는구나",
                "그런데 나에게 이동할 사이트 주소가 없는데?",
                "흠 • • •",
                "초록 원을 위로 올리면\n나의 깃허브로 이동시켜줄게",
                "다시 원래 화면으로 돌아가고 싶으면\n초록 원을 아래로 내려줘",
                "그러면 나는 너의 선택을 기다릴게",
                "• • •",
            )
            var message by rememberSaveable(urlState) { mutableStateOf("") }
            var messageNotifier by rememberSaveable { mutableStateOf(0) }
            LaunchedEffect(messageNotifier, urlState) {
                for (i in messageList.indices) {
                    message = ""
                    for (j in 0 ..< messageList[i].length) {
                        message += messageList[i][j]
                        delay(50)
                    }
                    delay(2500)
                }
            }

            Surface(
                onClick = { messageNotifier++ },
                color = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Filled.EmojiEmotions,
                        contentDescription = "버튼버튼",
                    )
                    Text(
                        text = "버튼버튼",
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                    )
                }
            }
            BasicText(
                text = message,
                maxLines = message.count { it == '\n' } + 1,
                style = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    lineHeight = 1.2f.em
                ),
                autoSize = TextAutoSize.StepBased(
                    maxFontSize = 24.sp,
                    stepSize = 2.sp,
                ),
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .anchoredDraggable(
                    state = anchoredDraggableState,
                    orientation = Orientation.Vertical,
                )
                .zIndex(if (ballUpProgress > 0f) 2f else 0f)
        ) {
            val widthPx = size.width
            val heightPx = size.height
            val normalBallPx = normalBallSize.toPx()
            val ballPx = ballSize.toPx()

            drawIntoCanvas { canvas ->
                // SquaredUp ↓

                val upX = widthPx * 0.5f - normalBallPx * SQUARED_UP_WIDTH / SQUARED_CIRCLE_SIZE * 0.5f
                val upY = heightPx - normalBallPx * (SQUARED_CIRCLE_SIZE - 1f) / SQUARED_CIRCLE_SIZE
                val upWidth = normalBallPx * SQUARED_UP_WIDTH / SQUARED_CIRCLE_SIZE
                val upHeight = normalBallPx * SQUARED_UP_HEIGHT / SQUARED_CIRCLE_SIZE

                canvas.saveLayer(
                    bounds = Rect(
                        left = upX - DRAW_MARGIN_PX,
                        top = upY - DRAW_MARGIN_PX,
                        right = upX + upWidth + DRAW_MARGIN_PX,
                        bottom = upY + upHeight + DRAW_MARGIN_PX
                    ),
                    paint = Paint()
                )

                drawImage(
                    image = squaredUpImage,
                    dstOffset = IntOffset(
                        x = upX.roundToInt(),
                        y = upY.roundToInt()
                    ),
                    dstSize = IntSize(
                        width = upWidth.roundToInt(),
                        height = upHeight.roundToInt()
                    ),
                    filterQuality = FilterQuality.None
                )

                drawRect(
                    color = colorScheme.onSurface.copy(alpha = 0.5f * (1f - ballDownProgress)),
                    topLeft = Offset(
                        x = upX - DRAW_MARGIN_PX,
                        y = upY - DRAW_MARGIN_PX
                    ),
                    size = Size(
                        width = upWidth + DRAW_MARGIN_PX * 2f,
                        height = upHeight + DRAW_MARGIN_PX * 2f
                    ),
                    blendMode = BlendMode.SrcIn
                )

                canvas.restore()

                // SquaredUp ↑
                // SquaredCircle ↓

                val circleX = widthPx * 0.5f - ballPx * 0.5f
                val circleY = anchoredDraggableState.requireOffset() - ballPx * 0.5f

                canvas.saveLayer(
                    bounds = Rect(
                        left = circleX - DRAW_MARGIN_PX,
                        top = circleY - DRAW_MARGIN_PX,
                        right = circleX + ballPx + DRAW_MARGIN_PX,
                        bottom = circleY + ballPx + DRAW_MARGIN_PX
                    ),
                    paint = Paint()
                )

                drawImage(
                    image = squaredCircleImage,
                    dstOffset = IntOffset(
                        x = circleX.roundToInt(),
                        y = circleY.roundToInt()
                    ),
                    dstSize = IntSize(
                        width = ballSize.toPx().roundToInt(),
                        height = ballSize.toPx().roundToInt()
                    ),
                    filterQuality = FilterQuality.None
                )

                drawRect(
                    color = colorScheme.primary
                        .copy(alpha = 1f - ballUpProgress)
                        .compositeOver(colorScheme.surface),
                    topLeft = Offset(
                        x = circleX - DRAW_MARGIN_PX,
                        y = circleY - DRAW_MARGIN_PX
                    ),
                    size = Size(
                        width = ballPx + DRAW_MARGIN_PX * 2f,
                        height = ballPx + DRAW_MARGIN_PX * 2f
                    ),
                    blendMode = BlendMode.SrcIn
                )

                canvas.restore()

                // SquaredCircle ↑
            }
        }
    }
}
