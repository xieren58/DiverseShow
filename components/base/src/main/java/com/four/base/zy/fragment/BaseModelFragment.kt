package com.four.base.zy.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import java.lang.reflect.ParameterizedType

abstract class BaseModelFragment<VM : ViewModel> : BaseFragment() {

    var viewModel: VM? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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