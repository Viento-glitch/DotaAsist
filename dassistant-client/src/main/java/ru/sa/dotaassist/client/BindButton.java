package ru.sa.dotaassist.client;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Данный класс, это специализированная для задавания хоткеев версия
 * обычной кнопки, работа с данной кнопкой упрощена до предела.
 * Нужно задать статичный текст и настройку из списка предоставляемых PropertyManager.
 * Данное расширение самостоятельно: загружает, отслеживает и сохраняет изменения.
 * Все, что вы делаете, дает эффект незамедлительно.
 */
public class BindButton extends JButton {
    private String text;
    // Имя настройки
    private String key;
    // Хранит состояние кнопки, при активном состоянии, слушает нажатия, в противном случае игнорирует
    private boolean active = false;
    // Текст, появляющийся при  ожидании какого-либо ввода
    private final String awaitingText = "...";

    /**
     * Стандартый конструктор. Готовит кнопку к использованию.
     * @param text статическая часть текста на кнопке, надпись.
     * @param key значение с которым кнопка будет работать
     *            данный экземпляр кнопки, доступные смотри в PropertyManager
     */
    public BindButton(String text, String key) {
        this.text = text;
        this.key = key;
        setNormalText();
        addKeyListener(new KeyBindListener());
        addActionListener(ActionEvent -> {
            active = true;
            setAwaitingText();
        });
    }

    /**
     * Задает текст кнопки в рядовом состоянии.
     * Применяется, при первоначальном создании и обновлении хоткея.
     */
    private void setNormalText() {
        setText(text+" "+PropertyManager.Companion.get(key));
    }

    /**
     * Задает текст кнопки в активном состоянии.
     * Подчеркивает, ожидается ввод.
     */
    private void setAwaitingText() {setText(text+" "+awaitingText);}

    /**
     * Слушает нажатия. Занимается обработкой
     * результата ввода, задает новый текст.
     * Вводит кнопку в неактивное состояние.
     * Реагирует, только в активном состоянии.
     */
    private class KeyBindListener extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            if (!e.equals(null)&&active) {
                PropertyManager.Companion.set(key, String.valueOf(e.getKeyChar()));
                setNormalText();
                active=false;
            }
        }
    }
}
