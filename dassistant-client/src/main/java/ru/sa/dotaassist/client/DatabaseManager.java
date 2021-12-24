package ru.sa.dotaassist.client;

import java.io.File;
import java.sql.*;
import java.util.Date;
import java.util.UUID;

public class DatabaseManager {
    private static Connection connection;
    private static final String USER_INFO_TABLE_NAME = "user_info";
    private static String uuid;

    static final boolean IS_DEVELOPING = true;

    /**
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
//        if (IS_DEVELOPING) {
//            File file = new File(getFilePath());
//            if(file.delete()){
//                System.out.println("File deleted");
//            }else{
//                System.out.println("File undeleted");
//            }
//        }
//        SQLTest sqlTest = new SQLTest();
        try {
            if (!DatabaseManager.isDatabaseExists()) {
                makeDatabase();
                connection = openConnection();
                makeSchema();
                UUID uuid = generateUUID();
                initUser(uuid);

//                makelogSchema();

            } else {
                connection = openConnection();
            }
            getUuid();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static long millisecondsPassed(Date startDate) {
        return new Date().getTime() - startDate.getTime();
    }

    private static boolean userIDTableExists() throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null, "%", null);
        while (resultSet.next()) {
            if (resultSet.getString(3).equals(USER_INFO_TABLE_NAME)) {
                System.out.println("true");
                return true;
            }
        }
        System.out.println("false");
        return false;
    }

    /**
     * Проверяет наличие таблицы user_id
     * в случае обнаружения таблицы изымает из неё UUID
     * присваивает его статическому полю
     * в случае отсуттвия таблицы создаёт таблицу user_id,
     * создаёт UUID
     * добавляет UUID в таблицу
     * и присваивает его статическому полю
     *
     * @return
     */

    static String getUuid() throws SQLException {
        if (uuid == null) {
            String table = USER_INFO_TABLE_NAME;
            final String query = "SELECT * FROM " + table;
            try (Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(query);
                if (result.next()) {
                    uuid = result.getString("uuid");
                    System.out.println("UUID tacked from table : " + table);
                    System.out.println("UUID is :" + uuid);
                    return uuid;
                }
            }
        } else return uuid;
        return null;
    }

    private static UUID generateUUID() {
        System.out.println("UUID generated.");
        return UUID.randomUUID();
    }

    private static String getConnectionUrl() {
        return "jdbc:sqlite:\\" + getFilePath();
    }

    private static String getFilePath() {
        return "C:\\SQLite\\logs.db";
    }

    static Connection openConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + getFilePath());
        //todo make worked with getConnectionUrl();
        if (connection != null) {
            System.out.println("Connected");
            return connection;
        } else {
            return null;
        }
    }


    //todo change to data loge saver;
    static void insert(String uuid) {
        String query =
                "INSERT INTO users(UUID) " +
                        "VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid);
            int insertedRows = statement.executeUpdate(query);
            System.out.println("Rows added: " + insertedRows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean uuidExists() {
        try {
            connection = openConnection();
            if (getUuid() == null) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
        connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    void select() {
        String query =
                "SELECT id, UUID " +
                        "FROM users " +
                        "ORDER BY id";
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String UUID = rs.getString("UUID");
                System.out.println(id + " | " + UUID);
            }
            rs.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static boolean isDatabaseExists() throws SQLException {
        String filePath = getFilePath();
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    static void makeDatabase() throws SQLException {
        String url = getConnectionUrl();
        try (Connection connection = DriverManager.getConnection(url)) {
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created, path: " + url);
            }
        }
    }

    static void makeSchema() throws SQLException {
        String query = "CREATE TABLE " + USER_INFO_TABLE_NAME + " (\n" +
                "uuid varchar(100));";
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("Table : " + USER_INFO_TABLE_NAME + " is created.");
        }
    }

    private static void initUser(UUID uuid) throws SQLException {
        DatabaseManager.uuid = String.valueOf(uuid);
        String query = "INSERT INTO " + USER_INFO_TABLE_NAME + "(uuid) " +
                "VALUES('" + uuid + "');";
        System.out.println(query);
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("Initialization user is correct.");
            System.out.println("UUID of user : " + uuid);
        }
    }
}
