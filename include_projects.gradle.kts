/**
 * 此处申明所有的模块
 */
val allProjects = arrayOf(
    ":app",

    ":components:base",
    ":components:common-net",
    ":components:common-util",

    ":features:ds-home"
)

gradle.settingsEvaluated {
    //这里我只能想到手解属性，似乎在settings.gradle下找不到properties
    var targetStr = "off"
    val targetPropertyName = "includeProjects"
    file("$rootDir/gradle.properties")
        .readLines()
        .forEach find@ {
        if (it.contains(targetPropertyName)) {
            targetStr = it
                .replace(" ", "")
                .replace("$targetPropertyName=", "")
            return@find
        }
    }
    if ("off" == targetStr || targetStr.isEmpty()) {
        return@settingsEvaluated
    } else if ("all" == targetStr) {
        allProjects.forEach {
            settings.include(it)
        }
    } else {
        targetStr.split(",").forEach { target->
            allProjects.forEach { project ->
                if (project.endsWith(target)) {
                    settings.include(project)
                }
            }
        }
    }

    println("finish include projects.")
}





