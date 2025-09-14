package com.taeyeon.xoduslol.util

import kotlin.js.Promise


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
    override fun connect(destination: AudioNode)
    fun start(whenSec: Double = definedExternally)
    fun stop(whenSec: Double = definedExternally)
}

external class AudioParam {
    fun setValueAtTime(value: Double, startTime: Double)
    fun linearRampToValueAtTime(value: Double, endTime: Double)
}

external class AudioContext {
    val currentTime: Double
    val destination: AudioDestinationNode
    fun resume(): Promise<JsAny?>
    fun createOscillator(): OscillatorNode
    fun createGain(): GainNode
}


@JsFun("() => window.AudioContext ? new window.AudioContext() : new window.webkitAudioContext()")
external fun createAudioContext(): AudioContext

fun AudioContext.playTone(
    frequency: Double,
    gain: Double,
    waveForm: String,
    setGainNode: (GainNode?) -> Unit,
    setOscillatorNode: (OscillatorNode?) -> Unit,
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

    gainNode.gain.linearRampToValueAtTime(gain, currentTime + 0.02)
    oscillatorNode.start()

    setGainNode(gainNode)
    setOscillatorNode(oscillatorNode)
}

fun AudioContext.stopTone(
    gainNode: GainNode,
    oscillatorNode: OscillatorNode,
    setGainNode: (GainNode?) -> Unit,
    setOscillatorNode: (OscillatorNode?) -> Unit,
) {
    gainNode.gain.linearRampToValueAtTime(0.0, currentTime + 0.02)
    oscillatorNode.stop(currentTime + 0.03)

    setGainNode(null)
    setOscillatorNode(null)
}

fun AudioContext.updateFrequency(
    newFrequency: Double,
    oscillatorNode: OscillatorNode,
) {
    val clamped = newFrequency.coerceIn(20.0, 20000.0)
    oscillatorNode.frequency.linearRampToValueAtTime(clamped, currentTime + 0.02)
}

fun AudioContext.updateGain(
    newGain: Double,
    gainNode: GainNode,
) {
    val clamped = newGain.coerceIn(0.0, 1.0)
    gainNode.gain.linearRampToValueAtTime(clamped, currentTime + 0.02)
}

fun AudioContext.updateWaveform(
    newWaveForm: String,
    oscillatorNode: OscillatorNode,
) {
    oscillatorNode.type = newWaveForm
}
