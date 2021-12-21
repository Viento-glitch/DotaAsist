//package ru.sa.dotaassist.client;
//
//import okhttp3.*;
//
//import java.io.IOException;
//import java.util.UUID;
//
//public class OkHttpExample1 {
//
//    private final OkHttpClient httpClient = new OkHttpClient();
//
//    public static void main(String[] args) throws IOException {
//        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
//        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
//        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
//        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dumpTreshold", "999999");
//        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
//
//        OkHttpExample1 obj = new OkHttpExample1();
//
//
//        UUID uuid = UUID.randomUUID();
//
////        obj.sendPOST(String.valueOf(uuid));
//        obj.sendGETSync();
//    }
//
//    private void sendPOST(String uuid) throws IOException {
//        // form parameters
//        RequestBody formBody = new FormBody.Builder()
//                .add("UUID", uuid)
//                .build();
//
//        Request request = new Request.Builder()
//                .url("http://httpbin.org/post")
//                .addHeader("User-Agent", "OkHttp Bot")
//                .post(formBody)
//                .build();
//
//        try (Response response = httpClient.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("Unexpected code " + response);
//            }
//
//            // Get response body
//            System.out.println(response.body().string());
//        }
//    }
//
//    private void sendGETSync() throws IOException {
//        Request request = new Request.Builder()
//                .url("https://httpbin.org/get")
//                .addHeader("custom-key", "mkyong")  // add request headers
//                .addHeader("User-Agent", "OkHttp Bot")
//                .build();
//
//        try (Response response = httpClient.newCall(request).execute()) {
//
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//            //Get response headers
//            Headers responseHeaders = response.headers();
//            for (int i = 0; i < responseHeaders.size(); i++) {
//                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//            }
//
//            //Get response body
//            System.out.println(response.body());
//        }
//    }
//}
