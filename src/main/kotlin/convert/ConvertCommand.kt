package com.github.tacticallaptopbag.convert

import com.github.tacticallaptopbag.CONVERT_FAIL
import com.github.tacticallaptopbag.CONVERT_NONE_SPECIFIED
import com.github.tacticallaptopbag.HELP_CONVERT
import com.github.tacticallaptopbag.HELP_CONVERT_BITRATE
import com.github.tacticallaptopbag.HELP_CONVERT_MP3PATH
import com.github.tacticallaptopbag.HELP_CONVERT_SAMPLERATE
import com.github.tacticallaptopbag.HELP_CONVERT_SOURCE
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.*
import kotlin.system.exitProcess

class ConvertCommand : CliktCommand(name = "convert") {
    override fun help(context: Context): String = HELP_CONVERT

    private val source: Path by argument()
        .path(
            mustExist = true,
            mustBeReadable = true,
        )
        .help(HELP_CONVERT_SOURCE)
    private val mp3Path: Path? by option("-m", "--mp3")
        .path()
        .help(HELP_CONVERT_MP3PATH)
    private val sampleRate: Int by option("-s", "--sample-rate")
        .int()
        .default(44_100)
        .help(HELP_CONVERT_SAMPLERATE)
    private val bitRate: Long by option("-b", "--bitrate")
        .convert {
            val input = it.lowercase()
            val inputUnit = input.last()
            val inputBits = input.substring(0..input.length-2)

            return@convert when (inputUnit) {
                'k' -> inputBits.toLong() * 1024
                'm' -> inputBits.toLong() * 1024 * 1024
                'g' -> inputBits.toLong() * 1024 * 1024 * 1024
                else -> it.toLong()
            }
        }
        .default(131_072L /* 128k */)
        .help(HELP_CONVERT_BITRATE)

    private val converters = mutableListOf<Converter>()

    private fun convert(file: Path) {
        if(!file.isRegularFile()) return
        val contentType = Files.probeContentType(file) ?: ""
        if(!contentType.startsWith("audio/")) return

        converters.forEach { converter ->
            echo("Converting $file to ${converter.extension}")
            val convertedFilePath = converter.convert(file, sampleRate, bitRate)
            echo("${converter.extension.uppercase()} placed in $convertedFilePath")
        }
    }

    override fun run() {
        mp3Path?.let { converters += MP3Converter(it, "mp3") }

        if(converters.isEmpty()) {
            echo(CONVERT_NONE_SPECIFIED, err = true)
            exitProcess(1)
        }

        if(source.isDirectory()) {
            source.toFile().list()?.forEach {
                val path = source.resolve(it)
                try {
                    convert(path)
                } catch (re: RuntimeException) {
                    echo(CONVERT_FAIL.format(path))
                    re.printStackTrace()
                }
            }
        } else {
            convert(source)
        }
    }
}
