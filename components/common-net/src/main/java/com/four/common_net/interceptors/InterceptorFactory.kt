package com.four.common_net.interceptors

/**
 * 1. app拦截器，处于okHttp网络请求之前，适合做网络以外的拦截，没有okhttp添加头部，允许多次process();
 * 2. net拦截器，处于okhttp网络请求完成之后，Connection对象不为空；
 */
object InterceptorFactory {

    /**
     * 建议放在net拦截器
     */
    fun createHttpLogInterceptor() = HttpLoggingInterceptor()

    /**
     * 域名替换拦截器
     */
    fun createHostInterceptor() = HostInterceptor()
}