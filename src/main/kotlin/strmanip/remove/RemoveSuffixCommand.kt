package com.github.tacticallaptopbag.strmanip.remove

import com.github.tacticallaptopbag.HELP_RM_SUFFIX
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help

class RemoveSuffixCommand: RemoveBaseCommand(name = "suffix") {
    private val suffix by argument()
        .help(HELP_RM_SUFFIX)

    override fun getNewName(fileName: String): String? {
        if(!fileName.startsWith(suffix)) return null
        return fileName.removePrefix(suffix)
    }
}