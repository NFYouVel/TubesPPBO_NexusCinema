package tubes.models;

import tubes.models.enums.Membership;
import tubes.models.enums.PaymentMethods;
import tubes.models.enums.StudioTypes;
import tubes.models.interfaces.Payment;

public class CardTransaction extends Transaction implements Payment {

    public CardTransaction(String transactionUUID, String paymentID) {
        super(transactionUUID, paymentID, PaymentMethods.CARD);
    }
    
    @Override
    public boolean validate() {
        return super.paymentID != null && super.paymentID.matches("\\d{16}");
    }
}
