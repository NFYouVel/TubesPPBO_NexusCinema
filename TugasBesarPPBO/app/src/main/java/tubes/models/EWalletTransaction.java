package tubes.models;

import tubes.models.enums.PaymentMethods;
import tubes.models.interfaces.Payment;

public class EWalletTransaction extends Transaction implements Payment {

    public EWalletTransaction(String transactionUUID, String paymentID) {
        super(transactionUUID, paymentID, PaymentMethods.E_WALLET);
    }
    
    @Override
    public boolean validate() {
        return super.paymentID != null && super.paymentID.matches("08\\d{8,11}");
    }
}
