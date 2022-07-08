package ru.sa.dotaassist.server;

import java.sql.*;

public class SQLTest {
    Connection connection;

    public static void main(String[] args) {
        SQLTest sqlTest = new SQLTest();

        sqlTest.openConnection();

//        UUID uuid = UUID.randomUUID();
//        sqlTest.insert(uuid);

        sqlTest.select();
        sqlTest.close();
    }

    boolean openConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:\\C:\\SQLite\\users.db");
            if (connection != null) {
                System.out.println("Connected");
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    void insert(String uuid) {
        String query =
                "INSERT INTO users(UUID) " +
                        "VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid);
            int insertedRows = statement.executeUpdate(query);
            System.out.println("Rows added: " + insertedRows);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
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

    void close() {
        try {
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
