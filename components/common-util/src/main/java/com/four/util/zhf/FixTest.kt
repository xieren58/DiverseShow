package com.four.util.zhf

class FixTest {
    companion object {
        @JvmStatic var changeQuickRedirect:ChangeQuickRedirect? = null
    }

    fun test():Int {
        changeQuickRedirect?.let {
            //changeQuickRedirect不为空时 由补丁包代理
            if (it.isSupport("test", arrayOf())) {
                return it.accessDispatch("test", arrayOf()) as Int
            }
        }
        return 99
    }
}