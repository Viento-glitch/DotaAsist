package ru.sa.dotaassist.server;

import com.google.gson.Gson;
import ru.sa.dotaassist.domain.ContainerJson;
import ru.sa.dotaassist.domain.LogResponse;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.nio.file.Paths;


public class SparkServer {
    private static final String LAST_VERSION = "1.0-SNAPSHOT";
    private static final String keystoreFilePath = String.valueOf(Paths.get(System.getProperty("user.home")).resolve("my-release-key.keystore"));
    private static final String keystorePassword = "20e51105a926";
    private static final String truststoreFilePath = null;
    private static final String truststorePassword = null;


    static Gson gson = new Gson();

    public static void main(String[] args) {
        System.out.println("Starting server...");
        Controller controller = new Controller();
        controller.controlDatabase();

        Config config = new Config();

        Spark.secure(keystoreFilePath, keystorePassword, truststoreFilePath, truststorePassword);

        Spark.get("/sayhello", (request, response) -> "Hello buddy!!!");

        Spark.get("/lastversion", (request, response) -> LAST_VERSION);
        Spark.post("/sendLoge", SparkServer::handle, new JsonTransformer());

        System.out.println("Open : " + config.getHost() + ":" + config.getPort());
        System.out.println("Server started successfully.");
    }

    private static Object handle(Request request, Response response) {
        ContainerJson containerJson = gson.fromJson(request.body(), ContainerJson.class);
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            // разобраться с userId
            Controller.insertDate(containerJson, databaseManager);
        } catch (DbException e) {
            System.err.println("Can't insert in Date Log\n" + e);
        }
        return new LogResponse(1, "All ok");
    }


}
