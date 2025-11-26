package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import tubes.models.Customer;
import tubes.models.Staff;
import tubes.models.Users;
import tubes.models.enums.Genders;
import tubes.models.enums.Membership;
import tubes.models.enums.Roles;
import tubes.utils.Database;

public class UserRepository {
    private static final Connection conn;
    
    static{
        conn = Database.connect();
    }

    // Login
    public List<Users> getUser(String email) {
        Roles role = null;
        String sqlRole = "SELECT u.* FROM users WHERE u.email = ? AND u.deletedAt IS NULL";
        String sql = "SELECT u.*, s.* FROM users u INNER JOIN staff s ON u.userUUID = s.userUUID WHERE u.deletedAt IS NULL";
        List<Users> users = new ArrayList<>();
        try{
            PreparedStatement pstmtRole = conn.prepareStatement(sqlRole);
            pstmtRole.setString(1, email);
            ResultSet rsRole = pstmtRole.executeQuery();
            
            while (rsRole.next()) {
                role = Roles.valueOf(rsRole.getString("role"));
            }

            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Genders gender = Genders.valueOf(rs.getString("gender"));
                if(role.equals(Roles.CUSTOMER)){
                    Membership membership = Membership.valueOf(rs.getString("membership"));
                    users.add(new Customer(rs.getString("customerUUID"), membership, rs.getInt("point"), rs.getString("userUUID"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("tanggalLahir"), gender, role));
                }else{
                    users.add(new Staff(rs.getString("staffUUID"), rs.getString("ein"), rs.getDouble("salary"), rs.getString("userUUID"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("tanggalLahir"), gender, role));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
