package com.github.tacticallaptopbag

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import java.io.File
import java.nio.file.Files
import kotlin.system.exitProcess

abstract class BaseCommand(name: String) : CliktCommand(name = name) {
    protected val dry by option("-d", "--dry")
        .flag(default = false)
        .help(HELP_DRY)
    private val fileType by option("-f", "--filetype")
        .help(HELP_FILETYPE)

    protected fun listFiles(path: String = "."): List<String> {
        val cwd = File(path)
        return cwd.list()?.toList()?.filterNotNull() ?: emptyList()
    }

    protected fun listFilesOfType(fileType: String, path: String = "."): List<String> {
        return listFiles(path).filter { it.endsWith(fileType) }
    }

    protected fun guessFileType(): String {
        return if(fileType.isNullOrBlank()) {
            val types = mutableMapOf<String, Int>()
            listFiles().forEach {
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


            if(largestType.first.isBlank()) {
                echo(FILETYPE_FAIL, err = true)
                exitProcess(1)
            }
            largestType.first
        } else {
            fileType!!
        }
    }

    protected fun notifyDry() {
        if(dry) {
            echo(DRY)
        }
    }
}