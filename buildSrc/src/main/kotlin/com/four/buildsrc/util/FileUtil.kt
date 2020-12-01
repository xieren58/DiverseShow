package com.four.buildsrc.util

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

object FileUtil {

    fun copyFileByOverlay(targetPath: String, copyPath: String) {
        generateFileSafely(targetPath)
        val copyFile = File(copyPath)
        if (!copyFile.exists()) {
            throw FileNotFoundException()
        }

        try {
            copyFile.copyTo(File(targetPath), true)
        } catch (e: IOException) {
            throw e
        }
    }

    fun generateFileSafely(path: String) {
        val file = File(path)
        if (file.isFile) {
            if (!file.exists()) {
                generateFileSafely(file.parentFile.absolutePath)
                file.createNewFile()
            }
        }
        if (file.isDirectory) {
            if (!file.exists()) {
                file.mkdirs()
            }
        }
    }

    fun writeStringByOverlay(path: String, string: String) {
        generateFileSafely(path)
        val file = File(path)
        try {
            file.writeText(string)
        } catch (e: IOException) {
            throw e
        }
    }
}