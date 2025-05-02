package com.github.tacticallaptopbag.convert

import java.nio.file.Path
import java.util.concurrent.TimeUnit
import kotlin.io.path.*

data class MP3Converter(
    override val path: Path,
    override val extension: String,
): Converter {
    override fun convert(sourceFile: Path, sampleRate: Int, bitRate: Long): Path {
        val dest = if(path.isDirectory() || (path.notExists() && path.extension.isBlank())) {
            path.createDirectories()
            path.resolve("${sourceFile.nameWithoutExtension}.$extension")
        } else {
            path.createParentDirectories()
            path
        }

        val proc = ProcessBuilder(
            "ffmpeg",
            "-i", "$sourceFile",
            "-acodec", "libmp3lame",
            "-ar", "$sampleRate",
            "-b:a", "$bitRate",
            "-vn",
            "-y",
            "$dest"
        )
            .redirectOutput(ProcessBuilder.Redirect.DISCARD)
            .redirectError(ProcessBuilder.Redirect.DISCARD)
            .start()

        proc.waitFor(60, TimeUnit.MINUTES)

        return dest
    }
}