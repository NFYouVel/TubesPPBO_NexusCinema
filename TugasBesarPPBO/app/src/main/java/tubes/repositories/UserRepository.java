package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import tubes.models.Staff;
import tubes.models.Users;
import tubes.models.enums.Genders;
import tubes.models.enums.Roles;
import tubes.utils.Database;

public class UserRepository {
    private static final Connection conn;
    
    static{
        conn = Database.connect();
    }

    // Login
    public List<Users> getUser() {
        String sql = "SELECT u.*, s.* FROM users u INNER JOIN staff s ON u.userUUID = s.userUUID";
        List<Users> users = new ArrayList<>();
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Genders gender = Genders.valueOf(rs.getString("gender"));
                Roles role = Roles.valueOf(rs.getString("role"));
                if(rs.getString("role").equalsIgnoreCase("STAFF"))
                users.add(new Staff(rs.getString("staffUUID"), rs.getString("ein"), rs.getDouble("salary"), rs.getString("userUUID"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("phone"), rs.getString("tanggalLahir"), gender, role));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
