package uk.ac.aston.cs1oop.accounts.exceptions;

import uk.ac.aston.cs1oop.accounts.model.Account;
import uk.ac.aston.cs1oop.accounts.model.Transaction;

public class IllegalTransactionException extends Exception {
	private static final long serialVersionUID = 1L;
    private Account account;
    private Transaction transaction;

    public IllegalTransactionException(String msg, Account account, Transaction transaction) {
        super(msg);
        this.account = account;
        this.transaction = transaction;
    }

    public Account getAccount() {
        return account;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}