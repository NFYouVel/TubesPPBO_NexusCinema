package tubes.models;

import tubes.models.enums.PaymentMethods;
import tubes.models.interfaces.Payment;

public class EWalletPayment implements Payment {
    private String phoneNumber;
    private PaymentMethods method;
    
    public EWalletPayment(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.method = PaymentMethods.E_WALLET;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    @Override
    public boolean validate() {
        return phoneNumber.matches("08\\d{8,11}");
    }
}
