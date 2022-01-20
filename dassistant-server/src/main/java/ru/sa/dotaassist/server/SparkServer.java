package ru.sa.dotaassist.server;

import spark.Spark;

public class SparkServer {

    public static void main(String[] args) {
        System.out.println("Starting server...");
        Config config = new Config();


        Spark.get("/sayhello", (request, response) -> {
            return "Hello buddy!!!";
            // uuid; event_type; event_datetime; received_datetime; domain;
        });

        Spark.get("/lastversion", (request, response) -> "1.0-SNAPSHOT");

        System.out.println("Open : " + config.getHost() + ":" + config.getPort());
        System.out.println("Server started successfully.");
    }
}
