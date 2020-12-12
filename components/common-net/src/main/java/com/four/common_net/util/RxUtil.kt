package com.four.common_net.util

import androidx.lifecycle.Lifecycle
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


object RxUtil {

    /**
     * rx线程调度，并自动解绑
     */
    fun <T> ioToUiAndAutoDispose(lifecycle: Lifecycle,
                      event: Lifecycle.Event = Lifecycle.Event.ON_STOP)
    : ObservableTransformer<T?, T?> {
        return ObservableTransformer<T?, T?> { it ->
            val provider = AndroidLifecycleScopeProvider.from(lifecycle, event)
            val convert = AutoDispose.autoDisposable<T?>(provider)
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(convert)
            it
        }
    }

    /**
     * rx线程调度
     */
    fun <T> ioToUi() : ObservableTransformer<T?, T?> {
        return ObservableTransformer {
            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }
}