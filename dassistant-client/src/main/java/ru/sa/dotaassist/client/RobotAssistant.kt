package ru.sa.dotaassist.client

import java.awt.Dimension
import java.awt.Point
import java.awt.Robot
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
            keyPress(KeyEvent.VK_ENTER)
            delay(10)
            keyRelease(KeyEvent.VK_ENTER)
        }
    }

    // Метод вырезания куска изображения с таймером игры
    fun cut() {

    }

}