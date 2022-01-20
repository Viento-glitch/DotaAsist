package ru.sa.dotaassist.client;

import java.util.List;

public class ContainerJson {
    String uuid;
    List<Session> sessions;

    public ContainerJson(String uuid, List<Session> sessions) {
        this.uuid = uuid;
        this.sessions = sessions;
    }
}
