package ru.sa.dotaassist.server;

import com.google.gson.Gson;
import ru.sa.dotaassist.domain.ContainerJson;
import ru.sa.dotaassist.domain.LogResponse;
import spark.Request;
import spark.Response;
import spark.Spark;


public class SparkServer {

    static Gson gson = new Gson();

    public static void main(String[] args) {
        System.out.println("Starting server...");
        //не убирать, в конструкторе проверка на первый запуск
        Controller controller = new Controller();

        Config config = new Config();

        Spark.get("/sayhello", (request, response) -> "Hello buddy!!!");

        Spark.get("/lastversion", (request, response) -> "1.0-SNAPSHOT");
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
