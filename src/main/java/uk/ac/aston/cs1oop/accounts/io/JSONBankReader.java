package uk.ac.aston.cs1oop.accounts.io;

import com.google.gson.Gson;
import uk.ac.aston.cs1oop.accounts.exceptions.IllegalTransactionException;
import uk.ac.aston.cs1oop.accounts.model.Account;
import uk.ac.aston.cs1oop.accounts.model.Bank;
import uk.ac.aston.cs1oop.accounts.model.Transaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JSONBankReader {
    private Bank bank;

    public JSONBankReader(Bank bank) {
        this.bank = bank;
    }
    
    public void readTransactionsJSON(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            readTransactionsJSON(reader);
        }
    }
    
    public void readTransactionsJSON(Reader reader) throws IOException {
        Gson gson = new Gson();
        Transaction[] transactions = gson.fromJson(reader, Transaction[].class);
        if (transactions != null) {
            for (Transaction tx : transactions) {
                try {
                    Account account = bank.getAccount(tx.getAccountId());
                    if (account != null) {
                        account.addTransaction(tx);
                        tx.setAccount(account); // Set the associated account for the transaction
                        tx.execute(bank);
                    } else {
                        System.out.println("Account with this ID " + tx.getAccountId() + " is not found for transaction: " + tx.getDescription());
                    }
                } catch (IllegalTransactionException e) {
                    System.out.println("Transaction is failed: " + e.getMessage());
                    // Rethrow the exception if needed
                    throw new IOException("Error while reading transactions from JSON", e);
                }
            }
        }
    }


    public void readAccountsJSON(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            readAccountsJSON(reader);
        }
    }

    public void readAccountsJSON(Reader reader) throws IOException {
        Gson gson = new Gson();
        Account[] accounts = gson.fromJson(reader, Account[].class);
        if (accounts != null) {
            for (Account account : accounts) {
                bank.addAccount(account);
            }
        }
    }
}