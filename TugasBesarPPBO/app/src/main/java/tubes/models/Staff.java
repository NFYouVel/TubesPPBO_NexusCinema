package tubes.models;

import tubes.models.enums.Genders;
import tubes.models.enums.Roles;

public class Staff extends Users {
    private String staffUUID;
    private String ein; //Employer Identification Number
    private double salary;

    public Staff(String staffUUID, String ein, double salary, String user_UUID, String name, String email, String password, String phone, String tanggalLahir, Genders jenisKelamin, Roles role) {
        super(user_UUID, name, email, password, phone, tanggalLahir, jenisKelamin, role);
        this.staffUUID = staffUUID;
        this.ein = ein;
        this.salary = salary;
    }
    
    public String getStaffUUID() {
        return this.staffUUID;
    }

    public void setStaffUUID(String staffUUID) {
        this.staffUUID = staffUUID;
    }

    public String getein() {
        return this.ein;
    }

    public void setein(String ein) {
        this.ein = ein;
    }

    public double getSalary() {
        return this.salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
