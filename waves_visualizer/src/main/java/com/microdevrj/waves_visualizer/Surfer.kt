package com.microdevrj.waves_visualizer

import com.microdevrj.waves_visualizer.rendering.WaveRenderer
import com.microdevrj.waves_visualizer.logic.WaveParser

interface Surfer {

    var parser: WaveParser
    var renderer: WaveRenderer<*>

    fun requestFrame()
}