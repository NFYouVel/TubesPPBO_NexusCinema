package tubes.models;

import tubes.models.enums.Genders;
import tubes.models.enums.Roles;

public class Users {
    private String user_UUID;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String dob;
    private Genders gender;
    private Roles role;

    public Users(String user_UUID, String name, String email, String password, String phone, String dob, Genders gender, Roles role) {
        this.user_UUID = user_UUID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.dob = dob;
        this.role = role;
    }

    public String getUser_UUID() {
        return this.user_UUID;
    }

    public void setUser_UUID(String user_UUID) {
        this.user_UUID = user_UUID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getdob() {
        return this.dob;
    }

    public void setdob(String dob) {
        this.dob = dob;
    }

    public Genders getgender() {
        return this.gender;
    }

    public void setgender(Genders gender) {
        this.gender = gender;
    }

    public Roles getRole() {
        return this.role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
