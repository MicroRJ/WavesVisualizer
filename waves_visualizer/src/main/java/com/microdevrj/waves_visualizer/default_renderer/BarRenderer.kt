package com.microdevrj.waves_visualizer.default_renderer

import android.graphics.Canvas
import android.graphics.Paint
import com.microdevrj.waves_visualizer.logic.WaveParser
import com.microdevrj.waves_visualizer.rendering.BarCustomize
import com.microdevrj.waves_visualizer.rendering.RenderBounds
import com.microdevrj.waves_visualizer.rendering.WaveRenderer


class BarRenderer(customize: BarCustomize) : WaveRenderer<BarCustomize>(customize) {

    private var bWidth = customize.width
    private var bHeight = customize.height
    private var bSpacing = customize.spacing
    private var bRadius = customize.radius
    private var bAlign = customize.align

    private val halfW: Float = customize.width / 2

    private var centerY: Float = 0f

    private var offset: Float = 0f

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = customize.style
        color = customize.color
    }

    override fun onCustomizeUpdate() {
        with(customize) {
            bWidth = this.width
            bHeight = this.height
            bSpacing = this.spacing
            bRadius = this.radius
            bAlign = this.align
        }
        paint.apply {
            style = customize.style
            color = customize.color
            strokeWidth = customize.strokeWidth
        }
        if (renderBounds != null)
            snapshotSize = onCalculateSnapshotSize(renderBounds!!)
    }

    override fun onCalculateSnapshotSize(rb: RenderBounds): Int {
        centerY = rb.height / 2
        val totalWidth = bWidth + bSpacing
        val fit = (rb.width / totalWidth).toInt()
        offset = (rb.width - fit * totalWidth) / 2 + (totalWidth - bWidth) / 2
        return fit
    }


    override fun onUpdate(delta: Double) {
        for (i in snapshot!!.indices)
            super.snapshot!![i] =
                snapshot!![i].mapTo(WaveParser.DECLINE, WaveParser.PEAK, 0f, bHeight / 2)
    }

    override fun onRender(
        canvas: Canvas
    ) {
        for (i in snapshot!!.indices) {
            val left = i * (bWidth + bSpacing) + offset
            val mapped = super.snapshot!![i]
            canvas.drawRoundRect(
                left,
                (centerY - mapped),
                left + bWidth,
                (centerY + mapped),
                halfW,
                halfW,
                paint
            )
        }
    }


}