package com.taeyeon.xoduslol.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.taeyeon.xoduslol.util.GainNode
import com.taeyeon.xoduslol.util.OscillatorNode
import com.taeyeon.xoduslol.util.createAudioContext
import com.taeyeon.xoduslol.util.playTone
import com.taeyeon.xoduslol.util.stopTone
import com.taeyeon.xoduslol.util.updateFrequency
import com.taeyeon.xoduslol.util.updateGain
import com.taeyeon.xoduslol.util.updateWaveform
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.imageResource
import xoduslol.composeapp.generated.resources.Res
import xoduslol.composeapp.generated.resources.SquaredCircle
import kotlin.math.pow
import kotlin.math.roundToInt


private const val MIN_FREQUENCY = 50f
private const val MAX_FREQUENCY = 2000f
private val KNOB_MIN_SIZE = 100.dp
private val KNOB_MAX_SIZE = 200.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    navController: NavController = rememberNavController()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val density = LocalDensity.current
        val colorScheme = MaterialTheme.colorScheme
        val squaredCircleImage = imageResource(Res.drawable.SquaredCircle)

        var knobXRatio by rememberSaveable { mutableStateOf(0.5f) }
        var knobYRatio by rememberSaveable { mutableStateOf(0.5f) }
        var knobSizeRatio by rememberSaveable { mutableStateOf(0.5f) } // TODO

        var canvasSize by remember { mutableStateOf(IntSize.Zero) }
        val knobSize by remember { derivedStateOf { KNOB_MIN_SIZE + (KNOB_MAX_SIZE - KNOB_MIN_SIZE) * knobSizeRatio } }
        var playButtonText by rememberSaveable { mutableStateOf("PLAY") }

        val audioContext = rememberSaveable { createAudioContext() }
        var gainNode by rememberSaveable { mutableStateOf<GainNode?>(null) }
        var oscillatorNode by rememberSaveable { mutableStateOf<OscillatorNode?>(null) }

        val gain by rememberSaveable { derivedStateOf { knobXRatio.toDouble() } }
        val frequency by rememberSaveable { derivedStateOf { (MIN_FREQUENCY * (MAX_FREQUENCY / MIN_FREQUENCY).pow(1f - knobYRatio)).toDouble() } }
        var waveForm by rememberSaveable { mutableStateOf("sine") } // TODO
        var isPlaying by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(gain) {
            if (gainNode != null && isPlaying) {
                audioContext.updateGain(
                    newGain = gain,
                    gainNode = gainNode!!
                )
            }
        }
        LaunchedEffect(frequency) {
            if (oscillatorNode != null && isPlaying) {
                audioContext.updateFrequency(
                    newFrequency = frequency,
                    oscillatorNode = oscillatorNode!!
                )
            }
        }
        LaunchedEffect(waveForm) {
            if (oscillatorNode != null && isPlaying) {
                audioContext.updateWaveform(
                    newWaveForm = waveForm,
                    oscillatorNode = oscillatorNode!!
                )
            }
        }

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

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onSizeChanged { canvasSize = it }
                .transformable(
                    state = rememberTransformableState { zoomChange, offsetChange, _ ->
                        knobSizeRatio *= zoomChange
                        knobSizeRatio = knobSizeRatio.coerceIn(0f, 1f)
                        with (density) {
                            knobXRatio += offsetChange.x / (canvasSize.width - knobSize.toPx())
                            knobYRatio += offsetChange.y / (canvasSize.height - knobSize.toPx())
                            knobXRatio = knobXRatio.coerceIn(0f, 1f)
                            knobYRatio = knobYRatio.coerceIn(0f, 1f)
                        }
                    }
                )
                .onPointerEvent(PointerEventType.Scroll) { pointerEvent ->
                    knobSizeRatio -= pointerEvent.changes.first().scrollDelta.y / 500f
                    knobSizeRatio = knobSizeRatio.coerceIn(0f, 1f)
                }
        ) {
            val widthPx = canvasSize.width
            val heightPx = canvasSize.height

            val knobPx = knobSize.toPx()

            drawIntoCanvas { canvas ->
                canvas.saveLayer(
                    bounds = Rect(
                        left = 0f,
                        top = 0f,
                        right = widthPx.toFloat(),
                        bottom = heightPx.toFloat()
                    ),
                    paint = Paint()
                )

                drawImage(
                    image = squaredCircleImage,
                    dstOffset = IntOffset(
                        x = ((widthPx - knobPx) * knobXRatio).roundToInt(),
                        y = ((heightPx - knobPx) * knobYRatio).roundToInt()
                    ),
                    dstSize = IntSize(
                        width = knobPx.roundToInt(),
                        height = knobPx.roundToInt()
                    ),
                    filterQuality = FilterQuality.None
                )

                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(colorScheme.tertiary, colorScheme.primary, colorScheme.secondary),
                        start = Offset(x = 0f, y = heightPx.toFloat()),
                        end = Offset(x = widthPx.toFloat(), y = 0f),
                    ),
                    topLeft = Offset(x = 0f, y = 0f),
                    size = Size(width = widthPx.toFloat(), height = heightPx.toFloat()),
                    blendMode = BlendMode.SrcIn
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isPlaying = !isPlaying
                    if (isPlaying) {
                        audioContext.playTone(
                            frequency = frequency,
                            gain = gain,
                            waveForm = waveForm,
                            setGainNode = { gainNode = it },
                            setOscillatorNode = { oscillatorNode = it },
                        )
                    } else {
                        if (gainNode != null && oscillatorNode != null) {
                            audioContext.stopTone(
                                gainNode = gainNode!!,
                                oscillatorNode = oscillatorNode!!,
                                setGainNode = { gainNode = it },
                                setOscillatorNode = { oscillatorNode = it },
                            )
                        }
                    }
                },
            color = colorScheme.primary,
            contentColor = colorScheme.onPrimary,
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
