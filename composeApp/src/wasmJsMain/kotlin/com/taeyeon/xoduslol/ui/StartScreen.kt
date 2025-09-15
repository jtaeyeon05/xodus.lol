package com.taeyeon.xoduslol.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.twotone.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.taeyeon.xoduslol.navigation.Screen
import com.taeyeon.xoduslol.util.replaceHash
import io.github.vinceglb.confettikit.compose.ConfettiKit
import io.github.vinceglb.confettikit.core.Angle
import io.github.vinceglb.confettikit.core.Party
import io.github.vinceglb.confettikit.core.Position
import io.github.vinceglb.confettikit.core.Spread
import io.github.vinceglb.confettikit.core.emitter.Emitter
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.imageResource
import xoduslol.composeapp.generated.resources.Res
import xoduslol.composeapp.generated.resources.SquaredFace
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds


@Composable
fun StartScreen(
    navController: NavController = rememberNavController(),
    screen: Screen.Start = Screen.Start()
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        var partyMode by rememberSaveable { mutableStateOf(screen.partyMode) }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var titleSize by remember { mutableStateOf(IntSize.Zero) }
            val offsetX by rememberInfiniteTransition()
                .animateFloat(
                    initialValue = -titleSize.width.toFloat(),
                    targetValue = 0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                )

            Text(
                modifier = Modifier.onSizeChanged { titleSize = it },
                text = buildAnnotatedString {
                    if (partyMode) {
                        pushStyle(
                            SpanStyle(
                                brush = Brush.linearGradient(
                                    colors = List(21) { index ->
                                        Color.hsl(
                                            hue = 36f * (index % 10),
                                            saturation = 0.9f,
                                            lightness = 0.4f
                                        )
                                    },
                                    start = Offset(offsetX, 0f),
                                    end = Offset(offsetX + titleSize.width.toFloat() * 2f, 0f)
                                )
                            )
                        )
                    }

                    append("Hi, It's ")
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append("XODUS.LOL")
                    }
                },
                fontSize = 30.sp,
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate(Screen.Main) },
                shape = RectangleShape,
                contentPadding = PaddingValues(
                    vertical = 16.dp,
                    horizontal = 24.dp
                ),
            ) {
                val buttonText = "Let's Go"
                var boldIndex by rememberSaveable { mutableStateOf(0) }
                LaunchedEffect(true) {
                    while (true) {
                        boldIndex++
                        if (boldIndex == Int.MAX_VALUE) boldIndex = 0
                        delay(250)
                    }
                }

                Text(
                    text = buildAnnotatedString {
                        for (i in 0 ..< buttonText.length) {
                            if (i == boldIndex % buttonText.length) {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.Bold)
                                ) {
                                    append(buttonText[i])
                                }
                            } else {
                                append(buttonText[i])
                            }
                        }
                    },
                    fontSize = 24.sp,
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp)
        ) {
            Text(
                text = "Contact: ",
                color = LocalContentColor.current.copy(alpha = 0.5f),
                fontSize = 18.sp,
            )
            SelectionContainer {
                Text(
                    text = "email@xodus.lol",
                    color = LocalContentColor.current.copy(alpha = 0.5f),
                    fontSize = 18.sp,
                )
            }
        }

        Surface(
            modifier = Modifier
                .padding(12.dp)
                .requiredSize(48.dp)
                .align(Alignment.TopEnd),
            onClick = {
                partyMode = !partyMode
                replaceHash(if (partyMode) "#start?partyMode" else "#start")
            },
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(
                modifier = Modifier
                    .padding(12.dp)
                    .size(24.dp),
                imageVector = if (partyMode) Icons.Filled.Lightbulb else Icons.TwoTone.Lightbulb,
                contentDescription = "파티!",
            )
        }

        val messageList = listOf(
            "안녕, 내 이름은 버튼버튼이야",
            "날 절-대 길게 클릭하거나 옮기지 마!",
            "날 길게 클릭하면 다른 사이트로 이동할거야",
            "대신 아래 버튼을 클릭해",
            "그러면 나는 이만 가볼게",
        )
        var message by rememberSaveable { mutableStateOf<String?>(null) }
        var messageNotifier by rememberSaveable { mutableStateOf(0) }
        LaunchedEffect(messageNotifier) {
            for (i in messageList.indices) {
                message = messageList[i]
                delay(2500)
            }
            message = null
        }

        var buttonButtonX by remember { mutableStateOf(0f) }
        var buttonButtonY by remember { mutableStateOf(0f) }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .offset { IntOffset(buttonButtonX.roundToInt(), buttonButtonY.roundToInt()) },
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnimatedContent(
                targetState = message,
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 250, delayMillis = 50))
                        .togetherWith(fadeOut(animationSpec = tween(durationMillis = 250)))
                        .using(SizeTransform(clip = false))
                },
                contentAlignment = Alignment.TopEnd,
            ) { targetMessage ->
                if (targetMessage != null) {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "버튼버튼",
                            color = LocalContentColor.current.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                        )
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 0.dp,
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp,
                            ),
                            color = MaterialTheme.colorScheme.inverseSurface,
                            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                                text = targetMessage,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
            }
            Surface(
                modifier = Modifier
                    .requiredSize(48.dp)
                    .combinedClickable(
                        onClick = {
                            messageNotifier++
                            buttonButtonX = 0f
                            buttonButtonY = 0f
                        },
                        onLongClick = { navController.navigate(Screen.Move(target = "https://jtaeyeon05.github.io/")) },
                    )
                    .draggable2D(
                        state = rememberDraggable2DState { delta ->
                            buttonButtonX += delta.x
                            buttonButtonY += delta.y
                        }
                    ),
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    modifier = Modifier
                        .padding(12.dp)
                        .size(24.dp),
                    painter = BitmapPainter(
                        image = imageResource(Res.drawable.SquaredFace),
                        filterQuality = FilterQuality.None,
                    ),
                    contentDescription = "버튼버튼",
                )
            }
        }

        if (partyMode) {
            ConfettiKit(
                modifier = Modifier.fillMaxSize(),
                parties = listOf(
                    Party(
                        speed = 0f,
                        maxSpeed = 15f,
                        damping = 0.9f,
                        angle = Angle.BOTTOM,
                        spread = Spread.ROUND,
                        colors = List(10) { index ->
                            Color.hsl(
                                hue = 36f * (index % 10),
                                saturation = 0.9f,
                                lightness = 0.8f
                            ).toArgb()
                        },
                        position = Position.Relative(0.0, 0.0).between(Position.Relative(1.0, 0.0)),
                        emitter = Emitter(duration = 10.seconds).perSecond(100),
                    )
                ),
            )
        }
    }
}
