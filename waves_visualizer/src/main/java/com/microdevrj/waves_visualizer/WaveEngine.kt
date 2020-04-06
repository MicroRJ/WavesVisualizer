package com.microdevrj.waves_visualizer

import com.microdevrj.waves_visualizer.logic.ChronoEngine
import com.microdevrj.waves_visualizer.logic.TickListener

class WaveEngine(id: String) : WaveSource.WaveListener,
    TickListener {

    enum class State {
        NONE, INACTIVE, ACTIVE
    }

    private val gear = ChronoEngine.get().addGear(id, this)

    private var waveSource: WaveSource? = null

    private val surfers: ArrayList<Surfer> = ArrayList()

    private var state: State =
        State.NONE

    private var raw: ByteArray? = null

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
        this.gear.isIdle = newState == State.INACTIVE || newState == State.NONE

    }

    override fun onCapture(b: ByteArray) {
        if (raw == null)
            raw = b

        for (i in b.indices)
            raw!![i] = b[i]
    }

    override fun onTick(delta: Double) {
        if (raw == null)
            return

        for (i in surfers.indices) {
            with(surfers[i]) {


//                this.parser.b = raw

                //reduced mem usage
                if (this.parser.b == null)
                    this.parser.b = raw!!

                for (ri in raw!!.indices)
                    this.parser.b!![i] = raw!![i]


                this.parser.parse(this.renderer.snapshotSize)

                this.renderer.snapshot = this.parser.parsed
                this.renderer.decline = -128f
                this.renderer.peak = 128f

                this.renderer.update(delta)

                this.requestFrame()
            }
        }

    }


}