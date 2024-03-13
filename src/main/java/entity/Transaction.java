package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.bytebuddy.implementation.bind.annotation.Super;

import javax.persistence.*;

@Entity
@Data
public class Transaction{
    @Id
    private Double amount;
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


        @ManyToOne
        @JoinColumn(name = "account")
        private Account account;

    public Transaction(Double amount, TransactionType transactionType, Account account) {
        this.amount = amount;
        this.account = account;
     this.transactionType =transactionType;
     account.setBalance(account.getBalance()+ amount);
    }

    public Transaction() {
    }

    public enum  TransactionType {deposit, withdraw};
    private TransactionType transactionType;









}
