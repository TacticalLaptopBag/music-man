package com.github.tacticallaptopbag.convert

import java.nio.file.Path

interface Converter {
    val path: Path?
    val extension: String

    fun convert(sourceFile: Path, sampleRate: Int, bitRate: Long): Path
}