package com.taeyeon.xoduslol.util

import kotlinx.browser.document
import org.w3c.dom.HTMLAudioElement
import kotlin.js.Promise


val OscillatorTypeList = listOf("sine", "square", "sawtooth", "triangle")


external interface AudioNode {
    fun connect(destination: AudioNode)
}

external interface AudioDestinationNode : AudioNode

external class GainNode: AudioNode {
    val gain: AudioParam
    override fun connect(destination: AudioNode)
}

external class OscillatorNode: AudioNode {
    var type: String
    val frequency: AudioParam
    val detune: AudioParam
    override fun connect(destination: AudioNode)
    fun start(whenSec: Double = definedExternally)
    fun stop(whenSec: Double = definedExternally)
}

external class AudioParam {
    fun setValueAtTime(value: Double, startTime: Double)
    fun linearRampToValueAtTime(value: Double, endTime: Double)
}

external class AudioContext {
    val state: String?
    val currentTime: Double
    val destination: AudioDestinationNode
    fun resume(): Promise<JsAny?>
    fun createOscillator(): OscillatorNode
    fun createGain(): GainNode
}


@JsFun("() => window.AudioContext ? new window.AudioContext() : new window.webkitAudioContext()")
external fun createAudioContext(): AudioContext

fun unblockIosPlayback() {
    // iOS/iPadOS에서 WebAudio는 벨소리로 재생되기 때문에 무음 스위치가 켜진 경우에는 작동하지 않음
    // 이를 해결하기 위해 빈 MP3 파일을 재생하고 WebAudio를 재생.
    val emptyMp3FileSrc = "data:audio/mp3;base64,SUQzBAAAAAAAIlRTU0UAAAAOAAADTGF2ZjYxLjcuMTAwAAAAAAAAAAAAAAD/+0DAAAAAAAAAAAAAAAAAAAAAAABJbmZvAAAADwAAABUAAAk+AB4eHh4qKioqKjU1NTU1QEBAQEBLS0tLV1dXV1diYmJiYm1tbW1teHh4eISEhISEj4+Pj4+ampqamqWlpaWxsbGxsby8vLy8x8fHx8fS0tLS3t7e3t7p6enp6fT09PT0/////wAAAABMYXZjNjEuMTkAAAAAAAAAAAAAAAAkBh4AAAAAAAAJPjptcRAAAAAAAP/7EMQAA8AAAaQAAAAgAAA0gAAABExBTUUzLjEwMFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVMQU1FMy4xMDBVVVVV//sQxCmDwAABpAAAACAAADSAAAAEVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVUxBTUUzLjEwMFVVVVX/+xDEUwPAAAGkAAAAIAAANIAAAARVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVTEFNRTMuMTAwVVVVVf/7EMR8g8AAAaQAAAAgAAA0gAAABFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVMQU1FMy4xMDBVVVVV//sQxKYDwAABpAAAACAAADSAAAAEVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVUxBTUUzLjEwMFVVVVX/+xDEz4PAAAGkAAAAIAAANIAAAARVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVTEFNRTMuMTAwVVVVVf/7EMTWA8AAAaQAAAAgAAA0gAAABFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVMQU1FMy4xMDBVVVVV//sQxNYDwAABpAAAACAAADSAAAAEVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVUxBTUUzLjEwMFVVVVX/+xDE1gPAAAGkAAAAIAAANIAAAARVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVTEFNRTMuMTAwVVVVVf/7EMTWA8AAAaQAAAAgAAA0gAAABFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVMQU1FMy4xMDBVVVVV//sQxNYDwAABpAAAACAAADSAAAAEVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVUxBTUUzLjEwMFVVVVX/+xDE1gPAAAGkAAAAIAAANIAAAARVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVTEFNRTMuMTAwVVVVVf/7EMTWA8AAAaQAAAAgAAA0gAAABFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVMQU1FMy4xMDBVVVVV//sQxNYDwAABpAAAACAAADSAAAAEVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVUxBTUUzLjEwMFVVVVX/+xDE1gPAAAGkAAAAIAAANIAAAARVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVTEFNRTMuMTAwVVVVVf/7EMTWA8AAAaQAAAAgAAA0gAAABFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV//sQxNYDwAABpAAAACAAADSAAAAEVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVX/+xDE1gPAAAGkAAAAIAAANIAAAARVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVf/7EMTWA8AAAaQAAAAgAAA0gAAABFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV//sQxNYDwAABpAAAACAAADSAAAAEVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVX/+xDE1gPAAAGkAAAAIAAANIAAAARVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVQ=="
    val audio = document.createElement("audio") as HTMLAudioElement
    audio.setAttribute("x-webkit-airplay", "deny")
    audio.preload = "auto"
    audio.loop = true
    audio.src = emptyMp3FileSrc
    audio.play()
}

