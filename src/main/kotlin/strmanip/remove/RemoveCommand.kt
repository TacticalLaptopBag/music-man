package com.github.tacticallaptopbag.strmanip.remove

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.tacticallaptopbag.HELP_RM

class RemoveCommand: CliktCommand(name = "remove") {
    override fun run() = Unit
    override fun help(context: Context) = HELP_RM
}