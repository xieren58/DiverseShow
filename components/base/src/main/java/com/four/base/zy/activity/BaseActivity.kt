package com.four.base.zy.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.four.base.zy.IViewFinder

abstract class BaseActivity : AppCompatActivity(), IViewFinder {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(createView())
    }

    open fun createView() : View {
        val inflater = LayoutInflater.from(this)
        return inflater.inflate(getLayoutId(),
            window.decorView as ViewGroup, false)
    }

    override fun <V : View> findView(id: Int): V? = findViewById<V>(id)

    @LayoutRes
    protected abstract fun getLayoutId() : Int
}