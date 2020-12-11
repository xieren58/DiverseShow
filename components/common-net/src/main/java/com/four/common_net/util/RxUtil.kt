package com.four.common_net.util

import androidx.lifecycle.Lifecycle
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class RxUtil {

    /**
     * rx线程调度，并自动解绑
     */
    fun <T, R> ioToUiAndAutoDispose(lifecycle: Lifecycle,
                      event: Lifecycle.Event = Lifecycle.Event.ON_STOP)
    : ObservableTransformer<T?, R?> {
        return ObservableTransformer {

            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            val provider = AndroidLifecycleScopeProvider.from(lifecycle, event)
            val convert = AutoDispose.autoDisposable<T>(provider)
            it.`as`(convert)
            it as ObservableSource<R?>
        }
    }

    /**
     * rx线程调度
     */
    fun <T, R> ioToUi() : ObservableTransformer<T?, R?> {
        return ObservableTransformer {

            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            it as ObservableSource<R?>
        }
    }
}