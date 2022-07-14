package ru.sa.dotaassist.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import ru.sa.dotaassist.client.exceptions.DbException;
import ru.sa.dotaassist.client.exceptions.FirstLoadException;
import ru.sa.dotaassist.client.exceptions.StatisticException;
import ru.sa.dotaassist.domain.ContainerJson;
import ru.sa.dotaassist.domain.Session;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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

        if (!isPackageExists()) {
            makePackage();
        }
        boolean isFirstLoad = !isDatabaseExists();
        databaseManager = new DatabaseManager();
        if (isFirstLoad) {
            try {
                view.warningMessage("\t\t\t\t\t\t\t\t\t\t Внимание!\nДля улучшения качества обслуживания \nотслеживается продолжительность запущенной программы");
                databaseManager.firstLoad();
            } catch (FirstLoadException e) {
                view.warningMessage("Ошибка инициализации БД! \n" +
                        "Свяжитесь с разработчиком \n" + e);
            }
        }
        try {
            view.setCheckBoxStartValue(databaseManager.isAutoUpdateEnabled());
//            view.setUseSmilesCheckBox(databaseManager.isSmilesEnabled());
        } catch (DbException e) {
            throw new RuntimeException(e);
        }

    }

    private void makePackage() {
        String path = String.valueOf(Paths.get(System.getProperty("user.home")).resolve("LocalDatabase"));
        boolean result = new File(path).mkdir();
        if (result) {
            System.out.println("Package has been created \n" +
                    "path:" + path);
        }
    }

    public boolean isPackageExists() {
        return new File(String.valueOf(Paths.get(System.getProperty("user.home")).resolve("LocalDatabase"))).isDirectory();
    }

    public boolean isDatabaseExists() {
        File file = new File(DatabaseManager.FILE_PATH);
        return file.exists();
    }

    public void saveDuration(Date startDate, Date endDate) {
        try {
            databaseManager.insertDate(startDate, endDate);
        } catch (DbException e) {
            view.warningMessage("Ошибка инициализации лога\n" +
                    "Свяжитесь с разработчиком \n" + e);
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
            view.warningMessage("Ошибка в удостоверении доставки лога\n" +
                    "Свяжитесь с разработчиком \n" + e);
        }
        return result;
    }

    public ContainerJson getJsonLoge() {
        try {
            databaseManager.dataSource.getConnection();
            List<Integer> undeliveredIDList = databaseManager.getListOfUndeliveredID();

            String uniqueID = databaseManager.getUniqueID();
            List<Session> sessionList = new ArrayList<>();
            ContainerJson containerJson = null;
            for (int undeliveredID : undeliveredIDList) {
                String startDate = databaseManager.getDateLoge(DatabaseManager.USER_LOG_COLUMN_START, undeliveredID);
                String endDate = databaseManager.getDateLoge(DatabaseManager.USER_LOG_COLUMN_END, undeliveredID);
                sessionList.add(new Session(startDate, endDate));
                containerJson = new ContainerJson(uniqueID, sessionList);
            }
            return containerJson;
        } catch (Exception e) {
            view.warningMessage("Неудалось поллучить JsonLog,\n" +
                    "Свяжитесь с разработчиком");
            return null;
        }
    }

    //! bad practice!!
    private static final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    //document is not need now
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    //document is not need now
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };

    private static final SSLContext trustAllSslContext;

    static {
        try {
            trustAllSslContext = SSLContext.getInstance("SSL");
            trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    //! bad practice!!
    private static final SSLSocketFactory trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();

    //! bad practice!!
    public static OkHttpClient trustAllSslClient(OkHttpClient client) {
        //Using the trustAllSslClient is highly discouraged and should not be used in Production!
        OkHttpClient.Builder builder = client.newBuilder();
        builder.sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        builder.hostnameVerifier((hostname, session) -> true);
        return builder.build();
    }

    public boolean serverIsOnline(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();

        OkHttpClient trustClient = trustAllSslClient(client);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = trustClient.newCall(request).execute();
            System.out.println("Сервер доступен.");
            return response.isSuccessful();
        } catch (IOException e) {
            System.out.println("Сервер не доступен.");
            return false;
        }
    }

    String post(String url, String json) throws IOException, StatisticException {
        OkHttpClient client = new OkHttpClient();
        OkHttpClient trustClient = trustAllSslClient(client);
        final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(json, mediaType); // new
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = trustClient.newCall(request).execute();
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
            System.out.println(json);
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

