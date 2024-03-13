package entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
@Data
@Entity

public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String accountNumber;
    private Double balance;


    public Account(String accountNumber, Double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Account() {

    }
}
