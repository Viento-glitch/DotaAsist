package ru.sa.dotaassist.client;

public class Runner {

    public static final String LASTVERSION = "/lastversion";
    public static final String URL = "http://localhost:3301";

    public static void main(String[] args) {
        /* ?баг репорт добавлять в отдельную колонку базы данных. */

        View view = new View();
        Controller controller = new Controller(view);

        if (controller.logeListIsDelivered()) {
            if (controller.serverIsOnline(URL + LASTVERSION)) {
                System.out.println("Сервер онлайн");
                String result = controller.sendDateLoge(URL);
                if (result.equals("{\"code\":1,\"message\":\"All ok\"}")) {
                    controller.hasDelivered();
                }
            } else {
                System.out.println("Сервер оффлайн");
            }
        }
        view.setVisible(true);

    }
}
