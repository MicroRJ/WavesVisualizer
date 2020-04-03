package com.microdevrj.wave_visualizer.logic

import com.microdevrj.deb
import com.microdevrj.wave_visualizer.RegistryList

class ChronoEngine private constructor() : Runnable,
    IdleListener {

    companion object {
        private var instance: ChronoEngine? = null

        @Synchronized
        fun get(): ChronoEngine {
            if (instance == null)
                instance =
                    ChronoEngine()
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
    private var ticksPerSecond: Long = 1000 / 30


    private var engine: Thread? = null

    private val tickers: RegistryList<String, Gear> =
        RegistryList()


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


    /**
     * @param id used to prevent ticker duplicates, if id is the same as another id it will be overridden
     * @param tickListener when the ticker is ticked
     */
    fun addGear(id: String, tickListener: TickListener) =
        tickers.registerUpdateReturnSecondParam(id,
            Gear(id, tickListener, this)
        )


    /**
     * Whenever a ticker's idle state changes
     */
    override fun onIdle() {
        var allIdle = true
        /*
        If at least one ticker is not idle turn on
         */
        tickers.iterate {
            if (!it.isIdle) {
                start()
                allIdle = false
                return@iterate
            }
        }

        idle = allIdle
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
                tick()
            }


            try {
                Thread.sleep(ticksPerSecond)
            } catch (t: InterruptedException) {
                t.printStackTrace()
            }
        }
        ticking = false
    }

    /**
     * Ticks all the tickers
     */


    private var prevTime: Long = 0L

    private var delta: Double = 0.0

    private fun tick() {
        val time = System.nanoTime()


        tickers.iterate {
            if (commandStop)
                return@iterate
            it.tick(delta)

        }

        delta = (time - prevTime) / 1_000_000_000.0

        val fps = 1 / delta

        fps.deb()

        prevTime = System.nanoTime()
    }


    /**
     * MUST NOT CALL FROM ENGINE THREAD
     */
    @Synchronized
    fun off() {
        //signal stop
        commandStop = true
        //wait for it to stop
        while (ticking)
            Thread.sleep(ticksPerSecond)

        engine = null
    }

}

/**
 * ////////////////////////////////////////////////////
 * //////////////////////TICKER////////////////////////
 * ////////////////////////////////////////////////////
 */

class Gear(
    val id: String,
    private val tickListener: TickListener,
    private val idleListener: IdleListener
) {


    @Volatile
    var isIdle = true
        set(value) {
            field = value
            idleListener.onIdle()
        }

    fun tick(delta: Double) {
        if (isIdle)
            return

        tickListener.onTick(delta)
    }

}

/**
 * ////////////////////////////////////////////////////
 * ////////////////////INTERFACES//////////////////////
 * ////////////////////////////////////////////////////
 */
interface TickListener {
    fun onTick(delta: Double)
}

interface IdleListener {
    fun onIdle()
}
