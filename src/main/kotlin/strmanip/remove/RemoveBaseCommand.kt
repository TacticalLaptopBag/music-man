package com.github.tacticallaptopbag.strmanip.remove

import com.github.tacticallaptopbag.*
import java.io.File

abstract class RemoveBaseCommand(name: String): BaseCommand(name) {
    abstract fun getNewName(fileName: String): String?

    override fun run() {
        val fileType = guessFileType()
        notifyDry()

        listFilesOfType(fileType).forEach { fileName ->
            val newName = getNewName(fileName)
            if(newName.isNullOrBlank()) return@forEach

            echo("$fileName -> $newName")
            if(!dry) {
                val file = File(fileName)
                file.renameTo(File(newName))
            }
        }
    }
}