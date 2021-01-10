package com.four.app_init_handler.api

/**
 * @param appLife 指定在app的哪个生命周期回掉此方法
 * @param priority 在当前的生命周期，这个方法执行的优先级，数字越大优先级越高
 *
 * example:
 * ---- object A {
 * -------- @OnAppLifeChanged(AppLifeEvent.ON_CREATE)
 * -------- fun aVoid(){ }
 * ---- }
 * // A.aVoid()就会在App onCreate的时候调用
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class OnAppLifeChanged(@AppLifeEventInt val appLife: Int, val priority: Int = 0)