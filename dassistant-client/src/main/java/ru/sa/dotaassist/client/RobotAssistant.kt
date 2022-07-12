package ru.sa.dotaassist.client

import java.awt.*
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage


// Более автоматизированная надстройка над первоначальным классом Assistant
class RobotAssistant : Assistant() {
    init {
        PropertyManager.subscribe(this as Object)
    }
    @Subscriber(key = "robot-xPos")
    private var xPos: Int = 0
    @Subscriber(key = "robot-yPos")
    private var yPos: Int = 0
    @Subscriber(key = "robot-width")
    private var width: Int = 0
    @Subscriber(key = "robot-height")
    private var height: Int = 0
    private val robot = Robot()

    // От имени игрока печатает текст, пока недописан в связи с написанием комментариев
    fun send(text: String) {
        robot.apply {
            val stringSelection = StringSelection(text)
            val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(stringSelection, stringSelection)
            robot.keyPress(KeyEvent.VK_CONTROL)
            robot.keyPress(KeyEvent.VK_V)
            robot.keyRelease(KeyEvent.VK_V)
            robot.keyRelease(KeyEvent.VK_CONTROL)
            keyPress(KeyEvent.VK_ENTER)
            delay(10)
            keyRelease(KeyEvent.VK_ENTER)
        }
    }

    // Метод вырезания куска изображения с таймером игры
    private fun cut() : BufferedImage {
        return robot.createScreenCapture(Rectangle(xPos, yPos, width, height))
    }

    fun imgToText(image: BufferedImage) {

    }

}

fun main() {
    val assistant = RobotAssistant();
    assistant.send("hello")


}