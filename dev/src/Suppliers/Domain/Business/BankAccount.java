package Suppliers.Domain.Business;

/**
 * We assumed that our company is based in israel, along with its suppliers.
 */
public class BankAccount {
    private final String countryCode, checkDigits, bankCode, branchNumber, accountNumber, internalPadding;

    public BankAccount(String IBAN) {
        String iban = IBAN.replaceAll("\\s+|-", "");
        if (iban.length() != 27 || !iban.startsWith("IL"))
            throw new IllegalArgumentException("Invalid Israeli IBAN length or Country Code.");
        checkDigits = iban.substring(2, 4);
        bankCode = iban.substring(4, 7);
        branchNumber = iban.substring(7, 10);
        accountNumber = iban.substring(10, 23);
        internalPadding = iban.substring(23, 27);
        countryCode = iban.substring(0, 2);
    }

    public void makeTransaction(double cost) {
        //TODO
    }

    @Override
    public String toString() {
        return countryCode + checkDigits + bankCode + branchNumber + accountNumber + internalPadding;
    }
}