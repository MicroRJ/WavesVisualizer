package com.microdevrj.wave_visualizer.logic


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
    }

    var parsed: FloatArray? = null

    var factor: Int = -1

    @Synchronized
    fun parse(b: ByteArray, parseSize: Int) {
        if (parsed == null) {
            parsed = FloatArray(parseSize)
            factor = b.size / parseSize
        }
        //b.indices == parsed.indices, avoid having to null check
        for (i in parsed!!.indices) {
            val avg = b.averageValueInBytes(i * factor, factor)
            //interpolate between previous value and new value
            parsed!![i] = lerp(DEFAULT_SMOOTHING, parsed!![i], avg)
        }
    }

    private fun ByteArray.averageValueInBytes(offset: Int, limit: Int): Int {
        if (limit == 1)
            return this[offset + limit].toInt()
        var sum = 0
        for (i in offset until offset + limit)
            sum += this[i]
        return sum / limit
    }


    private fun lerp(norm: Float, min: Float, max: Int): Float {
        return (max - min) * norm + min
    }
}