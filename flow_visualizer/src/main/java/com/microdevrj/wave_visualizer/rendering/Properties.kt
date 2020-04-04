package com.microdevrj.wave_visualizer.rendering

abstract class Properties {
    var updateListener: Runnable? = null
    fun update() { updateListener?.run() }
}