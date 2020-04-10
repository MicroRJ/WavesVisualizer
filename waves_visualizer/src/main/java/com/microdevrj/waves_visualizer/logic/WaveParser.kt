package com.microdevrj.waves_visualizer.logic

import android.util.Log
import com.microdevrj.deb
import java.lang.Exception
import java.lang.NullPointerException
import kotlin.math.sqrt


/**
 * This class is in charge of handling the wave form data.
 * It maps the data, to fit a displayable array.
 * It smooths the data, to be visualized
 */
class WaveParser {


    companion object {

        private val TAG = WaveParser::class.java.name

        const val T_DEFAULT = 0.45f
        const val T_SOFT = 0.25f
        const val T_ROUGH = 0.55f

        //max value in the float parsed array
        const val PEAK = 127f

        //min value in the float parsed array
        const val DECLINE = -128f

    }

    var parsed: FloatArray? = null

    var toParse: ByteArray? = null

    var chunkSize: Int = -1

    /**
     * Runs with a few logic checks
     */
    fun parseDebugMode(parseSize: Int) {
        if (toParse == null)
            throw NullPointerException("to parse arr is null")

        if (parseSize >= toParse!!.size) {
            Log.w(TAG, "parse size >= limit, see WaveParser.kt @ method parse")
            return
        }

        if (parseSize <= 0) {
            Log.w(TAG, "parse size <= 0,     see WaveParser.kt method parse")
            return
        }

        parse(parseSize)

    }

    fun parse(parseSize: Int) {
        adapt(parseSize)

        for (i in parsed!!.indices) {
            var sum = 0
            /*
            Find average values in chunks
             */
            val off = i * chunkSize
            for (b in off until off + chunkSize)
                sum += toParse!![b]

            //interpolate between previous value and new value
            try {

                //linear interpolation
                val p1 = sum / chunkSize
                val p0 = parsed!![i]
                parsed!![i] = p0 + (p1 - p0) * sqrt(T_SOFT)
//                parsed!![i] = (p0 + (p1 - p0) * DEFAULT_SMOOTHING
            } catch (d: Exception) {
                d.deb()
            }
        }
    }

    private fun adapt(parseSize: Int) {
        if (parsed == null || parsed!!.size != parseSize) {
            parsed = FloatArray(parseSize)
            chunkSize = toParse!!.size / parseSize
        }
    }
}