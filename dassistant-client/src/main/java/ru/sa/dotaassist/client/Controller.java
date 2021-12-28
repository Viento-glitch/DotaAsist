package ru.sa.dotaassist.client;

import java.sql.SQLException;
import java.util.Date;

public class Controller {
    Controller(View view, DatabaseManager databaseManager) {
        if (isFirstLoad(databaseManager)) {
            databaseManager = new DatabaseManager();
            try {
                databaseManager.firstLoad();
                view.warningMessage();

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            databaseManager.openConnection();
            System.out.println(databaseManager.getLastVersionInDataBase());

//            System.out.println(databaseManager.isLastVersion());

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        view.setCheckBoxStartValue(databaseManager.getUpdateBooleanFromDB());
    }

    public Controller() {

    }

    public boolean isFirstLoad(DatabaseManager databaseManager) {
        databaseManager = new DatabaseManager();
        try {
            return !databaseManager.isDatabaseExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isAutoUpdateTrue() {
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            databaseManager.openConnection();
            return databaseManager.getUpdateBooleanFromDB();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        return false;
    }

    public void saveDuration(Date startDate, Date endDate) {
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            databaseManager.openConnection();
            databaseManager.insertDate(startDate, endDate);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }
}

