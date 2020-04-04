package com.microdevrj.wave_visualizer.rendering

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.microdevrj.wave_visualizer.Surfer
import com.microdevrj.wave_visualizer.factory.BarRenderer
import com.microdevrj.wave_visualizer.logic.WaveParser


class WaveView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr), Surfer {

    override var parser: WaveParser = WaveParser()

    override var renderer: WaveRenderer =
        BarRenderer(BarRenderer.BarProperties(20f, 80f, 20f))

    private var width: Float = 0f

    private var height: Float = 0f

    private var measured: Boolean = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.width = w.toFloat()
        this.height = h.toFloat()
        this.measured = true
        renderer.updateRenderBounds(RenderBounds(0f, 0f, width, height))
    }

    override fun requestFrame() {
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        renderer.render(canvas ?: return)
    }
}