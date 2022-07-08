package ru.sa.dotaassist.client

import java.awt.Dimension
import java.awt.Point
import java.awt.Robot

class RobotAssistant(
    val imgSize: Dimension,
    val imgPos: Point
) : Assistant() {
    val robot = Robot()

}