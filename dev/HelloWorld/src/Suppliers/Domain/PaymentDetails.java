package Suppliers.Domain;

public class PaymentDetails {
    private BankAccount bankAccount;
    private String paymentTerms;

    public PaymentDetails(BankAccount bankAccount, String paymentTerms) {
        this.bankAccount = bankAccount;
        this.paymentTerms = paymentTerms;
    }

    public BankAccount getBankAccount() { return bankAccount; }
    public void setBankAccount(String IBAN) {bankAccount = new BankAccount(IBAN);}
    public String getPaymentTerms() { return paymentTerms; }
    public void setPaymentTerms(String paymentTerms) { this.paymentTerms = paymentTerms; }
}