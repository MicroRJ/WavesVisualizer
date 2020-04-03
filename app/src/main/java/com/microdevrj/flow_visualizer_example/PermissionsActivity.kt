package com.microdevrj.flow_visualizer_example

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

abstract class PermissionsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val granted = checkPermissions(Permissions.PERMISSIONS)

        if (!granted) {
            requestMissingPermissions(Permissions.PERMISSIONS)
            return
        } else {
            onPermissionsGranted()
        }
    }


    private fun requestMissingPermissions(permissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, Permissions.PERMISSIONS_REQUEST_KEY)
        }
    }

    private fun checkPermissions(permissions: Array<String>): Boolean {
        var granted = true
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                granted = false
            }
        }
        return granted
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /*
        On permissions related data passed in
         */
        if (requestCode == Permissions.PERMISSIONS_REQUEST_KEY) {
            /*
            Check all permissions again
             */
            val granted = checkPermissions(Permissions.PERMISSIONS)

            if (!granted)
                finishAfterTransition()
            else
                onPermissionsGranted()
        }
    }

    open fun onPermissionsGranted() {

    }

}