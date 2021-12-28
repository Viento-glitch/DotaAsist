package ru.sa.dotaassist.client;

import java.io.File;
import java.sql.*;
import java.util.Date;
import java.util.UUID;

public class DatabaseManager {
    private final String USER_INFO_TABLE_NAME = "user_info";

    private final String USER_UPDATE_LOG_TABLE_NAME = "user_update_log";

    private final String USER_CHECKBOX_TABLE_NAME = "update_checkbox";
    private final String COLUMN_UPDATE_NAME = "update_boolean";

    private final String USER_LOG_TABLE_NAME = "date_log";
    private final String USER_LOG_COLUMN_START = "start_date";
    private final String USER_LOG_COLUMN_END = "end_date";


    private Connection connection;
    private String lastVersionOnDatabase = null;
    private String uuid;

//    private final boolean IS_DEVELOPING = true;


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
//        DatabaseManager databaseManager = new DatabaseManager();


//            if (databaseManager.isLastVersion()) {
        //? попытка запроса на сервер
//            } else {
        /**todo
         * ? проверить наличие галочки в чекбоксе "Обновлять без лишних вопросов"
         *      при согласии на обновления без лишних вопросов
         *          todo реаилизация обновления путём скачивания файлов с github оповестить о окончании скачивания
         *              перезапустить приложение
         *                  показать нововведения
         *! при не согласии на обновления без лишних вопросов
         *  оповестить пользователя о возможности обновится
         *      изменить структуру приложения добавив в окно UpdateNews кнопку обновится
         *          ? при нажатии на клавишу обновится
         *              *todo реаилизация обновления путём скачивания файлов с github
         *                  оповестить о окончании скачивания
         *                  перезапустить приложение
         */
//            }
//            databaseManager.getUuid();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (databaseManager.connection != null) {
//                try {
//                    databaseManager.connection.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    public void firstLoad() throws SQLException, ClassNotFoundException {
        this.makeDatabase();
        this.openConnection();
        this.makeSchema();

        UUID uuid = generateUUID();
        this.initUser(uuid);

        this.makeUpdateSchema();
        this.initFakeLastVersion();

        this.makeAutoUpdateSchema();
        this.initAutoUpdateBoolean();
        this.setAutoUpdateBoolean(true);

        this.makeLogSchema();

        this.connection.close();
    }


    //final String query = "INSERT INTO " + USER_LOG_TABLE_NAME + " (update_version) \n " +
