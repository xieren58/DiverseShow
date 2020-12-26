package com.four.common_util.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.MainThread
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.four.common_util.log.DSLog
import com.four.common_util.schedule.postUIThread
import com.four.common_util.schedule.runUIThread
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

/**
 * 权限请求Helper
 */
object PermissionHelper {

    private var code = AtomicInteger(0)

    private val callbackList = mutableListOf<CallbackWithCode>()

    private val atomicCallbackList = AtomicReference(callbackList)

    /**
     * 请求权限，采用此方法就够了
     *
     * @param forceRequest 不管用户有没有拒绝，都要请求权限
     * @param callback 真正向用户请求权限时的回掉
     * @param showToastListener 检查是否是用户已经拒绝多次的权限，是的话则产生回掉
     */
    fun simpleRequestPermission(activity: FragmentActivity,
                                permissions: Array<String>,
                                callback: Callback,
                                forceRequest: Boolean = true,
                                showToastListener: OnShouldShowToastListener? = null) {
        val deniedPermissions = getDeniedPermissions(activity, permissions)
        if (deniedPermissions.isNotEmpty()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && !forceRequest) {
                for (permission in permissions) {
                    if (!activity.shouldShowRequestPermissionRationale(permission)) {
                        requestPermissions(activity, permissions, callback)
                        return
                    }
                    showToastListener?.onShowToast()
                }
            } else {
                requestPermissions(activity, permissions, callback)
            }
        } else {
            runUIThread {
                callback.onGranted()
            }
        }
    }

    fun getDeniedPermissions(context: Context, permissions: Array<String>): Array<String> {
        val targetList = mutableListOf<String>()
        permissions.forEach {
            val result = ActivityCompat.checkSelfPermission(context, it)
            if (result == PackageManager.PERMISSION_DENIED) {
                targetList.add(it)
            }
        }
        return targetList.toTypedArray()
    }

    fun requestPermissions(activity: FragmentActivity,
                           permissions: Array<String>,
                           callback: Callback) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val callbackCode = CallbackWithCode(callback, code.incrementAndGet())
            putCallback(callbackCode)
            ActivityCompat.requestPermissions(activity, permissions, callbackCode.code)
            activity.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    callbackCode.callback = null
                }
            })
        } else {
            postUIThread {
                if (activity.lifecycle.currentState != Lifecycle.State.DESTROYED) {
                    callback.onGranted()
                }
            }
        }
    }

    @MainThread
    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<out String>,
                                   grantResults: IntArray) {
        DSLog.default().info("on request permissions result.")
        var index = -1
        val length = callbackList.size
        while (++index < length) {
            val target = callbackList[index]
            if (target.code == requestCode) {
                if (!target.isUseful) {
                    return
                } else {
                    target.isUseful = false
                }
                val deniedPermissions = mutableListOf<String>()
                permissions.forEachIndexed { index, per ->
                    if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                        deniedPermissions.add(per)
                    }
                }
                if (deniedPermissions.isEmpty()) {
                    target.callback?.onGranted()
                } else {
                    target.callback?.onDenied(deniedPermissions)
                }
            }
        }
    }

    private fun putCallback(callback: CallbackWithCode) {
        while (!tryPutCallback(callback)){ }
    }

    private fun tryPutCallback(callback: CallbackWithCode) : Boolean {
        val subjectList = atomicCallbackList.getAndSet(null)
        return if (subjectList == null) {
            false
        } else {
            subjectList.add(callback)
            atomicCallbackList.set(subjectList)
            true
        }
    }

    interface Callback {

        /**
         * 当申请的全部权限通过时
         */
        @MainThread
        fun onGranted()

        /**
         * 当某些权限被拒绝时
         */
        @MainThread
        fun onDenied(deniedPermissions: List<String>)
    }

    interface OnShouldShowToastListener {

        /**
         * 当前第二次请求被拒绝的权限时，应该回掉此方法
         */
        fun onShowToast()
    }

    private class CallbackWithCode(var callback: Callback?, val code: Int, var isUseful: Boolean = false)
}