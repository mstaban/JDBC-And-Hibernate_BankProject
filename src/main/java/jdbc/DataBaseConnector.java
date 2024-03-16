package jdbc;

import java.sql.*;
import java.util.List;

public class DataBaseConnector {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String URL = "jdbc:h2:~/test";
    static final String USER = "sa";
    static final String PASS = "";

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement statement = null;
    private static Boolean tableExists = false;

    public DataBaseConnector() throws SQLException {
        createTables();
    }

    private void createTables() throws SQLException {

        String query = "CREATE TABLE IF NOT EXISTS Accounts ( " +
                "accountNumber INTEGER PRIMARY KEY," +
                "accountHolderName VARCHAR(255) NOT NULL,"+
                "balance REAL NOT NULL);" +

                "CREATE TABLE IF NOT EXISTS Transactions (" +
                "transactionNumber INTEGER PRIMARY KEY ," +
                "account INTEGER NOT NULL," +
                "amount REAL NOT NULL," +
                "FOREIGN KEY (account) REFERENCES Accounts(accountNumber));";

        VoidConnectionsTry(query);
    }

    public void insert(String tableName, List<String> columns, List<String> information) throws SQLException {

        String query = "INSERT INTO " + tableName + "( " + String.join(", ", columns) + ") " +
                "SELECT * FROM (SELECT " + String.join(", ", information) + " ) AS tmp " +
                "WHERE NOT EXISTS ( SELECT * FROM " + tableName + " WHERE " +  tableName + "." + columns.get(0) + " = " +information.get(0) + ") LIMIT 1;";

        VoidConnectionsTry(query);
    }

    public  ResultSet find(String tableName, String primaryName, String primaryValue) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE " + primaryName + " = " + primaryValue + ";";

        return returnConnectionsTry(query);

    }

    public ResultSet getAll(String tableName){
        String query = "SELECT * FROM " + tableName + ";";

        return returnConnectionsTry(query);
    }

    public void delete(String tableName, String primaryName, String primaryValue) {

        String query = "DELETE FROM " + tableName + " WHERE " + primaryName + " = " + primaryValue + ";";

        VoidConnectionsTry(query);
    }

    public void update(String tableName, String primaryName, String primaryValue, String editedVariable, String newValue) throws SQLException {

        String query = "UPDATE " +tableName+ " SET " + editedVariable + " = " + newValue + " WHERE " + primaryName + " = " + primaryValue + ";";

        VoidConnectionsTry(query);
    }

    public ResultSet maximum(String tableName, String primaryName){
        String query = "SELECT MAX(" + primaryName + ") FROM " + tableName + ";";

        return returnConnectionsTry(query);
    }

    private ResultSet returnConnectionsTry(String query) {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASS);
            statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void VoidConnectionsTry(String query) {
        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(URL, USER, PASS);
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
