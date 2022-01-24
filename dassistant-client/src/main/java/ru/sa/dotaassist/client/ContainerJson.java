package ru.sa.dotaassist.client;

import java.util.List;

public class ContainerJson {
    private String uuid;
    private List<Session> sessions;

    public ContainerJson(String uuid, List<Session> sessions) {
        this.uuid = uuid;
        this.sessions = sessions;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
    
}
