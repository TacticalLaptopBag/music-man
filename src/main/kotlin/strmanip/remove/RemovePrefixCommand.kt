package com.github.tacticallaptopbag.strmanip.remove

import com.github.tacticallaptopbag.HELP_RM_PREFIX
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help

class RemovePrefixCommand: RemoveBaseCommand(name = "prefix") {
    private val prefix by argument()
        .help(HELP_RM_PREFIX)

    override fun getNewName(fileName: String): String? {
        if(!fileName.startsWith(prefix)) return null
        return fileName.removePrefix(prefix)
    }
}