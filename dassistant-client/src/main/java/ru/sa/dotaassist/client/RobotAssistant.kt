package ru.sa.dotaassist.client

import ru.swarm.color.*
import java.awt.*
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel


class RobotAssistant : Assistant() {
    private val parser: SParser
    private val robot = Robot()
    init {
        val dictionary = ArrayList<SymbolRange>()
        dictionary.apply {
            add(SymbolRange(0.1f, 0.2f, "1"))
            add(SymbolRange(0.2f, 0.3f, "2"))
            add(SymbolRange(0.3f, 0.4f, "3"))
            add(SymbolRange(0.4f, 0.5f, "4"))
            add(SymbolRange(0.5f, 0.6f,"5"))
            add(SymbolRange(0.6f, 0.7f,"6"))
            add(SymbolRange(0.7f, 0.8f,"7"))
            add(SymbolRange(0.8f, 0.9f,"8"))
            add(SymbolRange(0.9f, 1.0f,"9"))
            add(SymbolRange(0.95f, 1.1f,"0"))
            add(SymbolRange(0.0f, 0.1f, ":"))
        }
        val textColor = HSLHumanicColor.create().onlyWhite()
        textColor.lMin = 59f
        textColor.sMin = -0.1f
        //textColor.hMin = 180f
        //textColor.hMax = 270f
        parser = SParser(textColor, dictionary)
    }

    fun send(text: String) {
        robot.apply {
            val stringSelection = StringSelection(text)
            val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(stringSelection, stringSelection)
            robot.keyPress(KeyEvent.VK_CONTROL)
            robot.keyPress(KeyEvent.VK_V)
            robot.keyRelease(KeyEvent.VK_V)
            robot.keyRelease(KeyEvent.VK_CONTROL)
            if (PropertyManager.get("auto-send").toBoolean()) {
                keyPress(KeyEvent.VK_ENTER)
                delay(10)
                keyRelease(KeyEvent.VK_ENTER)
            }
        }
    }

    private fun cut() : BufferedImage {
        return robot.createScreenCapture(Rectangle(PropertyManager.get("robot-xPos").toInt(), PropertyManager.get("robot-yPos").toInt(), PropertyManager.get("robot-width").toInt()-PropertyManager.get("robot-xPos").toInt(), PropertyManager.get("robot-height").toInt()-PropertyManager.get("robot-yPos").toInt()))
    }

    fun imgToText(image: BufferedImage): String {
        return parser.toText(parser.parse(image))
    }

}

fun main() {
    val assistant = RobotAssistant();
    //assistant.send("hello")
    val image = ImageIcon("/home/swarm/Downloads/NitroShare/Dota.jpg")
    val cutImage = BufferedImage(
        image.iconWidth,
        image.iconHeight,
        BufferedImage.TYPE_INT_RGB
    )
    val g: Graphics = cutImage.createGraphics()
// paint the Icon to the BufferedImage.
    // paint the Icon to the BufferedImage.
    image.paintIcon(null, g, 0, 0)
    g.dispose()
    JFrame().apply {
        isVisible = true
        val label = JLabel()
        val icn = cutImage.getSubimage(PropertyManager.get("robot-xPos").toInt(), PropertyManager.get("robot-yPos").toInt(), PropertyManager.get("robot-width").toInt()-PropertyManager.get("robot-xPos").toInt(), PropertyManager.get("robot-height").toInt()-PropertyManager.get("robot-yPos").toInt())
        label.icon = ImageIcon(icn)
        location = Point(200, 200)
        size = Dimension(icn.width, icn.height)
        add(label)
    }
    println(assistant.imgToText(cutImage.getSubimage(PropertyManager.get("robot-xPos").toInt(), PropertyManager.get("robot-yPos").toInt(), PropertyManager.get("robot-width").toInt()-PropertyManager.get("robot-xPos").toInt(), PropertyManager.get("robot-height").toInt()-PropertyManager.get("robot-yPos").toInt())))

}