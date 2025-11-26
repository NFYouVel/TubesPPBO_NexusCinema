package tubes.models;

import tubes.models.enums.PaymentMethods;

public class Transaction {
    private String transactionUUID;
    private PaymentMethods paymentMethods;

    public Transaction(PaymentMethods paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String getTransactionUUID() {
        return this.transactionUUID;
    }

    public void setTransactionUUID(String transactionUUID) {
        this.transactionUUID = transactionUUID;
    }

    public PaymentMethods getPaymentMethods() {
        return this.paymentMethods;
    }

    public void setPaymentMethods(PaymentMethods paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

}
