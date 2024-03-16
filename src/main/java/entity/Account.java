package entity;

import com.sun.istack.Nullable;
import exception.InsufficientFundsException;
import exception.InvalidTransactionException;
import jdbc.DataBaseConnector;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity

public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Double balance;
    private String accountHolderName;

    private static final List<String> columns = Arrays.asList("ACCOUNTNUMBER", "ACCOUNTHOLDERNAME", "BALANCE");

    private static DataBaseConnector dataBaseConnector;

    public Account(Integer accountNumber, String accountHolderName, Double balance) throws ClassNotFoundException, SQLException {
        this.id = accountNumber;
        this.accountHolderName = "'" + accountHolderName + "'";
        this.balance = balance;

        createAccountInDatabase();
    }

    public Account() {

    }


    private void createAccountInDatabase() throws SQLException {

        dataBaseConnector = new DataBaseConnector();

        List<String> sql = new ArrayList<>();
        sql.add(Integer.toString(id));
        sql.add(accountHolderName);
        sql.add(Double.toString(balance));

        dataBaseConnector.insert("ACCOUNTS", columns, sql);
    }

    @Nullable
    public static Account getOneAccount(int accountNumber) throws SQLException, ClassNotFoundException {
        dataBaseConnector = new DataBaseConnector();

        try {
            ResultSet resultSet = dataBaseConnector.find("ACCOUNTS", "accountNumber", Integer.toString(accountNumber));

            if (resultSet.next()) {
                return new Account(
                        resultSet.getInt("accountNumber"),
                        resultSet.getString("accountHolderName"),
                        resultSet.getDouble("balance")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static List<Account> getAllAccounts() throws SQLException {
        dataBaseConnector = new DataBaseConnector();

        List<Account > listOfAccounts = new ArrayList<>();

        try {
            ResultSet resultSet = dataBaseConnector.getAll("ACCOUNTS");

            while (resultSet.next()) {
                listOfAccounts.add(new Account(
                        resultSet.getInt("accountNumber"),
                        resultSet.getString("accountHolderName"),
                        resultSet.getDouble("balance")
                ));
            }

            return listOfAccounts;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static void deleteAccountFromDatabase(int accountNumber) throws SQLException {
        dataBaseConnector = new DataBaseConnector();

        dataBaseConnector.delete("ACCOUNTS", "accountNumber" , Integer.toString(accountNumber));
    }

    public static void showOneAccount(int accountNumber) throws SQLException, ClassNotFoundException {
        dataBaseConnector = new DataBaseConnector();

        Account account = getOneAccount(accountNumber);

        if (account != null) {
            System.out.println("[ number: " + account.id +
                    "\t  owner: " + account.accountHolderName +
                    "\t balance: " + account.balance + " ]");
        }
    }

    public static void showAllAccounts() throws SQLException {
        List<Account> accountList = getAllAccounts();
        accountList.stream()
                .map(account -> "number: " + account.id +
                        "\t\t owner: " + account.accountHolderName +
                        "\t\t balance: " + account.balance)
                .forEach(System.out::println);
    }

    public void deposit(Double amount) throws SQLException {
        if (amount < 0)
            throw new IllegalArgumentException("amount of deposit can not be negative");
        else {
            dataBaseConnector = new DataBaseConnector();

            dataBaseConnector.update("ACCOUNTS", "ACCOUNTNUMBER", Integer.toString(id),
                    "BALANCE", Double.toString(balance + amount));

            Transaction transaction = new Transaction(this, amount, Transaction.TransactionType.deposit);
        }
    }

    public void withdraw(Double amount) throws InsufficientFundsException, InvalidTransactionException, SQLException {
        if (amount > balance)
            throw new InsufficientFundsException("insufficient balance ");
        else if (amount < 0)
            throw new InvalidTransactionException("not valid amount ");
        else {
            dataBaseConnector = new DataBaseConnector();

            dataBaseConnector.update("ACCOUNTS", "ACCOUNTNUMBER", Integer.toString(id),
                    "BALANCE", Double.toString(balance - amount));

            Transaction transaction = new Transaction(this, amount, Transaction.TransactionType.withdraw );
        }
    }

    public int getId() {
        return id;
    }
}
