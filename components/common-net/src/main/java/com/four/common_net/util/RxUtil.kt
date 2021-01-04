package com.four.common_net.util

import androidx.lifecycle.Lifecycle
import com.four.common_util.log.DSLog
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers


object RxUtil {

    /**
     * 捕捉没有被catch的异常
     * 除了不可捕捉的三个Error
     */
    init {
        RxJavaPlugins.setErrorHandler {
            DSLog.def().error("RxJava发生未捕捉异常${it.message.toString()}")
            it.printStackTrace()
        }
    }
}

val logErrorConsumer = Consumer<Throwable?> {
    it?.also {
        DSLog.def().info(it.message ?: it.toString())
    }
}

fun <T> Observable<T>.ioToUi(): Observable<T> =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


fun <T> Observable<T>.ioToUiAndAutoDispose(lifecycle: Lifecycle,
                                           success: Consumer<in T>,
                                           error: Consumer<Throwable?> = logErrorConsumer
){
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDispose(AndroidLifecycleScopeProvider.from(lifecycle, Lifecycle.Event.ON_STOP))
        .subscribe(success, error)
}

fun <T> Observable<T>.ioToUiAndAutoDispose(lifecycle: Lifecycle,
                                           event: Lifecycle.Event,
                                           success: Consumer<in T>,
                                           error: Consumer<Throwable?> = logErrorConsumer
){
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDispose(AndroidLifecycleScopeProvider.from(lifecycle, event))
        .subscribe(success, error)
}