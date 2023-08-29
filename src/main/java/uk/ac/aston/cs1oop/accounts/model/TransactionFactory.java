package uk.ac.aston.cs1oop.accounts.model;

public class TransactionFactory {
    public static Transaction getTransaction(String idData) {
        String[] parts = idData.split(",");
        if (parts.length < 3 || parts.length > 4) {
            throw new IllegalArgumentException("Invalid transaction input's data format: " + idData);
        }

        long accountId = Long.parseLong(parts[0]);
        String description = parts[1];
        double amount = Double.parseDouble(parts[2]);

        if (parts.length == 3) {
            return new Transaction(accountId, description, amount);
        } else if (parts.length == 4) {
            long beneficiaryAccountId = Long.parseLong(parts[3]);
            return new Transfer(accountId, description, amount, beneficiaryAccountId);
        }

        throw new IllegalArgumentException("Invalid transaction input's data format: " + idData);
    }
}