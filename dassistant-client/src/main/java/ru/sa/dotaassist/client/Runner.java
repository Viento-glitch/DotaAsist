package ru.sa.dotaassist.client;

import java.util.Date;

public class Runner {
    public static Runner run;
    static Date startDate;

    public static void main(String[] args) {
/**
 * todo
 *  запоминает время входа в приложение(запуск приложения)
 *  запоминает время выхода из приложения(закрытие приложения)
 *  вычисляет время сеанса: запуск приложения - выход из приложения
 *  <p>
 *
 * todo: создать кнопку баг репорта;
 *?баг репорт добавлять в отдельную колонку базы данных.
 *?найти способ создавать дочерние окна полноценные.
 */

        startDate = new Date();

        View view = new View();
        view.setVisible(true);

    }
}
