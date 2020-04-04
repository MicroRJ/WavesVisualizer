package com.microdevrj.wave_visualizer.factory

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.microdevrj.wave_visualizer.rendering.Properties
import com.microdevrj.wave_visualizer.rendering.RenderBounds
import com.microdevrj.wave_visualizer.rendering.WaveRenderer


class BarRenderer(val barProp: BarProperties) : WaveRenderer(barProp) {

    private val halfW: Float = barProp.width / 2

    private var centerY: Float = 0f

    private var offset: Float = 0f

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = barProp.foregroundColor
        textSize = 24f
    }

    init {
        barProp.updateListener = Runnable {
            //invalidate snapshot size
            snashotSize = onUpdateRenderBounds(renderBounds!!)
        }
    }

    override fun onUpdateRenderBounds(rb: RenderBounds): Int {
        centerY = rb.height / 2
        val b = barProp.width + barProp.spacing
        val s = (rb.width / b).toInt()
        offset = (rb.width - s * b) / 2 + (b - barProp.width) / 2
        return s
    }


    override fun onUpdate(delta: Double) {
        for (i in snapshot!!.indices)
            super.snapshot!![i] = snapshot!![i].mapTo(decline, peak, 0f, barProp.maxHeight / 2)
    }

    override fun onRender(
        canvas: Canvas
    ) {
        for (i in snapshot!!.indices) {
            val left = i * (barProp.width + barProp.spacing) + offset
            val mapped = super.snapshot!![i]
            canvas.drawRoundRect(
                left,
                centerY - mapped,
                left + barProp.width,
                centerY + mapped,
                halfW,
                halfW,
                paint
            )
        }

        canvas.drawText("$snashotSize", 100f, 100f, paint)
    }


    class BarProperties(var width: Float, var maxHeight: Float, var spacing: Float) : Properties() {
        var foregroundColor: Int = Color.WHITE
    }


}