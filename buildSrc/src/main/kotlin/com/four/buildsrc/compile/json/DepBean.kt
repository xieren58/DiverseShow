package com.four.buildsrc.compile.json

data class DepBean(val impls: List<Data>,
                   val testImpls: List<Data>,
                   val androidTestImpls: List<Data>
                   ) {

    data class Data(val group: String,
                    val name: String,
                    val version: String,
                    val ext: String,
                    val projectPath: String? = null
    )
}