import com.four.buildsrc.*
import com.four.buildsrc.util.Logger.log

plugins {
    id("com.android.application")
    id ("kotlin-android")
}

android  {
    compileSdkVersion(Env.COMPILE_VERSION)
    buildToolsVersion("30.0.2")

    defaultConfig {
        applicationId(Env.APPLICATION_ID)
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

//afterEvaluate {
//        project.configurations.asMap["implementation"]?.allDependencies?.forEach {
//        log(it.name)
//    }
//}