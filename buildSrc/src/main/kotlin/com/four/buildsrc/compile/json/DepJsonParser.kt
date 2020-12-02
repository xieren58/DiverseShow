package com.four.buildsrc.compile.json

import org.json.JSONArray
import org.json.JSONObject

object DepJsonParser {

    fun parse(json: String) : DepBean {
        val target = JSONObject(json)
        val impls = getImplList(target.getJSONArray(DepConstant.IMPLEMENTATION_NAME))
        val testImpls = getImplList(target.getJSONArray(DepConstant.TEST_IMPLEMENTATION_NAME))
        val androidTestImpls = getImplList(target.getJSONArray(DepConstant.ANDROID_TEST_IMPLEETATION_NAME))
        return DepBean(impls, testImpls, androidTestImpls)
    }

    private fun getImplList(array : JSONArray?) : List<DepBean.Data> {
        val impls = mutableListOf<DepBean.Data>()
        array?.apply {
            var index = 0
            while (index < array.length()) {
                val data = getJSONObject(index)
                val group = data.getString(DepConstant.DEP_GROUP)
                val name = data.getString(DepConstant.DEP_NAME)
                val version = data.getString(DepConstant.DEP_VERSION)
                val ext = data.getString(DepConstant.DEP_EXT)
                val projectPath = data.getString(DepConstant.DEP_PROJECT_PATH)
                impls.add(DepBean.Data(group, name, version, ext, projectPath))
                index++
            }
        }
        return impls
    }
}