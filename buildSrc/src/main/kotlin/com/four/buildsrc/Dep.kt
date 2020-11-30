package com.four.buildsrc

object Dep {

    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Env.KOTLIN_VERSION}"
    const val coreKTX = "androidx.core:core-ktx:1.3.2"

    const val appcompat = "androidx.appcompat:appcompat:1.2.0"
    const val constraintLayout = "androidx.appcompat:appcompat:1.2.0"

    const val googleMaterial = "com.google.android.material:material:1.2.1"

    const val junit = "junit:junit:4.+"
    const val junitExt = "androidx.test.ext:junit:1.1.2"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.3.0"
}