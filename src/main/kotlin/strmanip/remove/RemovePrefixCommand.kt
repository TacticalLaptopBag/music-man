package com.github.tacticallaptopbag.strmanip.remove

import com.github.ajalt.clikt.core.Context
import com.github.tacticallaptopbag.HELP_RM_PREFIX
import com.github.ajalt.clikt.parameters.arguments.argument

class RemovePrefixCommand: RemoveBaseCommand(name = "prefix") {
    private val prefix by argument()

    override fun help(context: Context): String = HELP_RM_PREFIX

    override fun getNewName(fileName: String): String? {
        if(!fileName.startsWith(prefix)) return null
        return fileName.removePrefix(prefix)
    }
}