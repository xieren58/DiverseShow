import com.four.buildsrc.androidTestImpl
import com.four.buildsrc.implProject
import com.four.buildsrc.implRepo
import com.four.buildsrc.testImpl
import com.four.buildsrc.Env
import com.four.buildsrc.Dep

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
    testImpl(Dep.junit)
    androidTestImpl(Dep.junitExt)
    androidTestImpl(Dep.espressoCore)

    implProject(Dep.commonBaseProject)
    implProject(Dep.commonUtilProject)
    implProject(Dep.commonNetProject)
}