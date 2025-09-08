package com.taeyeon.xoduslol

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taeyeon.xoduslol.ui.AppTheme
import kotlinx.coroutines.delay


@Composable
fun App() {
    AppTheme(darkTheme = false) {
        Surface {
            MainScreen()
        }
    }
}


@Composable
fun MainScreen() {
    var boldIndex by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(true) {
        while (true) {
            boldIndex++
            if (boldIndex == Int.MAX_VALUE) boldIndex = 0
            delay(250)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
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
                onClick = {  },
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
    }
}
