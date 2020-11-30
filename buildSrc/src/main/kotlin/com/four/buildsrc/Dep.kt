package com.four.buildsrc

import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Dep {

    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Env.KOTLIN_VERSION}"
    const val coreKTX = "androidx.core:core-ktx:1.3.2"

    const val appcompat = "androidx.appcompat:appcompat:1.2.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"

    const val googleMaterial = "com.google.android.material:material:1.2.1"

    const val junit = "junit:junit:4.+"
    const val junitExt = "androidx.test.ext:junit:1.1.2"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
}

fun DependencyHandlerScope.impl(variant: String) {
    dependencies.add("implementation", variant)
}

fun DependencyHandlerScope.impl(project: Project) {
    dependencies.add("implementation", project)
}

fun DependencyHandlerScope.testImpl(variant: String) {
    dependencies.add("testImplementation", variant)
}

fun DependencyHandlerScope.androidTestImpl(variant: String) {
    dependencies.add("androidTestImplementation", variant)
}
