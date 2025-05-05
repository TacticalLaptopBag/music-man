package com.github.tacticallaptopbag.strmanip

import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.tacticallaptopbag.*

class ReplaceCommand : BaseCommand("replace") {
    private val phrase by argument()
        .help(HELP_REPL_PHRASE)
    private val newPhrase by argument()
        .help(HELP_REPL_NEW_PHRASE)
        .default("")
    private val regexFlag by option("-r", "--regex")
        .help(HELP_REPL_REGEX)
        .flag()

    override fun help(context: Context): String = HELP_REPL

    private fun getNewName(fileName: String): String {
        if(regexFlag) {
            val regex = Regex(phrase)
            return regex.replace(fileName, newPhrase)
        } else {
            return fileName.replace(phrase, newPhrase)
        }
    }

    override fun run() {
        val fileType = guessFileType()
        notifyDry()

        listFilesOfType(fileType).forEach { file ->
            val newName = getNewName(file.name)
            if(file.name == newName) return@forEach
            echo("${file.name} -> $newName")
            if(!dry) {
                rename(file, newName)
            }
        }
    }
}