package com.taeyeon.xoduslol.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.taeyeon.xoduslol.navigation.Screen


@Composable
fun CorridorButton(
    onClick: () -> Unit,
    text: String,
    color: Color,
    contentColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        color = color,
        contentColor = contentColor,
        border = BorderStroke(
            width = 8.dp,
            color = borderColor
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp,
            )
        }
    }
}

@Composable
fun CorridorScreen(
    navController: NavController = rememberNavController(),
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CorridorButton(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = { navController.navigate(Screen.AudioPlayground) },
            text = "AudioPlayground",
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            borderColor = MaterialTheme.colorScheme.primary,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CorridorButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                onClick = {
                    navController.navigate(
                        route = Screen.Plain(
                            message = "여기 대신 AudioPlayground에 가보는 것은 어때?\n" +
                                    "태양을 클릭하면 이전 화면으로 돌아갈 수 있어"
                        )
                    )
                },
                text = "Plain",
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                borderColor = MaterialTheme.colorScheme.secondary,
            )
            CorridorButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                onClick = { navController.popBackStack<Screen.Home>(inclusive = false) },
                text = "Home",
                color = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                borderColor = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}
