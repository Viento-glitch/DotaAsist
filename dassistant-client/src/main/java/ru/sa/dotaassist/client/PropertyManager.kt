package ru.sa.dotaassist.client

import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.Properties

class PropertyManager {

    companion object {
        private val path = "settings.properties"
        private fun standardSettings(): Properties {
            val properties = Properties()
            properties.apply {
                put("auto-send", false)
                put("auto-update", false)
                put("show-warns", false)
                put("send-statistic", false)
                put("robot-xPos", 0)
                put("robot-yPos", 0)
                put("robot-width", 0)
                put("robot-height", 0)
                put("robot-hotkey", KeyEvent.VK_T.toChar())
            }
            return properties
        }
    }
    private val file = File(path)
    private var properties = Properties()
    fun save() {

    }
    fun load() {
        try {
            if (file.isDirectory) {
                if (!file.delete()) {
                    println("ERROR: $path  is directory with files")
                    properties = standardSettings()
                }
            } else {
                if (file.exists()) {
                    properties.load(FileReader(file))
                } else {
                    properties = standardSettings()
                    file.mkdirs()
                    properties.store(FileWriter(file), "")
                }
            }
        } catch (e: Exception) {
            println("ERROR: cannot do anything with $path \n loading default settings in read only")
            properties = standardSettings()
        }

    }
}