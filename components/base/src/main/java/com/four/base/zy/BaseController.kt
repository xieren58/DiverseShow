package com.four.base.zy

import android.content.Context
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.four.base.zy.activity.BaseActivity
import com.four.base.zy.fragment.BaseFragment
import com.four.common_util.log.DSLog
import java.lang.reflect.ParameterizedType

/**
 * 业务逻辑过重可以增加Controller
 *
 * 形成：
 * Activity - Controller - ViewModel
 *
 */
abstract class BaseController<VM : ViewModel> (protected val context: Context)
    : LifecycleObserver {

    protected var viewModel : VM? = null

    protected val viewFinder: IViewFinder

    protected val lifecycleOwner: LifecycleOwner

    //Controller可以拆分，为多个View生成多套逻辑
    protected val controllers = mutableListOf<BaseController<*>>()

    init {
        if (context !is BaseActivity && context !is BaseFragment) {
            throw RuntimeException("the context type is error.")
        }
        viewFinder = context as IViewFinder
        lifecycleOwner = context as LifecycleOwner
        registerObserver()
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
        val vm = createViewModel()
        if (vm != null) {
            onViewModelInit(vm)
        }
        viewModel = vm
        bindView(context as IViewFinder)
        initData()
        DSLog.d("${this::class.qualifiedName} onCreate()")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        DSLog.d("${this::class.qualifiedName} onDestroy()")
    }

    protected open fun createViewModel() : VM? = createViewModelByType()

    protected open fun onViewModelInit(model: VM) { }

    protected open fun initData() { }

    protected abstract fun bindView(finder: IViewFinder)

    private fun registerObserver() {
        lifecycleOwner.lifecycle.addObserver(this)
        controllers.forEach {
            it.registerObserver()
        }
    }

    /**
     * 通过class直接拿到泛型
     */
    private fun createViewModelByType() : VM? {
        val type = this::class.java.genericSuperclass as ParameterizedType
        val vmClazz = type.actualTypeArguments[0]
        return if (vmClazz == ViewModel::class.java) {
            null
        } else {
            when(context) {
                is FragmentActivity ->
                    ViewModelProviders.of(context).get(vmClazz as Class<VM>)
                is Fragment -> ViewModelProviders.of(context).get(vmClazz as Class<VM>)
                else -> null
            }
        }
    }
}