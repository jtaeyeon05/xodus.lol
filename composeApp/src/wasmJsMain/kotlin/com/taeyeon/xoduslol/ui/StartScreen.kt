package com.taeyeon.xoduslol.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlin.math.roundToInt


@Composable
fun StartScreen(navController: NavController = rememberNavController()) {
    var boldIndex by rememberSaveable { mutableStateOf(0) }
    LaunchedEffect(true) {
        while (true) {
            boldIndex++
            if (boldIndex == Int.MAX_VALUE) boldIndex = 0
            delay(250)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = buildAnnotatedString {
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
                onClick = { navController.navigate(route = "main") },
                shape = RectangleShape,
                contentPadding = PaddingValues(
                    vertical = 16.dp,
                    horizontal = 24.dp
                ),
            ) {
                val buttonText = "Let's Go"
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

        FilledIconButton(
            onClick = { /* TODO */ },
            modifier = Modifier
                .padding(12.dp)
                .requiredSize(48.dp)
                .align(Alignment.TopEnd),
        ) {
            Icon(
                imageVector = Icons.Filled.Fingerprint,
                contentDescription = "헉",
                modifier = Modifier.padding(12.dp),
            )
        }

        val messageList = listOf(
            "안녕, 내 이름은 버튼버튼이야",
            "날 절-대 클릭하지마",
            "날 클릭하면 나의 다른 사이트로 이동할거야",
            "대신 아래 버튼을 클릭해",
            "아 그리고 날 옮기지 마!",
            "그러면 나는 이만 가볼게",
        )
        var message by rememberSaveable { mutableStateOf<String?>(null) }
        var messageNotifier by rememberSaveable { mutableStateOf(0) }
        LaunchedEffect(messageNotifier) {
            for (i in 0 ..< 6) {
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
                                text = targetMessage,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
            }
            Surface(
                modifier = Modifier
                    .requiredSize(48.dp)
                    .clip(CircleShape)
                    .combinedClickable(
                        onClick = { window.open("https://jtaeyeon05.github.io/", "_blank")?.focus() },
                        onLongClick = {
                            messageNotifier++
                            buttonButtonX = 0f
                            buttonButtonY = 0f
                        }
                    )
                    .draggable2D(
                        state = rememberDraggable2DState { delta ->
                            buttonButtonX += delta.x
                            buttonButtonY += delta.y
                        }
                    ),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = Icons.Filled.EmojiEmotions,
                    contentDescription = "버튼버튼",
                    modifier = Modifier
                        .padding(12.dp)
                        .size(24.dp),
                )
            }
        }
    }
}
