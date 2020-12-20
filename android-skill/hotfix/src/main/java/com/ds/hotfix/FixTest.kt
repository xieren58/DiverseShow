package com.ds.hotfix

import android.content.Context
import android.util.Log

class FixTest {

    @FixModifier
    fun test() {

    }

    fun launchFixTest(context: Context) {
        test()
    }
}