package Main;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Visual extends JFrame {
    private static final String version = " 1.0";
    JButton buttonActivate = new JButton("Включить");
    JButton buttonDeactivate = new JButton("Выключить");
    JButton buttonInstruction = new JButton("Инструкция");

    public Visual() {
        super("DAssistant"+version);
        this.setBounds(900, 450, 290, 115);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
                        "3301 (A)38:01  (R)41:01-44:01\n"

//                        "Программа отслеживает комбинацию клавишь: Ctrl+a+Ctrl+c\n" +
//                        "Проверяет на наличие цифр внутри и при нахождении выполняет трансформацию числа\n" +
//                        "Программа адаптированна под два варианта событий\n" +
//                        "1.После смерти рошана Вы сразу нажимаете на часы(или как либо иначе точно фиксируете время его смерти)\n" +
//                        "Далее Вы записываети время как простое число без каких либо дополнительных элементов вроде: \"/:\"\n" +
//                        "Пример(обращаю Ваше внимание на отсутствие пробелов):3301\n" +
//                        "Далее просто нажимаем Ctrl+A , цифра выделится, и сразу жмём Ctrl+C\n" +
//                        "Ctrl отпускать не обязательно\n" +
//                        "\n" +
//                        "2.Вы пропустили момент смерти рошана ((Все мы люди, я понимаю)(P.S.или не все!?))\n" +
//                        "в таком случае вам необходимо найти персонажа подобравшего аегис\n" +
//                        "Найдя вам нужно запомнить время на таймере аегиса и зафиксировать текущее время(способом вам удобным)\n" +
//                        "далее ввести в чат время оставшееся аегиса с минусом перед ним(-400(4минуты 00 секунд))\n" +
//                        "далее через пробел вы вводите \"затаймленное время\" к примеру 3401\n" +
//                        "должно получится следующее:-400 3401\n" +
//                        "дальше как и обычно Ctrl+A,Ctrl+C и результат уже у вас в буффере обмена\n" +
//                        "в любой момент нажав Ctrl+V вы получаете готовый результат\n" +
//                        "\n" +
//                        "Результат будет следующий: 3301 (A)38:01  (R)41:01-44:01\n" +
//                        "1 цифра - время смерти рошана\n" +
//                        "2 цифра - время окончания аегиса\n" +
//                        "3,4 цифра - это время минимального и максимального тайминга рошана\n"
                , "Инструкция", JOptionPane.PLAIN_MESSAGE);
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
        GlobalScreen.addNativeKeyListener(new Base());
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
