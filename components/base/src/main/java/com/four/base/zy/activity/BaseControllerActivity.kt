package com.four.base.zy.activity

import android.os.Bundle
import android.os.PersistableBundle
import com.four.base.zy.BaseController

/**
 * 业务逻辑过重可以使用这个Activity
 *
 * 形成：
 * Activity - Controller - ViewModel
 *
 * Activity和Controller之间的通信则使用EventLine，详情请见onActivityResult()
 */
abstract class BaseControllerActivity<T : BaseController<*>> : BaseActivity() {

    var controller: T? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        controller = createController()
    }

    protected abstract fun createController() : T
}