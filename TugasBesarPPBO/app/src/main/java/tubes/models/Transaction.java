package tubes.models;

import tubes.models.enums.Membership;
import tubes.models.enums.PaymentMethods;
import tubes.models.enums.StudioTypes;

public class Transaction {
    private String transactionUUID;
    private String transactionDateTime;
    private String studioNumber;
    private StudioTypes studioType;
    private String movieTitle;
    private String seatNumber;
    private String customerName;
    private Membership membership;
    private PaymentMethods paymentMethods;
    private int price;

    public Transaction(PaymentMethods paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public Transaction(String transactionUUID, String transactionDateTime, String studioNumber, StudioTypes studioType, String movieTitle,
            String seatNumber, String customerName, Membership membership, PaymentMethods paymentMethods, int price) {
        this.transactionUUID = transactionUUID;
        this.transactionDateTime = transactionDateTime;
        this.studioNumber = studioNumber;
        this.studioType = studioType;
        this.movieTitle = movieTitle;
        this.seatNumber = seatNumber;
        this.customerName = customerName;
        this.membership = membership;
        this.paymentMethods = paymentMethods;
        this.price = price;
    }

    public String getTransactionUUID() {
        return this.transactionUUID;
    }

    public void setTransactionUUID(String transactionUUID) {
        this.transactionUUID = transactionUUID;
    }

    public String getTransactionDateTime() {
        return this.transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getStudioNumber() {
        return this.studioNumber;
    }

    public void setStudioNumber(String studioNumber) {
        this.studioNumber = studioNumber;
    }

    public StudioTypes getStudioType() {
        return this.studioType;
    }

    public void setStudioType(StudioTypes studioType) {
        this.studioType = studioType;
    }

    public String getMovieTitle() {
        return this.movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getSeatNumber() {
        return this.seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Membership getMembership() {
        return this.membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public PaymentMethods getPaymentMethods() {
        return this.paymentMethods;
    }

    public void setPaymentMethods(PaymentMethods paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

}
