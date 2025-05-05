package com.github.tacticallaptopbag.strmanip.remove

import com.github.ajalt.clikt.core.Context
import com.github.tacticallaptopbag.HELP_RM_SUFFIX
import com.github.ajalt.clikt.parameters.arguments.argument

class RemoveSuffixCommand: RemoveBaseCommand(name = "suffix") {
    private val suffix by argument()

    override fun help(context: Context): String = HELP_RM_SUFFIX

    override fun getNewName(fileName: String): String? {
        if(!fileName.endsWith(suffix)) return null
        return fileName.removeSuffix(suffix)
    }
}