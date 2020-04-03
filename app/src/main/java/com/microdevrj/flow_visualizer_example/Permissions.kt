package com.microdevrj.flow_visualizer_example

import android.Manifest

class Permissions {
    companion object {
        const val PERMISSIONS_REQUEST_KEY: Int = 777
        /*
        PERMISSIONS TO REQUEST
         */
        val PERMISSIONS: Array<String> = arrayOf(
            Manifest.permission.RECORD_AUDIO
        )
    }
}