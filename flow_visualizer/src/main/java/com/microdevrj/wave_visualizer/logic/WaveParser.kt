package com.microdevrj.wave_visualizer.logic

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import java.io.ByteArrayInputStream
import kotlin.math.max


/**
 * This class is in charge of handling the wave form data.
 * It maps the data, to fit a displayable array.
 * It smooths the data, to be visualized
 */
class WaveParser {

    companion object {
        const val DEFAULT_SMOOTHING = 0.40f
        const val SOFT_SMOOTHING = 0.35f
        const val HARD_SMOOTHING = 0.55f
        const val PEAK = 128f
        const val DECLINE = 0f
    }


    //contains raw wave data
    var map: IntArray? = null

    //interpolated wave data
    var interpolation: FloatArray? = null

    private var factor: Int = -1

    //1,2,3,4,5,6,7,8,9,10,11,12
    //0,0,0,0,0
    //factor of 2.4 int 2
    //1.5, 3.5, 5.12, 7.2, 8 | lost -> 11.45
    //
    //| | | | |

    /**
     * @param rough the array to map
     * @param mapSize the size of the output array
     */
    fun map(rough: ByteArray, mapSize: Int) {
        require(mapSize < rough.size)
        if (map == null) {
            map = IntArray(mapSize)
            interpolation = FloatArray(mapSize)
            factor = rough.size / mapSize
        }
        require(factor > 0)
        require(map != null)

        for (i in map!!.indices)
            map!![i] = rough.averageValueInBytes(i * factor, factor)
    }

    fun interpolate(rough: IntArray) {
        for (i in interpolation!!.indices)
            interpolation!![i] = lerp(DEFAULT_SMOOTHING, interpolation!![i], max(0, map!![i]).toFloat())
    }

    private fun ByteArray.averageValueInBytes(offset: Int, limit: Int): Int {
        if (limit == 1)
            return this[offset + limit].toInt()
        var sum = 0
        for (i in offset until offset + limit)
            sum += this[i]
        return sum / limit
    }


    private fun lerp(norm: Float, min: Float, max: Float): Float {
        return (max - min) * norm + min
    }
}