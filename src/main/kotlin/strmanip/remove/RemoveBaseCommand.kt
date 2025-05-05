package com.github.tacticallaptopbag.strmanip.remove

import com.github.tacticallaptopbag.*
import java.io.File

abstract class RemoveBaseCommand(name: String): BaseCommand(name) {
    abstract fun getNewName(fileName: String): String?

    override fun run() {
        val fileType = guessFileType()
        notifyDry()

        listFilesOfType(fileType).forEach { file ->
            val newName = getNewName(file.name)
            if(newName.isNullOrBlank()) return@forEach

            echo("${file.name} -> $newName")
            if(!dry) {
                rename(file, newName)
            }
        }
    }
}