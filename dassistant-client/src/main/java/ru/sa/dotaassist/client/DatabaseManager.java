package ru.sa.dotaassist.client;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.sa.dotaassist.client.exceptions.DbException;
import ru.sa.dotaassist.client.exceptions.FirstLoadException;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {
    public static final String FILE_PATH = String.valueOf(Paths.get(System.getProperty("user.home")).resolve("LocalDatabase").resolve("logs.db"));

    public static final String USER_INFO_TABLE_NAME = "user_info";
    public static final String USER_INFO_COLUMN = "uniqueID";

    // таблица настроек
    public static final String USER_SETTINGS_TABLE_NAME = "user_settings_table";
    public static final String USER_SETTINGS_COLUMN_1 = "id";
    public static final String USER_SETTINGS_COLUMN_2 = "setting_name";
    public static final String USER_SETTINGS_COLUMN_3 = "setting_value";
    //список настроек
    public static final String USER_SETTING_SMILES = "smiles_boolean";
    public static final String USER_SETTING_AUTOUPDATE = "update_boolean";

    public static final String USER_UPDATE_LOG_TABLE_NAME = "user_update_log";

    public static final String USER_LOG_TABLE_NAME = "date_log";
    public static final String USER_LOG_TABLE_ID = "id";
    public static final String USER_LOG_TABLE_IS_DELIVERED_SERVER = "given_to_server";
    public static final String USER_LOG_COLUMN_START = "start_date";
    public static final String USER_LOG_COLUMN_END = "end_date";

    private String uniqueID;

    HikariDataSource dataSource = null;

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

    public DatabaseManager() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(getConnectionUrl());
        try {
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            try {
                config.setJdbcUrl("jdbc:sqlite:logs.db");
                dataSource = new HikariDataSource(config);
            } catch (Exception e1) {
                System.exit(-1);
            }
        }
    }

    public void firstLoad() throws FirstLoadException {
        String uniqueID = String.valueOf(UUID.randomUUID());
        try {
            System.out.println("stage:1\n" +
                    "makeDatabase.");
            makeDatabase();
            System.out.println("""
                    Database has been created;
                    stage:2
                    make uniqueID container.""");
            makeSchemaIdContainer();
            System.out.println("""
                    Container has been created;
                    stage:3
                    init user.""");
            initUser(uniqueID);
            System.out.println("""
                    User has been initiated;
                    stage:4
                    make update table.""");
            makeUpdateSchema();
            System.out.println("""
                    Update table has been created;
                    stage:5
                    init fake last Version.""");
            initFakeLastVersion();
            System.out.println("""
                    fake last Version has been initiated;
                    stage:6
                    make Settings table.""");
            makeSettingsSchema();
            System.out.println("""
                    AutoUpdate table has been created;
                    stage:7
                    init Default settings .""");
            makeDefaultSettings();

            setAutoUpdateBoolean(true);

            System.out.println("""
                    AutoUpdate boolean  has been initiated;
                    stage:8
                    make log table.""");
            makeLogSchema();
            System.out.println("Log schema has been created.");
        } catch (SQLException | DbException e) {
            throw new FirstLoadException("Can't init database for uniqueID: " + uniqueID, e);
        }
    }

    private void makeSettingsSchema() throws SQLException {
        final String query = "CREATE TABLE " + USER_SETTINGS_TABLE_NAME + " (\n" +
                USER_SETTINGS_COLUMN_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                USER_SETTINGS_COLUMN_2 + " VARCHAR(50), \n" +
                USER_SETTINGS_COLUMN_3 + " INTEGER(1) DEFAULT 0);";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
        }
    }

    private void makeDefaultSettings() throws SQLException {
        insertIntoSettings(USER_SETTING_SMILES, false);
        insertIntoSettings(USER_SETTING_AUTOUPDATE, true);
    }

    private void insertIntoSettings(String settingName, Boolean bool) throws SQLException {
        int value = bool ? 1 : 0;
        final String query = "INSERT INTO " + USER_SETTINGS_TABLE_NAME + "('" + USER_SETTINGS_COLUMN_2 + "','" + USER_SETTINGS_COLUMN_3 + "' )" +
                " VALUES('" + settingName + "','" + value + "');";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
        }
        System.out.println("Insert executed.");
    }

    /*
    обновить таблицу устанавив значение value(3 колонка) где
    название функции(2 колонка) равно updateBool
    */

    public void setAutoUpdateBoolean(boolean b) throws DbException {
        final String query = "UPDATE " + USER_SETTINGS_TABLE_NAME + " SET " + USER_SETTINGS_COLUMN_3 + " = ?" +
                " WHERE " + USER_SETTINGS_COLUMN_2 + " =('" + USER_SETTING_AUTOUPDATE + "');";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, b);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Update boolean has not been updated.", e);
        }
    }
    public void setSmilesBoolean(boolean b) throws DbException {
        final String query = "UPDATE " + USER_SETTINGS_TABLE_NAME + " SET " + USER_SETTINGS_COLUMN_3 + " = ?" +
                " WHERE " + USER_SETTINGS_COLUMN_2 + " =('" + USER_SETTING_SMILES + "');";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, b);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Smiles boolean has not been updated.", e);
        }
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
                return resultSet.getString("update_version");
            }
        }
        return null;
    }


    public boolean isAutoUpdateEnabled() throws DbException {
        final String query = "SELECT " + USER_SETTINGS_COLUMN_3 + " FROM " + USER_SETTINGS_TABLE_NAME + " WHERE " + USER_SETTINGS_COLUMN_2 + " =('" + USER_SETTING_AUTOUPDATE + "');";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getBoolean(USER_SETTINGS_COLUMN_3);
            } else {
                throw new Exception("Not found boolean from column: " + USER_SETTINGS_COLUMN_3);
            }
        } catch (Exception e) {
            throw new DbException("Can't read boolean from column: " + USER_SETTINGS_COLUMN_3, e);
        }
    }

    public boolean isSmilesEnabled() throws DbException {
        final String query = "SELECT " + USER_SETTINGS_COLUMN_3 + " FROM " + USER_SETTINGS_TABLE_NAME + " WHERE " + USER_SETTINGS_COLUMN_2 + " =('" + USER_SETTING_SMILES + "');";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getBoolean(USER_SETTINGS_COLUMN_3);
            } else {
                throw new Exception("Not found boolean from column: " + USER_SETTINGS_COLUMN_3);
            }
        } catch (Exception e) {
            throw new DbException("Can't read boolean from column: " + USER_SETTINGS_COLUMN_3, e);
        }
    }

    public boolean isLastVersion(String lastVersionOnDatabase) {
        return View.VERSION.equals(lastVersionOnDatabase);
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
    public String getUniqueID() throws SQLException {
        if (uniqueID == null) {
            final String query = "SELECT " + USER_INFO_COLUMN + " FROM " + USER_INFO_TABLE_NAME;
            try (
                    Connection connection = dataSource.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query)
            ) {
                ResultSet result = preparedStatement.executeQuery();
                if (result.next()) {
                    uniqueID = result.getString(USER_INFO_COLUMN);
                    System.out.println(USER_INFO_COLUMN + " tacked from table : " + USER_INFO_TABLE_NAME);
                    System.out.println(USER_INFO_COLUMN + " is :" + uniqueID);
                    return uniqueID;
                } else {
                    return null;
                }
            }
        } else {
            return uniqueID;
        }
    }

    private String getConnectionUrl() {
        return "jdbc:sqlite:\\" + FILE_PATH;
    }


    private void makeDatabase() throws DbException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("A new database has been created, path: " + getConnectionUrl());
        } catch (SQLException e) {
            throw new DbException("Can't make Database.", e);
        }
    }

    private void initUser(String uniqueID) throws DbException {
        final String query = "INSERT INTO " + USER_INFO_TABLE_NAME + "(" + USER_INFO_COLUMN + ") " +
                "VALUES( ? );";
        System.out.println(query);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uniqueID);
            statement.executeUpdate();
            System.out.println("Initialization user is correct.");
            System.out.println("UUID of user : " + uniqueID);
        } catch (SQLException e) {
            throw new DbException("Can'n init user with this uniqueID:\n" + uniqueID, e);
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
            System.out.println("Table : " + USER_UPDATE_LOG_TABLE_NAME + " is created." +
                    "schema:\n" + query);
        }
    }

    private void makeSchemaIdContainer() throws SQLException {
        final String query = "CREATE TABLE " + USER_INFO_TABLE_NAME + " (\n" +
                USER_INFO_COLUMN + " VARCHAR(150));";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(query);
            System.out.println("Table : " + USER_INFO_TABLE_NAME + " is created." +
                    "schema:\n" + query);
        }
    }

    private void makeLogSchema() throws DbException {
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
            System.out.println("Table : " + USER_LOG_TABLE_NAME + " is created." +
                    "schema:\n" + query);
        } catch (SQLException e) {
            throw new DbException("logSchema has been uncreated.", e);
        }
    }

    public void insertDate(Date startDate, Date endDate) throws DbException {
        String query = "INSERT INTO " + USER_LOG_TABLE_NAME + " \n" +
                "('" + USER_LOG_COLUMN_START + "' , '" + USER_LOG_COLUMN_END + "') \n" +
                "VALUES( ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, String.valueOf(startDate));
            statement.setString(2, String.valueOf(endDate));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Can't insert date in client Database.\n" +
                    "start Date :" + startDate + "\n" +
                    "end Date: " + endDate, e);
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

    public boolean isDelivered(int id) throws DbException {
        String query = "SELECT * " +
                "FROM " + USER_LOG_TABLE_NAME + " " +
                "WHERE " + USER_LOG_TABLE_ID + " = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean(USER_LOG_TABLE_IS_DELIVERED_SERVER);
            } else {
                throw new DbException("id:" + id + "is not exists in Database");
            }
        } catch (SQLException e) {
            throw new DbException("Can't read boolean \"isDelivered\"", e);
        }
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
        String query = "UPDATE " + USER_LOG_TABLE_NAME + " SET " + USER_LOG_TABLE_IS_DELIVERED_SERVER + " = ?;";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, true);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Can't set boolean on table: " + DatabaseManager.USER_LOG_TABLE_NAME, e);
        }
    }
}

