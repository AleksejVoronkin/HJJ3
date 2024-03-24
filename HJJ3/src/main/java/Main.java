import java.sql.*;

//
// * 0. Разобрать код с семниара
// * 1. Повторить код с семниара без подглядываний на таблице Student с полями:
// * 1.1 id - int
// * 1.2 firstName - string
// * 1.3 secondName - string
// * 1.4 age - int
//


public class Main {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
            acceptConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void acceptConnection(Connection connection) throws SQLException {
        createStudentTable(connection);
        insertData(connection);
        deleteRow(connection);
        updateRow(connection, 1, "Vladimir", "Vladimirovih", 21);
        selectData(connection);//вынес отдельно от acceptConnection
    }

    private static void createStudentTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                create table Student (
                    id int,
                    firstName varchar(256),
                    secondName varchar(256),
                    age int
                )
            """);
        }
    }

    private static void insertData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int affectedRows = statement.executeUpdate("""
                insert into Student (id, firstName, secondName, age) values
                (1, 'Igor', 'Igorev', 20),
                (2, 'Vlad', 'WhatIsLOve', 22),
                (3, 'John', 'Johnson', 23),
                (4, 'Aleksej', 'Alekseevih', 24),
                (5, 'Peter', 'Petrovih', 25)
            """);
            System.out.println("INSERT: affected rows: " + affectedRows);
        }
    }

    private static void updateRow(Connection connection, int id, String firstName, String secondName, int age) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("update Student set firstName = ?, secondName = ?, age = ? where id = ?")) {
            stmt.setString(1, firstName);
            stmt.setString(2, secondName);
            stmt.setInt(3, age);
            stmt.setInt(4, id);
            int affectedRows = stmt.executeUpdate();
            System.out.println("UPDATE: affected rows: " + affectedRows);
        }
    }

    private static void deleteRow(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int affectedRows = statement.executeUpdate("delete from Student where id = 3");
            System.out.println("DELETE: affected rows: " + affectedRows);
        }
    }

    private static void selectData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select id, firstName, secondName, age from Student");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String secondName = resultSet.getString("secondName");
                int age = resultSet.getInt("age");
                System.out.println("id = " + id + ", firstName = " + firstName + ", secondName = " + secondName + ", age = " + age);
            }
        }
    }
}