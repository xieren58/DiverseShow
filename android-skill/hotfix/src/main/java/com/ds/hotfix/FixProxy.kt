package com.ds.hotfix

class FixProxy: ChangeQuickRedirect {
    override fun accessDispatch(methodSignature: String, paramArrayOfObject: Array<Any>): Any? {
        if (methodSignature.equals("test")) {
            return 100
        }
        return null
    }

    override fun isSupport(methodSignature: String, paramArrayOfObject: Array<Any>): Boolean {
        if (methodSignature.equals("test")) {
            return true
        }
        return false
    }
}