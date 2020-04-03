package com.microdevrj.wave_visualizer.rendering

import android.graphics.Canvas
import android.graphics.RectF

abstract class WaveRenderer {

    private var drawBounds: RectF = RectF()
        set(value) {
            field = value
            this.width = value.width()
            this.height = value.height()
            this.centerX = this.width / 2
            this.centerY = this.height / 2
        }

    internal var centerY: Float = -1f

    internal var centerX: Float = -1f

    internal var width: Float = 0f

    internal var height: Float = 0f

    internal var plotSnapshot: FloatArray? = null


    abstract var sampleSize: Int
    /*
    Smooth data value, managed by the visualizer view
     */
    var dataSnapshot: FloatArray? = null
        set(value) {
            field = value

            if (value == null)
                return

            plotSnapshot = FloatArray(value.size) {
                0f
            }
        }

    /*
    Minimum data value within the snapshot
     */
    var decline: Float = -1f

    /*
    Maximum data value within the snapshot
     */
    var peak: Float = -1f


    open fun onBoundsChanged(bounds: RectF) {
        this.drawBounds = bounds
    }

    abstract fun onUpdate(deltaTime: Double)

    abstract fun onRender(canvas: Canvas)


    abstract fun setForegroundColor(color: Int)

    abstract fun setBackgroundColor(color: Int)

    ////////////////////////////////////////
    ////////////////UTILITIES///////////////
    ////////////////////////////////////////

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