package service;

import entity.Account;
import entity.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AccountService {
    public static void main(String[] args) {
       EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test3");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

       // EntityManager entityManager = EntityManagerHelper.getEntityManager();


            entityManager.getTransaction().begin();
//            entityManager.remove(accountType);
        Account account1 = new Account("1234", 1000_000.0);
        Account account2 = new Account("1235", 1000_000.0);
        Transaction transaction1 = new Transaction(200_000.0, Transaction.TransactionType.deposit, account1);
        Transaction transaction2 = new Transaction(200_000.0, Transaction.TransactionType.deposit, account1);
        entityManager.persist(account1);
        entityManager.persist(account2);
        entityManager.persist(transaction1);
        entityManager.persist(transaction2);
            entityManager.getTransaction().commit();

            entityManager.close();


    }
}
