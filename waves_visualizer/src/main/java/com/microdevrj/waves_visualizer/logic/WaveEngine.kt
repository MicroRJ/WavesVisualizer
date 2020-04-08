package com.microdevrj.waves_visualizer.logic

import com.microdevrj.waves_visualizer.rendering.Surfer

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

    private var snapshot: ByteArray? = null

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
        //capture the data
        if (snapshot == null)
            snapshot = b

        for (i in b.indices)
            snapshot!![i] = b[i]
    }

    override fun onTick(delta: Double) {
        if (snapshot == null)
            return

        for (i in surfers.indices) {
            with(surfers[i]) {


//                this.parser.b = raw

                //reduced mem usage
                if (this.parser.b == null)
                    this.parser.b = snapshot!!

                for (ri in snapshot!!.indices)
                    this.parser.b!![i] = snapshot!![i]


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