package com.microdevrj.wave_visualizer.events

import com.microdevrj.wave_visualizer.rendering.WaveRenderer
import com.microdevrj.wave_visualizer.logic.WaveParser

interface Surfer {

    var parser: WaveParser
    var renderer: WaveRenderer

    fun requestFrame()
}