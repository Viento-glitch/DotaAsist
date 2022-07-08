package ru.sa.dotaassist.client

import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.Properties

class PropertyManager {
    // Все статичный поля расположены здесь
    companion object {
        // Строка содержащая имя файла с настройками вместе с путем
        private val path = "settings.properties"
        // функция генерации стандартных настроек
        private fun standardSettings(): Properties {
            // Временный обьект настроек
            val properties = Properties()
            // Все строки внутри apply применяются к properties
            // Задавать новые настройки следует здесь в формате ключ - значение
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
    // Обычный файл java, используется для всех махинаций с самим файлом настроек
    private val file = File(path)
    // Ради этого обьекта существует весь класс, он нужен для работы с настройками
    private var properties = Properties()
    // Пользовательская функция сохранения, удобная обертка, все делает сама
    fun save() {
        try {
            properties.store(FileWriter(file), "")
        } catch (e: Exception) {
            println("ERROR: cannot save $path")
        }
    }
    // Пользовательская функция загрузки, удобная обертка, все делает сама
    fun load() {
        try {
            if (file.isDirectory) {
                if (!file.delete()) {
                    println("ERROR: $path  is a directory with files")
                    properties = standardSettings()
                } else {
                    properties = standardSettings()
                    properties.store(FileWriter(file), "")
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
    // Пользовательская функция, Получить настройку
    fun get(key: String): String? {
        return properties.getProperty(key)
    }
    // Пользовательская функция, Установить значение настройки
    fun set(key: String, value: String) {
        properties[key] = value
    }
}