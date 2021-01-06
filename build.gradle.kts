
plugins.apply(com.four.buildsrc.compile.AssembleDebugForAarPlugin::class.java)

buildscript {
    val kotlinVersion by extra(com.four.buildsrc.Env.KOTLIN_VERSION)
    val kotlin_version by extra("1.3.72")
    repositories {
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/jcenter")
        maven(url = "https://maven.aliyun.com/nexus/content/groups/public")
        maven(url = "https://maven.aliyun.com/nexus/content/repositories/gradle-plugin")
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    repositories {
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/jcenter")
        maven(url = "https://maven.aliyun.com/nexus/content/groups/public")
        maven(url = "https://maven.aliyun.com/nexus/content/repositories/gradle-plugin")
        maven(url = "https://oss.jfrog.org/libs-snapshot")
        maven(url = "https://jitpack.io")
        google()
        jcenter()
        flatDir {
            dirs(
                "${project.rootDir}/aarrun/aars/"
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

    this.afterEvaluate {
        extensions.findByType(com.android.build.gradle.BaseExtension::class.java)?.apply {
            compileOptions {
                sourceCompatibility(JavaVersion.VERSION_1_8)
                targetCompatibility(JavaVersion.VERSION_1_8)
            }
        }
    }
}

tasks.create("clean") {
    delete(rootProject.buildDir)
}