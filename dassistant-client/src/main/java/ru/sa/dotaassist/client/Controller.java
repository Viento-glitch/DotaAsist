package ru.sa.dotaassist.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import ru.sa.dotaassist.client.exceptions.DbException;
import ru.sa.dotaassist.client.exceptions.FirstLoadException;
import ru.sa.dotaassist.client.exceptions.StatisticException;
import ru.sa.dotaassist.domain.ContainerJson;
import ru.sa.dotaassist.domain.Session;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Controller {

    private View view;

    private DatabaseManager databaseManager;

    Controller() {
    }


    public void init(View view) {
        this.view = view;

        boolean isFirstLoad = !isDatabaseExists();
        databaseManager = new DatabaseManager();
        if (isFirstLoad) {

            try {
                databaseManager.firstLoad();
                view.warningMessage("\t\t\t\t\t\t\t\t\t\t Внимание!\nДля улучшения качества обслуживания \nотслеживается продолжительность запущенной программы");
                boolean autoUpdateEnabled = databaseManager.isAutoUpdateEnabled();
                view.setCheckBoxStartValue(autoUpdateEnabled);
            } catch (FirstLoadException | DbException e) {
                view.warningMessage("Ошибка инициализации БД! \n" +
                        "Свяжитесь с разработчиком \n" + e);
            }
        }
    }

    public boolean isDatabaseExists() {
        File file = new File(DatabaseManager.FILE_PATH);
        return file.exists();
    }

    public void saveDuration(Date startDate, Date endDate) {
        try {
            databaseManager.insertDate(startDate, endDate);
        } catch (DbException e) {
            e.printStackTrace();
            //todo
        }
    }

    public boolean logeListIsDelivered() {
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
        } catch (DbException | SQLException e) {
            // todo обработать ошибку
            e.printStackTrace();
        }
        return result;
    }

    public ContainerJson getJsonLoge() {
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

    String post(String url, String json) throws IOException, StatisticException {
        OkHttpClient client = new OkHttpClient();
        final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(json, mediaType); // new
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();

        if (responseBody == null) {
            throw new StatisticException("Can't get result from request: " + url);
        }
        return responseBody.string();
    }

    public void hasDelivered() {
        try {
            databaseManager.setDeliveredList();
        } catch (DbException e) {
            view.warningMessage("База данных не была обновлена,колонка :\n" +
                    "Свяжитесь с разработчиком \n" + DatabaseManager.USER_LOG_TABLE_IS_DELIVERED_SERVER + "\n" + e);
        }
    }

    public String postDateLoge(String url) {
        String result;
        try {
            ContainerJson containerJson = getJsonLoge();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(containerJson);
            String json = gson.toJson(containerJson);
            result = post(url + Runner.SEND_LOG, json);
            System.out.println("result post " + result);
            return result;
        } catch (IOException | StatisticException e) {
            view.warningMessage("Не удалось отправить DateLoge на сервер\n" +
                    "Свяжитесь с разработчиком \n" + e.getMessage());
            return null;
        }
    }
}

