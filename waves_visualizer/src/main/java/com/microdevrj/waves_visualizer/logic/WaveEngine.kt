package com.microdevrj.waves_visualizer.logic

import com.microdevrj.deb
import com.microdevrj.waves_visualizer.engine.ChronoEngine
import com.microdevrj.waves_visualizer.engine.TickListener

class WaveEngine(chronoId: Int, maxSurfers: Int) : WaveSource.WaveListener,
    TickListener {

    enum class State {
        NONE, INACTIVE, ACTIVE
    }

    private var waveSource: WaveSource? = null


    //we only have one gear for all the wave displays
    private val gear = ChronoEngine.get(1).addGear(chronoId, this)

    //same concept as the chrono engine
    private val waveDisplays: Array<WaveDisplay?> = arrayOfNulls(maxSurfers)

    //
    private var state: State =
        State.NONE

    //raw data capture
    private var capture: ByteArray? = null

    fun active(active: Boolean) {
        waveSource?.active = active
        updateState(if (active) State.ACTIVE else State.INACTIVE)
    }


    fun with(asi: Int) {
        waveSource?.release()
        waveSource = WaveSource(asi, this)
        updateState(State.NONE)
    }

    fun add(id: Int, waveDisplay: WaveDisplay) {
        waveDisplays[id] = waveDisplay
    }

    private fun updateState(newState: State) {
        this.state = newState

        //idle if not active
        this.gear.isIdle = newState == State.INACTIVE || newState == State.NONE


    }

    override fun onCapture(b: ByteArray) {
        //capture the data
        if (capture == null || b.size != capture!!.size)
            capture = b

        for (i in b.indices)
            capture!![i] = b[i]
    }

    override fun onTick(delta: Double) {
        if (capture == null)
            return

        for (i in waveDisplays.indices) {
            waveDisplays[i] ?: continue

            with(waveDisplays[i]!!) {

                /*
                Manage parser
                 */
                //reduced mem usage
                if (parser.toParse == null || capture!!.size != parser.toParse!!.size)
                    parser.toParse = capture!!

                for (ri in capture!!.indices)
                    parser.toParse!![i] = capture!![i]

                parser.parseDebugMode(renderer.snapshotSize)

                /*
                Manage snapshot
                 */
                renderer.snapshot = parser.parsed

                renderer.update(delta)

                onRequestFrame()
            }
        }

    }


}