package ru.sa.dotaassist.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.sa.dotaassist.domain.ContainerJson;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class Runner {
    /**
     * !Клиентская часть
     * ?Багрепорт
     * 1 время отправки багрепорта
     * 2 юид
     * 3 текст багрепорта
     * <p>
     * ?VersionControl
     * ?провепка наличия лога
     * !      *присутствие лога
     * !          ? сравнение версии приложения с сохранённой последней версией в логе обновлений
     * * при соответствии версии приложения с сохранённой последней версией в логе обновлений
     * ? попытка запроса на сервер
     * * случае удачного подключения к серверу
     * запросить цифру последнего обновления
     * ? сравнение последней версии в логе обновлений с цифрой последнего обновления полученной с сервера
     * * в случае соответствия версии пользователя версии полученной с сервера
     * продолжить стандартное выполнение программы
     * <p>
     * ! в случае не соответствия версии пользователя версии полученной с сервера
     * изменить лог апдейтов пользователя
     * ? создание нового лога апдейтов
     * ? перебор версий отсортированному по id
     * ? при нахождении соответствия версии приложения и выбранного id
     * запомнить id
     * если версия последняя
     * * перенести полученные изменения в лог апдейтов
     * сохранить последнюю версию обнаруженную
     * в лог апдейтов.
     * <p>
     * ! если версия не последняя
     * ? перебрать все последующие версии
     * * добавить к результату версию обновления
     * * добавить текст обновления
     * * добавить разделитель между обновлениями
     * <p>
     * !в случае не удачного подключения к серверу
     * продолжить стандартное выполнение программы
     * ! при несоответствии версии приложения с сохранённой последней версией в логе обновлений
     * ------------? попытка запроса на сервер
     * ------------*в случае удачного подключения к серверу
     * ? проверить наличие галочки в чекбоксе "Обновлять без лишних вопросов"
     * * при согласии на обновления без лишних вопросов
     * todo реаилизация обновления путём скачивания файлов с github оповестить о
     * окончании скачивания
     * перезапустить приложение
     * показать нововведения
     * ! при не согласии на обновления без лишних вопросов
     * оповестить пользователя о возможности обновится
     * изменить структуру приложения добавив в окно UpdateNews кнопку обновится
     * ? при нажатии на клавишу обновится
     * *todo реаилизация обновления путём скачивания файлов с github
     * оповестить о окончании скачивания
     * перезапустить приложение
     * !отсутствие лога
     * создание лога с текущей версией приложения
     * ? попытка запроса последней версии с сервера
     */

    public static final String LASTVERSION = "/lastversion";
    public static final String URL = "http://localhost:3301";

    public static void main(String[] args) throws IOException {
        /* ?баг репорт добавлять в отдельную колонку базы данных. */
//        takeCodeConnection();

        View view = new View();
        DatabaseManager databaseManager = new DatabaseManager();
//        String versionOnServer = run(URL);
        Controller controller = new Controller(view, databaseManager);

        if (!controller.logeListIsDelivered()) {
            if (controller.serverIsOnline(URL + LASTVERSION)) {
                System.out.println("Сервер онлайн");
                try {
                    // headers
                    // -- Content-Type..
                    // body
                    // -- file
                    // -- text

                    ContainerJson containerJson = controller.getJsonLoge();
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(containerJson);
                    String result = controller.post(URL + "/sendLoge", json);
                    System.out.println("result post "+result);
                    if (result.equals("{\"code\":1,\"message\":\"All ok\"}")){
                        databaseManager.setDeliveredList();
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Сервер оффлайн");
            }
        }
        view.setVisible(true);

        // https://square.github.io/okhttp/
//        System.out.println(versionOnServer);
    }

//    static boolean takeCodeConnection() {
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url(URL)
//                .build();
//
//        try (Response httpResponse = client.) {
//
//            int code = httpResponse.code();
//            System.out.println(code);
////            if (code == 200){}
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }


    static String run(String url) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .connectTimeout(5, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(10, 5, TimeUnit.SECONDS))
                .build();

        if (client.retryOnConnectionFailure()) {
            System.out.println("запущено без сервера");
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response httpResponse = client.newCall(request).execute()) {


            int code = httpResponse.code();
            System.out.println("code: " + code);
            if (code == 200) {
                Controller controller = new Controller();

                if (!controller.logeListIsDelivered()) {

                    controller.getJsonLoge();// todo
                    //todo отправить jsonLoge;

                }

                return httpResponse.body().string();
            } else {
                return null;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //! todo исправить

        return null;
    }
}
