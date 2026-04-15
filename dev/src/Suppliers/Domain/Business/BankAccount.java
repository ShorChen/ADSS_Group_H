package Suppliers.Domain.Business;

public class BankAccount {
    private final String countryCode;
    private final String checkDigits;
    private final String bankCode;
    private final String branchNumber;
    private final String accountNumber;
    private final String internalPadding;

    BankAccount(String IBAN) {
        String iban = IBAN.replaceAll("\\s+|-", "");
        if (iban.length() != 27 || !iban.startsWith("IL"))
            throw new IllegalArgumentException("Invalid Israeli IBAN length or Country Code.");
        countryCode = iban.substring(0, 2);
        checkDigits = iban.substring(2, 4);
        bankCode = iban.substring(4, 7);
        branchNumber = iban.substring(7, 10);
        accountNumber = iban.substring(10, 23);
        internalPadding = iban.substring(23, 27);
    }

    @Override
    public String toString() {
        return countryCode + checkDigits + bankCode + branchNumber + accountNumber + internalPadding;
    }
}