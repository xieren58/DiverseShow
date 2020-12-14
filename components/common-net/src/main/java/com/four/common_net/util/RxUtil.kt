package com.four.common_net.util

import androidx.lifecycle.Lifecycle
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


object RxUtil {

    /**
     * rx线程调度
     */
    fun <T> ioToUiTransformer() : ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> dataToUISafely(observable: Observable<T>,
                           lifecycle: Lifecycle,
                           observer: Observer<T>,
                           event: Lifecycle.Event = Lifecycle.Event.ON_STOP) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(AndroidLifecycleScopeProvider.from(lifecycle, event))
            .subscribe(observer)
    }

    fun <T> dataToUISafely(observable: Observable<T>,
                           lifecycle: Lifecycle,
                           success: (data: T) -> Unit,
                           failed: (e: Throwable) -> Unit = {},
                           event: Lifecycle.Event = Lifecycle.Event.ON_STOP) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(AndroidLifecycleScopeProvider.from(lifecycle, event))
            .subscribe(success, failed)
    }
}