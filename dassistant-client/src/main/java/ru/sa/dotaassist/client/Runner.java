package ru.sa.dotaassist.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Runner {
    public static final String LAST_VERSION = "/lastversion";
    public static final String SEND_LOG = "/sendLoge";
    public static final String URL = "https://localhost:3301";

    public static void main(String[] args) {
        /* ?баг репорт добавлять в отдельную колонку базы данных. */
        Controller controller = new Controller();
        View view = new View(controller);
        controller.init(view);
        view.init();
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                PropertyManager.Companion.save();
            }
        });

        if (controller.logeListIsDelivered()) {
            if (controller.serverIsOnline(URL + LAST_VERSION)) {
                String result = controller.postDateLoge(URL);
                if (result.equals("{\"code\":1,\"message\":\"All ok\"}")) {
                    controller.hasDelivered();
                }
            }
        }
        view.setVisible(true);
    }
}
