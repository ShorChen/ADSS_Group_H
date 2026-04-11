package Suppliers.Domain;

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

    public String getCountryCode() {
        return countryCode;
    }
    public String getCheckDigits() {
        return checkDigits;
    }
    public String getBankCode() {
        return bankCode;
    }
    public String getBranchNumber() {
        return branchNumber;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public String getInternalPadding() {
        return internalPadding;
    }

    @Override
    public String toString() {
        return countryCode + checkDigits + bankCode + branchNumber + accountNumber + internalPadding;
    }
}