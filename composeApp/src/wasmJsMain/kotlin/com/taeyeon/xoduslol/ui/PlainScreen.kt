package com.taeyeon.xoduslol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.taeyeon.xoduslol.navigation.Screen
import com.taeyeon.xoduslol.util.floorMultiple
import kotlinx.coroutines.delay
import kotlin.math.min
import kotlin.random.Random


@Composable
fun PlainScreen(
    navController: NavController = rememberNavController(),
    screen: Screen.Plain = Screen.Plain(),
) {
    BoxWithConstraints {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
                    .background(
                        MaterialTheme.colorScheme.tertiary
                            .copy(alpha = 0.3f)
                            .compositeOver(Color.White)
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                val message by rememberUpdatedState(screen.message)
                var textContent by rememberSaveable { mutableStateOf("") }
                LaunchedEffect(message) {
                    textContent = ""
                    if (message != null) {
                        for (i in message!!.indices) {
                            textContent += message!![i]
                            delay(100)
                        }
                    }
                }

                BasicText(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    text = textContent,
                    style = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        lineHeight = 1.2f.em,
                    ),
                    autoSize = TextAutoSize.StepBased(
                        maxFontSize = 32.sp,
                        stepSize = 2.sp,
                    ),
                )
            }
        }

        repeat(5) {
            var cloudNotifier by rememberSaveable { mutableStateOf(0) }
            var cloudW by remember { mutableStateOf(0.dp) }
            var cloudH by remember { mutableStateOf(0.dp) }
            var cloudX by remember { mutableStateOf(0.dp) }
            var cloudY by remember { mutableStateOf(0.dp) }

            LaunchedEffect(cloudNotifier, maxWidth, maxHeight) {
                cloudW = listOf(100.dp, 150.dp, 200.dp)[Random.nextInt(3)]
                cloudH = listOf(50.dp, 75.dp, 100.dp)[Random.nextInt(3)]
                cloudX = if (cloudNotifier == 0) ((maxWidth + cloudW).value * Random.nextFloat()).floorMultiple(10f).dp - cloudW else (maxWidth + 300.dp * Random.nextFloat()).value.floorMultiple(10f).dp
                cloudY = ((maxHeight * 0.6f - 100.dp - cloudH).value * Random.nextFloat() + 50).floorMultiple(25f).dp

                while (cloudX >= -cloudW) {
                    cloudX -= 50.dp
                    delay(200)
                }
                cloudNotifier++
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = cloudX, y = cloudY)
                    .width(cloudW)
                    .height(cloudH)
                    .background(Color.White)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    vertical = min((maxHeight.value * 0.6f - 100f) * 0.5f, 100f).coerceAtLeast(0f).dp,
                    horizontal = min((maxWidth.value - 100f) * 0.5f, 100f).coerceAtLeast(0f).dp
                )
                .size(100.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable(
                    onClick = {
                        navController.popBackStack()
                    }
                )
        )
    }
}