//                "VALUES('" + View.VERSION + "');";
    private void initAutoUpdateBoolean() throws SQLException {
        final String query = "INSERT INTO " + USER_CHECKBOX_TABLE_NAME + "(" + COLUMN_UPDATE_NAME + ") \n " +
                "VALUES('0');";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
        System.out.println("Insert executed.");
    }

    private void makeAutoUpdateSchema() throws SQLException {
        final String query = "CREATE TABLE " + USER_CHECKBOX_TABLE_NAME + " (\n" +
                COLUMN_UPDATE_NAME + " INTEGER(1));";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

    public void setAutoUpdateBoolean(boolean b) {
        int flag = (b) ? 1 : 0;

        final String query = "UPDATE " + USER_CHECKBOX_TABLE_NAME + "\n " +
                "SET " + COLUMN_UPDATE_NAME + " = '" + flag + "';";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getUpdateBooleanFromDB() {
        final String query = "SELECT * FROM " + this.USER_CHECKBOX_TABLE_NAME;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                System.out.println(resultSet.getBoolean(COLUMN_UPDATE_NAME));
                return resultSet.getBoolean(COLUMN_UPDATE_NAME);
            } else {
                throw new Exception(COLUMN_UPDATE_NAME + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isLastVersion() {
        if (View.VERSION.equals(lastVersionOnDatabase)) {
            return true;
        } else {
            return false;
        }
    }

    public String getLastVersionInDataBase() throws SQLException {
        final String query = "SELECT * " +
                "FROM " + USER_UPDATE_LOG_TABLE_NAME + " \n" +
                "WHERE ID =(SELECT MAX(id) FROM \n" +
                USER_UPDATE_LOG_TABLE_NAME +
                ");";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                this.lastVersionOnDatabase = resultSet.getString("update_version");
                return resultSet.getString("update_version");
            }
        }
        return null;
    }

    private void initFakeLastVersion() throws SQLException {
        final String query = "INSERT INTO " + USER_UPDATE_LOG_TABLE_NAME + " (update_version) \n " +
                "VALUES('" + View.VERSION + "');";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            System.out.println(query);
        }
    }

    private void makeUpdateSchema() throws SQLException {
        final String query = "CREATE TABLE " + USER_UPDATE_LOG_TABLE_NAME + " (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "update_version VARCHAR(50), \n" +
                "update_log VARCHAR(500));";
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("logSchema created");
        }
    }

    private void takeSchema(String table) throws SQLException {
//        final String query = ".schema " + table;
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getSchemas();
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
            //            if (resultSet.getString(3).equals(USER_INFO_TABLE_NAME)) {
//            }
        }
    }

    private long millisecondsPassed(Date startDate) {
        return new Date().getTime() - startDate.getTime();
    }

    private boolean userIDTableExists() throws SQLException {
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

    private String getUuid() throws SQLException {
        if (uuid == null) {
            String table = USER_INFO_TABLE_NAME;
            final String query = "SELECT * FROM " + table;
            try (Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(query);
                if (result.next()) {
                    this.uuid = result.getString("uuid");
                    System.out.println("UUID tacked from table : " + table);
                    System.out.println("UUID is :" + uuid);
                    return uuid;
                }
            }
        } else return this.uuid;
        return null;
    }

    private UUID generateUUID() {
        System.out.println("UUID generated.");
        return UUID.randomUUID();
    }

    private String getConnectionUrl() {
        return "jdbc:sqlite:\\" + getFilePath();
    }

    private String getFilePath() {
        return "C:\\SQLite\\logs.db";
    }

    public void openConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + getFilePath());
        //todo make worked with getConnectionUrl();
        if (connection != null) {
            System.out.println("Connected");
            this.connection = connection;
        }
    }


    //todo change to data loge saver;
    void insert(String uuid) {
        final String query =
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

    private boolean uuidExists() {
        try {
            openConnection();
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

    public void closeConnection() {
        try {
            connection.close();
            System.out.println("Disconnected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void select() {
        final String query =
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

    public boolean isDatabaseExists() throws SQLException {
        String filePath = getFilePath();
        File file = new File(filePath);
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

    private void makeSchema() throws SQLException {
        final String query = "CREATE TABLE " + USER_INFO_TABLE_NAME + " (\n" +
                "uuid VARCHAR(100));";
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("Table : " + USER_INFO_TABLE_NAME + " is created.");
        }
    }

    private void initUser(UUID uuid) throws SQLException {
        this.uuid = String.valueOf(uuid);
        final String query = "INSERT INTO " + USER_INFO_TABLE_NAME + "(uuid) " +
                "VALUES('" + uuid + "');";
        System.out.println(query);
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("Initialization user is correct.");
            System.out.println("UUID of user : " + uuid);
        }
    }

    private void makeLogSchema() {
        final String query = "CREATE TABLE " + USER_LOG_TABLE_NAME + " (\n" +
                USER_LOG_COLUMN_START + " VARCHAR(50) NOT NULL, \n" +
                USER_LOG_COLUMN_END + " VARCHAR(50) NOT NULL); ";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertDate(Date startDate, Date endDate) {
        String query = "INSERT INTO " + USER_LOG_TABLE_NAME + " \n" +
                "('" + USER_LOG_COLUMN_START + "' , '" + USER_LOG_COLUMN_END + "') \n" +
                "VALUES('" + startDate.toString() + "','" + endDate.toString() + "')";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
