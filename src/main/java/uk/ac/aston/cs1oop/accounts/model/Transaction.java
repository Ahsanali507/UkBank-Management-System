package uk.ac.aston.cs1oop.accounts.model;

import uk.ac.aston.cs1oop.accounts.exceptions.IllegalTransactionException;

public class Transaction {
    private long accountId;
    private String description;
    private double amount;
    private boolean completed;
    private boolean rejected;
    private Account account;

    public Transaction() {
        // Default constructor
    }

    public Transaction(long accountId, String description, double amount) {
        this.accountId = accountId;
        this.description = description;
        this.amount = amount;
        this.completed = false;
        this.rejected = false;
        
        // Add the following check for zero amount
        if (amount == 0) {
            throw new IllegalArgumentException("You cannot create a Transaction with zero amount.");
        }
    }

    

    // New method to set the associated account
    public void setAccount(Account account) {
        this.account = account;
    }

    // New method to get the associated account
    public Account getAccount() {
        return account;
    }
      

    // Getters and Setters for accountId, description, and amount

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("You cannot set a Transaction to have zero or negative amount.");
        }
        this.amount = amount;
    }

    // Methods to handle transaction status

    public boolean markCompleted() {
        if (!completed && !rejected) {
            completed = true;
            return true;
        }
        return false;
    }

    public boolean markRejected() {
        if (!completed && !rejected) {
            rejected = true;
            return true;
        }
        return false;
    }

    public boolean isNew() {
        return !completed && !rejected;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isRejected() {
        return rejected;
    }
    
    public void execute(Bank bank) throws IllegalTransactionException {
        try {
            if (!isNew()) {
                throw new IllegalArgumentException("Transaction is not new and cannot be processed.");
            }

            double transactionAmount = getAmount();
            Account account = getAccount();
            double accountBalance = account.getBalance();

            if (transactionAmount >= 0) {
                // Deposit (positive amount) transaction
                // Process the transaction by adding the amount to the account balance
                double newBalance = accountBalance + transactionAmount;
                account.setBalance(newBalance);
            } else {
                // Withdrawal (negative amount) transaction
                if (Math.abs(transactionAmount) <= accountBalance) {
                    // Process the transaction by subtracting the amount from the account balance
                    double newBalance = accountBalance + transactionAmount;
                    account.setBalance(newBalance);
                } else {
                    // Reject the transaction due to insufficient funds
                    markRejected();
                    throw new IllegalTransactionException("Insufficient balance.", account, this);
                }
            }

            markCompleted();
        } catch (IllegalArgumentException e) {
            markRejected();
            throw new IllegalTransactionException("Transaction failed: " + e.getMessage(), null, this);
        }
    }


    public String toString() {
        return "Account ID: " + accountId + ", Description: " + description + ", Amount: " + amount;
    }
}