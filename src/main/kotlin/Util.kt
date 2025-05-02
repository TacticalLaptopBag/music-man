package com.github.tacticallaptopbag

import java.io.File
import java.nio.file.Files

object Util {
    fun guessFileType(fileType: String?): String {
        return if(fileType.isNullOrBlank()) {
            val cwd = File(".")
            val files = cwd.list()?.toList() ?: emptyList()
            val types = mutableMapOf<String, Int>()
            files.filterNotNull().forEach {
                val file = File(it)
                if(file.extension.isBlank()) return@forEach
                val contentType = Files.probeContentType(file.toPath()) ?: ""
                if(!contentType.startsWith("audio/")) return@forEach
                if(!types.contains(file.extension)) {
                    types[file.extension] = 0
                }
                types[file.extension] = types[file.extension]!! + 1
            }

            var largestType = Pair("", 0)
            types.forEach { (type, count) ->
                if(count > largestType.second) {
                    largestType = Pair(type, count)
                }
            }

            largestType.first
        } else {
            fileType
        }
    }
}