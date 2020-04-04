package com.microdevrj.wave_visualizer.rendering

import android.graphics.Canvas

abstract class WaveRenderer(val properties: Properties) {

    var renderBounds: RenderBounds? = null
        private set

    var snashotSize: Int = -1
        internal set

    var snapshot: FloatArray? = null
        set(value) {
            if (value == null) {
                field = null
                return
            }
            if (field == null || field!!.size != value.size) {
                field = FloatArray(value.size) {
                    value[it]
                }
                return
            }
            for (i in value.indices)
                field!![i] = value[i]
        }

    var peak: Float = 0f

    var decline: Float = 0f

    abstract fun onUpdateRenderBounds(rb: RenderBounds): Int

    abstract fun onUpdate(delta: Double)

    abstract fun onRender(canvas: Canvas)

    fun updateRenderBounds(rb: RenderBounds) {
        renderBounds = rb
        snashotSize = onUpdateRenderBounds(rb)
    }

    fun update(delta: Double) {
        snapshot ?: return
        onUpdate(delta)
    }

    fun render(canvas: Canvas) {
        snapshot ?: return
        onRender(canvas)
    }

    internal fun Float.mapTo(
        inMin: Float,
        inMax: Float,
        outMin: Float,
        outMax: Float
    ) = (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin

    internal fun Int.mapTo(
        inMin: Int,
        inMax: Int,
        outMin: Int,
        outMax: Int
    ) = (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin

}