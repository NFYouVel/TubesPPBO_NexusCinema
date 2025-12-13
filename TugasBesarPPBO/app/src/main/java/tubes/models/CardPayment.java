package tubes.models;

import tubes.models.enums.PaymentMethods;
import tubes.models.interfaces.Payment;

public class CardPayment implements Payment {
    private String cardNumber;
    private PaymentMethods method;

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
        this.method = PaymentMethods.CARD;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    public PaymentMethods getMethod() {
        return method;
    }
    
    @Override
    public boolean validate() {
        return cardNumber != null && cardNumber.matches("\\d{16}");
    }
}
