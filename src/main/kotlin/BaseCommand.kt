package com.github.tacticallaptopbag

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension

abstract class BaseCommand(name: String) : CliktCommand(name = name) {
    val dry by option("-d", "--dry")
        .flag(default = false)
        .help(HELP_DRY)
    val fileType by option("-f", "--filetype")
        .help(HELP_FILETYPE)
    val baseDir by option(
        envvar = MUSIC_MAN_DIR,
        hidden = true,
    )
        .default(".")

    private fun listFiles(path: String = baseDir): List<File> {
        val cwd = File(path)
        return cwd.list()?.toList()?.filterNotNull()?.sorted()?.map {
            cwd.resolve(it)
        } ?: emptyList()
    }

    protected fun listFilesOfType(fileType: String, path: String = baseDir): List<File> {
        return listFiles(path).filter { it.extension == fileType.removePrefix(".") }
    }

    protected fun guessFileType(): String {
        return if(fileType.isNullOrBlank()) {
            val types = mutableMapOf<String, Int>()
            listFiles().forEach { file ->
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
                throw CliktError(FILETYPE_FAIL)
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

    protected fun rename(file: File, newName: String) {
        val parentPath = file.toPath().parent
        val newFile = File(parentPath.resolve(newName).toString())
        file.renameTo(newFile)
    }
}