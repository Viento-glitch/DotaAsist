package ru.sa.dotaassist.client;

import okhttp3.*;
import ru.sa.dotaassist.domain.ContainerJson;
import ru.sa.dotaassist.domain.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Controller {

    public static void main(String[] args) {
        Controller controller = new Controller();
        try {
            System.out.println(controller.getJsonLoge());

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    Controller(View view, DatabaseManager databaseManager) {
        if (isFirstLoad(databaseManager)) {
            databaseManager = new DatabaseManager();
            try {
                databaseManager.firstLoad();
                view.warningMessage();

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            databaseManager.openConnection();


            //            String currentVersion = databaseManager.getLastVersionInDataBase();

//            if (currentVersion.equals(lastVersion)) {
//                System.out.println("latest version");
//            } else {
//                System.out.println("not the latest version");
//
//                todo реализовать
//                if (isAutoUpdateTrue()) {
//
//                } else {
//
//                }
//            }
//            System.out.println(databaseManager.isLastVersion());


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        view.setCheckBoxStartValue(databaseManager.getUpdateBooleanFromDB());
    }

    public Controller() {

    }

    public boolean isFirstLoad(DatabaseManager databaseManager) {
        databaseManager = new DatabaseManager();
        try {
            return !databaseManager.isDatabaseExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isAutoUpdateTrue() {
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            databaseManager.openConnection();
            return databaseManager.getUpdateBooleanFromDB();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        return false;
    }

    public void saveDuration(Date startDate, Date endDate) {
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            databaseManager.openConnection();
            databaseManager.insertDate(startDate, endDate);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public boolean logeListIsDelivered() {
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            databaseManager.openConnection();

            int count = 0;
            count = databaseManager.getMaxIDOfDateLoge();
            if (count != 0) {
                for (int id = 1; id <= count; id++) {
                    if (!databaseManager.isDelivered(id)) {
                        return false;
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        return true;
    }

    public List<Integer> getListOfUndelivered() throws SQLException, ClassNotFoundException {
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            databaseManager.openConnection();

            int count = databaseManager.getMaxIDOfDateLoge();

            List<Integer> undeliveredIDList = new ArrayList<>();
            for (int id = 1; id <= count; id++) {
                if (!databaseManager.isDelivered(id)) {
                    undeliveredIDList.add(id);
                }
            }
            if (!undeliveredIDList.isEmpty()) {
                return undeliveredIDList;
            } else {
                System.out.println("undeliveredList is clear.");
                return null;
            }

//
            //todo отправить json с старт датой и end датой
        } finally {
            databaseManager.closeConnection();
        }
    }

    public ContainerJson getJsonLoge() throws SQLException, ClassNotFoundException {
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            databaseManager.openConnection();

            List<Integer> undeliveredIDList = getListOfUndelivered();

            String uuid = databaseManager.getUuid();
            List<Session> sessionList = new ArrayList<>();
            ContainerJson containerJson = null;
            for (int undeliveredID : undeliveredIDList) {
                String startDate = databaseManager.getDateLoge(databaseManager.USER_LOG_COLUMN_START, undeliveredID);
                String endDate = databaseManager.getDateLoge(databaseManager.USER_LOG_COLUMN_END, undeliveredID);
                sessionList.add(new Session(startDate, endDate));

                containerJson = new ContainerJson(uuid, sessionList);
            }

            return containerJson;
        } finally {
            databaseManager.closeConnection();
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
            return response.code() == 200; //todo найти нормальную константу
        } catch (IOException e) {
            return false;
        }
    }

//    public boolean sendJson(String jsonLoge) {
//        MediaType JSON = MediaType.parse(jsonLoge);
//
//
//
//
//
//        //        Runner runner = new Runner();
////        OkHttpClient client = new OkHttpClient.Builder()
////                .readTimeout(10,TimeUnit.SECONDS)
////                .retryOnConnectionFailure(true)
////                .connectTimeout(5,TimeUnit.SECONDS)
////                .connectionPool(new ConnectionPool(10,5,TimeUnit.SECONDS))
////                .build();
////
////        Request request = new Request.Builder()
////                .url(runner.URL)
////                .build();
////
////        try () {
////
////        }
////        return false;
//    }


    String post(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(json, mediaType); // new
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}

