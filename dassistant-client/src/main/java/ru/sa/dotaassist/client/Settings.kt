package ru.sa.dotaassist.client

import java.awt.Dimension
import java.awt.Point
import java.awt.Toolkit
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ScrollPaneConstants

class Settings(val parent: JFrame) : JFrame() {
    private val panel = JPanel()
    private val scroll = JScrollPane(panel)
    companion object {
        val WINDOW_SIZE = Dimension(400, 600)
        val PM = PropertyManager()
    }
    init {
        val tk = Toolkit.getDefaultToolkit()
        val sz = tk.screenSize
        title = "settings"
        defaultCloseOperation = HIDE_ON_CLOSE
        size = WINDOW_SIZE
        location = Point((sz.width-WINDOW_SIZE.width)/2, (sz.height-WINDOW_SIZE.height)/2)
        isResizable = false
        isVisible = true
        addWindowListener(WindowSettingsListener(this))
        add(scroll)
        scroll.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        scroll.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        isAlwaysOnTop = true

    }

    class WindowSettingsListener(val ST: Settings) : WindowAdapter() {
        override fun windowOpened(e: WindowEvent?) {
            super.windowOpened(e)
            ST.parent.isVisible = false
        }

        override fun windowClosed(e: WindowEvent?) {
            super.windowClosed(e)
            ST.parent.isVisible = true
        }

        override fun windowLostFocus(e: WindowEvent?) {
            super.windowLostFocus(e)
            ST.isVisible = true
        }
    }

}