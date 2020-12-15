val projectIndex: Set<String> = hashSetOf(
    ":app",
    ":components:base",
    ":components:common-net",
    ":components:common-util",
    ":components:common-map",

    ":features:ds-home",
    ":features:ds-weather",
    ":components:api:api-weather",

    ":android-skill:hotfix",
    ":android-skill:plugins"
)
gradle.settingsEvaluated {
    val TAG = "dynamic build"
    println("$TAG =========> ${extra.properties["dynamic_build"]}")
    val dynamic = extra.properties["dynamic_build"]
    if (dynamic != null && dynamic == "true") {
        println("$TAG =========> start")
        val p1 = Runtime.getRuntime().exec("git diff --name-only")
        val in1 = p1.inputStream
        val reader1 = java.io.BufferedReader(java.io.InputStreamReader(in1))
        val alterModuleSet = HashSet<String>()
        alterModuleSet.add(":app")
        reader1.readLines().forEach { path ->
            projectIndex.forEach { projectName ->
                if (path.contains(projectName)) {
                    alterModuleSet.add(projectName)
                    return@forEach
                }
            }
        }
        alterModuleSet.forEach {
            println(it)
            include(it)
        }
        in1.close()
        p1.destroy()
        println("$TAG =========> end")
    } else {
        projectIndex.forEach {
            println(it)
            include(it)
        }
    }
}