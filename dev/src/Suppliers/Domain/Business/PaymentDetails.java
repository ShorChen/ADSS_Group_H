package Suppliers.Domain.Business;

public class PaymentDetails {
    private BankAccount bankAccount;
    private String paymentTerms;

    PaymentDetails(String IBAN, String paymentTerms) {
        this.bankAccount = new BankAccount(IBAN);
        this.paymentTerms = paymentTerms;
    }

    public BankAccount getBankAccount() { return bankAccount; }
    public void setBankAccount(String IBAN) { this.bankAccount = new BankAccount(IBAN); }
    public String getPaymentTerms() { return paymentTerms; }
    public void setPaymentTerms(String paymentTerms) { this.paymentTerms = paymentTerms; }
}