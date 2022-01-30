package ru.sa.dotaassist.client;

public class Runner {
    public static final boolean ISDEVELOPING = true;
    public static final String LAST_VERSION = "/lastversion";
    public static final String SEND_LOG = "/sendLoge";
    public static final String URL = "http://localhost:3301";

    public static void main(String[] args) {
        /* ?баг репорт добавлять в отдельную колонку базы данных. */
        Controller controller = new Controller();
        View view = new View();

        controller.init(view);
        view.init(controller);

        if (controller.logeListIsDelivered()) {
            if (controller.serverIsOnline(URL + LAST_VERSION)) {
                System.out.println("Сервер онлайн");
                String result = controller.postDateLoge(URL);
                if (result.equals("{\"code\":1,\"message\":\"All ok\"}")) {
                    controller.hasDelivered();
                }
            } else {
                System.err.println("Сервер оффлайн");
            }
        }
        view.setVisible(true);
    }
}
