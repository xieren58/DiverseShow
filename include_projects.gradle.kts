/**
 * 此处申明所有的模块
 */
val allProjects = arrayOf(
    ":components:base",
    ":components:common-net",
    ":components:common-util",
    ":components:common-map",

    ":features:ds-home",
    ":features:ds-weather",
    ":components:api:api-weather",

    ":android-skill:hotfix",
    ":android-skill:plugins",
    ":global:ds-app",
    ":openCV"
)

/**
 * app和java module默认都会依赖上
 */
val defProjects = arrayOf(
    ":app"//,
//   因为这三个module是按照maven库的形式依赖，库和module同时会生产xxx.kotlin_module文件造成build失败
//   所以把它们注释掉，需要看源码可以打开，然后sync now
//    ":global:lib-init:app-init-handler",
//    ":global:lib-init:app-init-processor",
//    ":global:lib-init:app-init-transform"
)

gradle.settingsEvaluated {
    val openAarRunName = "run.openAarRun"
    val openAarRun = getProperty(openAarRunName, settings)?.toString()
    if (openAarRun != null && openAarRun == "false") {
        handleIncludeProjects(settings, "all")
        return@settingsEvaluated
    }

    val includeProjectsName = "run.projects"
    val includeStr = getProperty(includeProjectsName, settings)?.toString()

    if (includeStr.isNullOrEmpty()) {
        handleIncludeProjects(settings, "all")
    } else {
        handleIncludeProjects(settings, includeStr.replace(" ", ""))
    }

    println("finish include projects.")
}

fun getProperty(property: String, settings: Settings) : Any? {
    val properties = java.util.Properties()
    val inputStream = file("${settings.rootDir}/local.properties").inputStream()
    try {
        properties.load(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }
    val value = properties[property]
    return value ?: settings.extra.properties[property]
}

fun handleIncludeProjects(settings: Settings, str: String) {
    if ("all" == str) {
        allProjects.forEach {
            settings.include(it)
        }
    } else {
        str.split(",").forEach { target->
            allProjects.forEach { project ->
                if (project.substring(project.lastIndexOf(':') + 1) == target) {
                    settings.include(project)
                }
            }
        }
    }
    includeDefProjects(settings)
}

fun includeDefProjects(settings: Settings) {
    defProjects.forEach { target ->
        settings.include(target)
    }
}





