package uk.ac.aston.cs1oop.accounts.model;

import uk.ac.aston.cs1oop.accounts.exceptions.IllegalTransactionException;

public class Transfer extends Transaction {
    private long beneficiaryAccountId;

    public Transfer(long accountId, String description, double amount, long beneficiaryAccountId) {
        super(accountId, description, amount);
        this.beneficiaryAccountId = beneficiaryAccountId;
        
        // Add the following check for the same accounts
        if (accountId == beneficiaryAccountId) {
            throw new IllegalArgumentException("Account and Beneficiary Accounts must be different for a Transfer.");
        }
        
     // Add the following check for negative amount
        if (amount < 0) {
            throw new IllegalArgumentException("You cannot have a Transfer with a negative amount.");
        }
        
     // Add the following check for zero amount
        if (amount == 0) {
            throw new IllegalArgumentException("You cannot have a Transfer with a zero amount.");
        }
    }


    public long getBeneficiaryAccountId() {
        if (getAccountId() == beneficiaryAccountId) {
            throw new IllegalArgumentException("Account and Beneficiary Accounts must be different for a Transfer.");
        }
        return beneficiaryAccountId;
    }

    public void setBeneficiaryAccountId(long beneficiaryAccountId) {
        this.beneficiaryAccountId = beneficiaryAccountId;
    }
    
    @Override
    public void execute(Bank bank) throws IllegalTransactionException {
        if (!isNew()) {
            throw new IllegalArgumentException("Transaction is not new and cannot be processed.");
        }

        double transactionAmount = getAmount();
        Account sourceAccount = getAccount();
        double sourceAccountBalance = sourceAccount.getBalance();
        
        long beneficiaryAccountId = getBeneficiaryAccountId();
        if (sourceAccount.getId() == beneficiaryAccountId) {
            // Transfer to the same account is not allowed
            markRejected();
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }

        // Check if there is enough balance in the source account to complete the transfer
        if (Math.abs(transactionAmount) > sourceAccountBalance) {
            // Mark the transfer as rejected due to insufficient balance
            markRejected();
//            throw new IllegalTransactionException("Insufficient funds in the source account.", sourceAccount, this);
            throw new IllegalArgumentException("Insufficient funds in the source account.");
        }

        // Retrieve the beneficiary account from the bank
        Account beneficiaryAccount = bank.getAccount(getBeneficiaryAccountId());

        if (beneficiaryAccount == null) {
            // Mark the transfer as rejected due to beneficiary account not found
            markRejected();
//            throw new IllegalTransactionException("Beneficiary account not found.", sourceAccount, this);
            throw new IllegalArgumentException("Beneficiary account not found.");
        }

        // Transfer the amount from the source account to the beneficiary account
        sourceAccount.setBalance(sourceAccountBalance - transactionAmount);
        beneficiaryAccount.setBalance(beneficiaryAccount.getBalance() + transactionAmount);

        // Mark the transfer as completed
        markCompleted();
    }



    public String toString() {
        return super.toString() + ", Beneficiary Account ID: " + beneficiaryAccountId;
    }
}