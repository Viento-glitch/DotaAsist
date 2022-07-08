package ru.sa.dotaassist.client

import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ScrollPaneConstants

// Класс ответственный за меню настроек
class Settings(val parent: JFrame) : JFrame() {
    // Главная панель обернутая в объект ниже
    private val panel = JPanel()
    private val mainPanel = JPanel()
    // Панель с общими настройками
    private val common = JPanel()
    // Панель с настройка робота
    private val robot = JPanel()
    // Отвечает за прокрутку
    private val scroll = JScrollPane(panel)
    // Статические обьекты
    companion object {
        // Размер окна настроек
        val WINDOW_SIZE = Dimension(400, 600)
        // Главный экземпляр класса PropertyManager
        val PM = PropertyManager()
    }
    init {
        // Временные переменные
        val tk = Toolkit.getDefaultToolkit()
        val sz = tk.screenSize
        title = "settings"
        defaultCloseOperation = HIDE_ON_CLOSE
        size = WINDOW_SIZE
        // Расположение окна
        location = Point((sz.width-WINDOW_SIZE.width)/2, (sz.height-WINDOW_SIZE.height)/2)
        // Блокировка изменения размера окна
        isResizable = false
        isVisible = true
        // Добавление слушателя окна
        addWindowListener(WindowSettingsListener(this))
        // Добавление панели вместе с прокруткой в окно
        //add(mainPanel)
        //mainPanel.apply {
            add(scroll)
            //add(JButton("124234"))
            //add(JButton("124234"))
        //}
        scroll.apply {
            // Задается политика прокрутки
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
        }
        panel.apply {
            // Установка лэйоута, значения на текущий момент временные
            layout = GridLayout(8, 1, 20, 15)
            add(common)
            add(robot)
            background = Color.LIGHT_GRAY
        }
        common.apply {
            layout = GridLayout(4, 1, 5, 5)
            add(JLabel("Общие"))
            add(JCheckBox("обновляться автоматически"))
            add(JCheckBox("показывать окна с предупреждениями"))
            add(JCheckBox("отправлять статистику использования"))
        }
        robot.apply {
            layout = GridLayout(4, 2, 5, 5)
            add(JLabel("Робот"))
            add(JLabel())
            add(JCheckBox("автоматически отправлять сообщение"))
            add(JLabel())
            add(JButton("Калибровка захвата таймера"))
            add(JLabel())
            add(JButton("Бинд робота none"))
        }
        // Всегда поверх других окон
        isAlwaysOnTop = true
    }


    class WindowSettingsListener(private val ST: Settings) : WindowAdapter() {
        // Активируется на первоначальное появление окна
        override fun windowOpened(e: WindowEvent?) {
            super.windowOpened(e)
            // блокировка родительского окна
            ST.parent.isVisible = false
        }

        //
        override fun windowClosing(e: WindowEvent?) {
            super.windowClosed(e)
            // Разблокировка родительского окна
            ST.parent.isVisible = true
        }

    }

}