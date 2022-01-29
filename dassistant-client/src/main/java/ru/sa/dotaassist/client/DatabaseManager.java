package ru.sa.dotaassist.client;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {
    public static final String FILE_PATH = "C:\\SQLite\\logs.db";

    public static final String USER_INFO_TABLE_NAME = "user_info";

    public static final String USER_UPDATE_LOG_TABLE_NAME = "user_update_log";

    public static final String USER_CHECKBOX_TABLE_NAME = "update_checkbox";
    public static final String COLUMN_UPDATE_NAME = "update_boolean";

    public static final String USER_LOG_TABLE_NAME = "date_log";
    public static final String USER_LOG_TABLE_ID = "id";
    public static final String USER_LOG_TABLE_IS_DELIVERED_SERVER = "given_to_server";
    public static final String USER_LOG_COLUMN_START = "start_date";
    public static final String USER_LOG_COLUMN_END = "end_date";

    private String lastVersionOnDatabase = null;
    private String uuid;

    final HikariDataSource dataSource;


    /*
     * Создаёт базу данных
     * подключается к ней
     * создаёт таблицу для хранения UUID
     * создаёт UUID
     * добавляет UUID в таблицу для его хранения
     * <p>
     * Авторизация посредством изьятия UUID из таблицы
     * <p>
     */

    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            List<Integer> list = databaseManager.getListOfUndeliveredID();
            for (Integer integer : list) {
                System.out.println(integer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DatabaseManager() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getConnectionUrl());

        dataSource = new HikariDataSource(config);
    }

    public void firstLoad() throws FirstLoadException {
        UUID uuid = generateUUID();
        try {
            makeDatabase();
            makeSchemaUuidContainer();

            initUser(uuid);

            makeUpdateSchema();
            initFakeLastVersion();

            makeAutoUpdateSchema();
            initAutoUpdateBoolean();
            setAutoUpdateBoolean(true);

            makeLogSchema();
        } catch (SQLException e) {
            throw new FirstLoadException("Can't init database for uuid: " + uuid, e);
        }
    }

    private void initAutoUpdateBoolean() throws SQLException {
        final String query = "INSERT INTO " + USER_CHECKBOX_TABLE_NAME + "(" + COLUMN_UPDATE_NAME + ") \n " +
                "VALUES('0');";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
        }
        System.out.println("Insert executed.");
    }

    private void makeAutoUpdateSchema() throws SQLException {
        final String query = "CREATE TABLE " + USER_CHECKBOX_TABLE_NAME + " (\n" +
                COLUMN_UPDATE_NAME + " INTEGER(1));";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
        }
    }

    public void setAutoUpdateBoolean(boolean b) {
        int flag = (b) ? 1 : 0;

        final String query = "UPDATE " + USER_CHECKBOX_TABLE_NAME + "\n " +
                "SET " + COLUMN_UPDATE_NAME + " = '" + flag + "';";

        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isAutoUpdateEnabled() throws DbException {
        String query = "SELECT * FROM " + USER_CHECKBOX_TABLE_NAME;
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                System.out.println(resultSet.getBoolean(COLUMN_UPDATE_NAME));
                return resultSet.getBoolean(COLUMN_UPDATE_NAME);
            } else {
                throw new Exception("Not found boolean from column: " + COLUMN_UPDATE_NAME);
            }
        } catch (Exception e) {
            throw new DbException("Can't read boolean from column: " + COLUMN_UPDATE_NAME, e);
        }
    }

    public boolean isLastVersion() {
        return View.VERSION.equals(lastVersionOnDatabase);
    }

    public String getLastVersionInDataBase() throws SQLException {
        final String query = "SELECT * " +
                "FROM " + USER_UPDATE_LOG_TABLE_NAME + " \n" +
                "WHERE ID =(SELECT MAX(id) FROM \n" +
                USER_UPDATE_LOG_TABLE_NAME +
                ");";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                this.lastVersionOnDatabase = resultSet.getString("update_version");
                return resultSet.getString("update_version");
            }
        }
        return null;
    }

    private void initFakeLastVersion() throws SQLException {
        String query = "INSERT INTO " + USER_UPDATE_LOG_TABLE_NAME + " (update_version) \n " +
                "VALUES('" + View.VERSION + "');";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
            System.out.println(query);
        }
    }


    /**
     * Проверяет наличие таблицы user_id
     */
    public String getUuid() throws SQLException {
        if (uuid == null) {
            String table = USER_INFO_TABLE_NAME;
            final String query = "SELECT * FROM " + table;
            try (
                    Connection connection = dataSource.getConnection();
                    Statement statement = connection.createStatement()
            ) {
                ResultSet result = statement.executeQuery(query);
                if (result.next()) {
                    this.uuid = result.getString("uuid");
                    System.out.println("UUID tacked from table : " + table);
                    System.out.println("UUID is :" + uuid);
                    return uuid;
                } else {
                    return null;
                }
            }
        } else {
            return this.uuid;
        }
    }

    private UUID generateUUID() {
        System.out.println("UUID generated.");
        return UUID.randomUUID();
    }

    private String getConnectionUrl() {
        return "jdbc:sqlite:\\" + FILE_PATH;
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

    private void initUser(UUID uuid) throws SQLException {
        this.uuid = String.valueOf(uuid);
        final String query = "INSERT INTO " + USER_INFO_TABLE_NAME + "(uuid) " +
                "VALUES('" + uuid + "');";
        System.out.println(query);
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(query);
            System.out.println("Initialization user is correct.");
            System.out.println("UUID of user : " + uuid);
        }
    }

    private void makeUpdateSchema() throws SQLException {
        final String query = "CREATE TABLE " + USER_UPDATE_LOG_TABLE_NAME + " (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "update_version VARCHAR(50), \n" +
                "update_log VARCHAR(500));";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(query);
            System.out.println("logSchema created");
        }
    }

    private void makeSchemaUuidContainer() throws SQLException {
        final String query = "CREATE TABLE " + USER_INFO_TABLE_NAME + " (\n" +
                "uuid VARCHAR(36));";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(query);
            System.out.println("Table : " + USER_INFO_TABLE_NAME + " is created.");
        }
    }

    private void makeLogSchema() throws SQLException {
        final String query = "CREATE TABLE " + USER_LOG_TABLE_NAME + " (\n" +
                USER_LOG_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                USER_LOG_TABLE_IS_DELIVERED_SERVER + " INTEGER(1) DEFAULT 0, \n" +
                USER_LOG_COLUMN_START + " VARCHAR(50) NOT NULL, \n" +
                USER_LOG_COLUMN_END + " VARCHAR(50) NOT NULL); ";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
        }
    }

    public void insertDate(Date startDate, Date endDate) {
        String query = "INSERT INTO " + USER_LOG_TABLE_NAME + " \n" +
                "('" + USER_LOG_COLUMN_START + "' , '" + USER_LOG_COLUMN_END + "') \n" +
                "VALUES('" + startDate.toString() + "' , '" + endDate.toString() + "')";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMaxIDOfDateLoge() throws SQLException {
        final String query = "SELECT * FROM " + USER_LOG_TABLE_NAME + " \n" +
                "WHERE ID =(SELECT MAX(" + USER_LOG_TABLE_ID + ") FROM \n" +
                USER_LOG_TABLE_NAME +
                ");";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt(USER_LOG_TABLE_ID);
            }
        }
        return 0;
    }

    public boolean isDelivered(int id) throws SQLException {
        String query = "SELECT * " +
                "FROM " + USER_LOG_TABLE_NAME + " " +
                "WHERE " + USER_LOG_TABLE_ID + " = " + id + ";";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                int result = resultSet.getInt(USER_LOG_TABLE_IS_DELIVERED_SERVER);
                if (result == 1) {
                    return true;
                } else if (result == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getDateLoge(String columnStartOrEnd, int undeliveredID) throws SQLException {
        String query = "SELECT * " +
                "FROM " + USER_LOG_TABLE_NAME + " " +
                "WHERE " + USER_LOG_TABLE_ID + " = " + undeliveredID + ";";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.getString(columnStartOrEnd);
        }

    }

    public List<Integer> getListOfUndeliveredID() throws SQLException {
        String query = "SELECT " + USER_LOG_TABLE_ID + " FROM " + USER_LOG_TABLE_NAME +
                " WHERE " + USER_LOG_TABLE_IS_DELIVERED_SERVER + " = '0';";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<Integer> undeliveredList = new ArrayList<>();
            while (resultSet.next()) {
                undeliveredList.add(resultSet.getInt(USER_LOG_TABLE_ID));
            }
            return undeliveredList;
        }
    }


    public void setDeliveredList() throws DbException {
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            List<Integer> listOfUndeliveredID;
            listOfUndeliveredID = getListOfUndeliveredID();
            for (int ignored : listOfUndeliveredID) {
                String query = "UPDATE " +
                        USER_LOG_TABLE_NAME +
                        " SET " + USER_LOG_TABLE_IS_DELIVERED_SERVER + " = " + "1 ;";
                statement.execute(query);
            }
        } catch (SQLException e) {
            throw new DbException("Can't set boolean on table: " + DatabaseManager.USER_LOG_TABLE_NAME, e);
        }
    }
}
