package com.microdevrj.wave_visualizer.rendering

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.microdevrj.deb


class BarWaveRenderer(
    private val barWidth: Float,
    private val barHeight: Float,
    private val spacing: Float
) :
    WaveRenderer() {

    private val halfW: Float = barWidth / 2

    private var offset = 0f

    private var boundHalf = ((halfW + spacing) / 2)

    override var sampleSize: Int = -1

    override fun onBoundsChanged(bounds: RectF) {
        super.onBoundsChanged(bounds)

        this.offset = (spacing + barWidth) / 2
        this.sampleSize = (width / (barWidth + spacing)).toInt()
    }

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    override fun setForegroundColor(color: Int) {
        paint.color = color
    }

    override fun setBackgroundColor(color: Int) {

    }


    override fun onUpdate(deltaTime: Double) {
        if (dataSnapshot == null)
            return
        dataSnapshot?.size?.deb()

        for (i in dataSnapshot!!.indices)
            super.plotSnapshot!![i] = dataSnapshot!![i].mapTo(decline, peak, 0f, barHeight / 2)

    }

    override fun onRender(
        canvas: Canvas
    ) {
        if (dataSnapshot == null)
            return

        "bar renderer rendering".deb()

        for (i in dataSnapshot!!.indices) {
            val left = i * (barWidth + spacing) + offset
            val mapped = super.plotSnapshot!![i]
            canvas.drawRoundRect(
                left,
                centerY - mapped,
                left + barWidth,
                centerY + mapped,
                halfW,
                halfW,
                paint
            )
        }
    }


}