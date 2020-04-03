package com.microdevrj.wave_visualizer.rendering

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF


class BarRenderer(
    var barWidth: Float,
    var barHeight: Float,
    var barSpacing: Float
) :
    WaveRenderer() {

    private val halfW: Float = barWidth / 2

    override var sampleSize: Int = -1

    override fun onBoundsChanged(bounds: RectF) {
        super.onBoundsChanged(bounds)
        update()
    }

    //todo center the bars 
    fun update(){
        this.sampleSize = (width / (barWidth + barSpacing)).toInt()
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
        for (i in dataSnapshot!!.indices)
            super.plotSnapshot!![i] = dataSnapshot!![i].mapTo(decline, peak, 0f, barHeight / 2)

    }

    override fun onRender(
        canvas: Canvas
    ) {
        if (dataSnapshot == null)
            return
        for (i in dataSnapshot!!.indices) {
            val left = i * (barWidth + barSpacing)
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