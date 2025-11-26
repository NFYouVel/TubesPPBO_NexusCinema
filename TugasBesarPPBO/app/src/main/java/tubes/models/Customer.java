package tubes.models;

import tubes.models.enums.Genders;
import tubes.models.enums.Membership;
import tubes.models.enums.Roles;

public class Customer extends Users {
    private String customerUUID;
    private Membership membership;
    private int point;

    public Customer(String customerUUID, Membership membership, int point, String user_UUID, String name, String email, String password, String phone, String tanggalLahir, Genders jenisKelamin, Roles role) {
        super(user_UUID, name, email, password, phone, tanggalLahir, jenisKelamin, role);
        this.customerUUID = customerUUID;
        this.membership = membership;
        this.point = point;
    }

    public String getCustomerUUID() {
        return this.customerUUID;
    }

    public void setCustomerUUID(String customerUUID) {
        this.customerUUID = customerUUID;
    }

    public Membership getMembership() {
        return this.membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }
    
    public int getPoint() {
        return this.point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
