package com.taeyeon.xoduslol.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource


@Composable
private fun SquaredIconButtonSurface(
    modifier: Modifier,
    onClick: (() -> Unit),
    onLongClick: (() -> Unit)?,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .requiredSize(48.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        content = content
    )
}

@Composable
fun SquaredIconButton(
    onClick: () -> Unit,
    resource: DrawableResource,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
) {
    SquaredIconButtonSurface(
        modifier = modifier,
        onClick = onClick,
        onLongClick = onLongClick,
    ) {
        Icon(
            modifier = Modifier
                .padding(12.dp)
                .size(24.dp),
            painter = BitmapPainter(
                image = imageResource(resource),
                filterQuality = FilterQuality.None,
            ),
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun SquaredIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
) {
    SquaredIconButtonSurface(
        modifier = modifier,
        onClick = onClick,
        onLongClick = onLongClick,
    ) {
        Icon(
            modifier = Modifier
                .padding(12.dp)
                .size(24.dp),
            imageVector = imageVector,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun SquaredIconButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
) {
    SquaredIconButtonSurface(
        modifier = modifier,
        onClick = onClick,
        onLongClick = onLongClick,
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            BasicText(
                text = text,
                maxLines = text.count { it == '\n' } + 1,
                style = LocalTextStyle.current.copy(
                    color = LocalContentColor.current,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 1.0f.em,
                ),
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 2f.sp,
                    stepSize = 0.1f.sp
                ),
            )
        }
    }
}
