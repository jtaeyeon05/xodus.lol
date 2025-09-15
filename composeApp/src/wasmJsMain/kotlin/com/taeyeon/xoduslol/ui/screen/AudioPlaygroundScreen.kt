package com.taeyeon.xoduslol.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.taeyeon.xoduslol.navigation.Screen
import com.taeyeon.xoduslol.ui.SquaredIconButton
import com.taeyeon.xoduslol.util.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.imageResource
import xoduslol.composeapp.generated.resources.Res
import xoduslol.composeapp.generated.resources.SquaredCircle
import xoduslol.composeapp.generated.resources.SquaredFace
import xoduslol.composeapp.generated.resources.SquaredLeft
import kotlin.math.pow
import kotlin.math.roundToInt


private const val MIN_FREQUENCY = 50f
private const val MAX_FREQUENCY = 2000f
private const val MIN_DETUNE = -50f
private const val MAX_DETUNE = +50f
private val KNOB_MIN_SIZE = 100.dp
private val KNOB_MAX_SIZE = 200.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AudioPlaygroundScreen(
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
        var knobSizeRatio by rememberSaveable { mutableStateOf(0.5f) }
        var waveFormSelector by rememberSaveable { mutableStateOf(0) }

        var canvasSize by remember { mutableStateOf(IntSize.Zero) }
        val knobSize by remember { derivedStateOf { KNOB_MIN_SIZE + (KNOB_MAX_SIZE - KNOB_MIN_SIZE) * knobSizeRatio } }
        var playButtonText by rememberSaveable { mutableStateOf("PLAY") }

        val audioContext = rememberSaveable { createAudioContext() }
        var gainNode by rememberSaveable { mutableStateOf<GainNode?>(null) }
        var oscillatorNode by rememberSaveable { mutableStateOf<OscillatorNode?>(null) }
        var oscillatorDetuneNode by rememberSaveable { mutableStateOf<OscillatorNode?>(null) }

        val gain by rememberSaveable { derivedStateOf { knobXRatio.toDouble() } }
        val frequency by rememberSaveable { derivedStateOf { (MIN_FREQUENCY * (MAX_FREQUENCY / MIN_FREQUENCY).pow(1f - knobYRatio)).toDouble() } }
        val detune by rememberSaveable { derivedStateOf { (MIN_DETUNE + (MAX_DETUNE - MIN_DETUNE) * knobSizeRatio).toDouble() } }
        val waveForm by rememberSaveable { derivedStateOf { OscillatorTypeList[waveFormSelector % OscillatorTypeList.size] } }
        var isPlaying by rememberSaveable { mutableStateOf(false) }

        DisposableEffect(true) {
            onDispose {
                if (isPlaying && gainNode != null && oscillatorNode != null && oscillatorDetuneNode != null) {
                    audioContext.stopTone(
                        gainNode = gainNode!!,
                        oscillatorNode = oscillatorNode!!,
                        oscillatorDetuneNode = oscillatorDetuneNode!!,
                        setGainNode = { gainNode = it },
                        setOscillatorNode = { oscillatorNode = it },
                        setOscillatorDetuneNode = { oscillatorDetuneNode = it },
                    )
                }
            }
        }

        LaunchedEffect(gain) {
            if (gainNode != null && isPlaying) {
                audioContext.updateGain(
                    newGain = gain,
                    gainNode = gainNode!!
                )
            }
        }
        LaunchedEffect(frequency) {
            if (oscillatorNode != null && oscillatorDetuneNode != null && isPlaying) {
                audioContext.updateFrequency(
                    newFrequency = frequency,
                    oscillatorNode = oscillatorNode!!,
                    oscillatorDetuneNode = oscillatorDetuneNode!!,
                )
            }
        }
        LaunchedEffect(detune) {
            print(detune)
            if (oscillatorDetuneNode != null && isPlaying) {
                audioContext.updateDetune(
                    newDetune = detune,
                    oscillatorDetuneNode = oscillatorDetuneNode!!
                )
            }
        }
        LaunchedEffect(waveForm) {
            if (oscillatorNode != null && isPlaying) {
                updateWaveform(
                    newWaveForm = waveForm,
                    oscillatorNode = oscillatorNode!!,
                    oscillatorDetuneNode = oscillatorDetuneNode!!,
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { canvasSize = it }
                    .transformable(
                        state = rememberTransformableState { zoomChange, offsetChange, _ ->
                            knobSizeRatio *= zoomChange
                            knobSizeRatio = knobSizeRatio.coerceIn(0f, 1f)
                            with(density) {
                                knobXRatio += offsetChange.x / (canvasSize.width - knobSize.toPx())
                                knobYRatio += offsetChange.y / (canvasSize.height - knobSize.toPx())
                                knobXRatio = knobXRatio.coerceIn(0f, 1f)
                                knobYRatio = knobYRatio.coerceIn(0f, 1f)
                            }
                        }
                    )
                    .onPointerEvent(PointerEventType.Scroll) { pointerEvent ->
                        knobSizeRatio -= pointerEvent.changes.first().scrollDelta.y / 1_000f
                        knobSizeRatio = knobSizeRatio.coerceIn(0f, 1f)
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                knobXRatio = 0.5f
                                knobYRatio = 0.5f
                                knobSizeRatio = 0.5f
                            }
                        )
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

            // Popup Buttons
            SquaredIconButton(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart),
                onClick = { navController.popBackStack<Screen.Start>(inclusive = false) },
                resource = Res.drawable.SquaredLeft,
                contentDescription = "뒤로",
            )

            Row(
                modifier = Modifier
                    .padding(
                        start = 12.dp + 48.dp + 12.dp,
                        top = 12.dp,
                        end = 12.dp
                    )
                    .align(Alignment.TopEnd),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val messageList = listOf(
                    "AudioPlayground에 온 것을 환영해",
                    "이곳에서는 WebAudio를 통해 놀 수 있어",
                    "노브를 좌우로 조절해 Gain을,",
                    "상하로 조절해 Frequency를,",
                    "크기를 조절해 Detune을 조절할 수 있어",
                    "노브를 더블탭해 이 값들을 초기화시킬 수 있어",
                    "또한, 아래 버튼들을 통해 파형을 조절할 수 있어",
                    "그러면, 이를 이용해 여러 소리를 내봐!",
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

                if (message != null) {
                    Column(
                        modifier = Modifier.weight(1f),
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
                                text = message!!,
                                style = LocalTextStyle.current.copy(
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.End,
                                    lineBreak = LineBreak.Paragraph
                                )
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.requiredWidth(48.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    SquaredIconButton(
                        onClick = { messageNotifier++ },
                        resource = Res.drawable.SquaredFace,
                        contentDescription = "버튼버튼",
                    )
                    SquaredIconButton(
                        onClick = {
                            if (waveFormSelector + 1 == OscillatorTypeList.size) waveFormSelector = 0
                            else waveFormSelector++
                        },
                        text = waveForm
                    )
                }
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
                            detune = detune,
                            waveForm = waveForm,
                            setGainNode = { gainNode = it },
                            setOscillatorNode = { oscillatorNode = it },
                            setOscillatorDetuneNode = { oscillatorDetuneNode = it },
                        )
                    } else {
                        if (gainNode != null && oscillatorNode != null && oscillatorDetuneNode != null) {
                            audioContext.stopTone(
                                gainNode = gainNode!!,
                                oscillatorNode = oscillatorNode!!,
                                oscillatorDetuneNode = oscillatorDetuneNode!!,
                                setGainNode = { gainNode = it },
                                setOscillatorNode = { oscillatorNode = it },
                                setOscillatorDetuneNode = { oscillatorDetuneNode = it },
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
