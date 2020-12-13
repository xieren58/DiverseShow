package com.ds.hotfix

import android.content.Context
import android.util.Log

class FixTest {
    companion object {
        @JvmStatic var changeQuickRedirect: ChangeQuickRedirect? = null
        const val TAG = "FixTest"
    }

    fun test():Int {
        changeQuickRedirect = FixProxy()
        changeQuickRedirect?.let {
            //changeQuickRedirect不为空时 由补丁包代理
            if (it.isSupport("test", arrayOf())) {
                return it.accessDispatch("test", arrayOf()) as Int
            }
        }
        return 99
    }

    fun launchFixTest(context: Context) {
        Log.e(TAG,"launchFixTest invoke")
    }
}