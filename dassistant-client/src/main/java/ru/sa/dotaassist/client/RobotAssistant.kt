package ru.sa.dotaassist.client

import java.awt.Dimension
import java.awt.Point
import java.awt.Robot
import java.awt.event.KeyEvent

class RobotAssistant(
    val imgSize: Dimension,
    val imgPos: Point
) : Assistant() {
    val robot = Robot()

    fun send(text: String) {
        robot.apply {
            keyPress(KeyEvent.VK_ENTER)
            delay(10)
            keyRelease(KeyEvent.VK_ENTER)
        }
    }

    fun cut() {

    }

}