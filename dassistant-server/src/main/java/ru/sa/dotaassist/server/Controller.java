package ru.sa.dotaassist.server;

import ru.sa.dotaassist.domain.ContainerJson;

import java.io.File;

public class Controller {

    public Controller() {
        if(!isDatabaseExists()){
            firstLoad();
        }
    }

    void firstLoad() {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            System.out.println("""
                    Stage: 1
                    Making Database.
                    """);
            databaseManager.makeDatabase();
            System.out.println("""
                    Stage: 2
                    Making UserList table in Database.
                    """);
            databaseManager.makeUserListSchema();
            System.out.println("""
                    Stage: 3
                    Making DateLog table in Database.
                    """);
            databaseManager.makeDateLogSchema();

            System.out.println("First boot completed correctly.\n");
        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    private boolean isDatabaseExists() {
        File file = new File(DatabaseManager.FILE_PATH);
        return file.exists();
    }

    static void insertDate(ContainerJson containerJson, DatabaseManager databaseManager) throws DbException {
        int userId;
        String uuid = containerJson.getUuid();
        if (databaseManager.isUuidExists(uuid)) {
            userId = databaseManager.getUserId(uuid);
        } else {
            userId = databaseManager.addNewUuidInDataBase(uuid);
        }

        databaseManager.insertInDateLog(containerJson.getSessions(), userId);
    }
}
