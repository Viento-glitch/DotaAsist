package ru.sa.dotaassist.server;

import ru.sa.dotaassist.domain.ContainerJson;

import java.io.File;
import java.nio.file.Paths;

public class Controller {

    public Controller() {
    }

    void controlDatabase() {
        if(isPackageExists()){
            makePackage();
        }

        if(!isDatabaseExists()){
            firstLoad();
        }
    }

    private void makePackage() {
        String path = String.valueOf(Paths.get(System.getProperty("user.home")).resolve("LocalDatabase"));
        boolean result = new File(path).mkdir();
        if (result) {
            System.out.println("Package has been created \n" +
                    "path:" + path);
        }
    }

    public boolean isPackageExists() {
        return new File(String.valueOf(Paths.get(System.getProperty("user.home")).resolve("LocalDatabase"))).isDirectory();
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
