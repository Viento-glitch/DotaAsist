package ru.sa.dotaassist.client;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Date;

class View extends JFrame {
    private static final String VERSION = "1.0-SNAPSHOT";

    JButton buttonInstruction = new JButton("Инструкция");
    JButton buttonActivate = new JButton("Включить");
    JButton buttonDeactivate = new JButton("Выключить");
    JButton buttonBugReport = new JButton("Баг репорт");

    public View() {
        super("DAssistant " + VERSION);
        warningMessage();
        setBounds(900, 450, 330, 115);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                /**todo
                 *?  создать масштабную таблицу действий
                 *!  создать багрепорт
                 *!  создать таблицу багрепорта
                 *
                 *  ?формат Database
                 *  * "logs"
                 *  1. юид.
                 *  2. время запуска приложения.
                 *  3. время закрытия приложения.
                 *  !______________________________
                 *  !Клиентская часть
                 *  ?Багрепорт
                 *  1 время отправки багрепорта
                 *  2 юид
                 *  3 текст багрепорта
                 *
                 *  ?VersionControl
                 *
                 *  ?1 сравнение версии приложения с сохранённой последней версией в логе обновлений
                 *      * при соответствии версии приложения с сохранённой последней версией в логе обновлений
                 *
                 *          ? попытка запроса на сервер
                 *              * случае удачного подключения к серверу
                 *                   запросить цифру последнего обновления
                 *                      ? сравнение последней версии в логе обновлений с цифрой последнего обновления полученной с сервера
                 *                        * в случае соответствия версии пользователя версии полученной с сервера
                 *                               продолжить стандартное выполнение программы
                 *
                 *                        ! в случае не соответствия версии пользователя версии полученной с сервера
                 *                               изменить лог апдейтов пользователя
                 *                              ? создание нового лога апдейтов
                 *                                  ? перебор версий отсортированному по id
                 *                                      ? при нахождении соответствия версии приложения и выбранного id
                 *                                           запомнить id
                 *                                           если версия последняя
                 *                                              * перенести полученные изменения в лог апдейтов
                 *                                                   сохранить последнюю версию обнаруженную
                 *                                                  в лог апдейтов.
                 *
                 *                                          ! если версия не последняя
                 *                                          ? перебрать все последующие версии
                 *                                                  * добавить к результату версию обновления
                 *                                                  * добавить текст обновления
                 *                                                  * добавить разделитель между обновлениями
                 *
                 *          ! в случае не удачного подключения к серверу
                 *
                 *      ! при несоответствии версии приложения с сохранённой последней версией в логе обновлений
                 *          ? проверить наличие галочки в чекбоксе "Обновлять без лишних вопросов"
                 *              * при согласии на обновления без лишних вопросов
                 *                     todo реаилизация обновления путём скачивания файлов с github оповестить о
                 *                      окончании скачивания
                 *                      перезапустить приложение
                 *                      показать нововведения
                 *              ! при не согласии на обновления без лишних вопросов
                 *                   оповестить пользователя о возможности обновится
                 *                       изменить структуру приложения добавив в окно UpdateNews кнопку обновится
                 *                          ? при нажатии на клавишу обновится
                 *                              *todo реаилизация обновления путём скачивания файлов с github
                 *                                  оповестить о окончании скачивания
                 *                                  перезапустить приложение
                 *
                 *      /////////////////////////////////////////////////////////////////////////////////
                 *
                 *  !аналог
                 *  0 сравнить версию программы с сохранённой последней версией в базе данных клиента.
                 *      0.1 при несоответствии продеманстрировать сообщение сопутствующее обновлению.
                 *  1 Запрос серверу версии в виде String данных.
                 *  2 сравнение с текущей версией программы.
                 *  3 при несоответствии версии.
                 *  4 сохранить последнюю версию в бд.
                 *  5 запросить блок текста сопутствующего обновлению .
                 *  6 сохранить в базе данных.
                 *  7 выдать сообщение пользователю.
                 *  8 прервать выполнение остальной части программы.
                 *  !______________________________
                 *  !Серверная часть
                 *  ?Багрепорт
                 *  1 время отправки багрепорта
                 *  2 юид
                 *  3 текст багрепорта
                 *  ?VersionControl
                 *  *   DataBase
                 *  1 update table
                 *      1.2 лог версий
                 *          1.2.1
                 *
                 *  2 bugReport table
                 *      1.1 id пользователя
                 *      1.2 дата отправки
                 *      1.3 текст сообщения
                 *  3
                 *  !______________________________
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

    private void warningMessage() {
        try {
            if (!DatabaseManager.isDatabaseExists()) {
                JOptionPane warningMessage = new JOptionPane();
                warningMessage.showMessageDialog(null,
                        "                                         Внимание!\n" +
                                "Для улучшения качества обслуживания \n" +
                                "отслеживается продолжительность запущенной программы", "", JOptionPane.INFORMATION_MESSAGE, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

                            "   2.3. Записать время.\n" +
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
            JTextPane bugReportMessage = new JTextPane();
//            bugReportMessage.
            bugReportMessage.setSize(100, 300);
//            bugReportMessage.addKeyListener(new KeyAdapter() {
//                public void keyPressed(KeyEvent e) {
//                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                        sandBugReportButton.doClick();
//                    }
//                }
//            });
//            sandBugReportButton.addActionListener();

            JTextPane dialog = new JTextPane();
            dialog.setText("Данное поле было создано с целью  повышения качества приложения \n" +
                    "Для этого вы можете в поле ниже написать как получили ту или инную ошибку \n"
            );
            dialog.setEditable(false);
            bugReportFrame.add(dialog);
            bugReportFrame.add(bugReportMessage);
            bugReportFrame.add(sandBugReportButton);
            bugReportFrame.setVisible(true);

            /**todo
             *  создать дочернее окно с полем для ввода
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


