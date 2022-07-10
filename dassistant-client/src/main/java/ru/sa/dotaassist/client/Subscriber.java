package ru.sa.dotaassist.client;

import java.lang.annotation.*;

@Target(ElementType.LOCAL_VARIABLE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * Используй данный класс для взаимодействия с настройками.
 * Загружает по ключу значение из списка настроек.
 * Автоматически обновляет переменную с изменением настройки.
 * Чтобы аннотация работала, следует подписать класс у PropertyManager.
 */
public @interface Subscriber {
    String key();
}
