package com.taeyeon.xoduslol.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay


@Composable
fun MainScreen(
    navController: NavController = rememberNavController()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        var isPlaying by rememberSaveable { mutableStateOf(false) }
        var playButtonText by rememberSaveable { mutableStateOf("PLAY") }

        LaunchedEffect(isPlaying) {
            if (isPlaying) {
                for (i in 0 .. Int.MAX_VALUE) {
                    playButtonText = "PLAYING" + ".".repeat(i % 4)
                    delay(200)
                }
            } else {
                playButtonText = "PLAY"
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {}

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isPlaying = !isPlaying },
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = playButtonText,
                    fontSize = 32.sp,
                )
            }
        }
    }
}
