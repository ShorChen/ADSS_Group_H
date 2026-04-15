package Suppliers.Domain.Business;

public class PaymentDetails {
    private BankAccount bankAccount;
    private String paymentTerms;

    public PaymentDetails(String IBAN, String paymentTerms) {
        bankAccount = new BankAccount(IBAN);
        this.paymentTerms = paymentTerms;
    }

    public BankAccount getBankAccount() { return bankAccount; }
    public void setBankAccount(String IBAN) {bankAccount = new BankAccount(IBAN);}
    public String getPaymentTerms() { return paymentTerms; }
    public void setPaymentTerms(String paymentTerms) { this.paymentTerms = paymentTerms; }
}