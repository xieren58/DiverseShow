import com.four.buildsrc.Dep
import com.four.buildsrc.Env
import com.four.buildsrc.implRepo

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
        externalNativeBuild {
            cmake {
                cppFlags(" -frtti -fexceptions -std=c++14  ")
                cFlags("-DSTDC_HEADERS")
                arguments("-DANDROID_STL=c++_shared")//使用c++_shared.so
            }
        }

    }

    packagingOptions {
        pickFirst("lib/arm64-v8a/libopencv_java4.so")
    }

    sourceSets {
        getAt("main").jniLibs.srcDirs("src/main/jniLibs")
    }

    buildTypes {
        getAt("release").apply {
            minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    externalNativeBuild {
        cmake {

            // Provides a relative path to your CMake build script.
            path("src/main/CMakeLists.txt")
        }
    }
}

dependencies {

    implRepo(Dep.kotlinStdlib)
    implRepo(Dep.coreKTX)
    implRepo(Dep.appcompat)
    implRepo(Dep.googleMaterial)
    implRepo(Dep.constraintLayout)
}