import com.four.buildsrc.*

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
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

    implRepo(Dep.kotlinStdlib)
    implRepo(Dep.coreKTX)
    implRepo(Dep.appcompat)
    implRepo(Dep.googleMaterial)
    implRepo(Dep.constraintLayout)
    androidTestImpl(Dep.junitExt)
    androidTestImpl(Dep.espressoCore)

    implJar(files("${project.projectDir}/libs/AMap2DMap_6.0.0_AMapSearch_7.7.0_AMapLocation_5.2.0_20201027.jar"))
}