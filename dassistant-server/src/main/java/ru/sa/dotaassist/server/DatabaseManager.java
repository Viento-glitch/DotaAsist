package ru.sa.dotaassist.server;

import java.io.File;
import java.sql.*;

public class DatabaseManager {

    private final String FILE_PATH = "C:\\SQLite\\Server_logs.db";

    public final String SERVER_DATE_LOG_TABLE_NAME = "date_log";
    public final String SERVER_DATE_LOG_TABLE_ID = "id";
    public final String SERVER_DATE_LOG_COLUMN_UUID = "uuid";
    public final String SERVER_DATE_LOG_COLUMN_START = "start_date";
    public final String SERVER_DATE_LOG_COLUMN_END = "end_date";

    private Connection connection;
    private String lastVersionOnDatabase = null;

    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            if(!databaseManager.isDatabaseExists()){
                databaseManager.firstLoad();
            }
            databaseManager.openConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    void firstLoad() throws SQLException, ClassNotFoundException {
        this.makeDatabase();
        this.openConnection();
        makeDateLogSchema();


        this.connection.close();
    }

//    void makeSchema() {
//        tableName
//        /** id
//         *  uuid user
//         *  startDate
//         *  endDate
//         *
//         */
//    }


    private String getConnectionUrl() {
        return "jdbc:sqlite:\\" + FILE_PATH;
    }

    public void openConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + FILE_PATH);
        //todo make worked with getConnectionUrl();
        if (connection != null) {
            System.out.println("Connected");
            this.connection = connection;
        }
    }

    public boolean isDatabaseExists() throws SQLException {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void makeDatabase() throws SQLException {
        String url = getConnectionUrl();
        try (Connection connection = DriverManager.getConnection(url)) {
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created, path: " + url);
            }
        }
    }

    private void makeDateLogSchema() {
        final String query = "CREATE TABLE " + SERVER_DATE_LOG_TABLE_NAME + " (\n" +
                SERVER_DATE_LOG_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                SERVER_DATE_LOG_COLUMN_UUID + " VARCHAR(36) NOT NULL, \n" +
                SERVER_DATE_LOG_COLUMN_START + " VARCHAR(50) NOT NULL, \n" +
                SERVER_DATE_LOG_COLUMN_END + " VARCHAR(50) NOT NULL); ";
        System.out.println(query);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean insertDatа(String uuid, String startDate, String endDate) {
        String query = "INSERT INTO " + SERVER_DATE_LOG_TABLE_NAME + " \n('" +
                SERVER_DATE_LOG_COLUMN_UUID + "' , '" +
                SERVER_DATE_LOG_COLUMN_START + "' , '" +
                SERVER_DATE_LOG_COLUMN_END + "') \n" +
                "VALUES('" +
                uuid + "' , '" +
                startDate + "' , ' " +
                endDate +
                "');";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
