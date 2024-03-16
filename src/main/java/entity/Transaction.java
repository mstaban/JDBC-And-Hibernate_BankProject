package entity;

import jdbc.DataBaseConnector;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class Transaction{
    @Id
    private Double amount;
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "account")
    private Account account;

    public Transaction(Account account, Double amount, TransactionType transactionType) throws SQLException {
        id = lastTransactionNumber() + 1;
        this.amount = amount;
        this.account = account;
        this.transactionType = transactionType;
        createTransactionInDatabase();
    }

    private static final List<String> columns = Arrays.asList("TRANSACTIONNUMBER", "ACCOUNT", "AMOUNT");

    private static DataBaseConnector dataBaseConnector;

    public Transaction() {
    }

    private void createTransactionInDatabase() throws SQLException {

        dataBaseConnector = new DataBaseConnector();

        List<String> sql = new ArrayList<>();
        sql.add(Integer.toString(id));
        sql.add(Integer.toString(account.getId()));
        sql.add(Double.toString(amount));

        dataBaseConnector.insert("TRANSACTIONS", columns, sql);
    }

    private int lastTransactionNumber() throws SQLException {
        dataBaseConnector = new DataBaseConnector();

        ResultSet resultSet = dataBaseConnector.maximum("TRANSACTIONS", "TRANSACTIONNUMBER");
        if (resultSet.next()){
            return resultSet.getInt("MAX(TRANSACTIONNUMBER)");
        }
        return 0;
    }


    public enum  TransactionType {deposit, withdraw};
    private TransactionType transactionType;

}
