package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tubes.models.Customer;
import tubes.models.Staff;
import tubes.models.User;
import tubes.models.enums.Genders;
import tubes.models.enums.Membership;
import tubes.models.enums.Roles;
import tubes.models.execptions.LoginFailedException;
import tubes.utils.Database;
import tubes.utils.UtilUUIDGenerator;

public class UserRepository {
    private static final Connection conn;
    
    static{
        conn = Database.connect();
    }

    // Login
    public User getUser(String email) throws LoginFailedException {
        Roles role = null;
        String sqlRole = "SELECT u.* FROM users u WHERE u.email = ? AND u.deleted_at IS NULL";
        String sqlStaff = "SELECT u.*, s.* FROM users u INNER JOIN staff s ON u.user_UUID = s.user_UUID WHERE u.deleted_at IS NULL AND u.email = ?";
        String sqlCustomer = "SELECT u.*, c.* FROM users u INNER JOIN customer c ON u.user_UUID = c.user_UUID WHERE u.deleted_at IS NULL AND u.email = ?";
        try{
            PreparedStatement pstmtRole = conn.prepareStatement(sqlRole);
            pstmtRole.setString(1, email);
            ResultSet rsRole = pstmtRole.executeQuery();
            
            if (!rsRole.next()) {
                throw new LoginFailedException("User with email '" + email + "' not found.");
            }

            role = Roles.valueOf(rsRole.getString("role"));
            Genders gender = Genders.valueOf(rsRole.getString("gender"));
            
            if(role.equals(Roles.CUSTOMER)){
                PreparedStatement pstmt = conn.prepareStatement(sqlCustomer);
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();
    
                if (!rs.next()) {
                    throw new LoginFailedException("Customer with email '" + email + "' not found.");
                }

                Membership membership = Membership.valueOf(rs.getString("membership"));
                Customer customer = new Customer(rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("date_of_birth"), gender);
                customer.setCustomerUUID(rs.getString("cust_UUID"));
                customer.setPoint(rs.getInt("point"));
                customer.setMembership(membership);
                return customer;
            }else{
                PreparedStatement pstmt = conn.prepareStatement(sqlStaff);
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.next()) {
                    throw new LoginFailedException("Staff with email '" + email + "' not found.");
                }

                Staff staff = new Staff(rs.getString("ein"), rs.getDouble("salary"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("date_of_birth"), gender, role);
                staff.setStaffUUID(rs.getString("staff_UUID"));
                return staff;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Sign Up Customer
    public void insertUser(User user) {
        String sql = "INSERT INTO users (user_UUID, name, email, password, phone, date_of_birth, gender, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String userUUID = UtilUUIDGenerator.generateUUID();
        String customerUUID = UtilUUIDGenerator.generateUUID();
        try{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userUUID);
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getEmail());
        pstmt.setString(4, user.getPassword());
        pstmt.setString(5, user.getPhone());
        pstmt.setString(6, user.getDob());
        pstmt.setString(7, user.getGender().toString());
        pstmt.setString(8, user.getRole().toString());
        pstmt.executeUpdate();
        
        String sqlCustomer = "INSERT INTO customer (cust_UUID, user_UUID, point, membership) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmtCustomer = conn.prepareStatement(sqlCustomer);
        pstmtCustomer.setString(1, customerUUID);
        pstmtCustomer.setString(2, userUUID);
        pstmtCustomer.setInt(3, 0);
        pstmtCustomer.setString(4, Membership.REGULAR.toString());
        pstmtCustomer.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sign Up Staff
    public void insertUser(User user, String ein, double salary) {
        String sql = "INSERT INTO users (user_UUID, name, email, password, phone, date_of_birth, gender, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String userUUID = UtilUUIDGenerator.generateUUID();
        String staffUUID = UtilUUIDGenerator.generateUUID();
        try{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userUUID);
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getEmail());
        pstmt.setString(4, user.getPassword());
        pstmt.setString(5, user.getPhone());
        pstmt.setString(6, user.getDob());
        pstmt.setString(7, user.getGender().toString());
        pstmt.setString(8, user.getRole().toString());
        pstmt.executeUpdate();
        
        String sqlStaff = "INSERT INTO staff (staff_UUID, ein, salary, user_UUID) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmtStaff = conn.prepareStatement(sqlStaff);
        pstmtStaff.setString(1, staffUUID);
        pstmtStaff.setString(2, ein);
        pstmtStaff.setDouble(3, salary);
        pstmtStaff.setString(4, userUUID);
        pstmtStaff.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
