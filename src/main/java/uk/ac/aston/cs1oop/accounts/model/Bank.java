package uk.ac.aston.cs1oop.accounts.model;

import java.util.HashMap;
import java.util.Map;
import uk.ac.aston.cs1oop.accounts.exceptions.IllegalTransactionException;
import uk.ac.aston.cs1oop.accounts.interfaces.TransactionProcessor;

public class Bank implements TransactionProcessor {
    private Map<Long, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
    }

    public void addAccount(Account account) {
        accounts.put(account.getId(), account);
    }

    public Account getAccount(long id) {
        return accounts.get(id);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Accounts in the bank:\n");
        for (Account account : accounts.values()) {
            sb.append(account).append("\n");
        }
        return sb.toString();
    }

    public Map<Long, Account> getAccounts() {
        return accounts;
    }
    
    @Override
    public void doTransaction(Transaction tx) throws IllegalTransactionException {
        // Refuse to run transactions that are not new
        if (!tx.isNew()) {
            throw new IllegalArgumentException("Transaction is not new and cannot be processed.");
        }

        // Get the account ID from the transaction
        long accountId = tx.getAccountId();

        // Check if the account ID is part of this Bank
        Account account = getAccount(accountId);
        if (account == null) {
            throw new IllegalTransactionException("Account not found.", null, tx);
        }

        // Add the transaction to the list of transactions of its matching Account
        account.addTransaction(tx);

        // Ask the transaction to execute() itself on this Bank
        tx.execute(this);
    }
}