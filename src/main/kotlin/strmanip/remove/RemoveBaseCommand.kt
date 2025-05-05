package com.github.tacticallaptopbag.strmanip.remove

import com.github.tacticallaptopbag.*

abstract class RemoveBaseCommand(name: String): BaseCommand(name) {
    abstract fun getNewName(fileName: String): String?

    override fun run() {
        val fileType = guessFileType()
        notifyDry()

        listFilesOfType(fileType).forEach { file ->
            val newName = getNewName(file.nameWithoutExtension)
            if(newName.isNullOrBlank()) return@forEach

            val extension = if(file.extension.isNotBlank())
                ".${file.extension}"
            else
                ""
            val newNameWithExtension = "$newName$extension"

            echo("${file.name} -> $newNameWithExtension")
            if(!dry) {
                rename(file, newNameWithExtension)
            }
        }
    }
}