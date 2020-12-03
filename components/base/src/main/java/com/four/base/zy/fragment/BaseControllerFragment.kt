package com.four.base.zy.fragment

import android.os.Bundle
import android.view.View
import com.four.base.zy.BaseController

abstract class BaseControllerFragment<T : BaseController<*>> : BaseFragment() {

    protected var controller: T? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = createController()
    }

    protected abstract fun createController() : T
}