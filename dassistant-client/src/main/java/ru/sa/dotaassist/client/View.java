package ru.sa.dotaassist.client;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

class View extends JFrame {
    private static final String VERSION = "1.0-SNAPSHOT";

    JButton buttonInstruction = new JButton("Инструкция");
    JButton buttonActivate = new JButton("Включить");
    JButton buttonDeactivate = new JButton("Выключить");
    JButton buttonBugReport = new JButton("Баг репорт");

    public View() {
        super("DAssistant " + VERSION);
        setBounds(900, 450, 330, 115);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                /**todo
                 *  создать инсерт sessionDuration
                 *?  создать масштабную таблицу действий
                 *!  создать багрепорт
                 *  создать таблицу багрепорта
                 *?  создать предупреждение о получаемой информации пользователя
                 */
                Date endDate = new Date();
                long sessionDuration = endDate.getTime() - Runner.startDate.getTime();
                System.out.println(sessionDuration);
                exitProcedure();
            }
        });


        Container container = this.getContentPane();
        container.setLayout(new GridLayout(2, 1, 1, 2));
        buttonActivate.addActionListener(new ButtonEventListenerActivate());

        buttonDeactivate.addActionListener(new ButtonEventListenerDeactivate());
        buttonInstruction.addActionListener(new ButtonEventListenerInstruction());
        buttonBugReport.addActionListener(new ButtonEventListenerBugReport());

        container.add(buttonInstruction);
        container.add(buttonBugReport);
        container.add(buttonActivate);
        container.add(buttonDeactivate);
    }

    private void exitProcedure() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
                            "   2.1. Обнаружить владельца Аегиса\n" +

                            "   2.2. Посмотреть на аегисе его оставшееся время\n" +
                            "       2.2.1. Зафиксировать текущее время.\n" +

                            "   2.3. Записать время." +
                            "       2.3.1. Записать оставшееся время Аегиса одной цифрой с знаком минус(-400 если 4:00)\n" +
                            "       2.3.2. Добавить пробел.\n" +
                            "       2.3.3. Записать Зафиксированное время в виде одной цифры (см выше п. 2.2.1)   \n" +

                            "   2.4 Выполнить пункт 1.4 , 1.4.1 , 1.4.2\n" +
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

    class ButtonEventListenerBugReport implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame bugReportFrame = new JFrame();
            bugReportFrame.setBounds(900, 450, 480, 400);
            bugReportFrame.setLayout(new GridLayout(3, 1, 2, 3));
            JButton sandBugReportButton = new JButton("Отправить");
            JTextPane input = new JTextPane();
            input.setSize(100,300);
            input.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        sandBugReportButton.doClick();
                    }
                }
            });
//            sandBugReportButton.addActionListener();

            JTextPane dialog = new JTextPane();
            dialog.setText("Данное поле было создано с целью  повышения качества приложения \n" +
                    "Для этого вы можете в поле ниже написать как получили ту или инную ошибку \n"
            );
            dialog.setEditable(false);
            bugReportFrame.add(dialog);
            bugReportFrame.add(input);
            bugReportFrame.add(sandBugReportButton);
            bugReportFrame.setVisible(true);

            /**todo
             * создать дочернее окно с полем для ввода
             */
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
            GlobalScreen.addNativeKeyListener(new Assistant());
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


