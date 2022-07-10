package ru.sa.dotaassist.client;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Данный класс следует использовать в меню настроек.
 * Автоматически синхронизирует изменения по всему приложению.
 */
public class SettingBox extends JCheckBox {
    /**
     *
     * @param key это ключ настройки, существующие см в PropertiesManager.
     *            Изменения будут синхронизироваться с указанным ключом
     * @param text Отображаемая надпись рядом с чек боксом, подпись, надпись.
     */
    public SettingBox(String key, String text) {
        setSelected(Boolean.valueOf(PropertyManager.Companion.get(key)));
        addChangeListener(changeEvent -> {
            PropertyManager.Companion.set(key, String.valueOf(isSelected()));
        });
        setText(text);
    }
}
