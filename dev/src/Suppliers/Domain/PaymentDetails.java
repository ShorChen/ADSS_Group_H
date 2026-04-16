package Suppliers.Domain;

import java.io.Serial;
import java.io.Serializable;

public class PaymentDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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