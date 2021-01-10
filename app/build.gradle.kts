import com.four.buildsrc.*

plugins {
    id("com.android.application")
    id ("kotlin-android")
    kotlin("kapt")
}

plugins.apply(com.four.buildsrc.hotfix.HotfixPlugin::class.java)
plugins.apply(com.four.app_init_transform.AppInitPlugin::class.java)

kapt {
    useBuildCache = true
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

    signingConfigs {
        getAt("debug").apply {
            storeFile(file("${rootProject.rootDir}/config/debugApk.jks"))
            storePassword("123456")
            keyAlias("key0")
            keyPassword("123456")
        }
    }

    buildTypes {
        getAt("debug").apply {
            signingConfig = signingConfigs.getAt("debug")
        }

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

    implProject(Dep.featureHomeProject)
    implProject(Dep.commonBaseProject)
}

