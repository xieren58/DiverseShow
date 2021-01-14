package com.ds.hotfix

import android.content.Context
import android.util.Log
@HotFix
class FixTest {

    @FixModifier
    fun test() {

    }

    fun launchFixTest(context: Context) {
        test()
    }
}