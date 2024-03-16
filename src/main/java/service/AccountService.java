package service;

import entity.Account;
import entity.Transaction;
import exception.InsufficientFundsException;
import exception.InvalidTransactionException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;

public class AccountService {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, InsufficientFundsException, InvalidTransactionException {
       EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

//        EntityManager entityManager = EntityManagerHelper.getEntityManager();


            entityManager.getTransaction().begin();
//            entityManager.remove(accountType);
        Account account1 = new Account(1, "MohammadSaleh", 5000.0);
        Account account2 = new Account(2, "MohammadSadegh", 1000.0 );
        account1.deposit(1000.0);
        account2.withdraw(100.0);
        entityManager.persist(account1);
        entityManager.persist(account2);

        entityManager.getTransaction().commit();

        entityManager.close();



    }
}
