package ru.sa.dotaassist.server;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class View extends JFrame {
    private static final String VERSION = "1.0";

    JButton buttonActivate = new JButton("Включить");
    JButton buttonDeactivate = new JButton("Выключить");
    JButton buttonInstruction = new JButton("Инструкция");

    public View() {
        super("DAssistant " + VERSION);
        setBounds(900, 450, 290, 115);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 1, 1, 2));
        buttonActivate.addActionListener(new ButtonEventListenerActivate());
//        buttonActivate.add

        buttonDeactivate.addActionListener(new ButtonEventListenerDeactivate());
        buttonInstruction.addActionListener(new ButtonEventListenerInstruction());

        container.add(buttonInstruction);
        container.add(buttonActivate);
        container.add(buttonDeactivate);
    }

    class ButtonEventListenerInstruction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null,
        "Данная программа создана для простого отслеживания таймингов Аегиса и Рошана.\n" +
                "\n" +
                "После смерти Рошана вы должны:\n" +
                "   1.1. Зафиксировать время.\n" +
                "   1.2. Открыть чат.(Enter)\n" +
                "   1.3. Переписать зафиксированное время в чат в формате одного числа.\n" +
                "       1.3.1. Если время 33:01 ввести потребуется 3301\n" +
                "   1.4. Зажать клавишу \"Ctrl\"\n" +
                "       1.4.1. Нажать клавишу \"A\"\n" +
                "       1.4.2. Нажать клавишу \"C\"\n" +
                "\n" +
                "\n" +
                "Если вы не зафиксировали время смерти рошана\n" +
                "   1. Обнаружить владельца Аегиса\n" +
                "   2. Посмотреть на аегисе его оставшееся время\n" +
                "       2.1. Зафиксировать текущее время.\n" +
                "   3. Записать время." +
                "       3.1. Записать оставшееся время Аегиса одной цифрой с знаком минус(-400 если 4:00)\n" +
                "       3.2. Добавить пробел.\n" +
                "       3.3. Записать Зафиксированное время в виде одной цифры (см выше п. 2.1)   \n" +
                "   4 Выполнить пункт 1.4 , 1,4.1 , 1.4.2\n" +
                "   \n" +
                "   \n" +
                "Получение результата\n" +
                "   1. Зижмите клавишу \"Ctrl\"\n" +
                "       1.1. нажмите клавишу \"V\"\n" +
                "В качестве результата вы получите строку такого формата.\n" +
                "3301 (A)38:01  (R)41:01-44:01\n",
            "Инструкция",
                JOptionPane.PLAIN_MESSAGE
            );
        }
    }

    class ButtonEventListenerActivate implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException nativeHookException) {
                nativeHookException.printStackTrace();
            }
//            GlobalScreen.addNativeKeyListener(new Assistant());
        }
    }

    class ButtonEventListenerDeactivate implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException nativeHookException) {
                nativeHookException.printStackTrace();
            }
        }
    }
}


