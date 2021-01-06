package com.example.opencv

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * created by demoless on 2021/1/6
 * description:
 */
object OpenCVLauncher {

    init {
        System.loadLibrary("openCV")
    }
    @JvmStatic
    private external fun openCVTest() : String
    private const val TAG = "OpenCVLauncher"
    @JvmStatic fun launchOpenCV(context: Context) : String {
        val intent = Intent(context, OpenCVActivity::class.java)
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
        return openCVTest()
    }
}