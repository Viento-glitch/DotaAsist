package ru.sa.dotaassist.server;

import com.google.gson.Gson;
import ru.sa.dotaassist.domain.ContainerJson;
import ru.sa.dotaassist.domain.LogResponse;
import ru.sa.dotaassist.domain.Session;
import spark.Spark;

public class SparkServer {

    static Gson gson = new Gson();

    public static void main(String[] args) {
        System.out.println("Starting server...");
        Config config = new Config();


        Spark.get("/sayhello", (request, response) -> {
            return "Hello buddy!!!";
            // uuid; event_type; event_datetime; received_datetime; domain;
        });


        Spark.get("/lastversion", (request, response) -> "1.0-SNAPSHOT");
        Spark.post("/sendLoge", (request, response) -> {
            ContainerJson containerJson = gson.fromJson(request.body(), ContainerJson.class);

            for (Session session : containerJson.getSessions()) {
                System.out.println(containerJson.getUuid() + ": " + session);
            }

            return new LogResponse(1, "All ok");
        }, new JsonTransformer());

        //localhost:9090/the/path/to/endpoint?var1=value1&var2=value2

        System.out.println("Open : " + config.getHost() + ":" + config.getPort());
        System.out.println("Server started successfully.");
    }
}
