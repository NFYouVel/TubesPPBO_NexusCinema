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

public class UserRepository {
    private static final Connection conn;
    
    static{
        conn = Database.connect();
    }

    // Login
    public User getUser(String email) throws LoginFailedException {
        Roles role = null;
        String sqlRole = "SELECT u.* FROM users WHERE u.email = ? AND u.deletedAt IS NULL";
        String sql = "SELECT u.*, s.* FROM users u INNER JOIN staff s ON u.user_UUID = s.user_UUID WHERE u.deletedAt IS NULL AND u.email = ?";
        try{
            PreparedStatement pstmtRole = conn.prepareStatement(sqlRole);
            pstmtRole.setString(1, email);
            ResultSet rsRole = pstmtRole.executeQuery();
            
            if (!rsRole.next()) {
                throw new LoginFailedException("User with email '" + email + "' not found.");
            }

            role = Roles.valueOf(rsRole.getString("role"));

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new LoginFailedException("User with email '" + email + "' not found.");
            }
            
            Genders gender = Genders.valueOf(rs.getString("gender"));

            if(role.equals(Roles.CUSTOMER)){
                Membership membership = Membership.valueOf(rs.getString("membership"));
                return new Customer(rs.getString("customer_UUID"), membership, rs.getInt("point"), rs.getString("user_UUID"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("tanggalLahir"), gender, role);
            }else{
                return new Staff(rs.getString("staff_UUID"), rs.getString("ein"), rs.getDouble("salary"), rs.getString("user_UUID"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("tanggalLahir"), gender, role);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Sign Up
    public void insertUser(User user) {
        String sql = "INSERT INTO users (user_UUID, name, email, password, phone, tanggalLahir, gender, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, user.getUserUUID());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getEmail());
        pstmt.setString(4, user.getPassword());
        pstmt.setString(5, user.getPhone());
        pstmt.setString(6, user.getDob());
        pstmt.setString(7, user.getgender().toString());
        pstmt.setString(8, user.getRole().toString());
        pstmt.executeUpdate();
        
        if(user.getRole().equals(Roles.CUSTOMER)){
            String sqlCustomer = "INSERT INTO customers (customer_UUID, point, user_UUID) VALUES (?, ?, ?)";
            PreparedStatement pstmtCustomer = conn.prepareStatement(sqlCustomer);
            pstmtCustomer.setString(1, user.getUserUUID());
            pstmtCustomer.setInt(2, 0);
            pstmtCustomer.setString(3, user.getUserUUID());
            pstmtCustomer.executeUpdate();
        }else{
            String sqlStaff = "INSERT INTO staff (staff_UUID, ein, salary, user_UUID) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmtStaff = conn.prepareStatement(sqlStaff);
            pstmtStaff.setString(1, user.getUserUUID());
            pstmtStaff.setString(2, null);
            pstmtStaff.setDouble(3, 0);
            pstmtStaff.setString(4, user.getUserUUID());
            pstmtStaff.executeUpdate();
        }
        
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
