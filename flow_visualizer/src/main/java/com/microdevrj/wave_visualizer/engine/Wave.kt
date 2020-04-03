package com.microdevrj.wave_visualizer.engine

import com.microdevrj.deb
import com.microdevrj.wave_visualizer.logic.ChronoEngine
import com.microdevrj.wave_visualizer.logic.TickListener
import com.microdevrj.wave_visualizer.events.Surfer
import com.microdevrj.wave_visualizer.source.WaveSource

class Wave(id: String) : WaveSource.WaveListener,
    TickListener {

    enum class State {
        NONE, INACTIVE, ACTIVE
    }

    private val gear = ChronoEngine.get().addGear(id, this)

    private var waveSource: WaveSource? = null

    private val surfers: ArrayList<Surfer> = ArrayList()

    private var state: State = State.NONE


    fun setActive(active: Boolean) {
        waveSource?.active = active
        updateState(if (active) State.ACTIVE else State.INACTIVE)
    }


    fun with(asi: Int) {
        waveSource?.release()
        waveSource = WaveSource(asi, this)
        updateState(State.NONE)
    }

    fun add(surfer: Surfer) {
        surfers.add(surfer)
    }

    private fun updateState(newState: State) {
        this.state = newState

        //idle if not active
        this.gear.isIdle = newState != State.ACTIVE
    }

    override fun onCapture(b: ByteArray) {
        for (i in surfers.indices) {
            "new capture".deb()
            val p = surfers[i].parser

            val r = surfers[i].renderer

            //map the data
            p.map(b, r.sampleSize)
            //interpolate
            p.interpolate(p.map!!)
            //update renderer data snapshot
            r.dataSnapshot = p.interpolation

            r.decline = 0f

            r.peak = 128f

        }
    }

    override fun onTick(delta: Double) {
        for (i in surfers.indices) {
            surfers[i].renderer.onUpdate(delta)
            surfers[i].requestFrame()
        }
    }


}