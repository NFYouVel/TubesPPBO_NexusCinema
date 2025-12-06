package tubes.models;

import tubes.models.enums.Genders;
import tubes.models.enums.Roles;

public class Staff extends User {
    private String staffUUID;
    private String ein; //Employer Identification Number
    private double salary;

    public Staff(String name, String email, String password, String phone, String dob, Genders gender, Roles role) {
        super(name, email, password, phone, dob, gender, role);
        this.ein = "00000";
        this.salary = 0;
    }
    
    public String getStaffUUID() {
        return this.staffUUID;
    }

    public void setStaffUUID(String staffUUID) {
        this.staffUUID = staffUUID;
    }

    public String getEin() {
        return this.ein;
    }

    public void setEin(String ein) {
        this.ein = ein;
    }

    public double getSalary() {
        return this.salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String getUserUUID() {
        return this.staffUUID;
    }
}
