plugins {
    `kotlin-dsl`
}

//定义资源目录
val javaSourcePath = "src/main/kotlin"
sourceSets.main.get().java.srcDir(javaSourcePath)

repositories {
    //gradle tools
    google()
    mavenCentral()
    //kotlin-dsl
    jcenter()

}

dependencies {
    //gradle工具
    implementation("com.android.tools.build:gradle:4.1.1")
}