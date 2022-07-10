package ru.sa.dotaassist.client

import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JButton
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
    private val calibrate = JButton("Калибровка захвата таймера")
    // Отвечает за прокрутку
    //val bind = JButton("Бинд робота ${PropertyManager.get("robot-hotkey")}")
    private val bind = BindButton("Бинд робота", "robot-hotkey")
    //var bindListen = false
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
            layout = GridLayout(5, 1, 5, 5)
            add(JLabel("Общие"))
            add(SettingBox("auto-update", "обновляться автоматически"))
            add(SettingBox("show-warns", "показывать окна с предупреждениями"))
            add(SettingBox("send-statistic", "отправлять статистику использования"))
            add(SettingBox("use-smiles", "Использовать смайлы"))
        }
        robot.apply {
            layout = GridLayout(4, 1, 5, 5)
            add(JLabel("Робот"))
            //add(JLabel())
            add(SettingBox("auto-send", "автоматически отправлять сообщение"))
            //add(JLabel())
            add(calibrate)
            //add(JLabel())
            add(bind)
        }
        calibrate.addActionListener {
            RobotCalibrate(this)
        }
        /*bind.addActionListener {
            bind.text = "Бинд робота ..."
            bindListen = true
        }
        bind.addKeyListener(KeyBindListener(this))*/
        // Всегда поверх других окон
        isAlwaysOnTop = true
    }


   /* class KeyBindListener(val ST: Settings) : KeyAdapter() {
        override fun keyTyped(p0: KeyEvent?) {
            if (p0 != null && ST.bindListen) {
                ST.bind.text = "Бинд робота ${p0.keyChar}"
                PropertyManager.set("robot-hotkey", p0.keyChar.toString())
                ST.bindListen = false
            }
        }
    }*/

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
            PropertyManager.save()
            // Разблокировка родительского окна
            ST.parent.isVisible = true
        }

    }

}