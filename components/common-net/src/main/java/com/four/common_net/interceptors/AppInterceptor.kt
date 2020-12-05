package com.four.common_net.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/**
 * app 拦截器，处于使用者-okHttp之间
 * 可进行原始数据查看，重定向
 */
class AppInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        //事件交给下一个拦截器
        return chain.proceed(chain.request())
    }
}