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
        String sql = "SELECT u.*, s.* FROM users u INNER JOIN staff s ON u.userUUID = s.userUUID WHERE u.deletedAt IS NULL AND u.email = ?";
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
                return new Customer(rs.getString("customerUUID"), membership, rs.getInt("point"), rs.getString("userUUID"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("tanggalLahir"), gender, role);
            }else{
                return new Staff(rs.getString("staffUUID"), rs.getString("ein"), rs.getDouble("salary"), rs.getString("userUUID"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("tanggalLahir"), gender, role);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
