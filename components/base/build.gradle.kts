import com.four.buildsrc.androidTestImpl
import com.four.buildsrc.impl
import com.four.buildsrc.testImpl
import com.four.buildsrc.Env
import com.four.buildsrc.Dep

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android  {
    compileSdkVersion(Env.COMPILE_VERSION)
    buildToolsVersion("30.0.2")

    defaultConfig {
        minSdkVersion(Env.MIN_SDK_VERSION)
        targetSdkVersion(Env.TARGET_SDK_VERSION)
        versionCode(Env.VERSION_CODE)
        versionName(Env.VERSION)

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        getAt("release").apply {
            minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {

    impl(Dep.kotlinStdlib)
    impl(Dep.coreKTX)
    impl(Dep.appcompat)
    impl(Dep.googleMaterial)
    impl(Dep.constraintLayout)
    testImpl(Dep.junit)
    androidTestImpl(Dep.junitExt)
    androidTestImpl(Dep.espressoCore)
}