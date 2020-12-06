package com.four.util.zhf;

import android.util.Log;

public class Fix {
    public static ChangeQuickRedirect changeQuickRedirect;
    public int test() {
        if (changeQuickRedirect != null && changeQuickRedirect.isSupport("test",new Object[0])) {
            return (int)changeQuickRedirect.accessDispatch("test",new Object[0]);
        }
        return 99;
    }

    void te() {
        System.out.println("hello world");
        Log.d("log","hello world!");
    }
}
