package com.microdevrj.waves_visualizer.logic

import com.microdevrj.waves_visualizer.rendering.WaveRenderer
import com.microdevrj.waves_visualizer.logic.WaveParser

interface WaveDisplay {

    /**
     * Used directly by the WaveEngine
     */
    var parser: WaveParser
    /**
     * Used directly by the WaveEngine
     */
    var renderer: WaveRenderer<*>
    /**
     * Used directly by the WaveEngine
     */
    fun onRequestFrame()
}