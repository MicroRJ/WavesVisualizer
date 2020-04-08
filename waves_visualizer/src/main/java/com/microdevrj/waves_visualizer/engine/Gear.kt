package com.microdevrj.waves_visualizer.engine

/**
 * Should not have id duplicates
 * unless update is desired
 */
class Gear(
    val id: Int,
    private val tickListener: TickListener,
    private val idleListener: IdleListener
) {


    @Volatile
    var isIdle = true
        set(value) {
            field = value
            idleListener.onGearStateChanged()
        }

    fun turn(delta: Double) {
        if (isIdle)
            return

        tickListener.onTick(delta)
    }

}