// TransactionProcessor.java

package uk.ac.aston.cs1oop.accounts.interfaces;


import uk.ac.aston.cs1oop.accounts.model.Transaction;
import uk.ac.aston.cs1oop.accounts.exceptions.IllegalTransactionException;

public interface TransactionProcessor {
	 void doTransaction(Transaction tx) throws IllegalTransactionException;
}