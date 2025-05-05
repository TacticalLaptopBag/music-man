package com.github.tacticallaptopbag

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.github.tacticallaptopbag.convert.ConvertCommand
import com.github.tacticallaptopbag.strmanip.FixTrackNumbersCommand
import com.github.tacticallaptopbag.strmanip.FormatCommand
import com.github.tacticallaptopbag.strmanip.remove.RemoveCommand
import com.github.tacticallaptopbag.strmanip.remove.RemovePrefixCommand
import com.github.tacticallaptopbag.strmanip.remove.RemoveSuffixCommand
import com.github.tacticallaptopbag.strmanip.ReplaceCommand

class MainCommand: CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) = MainCommand()
    .subcommands(
        ConvertCommand(),
        RemoveCommand()
            .subcommands(
                RemovePrefixCommand(),
                RemoveSuffixCommand(),
            ),
        ReplaceCommand(),
        FixTrackNumbersCommand(),
        FormatCommand(),
    )
    .main(args)
