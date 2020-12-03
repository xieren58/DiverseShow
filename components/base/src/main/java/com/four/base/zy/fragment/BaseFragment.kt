package com.four.base.zy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.four.base.zy.IViewFinder

abstract class BaseFragment : Fragment(), IViewFinder {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(inflater, container, savedInstanceState)
    }

    open fun createView(inflater: LayoutInflater,
                        container: ViewGroup?,
                        savedInstanceState: Bundle?) : View {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun <V : View> findView(id: Int) = this.view?.findViewById<V>(id)

    @LayoutRes
    protected abstract fun getLayoutId() : Int
}