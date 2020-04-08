package com.microdevrj.waves_visualizer.engine

import com.microdevrj.deb

/**
 * There's really no need to add synchronized to everything
 *
 * todo warn empty gear space when updating
 */
class ChronoEngine private constructor(private val maxGears: Int) : Runnable,
    IdleListener {

    companion object {

        private var instance: ChronoEngine? = null

        @Synchronized
        fun get(maxGears: Int): ChronoEngine {
            if (instance == null)
                instance =
                    ChronoEngine(maxGears)
            return instance!!
        }
    }

    @Volatile
    private var ticking: Boolean = false

    @Volatile
    private var idle: Boolean = false

    @Volatile
    private var nanosIdle: Long = 0

    @Volatile
    private var commandStop: Boolean = false

    @Volatile
    private var fps: Long = 1000 / 30

    private var engine: Thread? = null

    @Volatile
    private var prevTime: Long = 0L

    @Volatile
    private var delta: Double = 0.0


    private val gears: Array<Gear?> = arrayOfNulls(maxGears)


    @Synchronized
    fun start() {
        commandStop = false

        if (ticking)
            return

        if (engine == null) {
            engine = Thread(this)
            engine!!.start()
        }
    }


    @Synchronized
    private fun update() {
        val time = System.nanoTime()
        for (i in gears.indices) {
            gears[i] ?: continue
            if (gears[i]!!.isIdle)
                continue
            gears[i]!!.turn(delta)
        }

        delta = (time - prevTime) / 1_000_000_000.0

        prevTime = System.nanoTime()

        //before 27 > fps now 28 >
//        val fps = 1 / delta
    }


    @Synchronized
    fun addGear(id: Int, tickListener: TickListener): Gear {
        val g = Gear(id, tickListener, this)
        gears[id] = g
        return g
    }


    override fun run() {
        nanosIdle = 0
        ticking = true
        while (!commandStop) {

            if (idle) {
                nanosIdle = System.nanoTime() - nanosIdle
                //todo turn off when inactive for too long
            } else {
                nanosIdle = 0
                update()
            }
            try {
                Thread.sleep(fps)
            } catch (t: InterruptedException) {
                t.printStackTrace()
            }
        }
        ticking = false
    }

    /*
    When all gears are idle stop the engine after
    a given time frame
    //todo
     */
    override fun onGearStateChanged() {
        for (g in gears) {
            g ?: continue
            if (!g.isIdle) {
                start()
                break
            }
        }
    }


    /**
     * MUST NOT CALL FROM ENGINE THREAD
     *
     * Will create recursive behavior
     */
    @Synchronized
    fun off() {
        //signal stop
        commandStop = true
        //wait for it to stop
        while (ticking)
            Thread.sleep(fps)
        engine = null
    }

}