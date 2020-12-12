package com.four.buildsrc

import org.gradle.kotlin.dsl.DependencyHandlerScope
import com.four.buildsrc.compile.intercept.DepInterceptor
import org.gradle.kotlin.dsl.accessors.runtime.addExternalModuleDependencyTo

/**
 * 所有依赖的库
 */
object Dep {

    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Env.KOTLIN_VERSION}"
    const val coreKTX = "androidx.core:core-ktx:1.3.2"

    const val appcompat = "androidx.appcompat:appcompat:1.2.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val googleMaterial = "com.google.android.material:material:1.2.1"

    const val junit = "junit:junit:4.+"
    const val junitExt = "androidx.test.ext:junit:1.1.2"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"

    const val lifecycleLiveDataKTX = "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0"
    const val lifecycleViewModelKTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
    const val lifecycleExtensions  =  "android.arch.lifecycle:extensions:2.2.0"

    const val eventLine = "com.github.zhaozuyuan:eventline:1.0.3-release"

    const val rxAndroid2 = "io.reactivex.rxjava2:rxandroid:2.1.1"
    const val rxjava2 = "io.reactivex.rxjava2:rxjava:2.1.1"
    const val autoDisposeAndroid = "com.uber.autodispose:autodispose-android-archcomponents:1.4.0"

    const val retrofit2 = "com.squareup.retrofit2:retrofit:2.9.0"
    const val retrofitRxJava2 = "com.squareup.retrofit2:adapter-rxjava2:2.9.0"
    const val retrofitGson = "com.squareup.retrofit2:converter-gson:2.1.0"

    const val gson = "com.google.code.gson:gson:2.8.1"
    const val okHttp3 = "com.squareup.okhttp3:okhttp:4.9.0"

    const val commonBaseProject = ":components:base"
    const val commonNetProject = ":components:common-net"
    const val commonUtilProject = ":components:common-util"

    const val featureHomeProject = ":features:ds-home"
    const val featureWeatherProject = ":features:ds-weather"
    const val apiWeatherProject = ":components:api:api-weather"

    const val skillHotfixProject = ":android-skill:hotfix"
}

fun DependencyHandlerScope.implRepo(variant: String) {
    if (!DepInterceptor.interceptImplRepo(this, variant)) {
        dependencies.add("implementation", variant)
    }
}

fun DependencyHandlerScope.implProject(path: String) {
    if (!DepInterceptor.interceptImplProject(this, path)) {
        dependencies.add("implementation", project(mapOf("path" to path)))
    }
}

fun DependencyHandlerScope.implAar(name: String, group: String = "", version: String? = null) {
    if (!DepInterceptor.interceptImplAar(this, name, group, version)) {
        addExternalModuleDependencyTo(this.dependencies,
            "implementation", group, name, version,
            null, null, "aar", null)
    }
}

fun DependencyHandlerScope.testImpl(variant: String) {
    if (!DepInterceptor.interceptTestImpl(this, variant)) {
        dependencies.add("testImplementation", variant)
    }
}

fun DependencyHandlerScope.androidTestImpl(variant: String) {
    if (!DepInterceptor.interceptAndroidTestImpl(this, variant)) {
        dependencies.add("androidTestImplementation", variant)
    }
}

fun DependencyHandlerScope.apiRepo(variant: String) {
    if (!DepInterceptor.interceptImplRepo(this, variant)) {
        dependencies.add("api", variant)
    }
}

fun DependencyHandlerScope.apiProject(path: String) {
    if (!DepInterceptor.interceptImplProject(this, path)) {
        dependencies.add("api", project(mapOf("path" to path)))
    }
}
