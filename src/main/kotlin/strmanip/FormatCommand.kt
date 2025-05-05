package com.github.tacticallaptopbag.strmanip

import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.*
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import com.github.tacticallaptopbag.*
import java.io.File

class FormatCommand : BaseCommand("format") {
    val source by argument()
        .help(HELP_FORMAT_SOURCE)
        .enum<MusicSource>()
        .default(MusicSource.Bandcamp)
    val artistOpt by option("--artist")
        .help(HELP_FORMAT_ARTIST)
    val albumOpt by option("--album")
        .help(HELP_FORMAT_ALBUM)

    override fun help(context: Context): String = HELP_FORMAT

    override fun run() {
        val fileType = guessFileType()
        notifyDry()

        val cwd = File(baseDir).absoluteFile
        val album = albumOpt ?: cwd.name
        val artist = artistOpt ?: cwd.parentFile.name

        val formatIncomplete = when(source) {
            MusicSource.Bandcamp -> formatBandcamp(fileType, cwd, album, artist)
            MusicSource.Qobuz -> formatQobuz(fileType, cwd, album, artist)
            MusicSource.iTunes -> TODO("iTunes format is not yet supported")
        }

        if(formatIncomplete) {
            echo(FORMAT_INCOMPLETE.format(cwd.absolutePath))
        }
    }

    fun formatBandcamp(fileType: String, cwd: File, album: String, artist: String): Boolean {
        var incompleteFlag = false

        listFilesOfType(fileType).forEach { file ->
            val regex = Regex("""^$artist - $album - \d+.+\.$fileType$""")
            if(regex.matches(file.name)) {
                val newName = file.name.replace("$artist - $album - ", "")
                echo("${file.name} -> $newName")
                if(!dry) {
                    rename(file, newName)
                }
            } else {
                incompleteFlag = true
            }
        }

        return incompleteFlag
    }

    fun formatQobuz(fileType: String, cwd: File, album: String, artist: String): Boolean {
        var incompleteFlag = false
        val qobuzArtist = artist.replace(" ", "-")

        listFilesOfType(fileType).forEach { file ->
            val regex = Regex("""^\d_(\d+)_${qobuzArtist}_([\w-]+)_\d\.$fileType$""")
            val match = regex.find(file.name)
            if(match != null) {
                val trackno = match.groupValues[1].padStart(2, '0')
                val trackTitle = match.groupValues[2].replace("-", " ")
                val newName = "$trackno $trackTitle.$fileType"
                echo("${file.name} -> $newName")
                if(!dry) {
                    rename(file, newName)
                }
            } else {
                incompleteFlag = true
            }
        }

        return incompleteFlag
    }
}