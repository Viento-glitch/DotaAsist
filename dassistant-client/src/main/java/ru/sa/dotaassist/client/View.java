package ru.sa.dotaassist.client;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import ru.sa.dotaassist.client.exceptions.DbException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

class View extends JFrame {

    static final String VERSION = "1.0-SNAPSHOT";

    static JFrame This;
    static boolean autoUpdateBoolean;
    static boolean smilesBoolean;
    Controller controller;

    public View (Controller controller){
        super("DAssistant " + VERSION);
        this.controller = controller;
    }

    JButton settings= new JButton("Настройки(частично работает)");
    JButton buttonInstruction = new JButton("Инструкция");
    JButton buttonActivate = new JButton("Включить");
    JButton buttonDeactivate = new JButton("Выключить");
    JButton buttonBugReport = new JButton("Баг репорт");
    JCheckBox autoUpdateCheckBox = new JCheckBox("Авто обновление", null, autoUpdateBoolean);
    JCheckBox useSmilesCheckBox = new JCheckBox("Использовать смайлы", null, isSmilesActive());

    public static boolean isSmilesActive() {
        return smilesBoolean;
    }

    public static void setSmilesBoolean(boolean smilesBoolean) {
        View.smilesBoolean = smilesBoolean;
    }

    public void setUseSmilesCheckBox(boolean value){
        smilesBoolean = value;
    }

    public void setCheckBoxStartValue(boolean value) {
        this.autoUpdateCheckBox.setSelected(value);
    }


    public void init() {
        This = this;
        Date startDate = new Date();
        Toolkit tk = Toolkit.getDefaultToolkit();
        int width = 340;
        int height = 125;
        int xPos = (tk.getScreenSize().width-width)/2;
        int yPos = (tk.getScreenSize().height-height)/2;

//        smilesBoolean = controller.
        setBounds(xPos, yPos, width, height);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
                 *              !в случае не удачного подключения к серверу
                 *                  продолжить стандартное выполнение программы
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
                controller.saveDuration(startDate, endDate);

                exitProcedure();
            }
        });


        Container container = this.getContentPane();
        container.setLayout(new GridLayout(5, 1, 1, 1));
//        container.setLayout(new GridLayout(3, 1, 1, 2));
        buttonActivate.addActionListener(new ActivateButtonEventListener());

        buttonDeactivate.addActionListener(new DeactivateButtonEventListener());
        buttonInstruction.addActionListener(new ButtonEventListenerInstruction());
        buttonBugReport.addActionListener(new ButtonEventListenerBugReport());
        autoUpdateCheckBox.addActionListener(new CheckboxAction());
        useSmilesCheckBox.addActionListener(new SmileCheckboxAction());

        settings.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Settings(View.This);
            }
        });

        useSmilesCheckBox.setSelected(isSmilesActive());
        
        container.add(useSmilesCheckBox);
        container.add(buttonInstruction);
//        container.add(buttonBugReport);
        container.add(settings);

        container.add(buttonActivate);
        container.add(buttonDeactivate);

//        container.add(autoUpdateCheckBox);
    }

    public void warningMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "", JOptionPane.WARNING_MESSAGE, null);
    }

    static class SmileCheckboxAction extends AbstractAction {
        public SmileCheckboxAction() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JCheckBox cbLog = (JCheckBox) actionEvent.getSource();
            DatabaseManager databaseManager = new DatabaseManager();
            if (cbLog.isSelected()) {
                try {
                    databaseManager.setSmilesBoolean(true);
                    smilesBoolean = true;
                    System.out.println(databaseManager.isSmilesEnabled());
                    System.out.println("Smiles is activated");
                } catch (DbException e) {
                    e.printStackTrace();
                    //todo
                }
            } else {
                try {
                    databaseManager.setSmilesBoolean(false);
                    smilesBoolean = false;
                    System.out.println(databaseManager.isSmilesEnabled());
                    System.out.println("Smiles is deactivated");
                } catch (DbException e) {
                    e.printStackTrace();
                    //todo
                }
            }
        }
    }

    static class CheckboxAction extends AbstractAction {
        public CheckboxAction() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JCheckBox cbLog = (JCheckBox) actionEvent.getSource();
            DatabaseManager databaseManager = new DatabaseManager();
            if (cbLog.isSelected()) {
                try {
                    databaseManager.setAutoUpdateBoolean(true);
                    System.out.println(databaseManager.isAutoUpdateEnabled());
                    System.out.println("AutoUpdating is activated");
                } catch (DbException e) {
                    e.printStackTrace();
                    //todo
                }
            } else {
                try {
                    databaseManager.setAutoUpdateBoolean(false);
                    System.out.println("AutoUpdating is deactivated");
                } catch (DbException e) {
                    e.printStackTrace();
                    //todo
                }
            }
        }
    }

    private void exitProcedure() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    static class ButtonEventListenerInstruction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null,
                    """
                            Данная программа создана для простого отслеживания таймингов Аегиса и Рошана.

                            После смерти Рошана вы должны:
                               1.1. Зафиксировать время.
                               1.2. Открыть чат.(Enter)
                               1.3. Переписать зафиксированное время в чат в формате одного числа.
                                   1.3.1. Если время 33:01 ввести потребуется 3301
                               1.4. Зажать клавишу "Ctrl"
                                   1.4.1. Нажать клавишу "A"
                                   1.4.2. Нажать клавишу "C"


                            Если вы не зафиксировали время смерти рошана
                               2.1. Обнаружить владельца Аегиса
                               2.2. Посмотреть на аегисе его оставшееся время
                                   2.2.1. Зафиксировать текущее время.
                               2.3. Записать время.
                                   2.3.1. Записать оставшееся время Аегиса одной цифрой с знаком минус(-400 если 4:00)
                                   2.3.2. Добавить пробел.
                                   2.3.3. Записать Зафиксированное время в виде одной цифры (см выше п. 2.2.1)  \s
                               2.4 Выполнить пункт 1.4 , 1.4.1 , 1.4.2
                              \s
                              \s 
                            Получение результата
                               1. Зажмите клавишу "Ctrl"
                                   1.1. нажмите клавишу "V"
                            В качестве результата вы получите строку такого формата.
                            3301 (A)38:01  (R)41:01-44:01
                            """,
                    "Инструкция",
                    JOptionPane.PLAIN_MESSAGE
            );
        }
    }

    static class ButtonEventListenerBugReport implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JPanel panel = new JPanel();
            JFrame bugReportFrame = new JFrame();
            bugReportFrame.setSize(350, 500);
            bugReportFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            bugReportFrame.setVisible(true);
            bugReportFrame.add(panel);

            panel.setLayout(null);

            JTextPane bugReportMessage = new JTextPane();

            bugReportMessage.setBounds(0, 0, bugReportFrame.getWidth(), bugReportFrame.getHeight());
            panel.add(bugReportMessage);
        }

        static class SandBugReportActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Сообщение было сохранено и по возможности будет отправлено",
                        "Благодарю за обращение", JOptionPane.PLAIN_MESSAGE, null);
                    //to do реализовать сохранение сообщения
            }
        }
    }

    static class ActivateButtonEventListener implements ActionListener {
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

    static class DeactivateButtonEventListener implements ActionListener {
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



