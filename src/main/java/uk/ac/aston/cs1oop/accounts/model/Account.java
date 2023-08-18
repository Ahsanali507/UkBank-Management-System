package uk.ac.aston.cs1oop.accounts.model;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private long id;
    private String name;
    private double balance;
    private List<Transaction> transactions;

    public Account() {
    	this.transactions = new ArrayList<>(); // Initialize the transactions list
    }

    public Account(long id, String name) {
        this.id = id;
        this.name = name;
        this.balance = 0.0;
        this.transactions = new ArrayList<>(); // Initialize the transactions list
    }

    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Other methods for handling the transactions

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> txs) {
        this.transactions = txs;
    }

    public void addTransaction(Transaction tx) {
        transactions.add(tx);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account ID: ").append(id).append("\n");
        sb.append("Account Name: ").append(name).append("\n");
        sb.append("Balance: ").append(balance).append("\n");
        sb.append("Transactions: ");
        if (transactions == null) {
            sb.append("null transactions");
        } else if (transactions.isEmpty()) {
            sb.append("No transactions");
        } else {
            for (Transaction tx : transactions) {
                sb.append(tx.getDescription()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length()); // Remove the last ", "
        }
        return sb.toString();
    }
}