/**
 * 此处申明所有的模块
 */
val allProjects = arrayOf(
    ":app",
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
    ":global:lib-init:app-init-handler",
    ":global:lib-init:app-init-processor",
    ":global:lib-init:app-init-transform"
)

gradle.settingsEvaluated {
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
                if (project.endsWith(target)) {
                    settings.include(project)
                }
            }
        }
    }
}





