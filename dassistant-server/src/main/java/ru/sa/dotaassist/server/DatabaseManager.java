package ru.sa.dotaassist.server;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.sa.dotaassist.domain.Session;

import java.nio.file.Paths;
import java.sql.*;
import java.util.List;

public class DatabaseManager {

    public static final String FILE_PATH = String.valueOf(Paths.get(System.getProperty("user.home")).resolve("LocalDatabase").resolve("Server_logs.db"));

    public static final String SERVER_DATE_LOG_TABLE_NAME = "date_log";
    public static final String SERVER_DATE_LOG_COLUMN_ID = "id";
    public static final String SERVER_DATE_LOG_COLUMN_DATETIME = "date_time";
    public static final String SERVER_DATE_LOG_COLUMN_START = "start_date";
    public static final String SERVER_DATE_LOG_COLUMN_END = "end_date";

    public static final String SERVER_USER_LIST_TABLE_NAME = "user_list";
    public static final String SERVER_USER_COLUMN_ID = "user_id";
    public static final String SERVER_USER_LIST_COLUMN_NICK = "user_nickname";
    public static final String SERVER_USER_LIST_COLUMN_UNIQUE_ID = "uniqueID";

    private String lastVersionOnDatabase = null;
    final HikariDataSource dataSource;

    public DatabaseManager() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getConnectionUrl());

        dataSource = new HikariDataSource(config);
    }

    void makeUserListSchema() throws DbException {
        String query = "CREATE TABLE " + SERVER_USER_LIST_TABLE_NAME + "(\n" +
                SERVER_USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                SERVER_USER_LIST_COLUMN_NICK + " VARCHAR(50) , \n" +
                SERVER_USER_LIST_COLUMN_UNIQUE_ID + " VARCHAR(150) NOT NULL); ";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("UserList table has been created.\n" +
                    "Name of UserList table: " + SERVER_USER_LIST_TABLE_NAME + "\n" +
                    "schema:\n" + query + "\n");
        } catch (SQLException e) {
            throw new DbException("Can't make UserList with this query :" +
                    "(" + query + ")", e);
        }
    }

    private String getConnectionUrl() {
        return "jdbc:sqlite:\\" + FILE_PATH;
    }

    void makeDatabase() throws DbException {
        String url = getConnectionUrl();
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created, path: " + url + "\n");
            }
        } catch (SQLException e) {
            throw new DbException("Can't make new Database.", e);
        }
    }

    void makeDateLogSchema() throws DbException {
        String query = "CREATE TABLE " + SERVER_DATE_LOG_TABLE_NAME + " (\n" +
                SERVER_DATE_LOG_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n" +
                SERVER_USER_COLUMN_ID + " INTEGER NOT NULL, \n" +
                SERVER_DATE_LOG_COLUMN_DATETIME + " DATETIME DEFAULT CURRENT_TIMESTAMP, \n" +
                SERVER_DATE_LOG_COLUMN_START + " VARCHAR(50) NOT NULL, \n" +
                SERVER_DATE_LOG_COLUMN_END + " VARCHAR(50) NOT NULL, \n" +
                "FOREIGN KEY(" + SERVER_USER_COLUMN_ID + ") REFERENCES " + SERVER_USER_LIST_TABLE_NAME + "(" + SERVER_USER_COLUMN_ID + ")); ";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            System.out.println("DateLog table is created.\n" +
                    "name of DateLog table: " + SERVER_DATE_LOG_TABLE_NAME + "\n" +
                    "schema:\n" + query + "\n");

        } catch (SQLException e) {
            throw new DbException("Can't make Date log schema with this query: \n" +
                    query + "\n", e);
        }

    }

    void insertInDateLog(List<Session> sessions, long userId) throws DbException {
        String query = "INSERT INTO " + SERVER_DATE_LOG_TABLE_NAME + " \n" +
                "(" + SERVER_USER_COLUMN_ID + ", " +
                SERVER_DATE_LOG_COLUMN_START + ", " +
                SERVER_DATE_LOG_COLUMN_END + ") \n" +
                "VALUES(?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            int count = 0;
            for (Session session : sessions) {
                statement.setLong(1, userId);
                statement.setString(2, session.getStartDate());
                statement.setString(3, session.getEndDate());
                statement.addBatch();
                count++;
                if (count % 100 == 0 || count == sessions.size()) {
                    statement.executeBatch();
                }
            }
            System.out.println("Rows added: " + count + "\n" +
                    "uniqueID:"+getUuid(userId));
        } catch (SQLException e) {
            throw new DbException("Can't insert date with userId:" + userId + "\n", e);
        }
    }

    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            System.out.println(databaseManager.getUuid(1));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    String getUuid(long userId) throws DbException {
        String query = "SELECT " + SERVER_USER_LIST_COLUMN_UNIQUE_ID + " FROM "+SERVER_USER_LIST_TABLE_NAME+ " WHERE " + SERVER_USER_COLUMN_ID + " = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(SERVER_USER_LIST_COLUMN_UNIQUE_ID);
            } else {
                throw new DbException("Not found userId:" + userId);
            }
        } catch (SQLException e) {
            throw new DbException("Can't take uniqueID from Database", e);
        }
    }


    Integer getUserId(String uniqueID) throws DbException {
        String query = "SELECT " + SERVER_USER_COLUMN_ID + " FROM " + SERVER_USER_LIST_TABLE_NAME + " WHERE " + SERVER_USER_LIST_COLUMN_UNIQUE_ID + " = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uniqueID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(SERVER_USER_COLUMN_ID);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DbException("Can't get user id from " + SERVER_USER_LIST_TABLE_NAME, e);
        }
    }

    boolean isUuidExists(String uniqueID) throws DbException {
        String query = "SELECT " + SERVER_USER_COLUMN_ID + " FROM " + SERVER_USER_LIST_TABLE_NAME + " WHERE " + SERVER_USER_LIST_COLUMN_UNIQUE_ID + " = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uniqueID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new DbException("Can't check availability uniqueID in " + SERVER_USER_LIST_TABLE_NAME, e);
        }
    }

    Integer addNewUuidInDataBase(String uniqueID) throws DbException {
        String query = "INSERT INTO " + SERVER_USER_LIST_TABLE_NAME + "(" + SERVER_USER_LIST_COLUMN_UNIQUE_ID + ") VALUES(?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uniqueID);
            statement.executeUpdate();
            return getUserId(uniqueID);
        } catch (SQLException e) {
            throw new DbException("Can't add new UUID in Database\n" +
                    "UUID:" + uniqueID, e);
        }
    }
}
