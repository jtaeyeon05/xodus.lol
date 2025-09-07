package com.taeyeon.xoduslol

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import xoduslol.composeapp.generated.resources.Galmuri11
import xoduslol.composeapp.generated.resources.Galmuri11_Bold
import xoduslol.composeapp.generated.resources.Galmuri11_Condensed
import xoduslol.composeapp.generated.resources.Res


@Composable
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val galmuri11 = FontFamily(
                Font(
                    resource = Res.font.Galmuri11,
                    weight = FontWeight.Normal,
                    style = FontStyle.Normal,
                ),
                Font(
                    resource = Res.font.Galmuri11_Bold,
                    weight = FontWeight.Bold,
                    style = FontStyle.Normal,
                ),
                Font(
                    resource = Res.font.Galmuri11_Condensed,
                    weight = FontWeight.Light,
                    style = FontStyle.Normal,
                ),
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
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
                    fontFamily = galmuri11,
                    fontSize = 30.sp,
                )
            }
        }
    }
}
