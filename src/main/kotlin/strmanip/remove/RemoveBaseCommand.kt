package com.github.tacticallaptopbag.strmanip.remove

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.tacticallaptopbag.*
import java.io.File
import kotlin.system.exitProcess

abstract class RemoveBaseCommand(name: String): CliktCommand(name = name) {
    private val dry by option("-d", "--dry")
        .flag(default = false)
        .help(HELP_DRY)
    private val fileType by option("-f", "--filetype")
        .help(HELP_FILETYPE)

    abstract fun getNewName(fileName: String): String?

    override fun run() {
        val fileType = Util.guessFileType(fileType)
        if(fileType.isBlank()) {
            echo(FILETYPE_FAIL, err = true)
            exitProcess(1)
        }

        if(dry) {
            echo(DRY)
        }

        val files = File(".").list()?.toList() ?: emptyList()
        files.forEach { fileName ->
            if(!fileName.endsWith(fileType)) return@forEach

            val newName = getNewName(fileName)
            if(newName.isNullOrBlank()) return@forEach

            echo("$fileName -> $newName")
            if(!dry) {
                val file = File(fileName)
                file.renameTo(File(newName))
            }
        }
    }
}