package ru.sa.dotaassist.client

import java.awt.*
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent


// Более автоматизированная надстройка над первоначальным классом Assistant
class RobotAssistant(
    val imgSize: Dimension,
    val imgPos: Point
) : Assistant() {
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
    fun cut() {

    }

    fun imgToText() {

    }

}

fun main() {
    val assistant = RobotAssistant(Dimension(0, 0), Point(0, 0));
    assistant.send("hello")


}