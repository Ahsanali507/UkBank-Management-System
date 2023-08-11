package uk.ac.aston.cs1oop.accounts.main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.function.Supplier;

import uk.ac.aston.cs1oop.accounts.exceptions.IllegalTransactionException;
import uk.ac.aston.cs1oop.accounts.io.JSONBankReader;
import uk.ac.aston.cs1oop.accounts.model.Account;
import uk.ac.aston.cs1oop.accounts.model.Bank;
import uk.ac.aston.cs1oop.accounts.model.Transaction;
//import uk.ac.aston.cs1oop.accounts.model.Transfer;
//import uk.ac.aston.cs1oop.accounts.interfaces.TransactionProcessor;
import uk.ac.aston.cs1oop.accounts.model.TransactionFactory;

public class BankAccounts {
    private static final String ACCOUNTS_FILE = "resources/accounts.json";
    private static final String TRANSACTIONS_FILE = "resources/transactions.json";

    private Bank bank;

    public BankAccounts() {
        this.bank = new Bank();
    }

    public void initialize() throws IOException {
        initialize(new File(ACCOUNTS_FILE), new File(TRANSACTIONS_FILE));
    }

    public void initialize(File accountsFile, File transactionsFile) throws IOException {
        JSONBankReader bankReader = new JSONBankReader(bank);
        try {
            bankReader.readAccountsJSON(accountsFile);
        } catch (IOException ex) {
            throw new IOException("Error while loading accounts", ex);
        }

        try {
            bankReader.readTransactionsJSON(transactionsFile);
        } catch (IOException ex) {
            throw new IOException("Error while loading transactions", ex);
        }
    }

    public void commandLoop(Supplier<String> lines) {
        String line = lines.get();
        while (line != null && !"quit".equals(line)) {
            if ("view".equals(line)) {
                System.out.println(bank);
            } else {
                try {
                    Transaction tx = TransactionFactory.getTransaction(line);
                    doTransaction(tx);
                } catch (Exception e) {
                    System.out.println("Transaction failed: " + e.getMessage());
                }
            }
            line = lines.get();
        }
        System.out.println("Bye. Thanks for visiting the bank");
    }
    
    public void doTransaction(Transaction tx) {
        Account account = bank.getAccount(tx.getAccountId());

        if (account == null) {
            System.out.println("Account not found for transaction: " + tx.getDescription());
            return; // Reject the transaction
        }

        try {
            account.addTransaction(tx);
            tx.setAccount(account); // Set the associated account for the transaction
            tx.execute(bank);

            System.out.println("Transaction was completed.");
        } catch (IllegalTransactionException e) {
            System.out.println("Transaction failed: " + e.getMessage());
            tx.markRejected();
        }
    }



    public static void main(String... args) {
        BankAccounts ba = new BankAccounts();
        try {
            ba.initialize();
            final Scanner sc = new Scanner(System.in);
            ba.commandLoop(() -> prompt(ba.bank, sc));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String prompt(Bank bank, Scanner scanner) {
        System.out.println("Transaction Processing.  Options are:");
        System.out.println("View: Enter view to view all accounts");
        System.out.println("Deposit/Withdraw: Enter the account id, a description, and an amount (separated by commas)");
        System.out.println(
                "Transfer: Enter the account id, a description, an amount, and the ID of a beneficiary account (separated by commas)");
        System.out.println("Exit: Enter quit");

        return scanner.hasNextLine() ? scanner.nextLine().trim() : null;
    }
}