fun AudioContext.playTone(
    frequency: Double,
    gain: Double,
    detune: Double,
    waveForm: String,
    setGainNode: (GainNode?) -> Unit,
    setOscillatorNode: (OscillatorNode?) -> Unit,
    setOscillatorDetuneNode: (OscillatorNode?) -> Unit,
) {
    resume()

    val gainNode = createGain().also {
        it.gain.setValueAtTime(0.0, currentTime)
        it.connect(destination)
    }
    val oscillatorNode = createOscillator().also {
        it.type = waveForm
        it.frequency.setValueAtTime(frequency, currentTime)
        it.connect(gainNode)
    }
    val oscillatorDetuneNode = createOscillator().also {
        it.type = waveForm
        it.frequency.setValueAtTime(frequency, currentTime)
        it.detune.setValueAtTime(detune, currentTime)
        it.connect(gainNode)
    }

    gainNode.gain.linearRampToValueAtTime(gain, currentTime + 0.02)
    oscillatorNode.start()
    oscillatorDetuneNode.start()

    setGainNode(gainNode)
    setOscillatorNode(oscillatorNode)
    setOscillatorDetuneNode(oscillatorDetuneNode)
}

fun AudioContext.stopTone(
    gainNode: GainNode,
    oscillatorNode: OscillatorNode,
    oscillatorDetuneNode: OscillatorNode,
    setGainNode: (GainNode?) -> Unit,
    setOscillatorNode: (OscillatorNode?) -> Unit,
    setOscillatorDetuneNode: (OscillatorNode?) -> Unit,
) {
    gainNode.gain.linearRampToValueAtTime(0.0, currentTime + 0.02)
    oscillatorNode.stop(currentTime + 0.03)
    oscillatorDetuneNode.stop(currentTime + 0.03)

    setGainNode(null)
    setOscillatorNode(null)
    setOscillatorDetuneNode(null)
}

fun AudioContext.updateFrequency(
    newFrequency: Double,
    oscillatorNode: OscillatorNode,
    oscillatorDetuneNode: OscillatorNode,
) {
    val clamped = newFrequency.coerceIn(20.0, 20000.0)
    oscillatorNode.frequency.linearRampToValueAtTime(clamped, currentTime + 0.02)
    oscillatorDetuneNode.frequency.linearRampToValueAtTime(clamped, currentTime + 0.02)
}

fun AudioContext.updateGain(
    newGain: Double,
    gainNode: GainNode,
) {
    val clamped = newGain.coerceIn(0.0, 1.0)
    gainNode.gain.linearRampToValueAtTime(clamped, currentTime + 0.02)
}

fun AudioContext.updateDetune(
    newDetune: Double,
    oscillatorDetuneNode: OscillatorNode,
) {
    oscillatorDetuneNode.detune.linearRampToValueAtTime(newDetune, currentTime + 0.02)
}

fun updateWaveform(
    newWaveForm: String,
    oscillatorNode: OscillatorNode,
    oscillatorDetuneNode: OscillatorNode,
) {
    oscillatorNode.type = newWaveForm
    oscillatorDetuneNode.type = newWaveForm
}
