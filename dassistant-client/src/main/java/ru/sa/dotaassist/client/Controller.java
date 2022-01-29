package ru.sa.dotaassist.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import ru.sa.dotaassist.domain.ContainerJson;
import ru.sa.dotaassist.domain.Session;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Controller {


    public Controller() {
    }

    Controller(View view) {
        if (isFirstLoad()) {
            DatabaseManager databaseManager = new DatabaseManager();
            try {
                databaseManager.firstLoad();
                view.warningMessage("                                         Внимание!\nДля улучшения качества обслуживания \nотслеживается продолжительность запущенной программы");


                boolean autoUpdateEnabled = databaseManager.isAutoUpdateEnabled();
                view.setCheckBoxStartValue(autoUpdateEnabled);
            } catch (FirstLoadException | DbException e) {
                view.warningMessage("Ошибка инициализации БД! \n" +
                        "Свяжитесь с разработчиком \n" + e);
            }
        }
    }

    public boolean isFirstLoad() {
        return !isDatabaseExists();
    }

    public boolean isDatabaseExists() {
        File file = new File(DatabaseManager.FILE_PATH);
        return file.exists();
    }

    public void saveDuration(Date startDate, Date endDate) {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.insertDate(startDate, endDate);
    }

    public boolean logeListIsDelivered() {
        DatabaseManager databaseManager = new DatabaseManager();
        boolean result = false;
        try {

            int count = databaseManager.getMaxIDOfDateLoge();
            if (count != 0) {
                for (int id = 1; id <= count; id++) {
                    if (!databaseManager.isDelivered(id)) {
                        result = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ContainerJson getJsonLoge() {
        DatabaseManager databaseManager = new DatabaseManager();

        try {
            databaseManager.dataSource.getConnection();
            List<Integer> undeliveredIDList = databaseManager.getListOfUndeliveredID();

            String uuid = databaseManager.getUuid();
            List<Session> sessionList = new ArrayList<>();
            ContainerJson containerJson = null;
            for (int undeliveredID : undeliveredIDList) {
                String startDate = databaseManager.getDateLoge(DatabaseManager.USER_LOG_COLUMN_START, undeliveredID);
                String endDate = databaseManager.getDateLoge(DatabaseManager.USER_LOG_COLUMN_END, undeliveredID);
                sessionList.add(new Session(startDate, endDate));
                containerJson = new ContainerJson(uuid, sessionList);
            }
            return containerJson;
        } catch (Exception e) {
            View view = new View();
            view.warningMessage("Неудалось поллучить JsonLoge,\n" +
                    "Свяжитесь с разработчиком");
            return null;
        }
    }

    public boolean serverIsOnline(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        }
    }

    String post(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(json, mediaType); // new
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }


    public void hasDelivered() {
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            databaseManager.setDeliveredList();
        } catch (DbException e) {
            View view = new View();
            view.warningMessage("База данных не была обновлена,колонка :\n" +
                    "Свяжитесь с разработчиком \n" + DatabaseManager.USER_LOG_TABLE_IS_DELIVERED_SERVER + "\n" + e);
        }
    }

    public String sendDateLoge(String url) {
        String result;
        try {
            ContainerJson containerJson = getJsonLoge();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(containerJson);
            String json = gson.toJson(containerJson);
            result = post(url + "/sendLoge", json);
            System.out.println("result post " + result);
            return result;
        } catch (IOException e) {
            View view = new View();
            view.warningMessage("Не удалось отправить DateLoge на сервер\n" +
                    "Свяжитесь с разработчиком \n" + e);
            return null;
        }
    }
}

