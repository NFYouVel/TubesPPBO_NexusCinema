package tubes.models;

import tubes.models.enums.Genders;
import tubes.models.enums.Roles;

public abstract class User {
    private String userUUID;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String dob;
    private Genders gender;
    private Roles role;

    public User(String name, String email, String password, String phone, String dob, Genders gender, Roles role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
        this.role = role;
    }

    public String getUserUUID() {
        return this.userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
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

    public String getDob() {
        return this.dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Genders getGender() {
        return this.gender;
    }

    public void setGender(Genders gender) {
        this.gender = gender;
    }

    public Roles getRole() {
        return this.role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
