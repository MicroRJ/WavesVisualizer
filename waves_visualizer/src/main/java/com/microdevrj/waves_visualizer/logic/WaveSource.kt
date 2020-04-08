package com.microdevrj.waves_visualizer.logic

import android.media.audiofx.Visualizer

/**
 * Abstraction layer over the visualizer
 */
class WaveSource(audioSessionId: Int = -1, private val waveListener: WaveListener) : Visualizer.OnDataCaptureListener {

    private var visualizer: Visualizer? = null

    var active: Boolean = false
        set(value) {
            if (visualizer?.enabled == value)
                return
            visualizer?.enabled = value
            field = value
        }


    init {
        visualizer = Visualizer(audioSessionId).apply {
            setDataCaptureListener(this@WaveSource, getCaptureRate(), true, false)
            captureSize = Visualizer.getCaptureSizeRange()[1]
            enabled = this@WaveSource.active
        }
    }


    fun release() {
        this.visualizer?.apply {
            if (this.enabled) this.enabled = false
            release()
        }
        this.visualizer = null
    }

    private fun getCaptureRate() = Visualizer.getMaxCaptureRate() / 2


    override fun onWaveFormDataCapture(v: Visualizer?, b: ByteArray?, sr: Int) {
        if (b == null)
            return
        waveListener.onCapture(b)
    }

    override fun onFftDataCapture(v: Visualizer?, f: ByteArray?, sr: Int) {

    }

    interface WaveListener{
        fun onCapture(b: ByteArray)
    }

}