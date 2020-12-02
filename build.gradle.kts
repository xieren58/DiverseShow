// Top-level build file where you can add configuration options common to all sub-projects/modules.

com.four.buildsrc.util.Logger.init(project)

plugins.apply(com.four.buildsrc.compile.AssembleDebugForAarPlugin::class.java)

buildscript {
    val kotlinVersion by extra(com.four.buildsrc.Env.KOTLIN_VERSION)
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kkr kts.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        flatDir {
            dirs(
                "${project.rootDir}/compile/aars/"
            )
        }
    }

    //配置java、kotlin
    tasks.withType(JavaCompile::class.java) {
        sourceCompatibility = com.four.buildsrc.Env.JAVA_VERSION
        targetCompatibility = com.four.buildsrc.Env.JAVA_VERSION
    }

    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class) {
        kotlinOptions.jvmTarget = com.four.buildsrc.Env.KOTLIN_JVM_TARGET
    }
}

tasks.create("clean") {
    delete(rootProject.buildDir)
}