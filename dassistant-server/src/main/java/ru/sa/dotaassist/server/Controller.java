package ru.sa.dotaassist.server;

import java.io.File;

public class Controller {

    public Controller() {
        if(isFirstLoad()){
            firstBoot();
        }
    }

    public boolean isFirstLoad(){
        return !isDatabaseExists();
    }
    void firstBoot() {
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
}
