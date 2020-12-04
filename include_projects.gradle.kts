/**
 * 此处申明所有的模块
 */
val allProjects = arrayOf(
    ":app",

    ":components:base",
    ":components:common-net",
    ":components:common-util",

    ":features:ds-home",
    ":features:ds-weather",

    ":components:api:api-weather"
)

gradle.settingsEvaluated {
    //这里我只能想到手解属性，似乎在settings.gradle下找不到properties
    val includeProjectsName = "includeProjects"
    val outProjectsName = "outProjects"
    file("$rootDir/gradle.properties")
        .readLines()
        .forEach find@ {
            var findChar = false
            it.forEach findChar@ { char ->
                if (char != ' ') {
                    if (char == '#') {
                        findChar = true
                    }
                    return@findChar
                }
            }
            if (!findChar) {
                if (it.contains(includeProjectsName)) {
                    val targetStr = it
                        .replace(" ", "")
                        .replace("$includeProjectsName=", "")
                    handleIncludeProjects(settings, targetStr)
                    return@find
                }
                if (it.contains(outProjectsName)) {
                    val targetStr = it
                        .replace(" ", "")
                        .replace("$outProjectsName=", "")
                    handleOutProjects(settings, targetStr)
                    return@find
                }
            }
    }

    println("finish include projects.")
}

fun handleIncludeProjects(settings: Settings, str: String) {
    if ("off" == str || str.isEmpty()) {
        return
    } else if ("all" == str) {
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

fun handleOutProjects(settings: Settings, str: String) {
    if (str.isEmpty() || "all" == str) {
        return
    } else if ("off" == str) {
        allProjects.forEach {
            settings.include(it)
        }
    } else {
        val list = allProjects.toMutableList()
        str.split(",").forEach { target->
            allProjects.forEach { project ->
                if (project.endsWith(target)) {
                    list.remove(project)
                }
            }
        }
        list.forEach {
            settings.include(it)
        }
    }
}





