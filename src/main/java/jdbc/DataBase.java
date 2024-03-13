package jdbc;

import java.sql.*;

public class DataBase {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:tcp://localhost/~/test3";
    static final String USER = "sa";
    static final String PASS = "";

    public static void main(String[] args) throws SQLException {
           create_table();
           insert();
           select();
    }

    public static void create_table() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Creating table ...");
            statement = connection.createStatement();

            String sql = "CREATE TABLE H2_TEST " +
                    "(ID NUMBER not NULL, " +
                    " STR VARCHAR(20), " +
                    " PRIMARY KEY ( ID ))";
            statement.executeUpdate(sql);
            System.out.println("Created table in given database...");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }

    }
    public static void select() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {

            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            System.out.println("Connected to database ...");
            statement = connection.createStatement();
            String sql = "SELECT ID, ACCOUNT_NUMBER, BALANCE FROM ACCOUNT";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String accountNumber = resultSet.getString("ACCOUNT_NUMBER");
                Double balance = resultSet.getDouble("AMOUNT");

                // Display values
                System.out.println("id: " + id);
                System.out.println("accountNumber: " + accountNumber);
                System.out.println("balance: " + balance);
                System.out.println("=========================================");
            }

            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
    }

    public static void insert(/*String sql*/) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to a selected database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to database ...");
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            String sql = "INSERT INTO Account VALUES (6, '1.3.3.4', 2000)";
            statement.executeUpdate(sql);
            sql = "INSERT INTO ACCOUNT " + "VALUES (7, '5.3.2.1', 50)";
            statement.executeUpdate(sql);
            System.out.println("Inserted records into the table...");
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null)
                connection.rollback();

        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
    }
}
