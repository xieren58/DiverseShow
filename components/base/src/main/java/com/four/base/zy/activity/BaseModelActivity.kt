package com.four.base.zy.activity

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import java.lang.reflect.ParameterizedType

abstract class BaseModelActivity<VM : ViewModel> : BaseActivity() {

    var viewModel: VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
        viewModel = createViewModel()
        initData()
    }

    protected open fun createViewModel() = createViewModelByType()

    protected abstract fun bindView()

    protected abstract fun initData()

    /**
     * 通过class直接拿到泛型
     */
    private fun createViewModelByType() : VM? {
        val type = this::class.java.genericSuperclass as ParameterizedType
        val vmClazz = type.actualTypeArguments[0]
        return if (vmClazz == ViewModel::class.java) {
            null
        } else {
            ViewModelProviders.of(this).get(vmClazz as Class<VM>)
        }
    }
}