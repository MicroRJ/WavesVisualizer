package com.microdevrj.waves_visualizer.rendering

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.microdevrj.waves_visualizer.default_renderer.BarRenderer
import com.microdevrj.waves_visualizer.logic.WaveDisplay
import com.microdevrj.waves_visualizer.logic.WaveParser

/**
 * Updating the renderer when the size changes
 */
class WaveView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr),
    WaveDisplay {

    /*
    Future -> Optimize
    Using same parser for different renderers
     */
    //calculates sample size etc
    override var parser: WaveParser = WaveParser()

    //actually renders the bars
    override var renderer: WaveRenderer<*> =
        BarRenderer(BarCustomize())

    private var width: Float = 0f

    private var height: Float = 0f

    private var measured: Boolean = false

    /**
     * Gets called when the view is first measured or
     * its size has changed
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.width = w.toFloat()
        this.height = h.toFloat()
        this.measured = true
        /**
         * Update the renderer's render bounds
         */
        renderer.updateRenderBounds(RenderBounds(0f, 0f, width, height))
    }

    /**
     * Post invalidate upon requesting from,
     * Gets called by the wave engine
     */
    override fun onRequestFrame() {
        //allows for background thread rendering
        postInvalidate()
    }

    /**
     * Gets called after post invalidate
     */
    override fun onDraw(canvas: Canvas?) {
        renderer.render(canvas ?: return)
    }
}