package com.example.opencv

import android.util.Log

/**
 * created by demoless on 2021/1/6
 * description:
 */
class OpenCVLauncher {

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
        @JvmStatic
        private external fun openCVTest() : String
        private const val TAG = "OpenCVLauncher"
        @JvmStatic fun launchOpenCV() : String {
            return openCVTest()
        }
    }
}