package com.github.tacticallaptopbag.strmanip

import com.github.ajalt.clikt.core.Context
import com.github.tacticallaptopbag.BaseCommand
import com.github.tacticallaptopbag.HELP_TRACKNO

class FixTrackNumbersCommand : BaseCommand(name = "fixtrackno") {
    override fun help(context: Context): String = HELP_TRACKNO

    override fun run() {
        val fileType = guessFileType()
        notifyDry()

        val regex = Regex("""\d+""")
        val files = listFilesOfType(fileType).filter { regex.find(it.nameWithoutExtension) != null }
        val zeroCount = files.size.toString().length
        files.forEach { file ->
            val match = regex.find(file.nameWithoutExtension)!!
            val trackno = match.groupValues.first()
            val fixedTrackno = trackno.padStart(zeroCount, '0')
            val fixedName = file.name.substring(0 until match.range.first) + fixedTrackno + file.name.substring(match.range.last + 1)
            if(fixedName == file.name) return@forEach
            echo("${file.name} -> $fixedName")
            if(!dry) {
                rename(file, fixedName)
            }
        }
    }
}