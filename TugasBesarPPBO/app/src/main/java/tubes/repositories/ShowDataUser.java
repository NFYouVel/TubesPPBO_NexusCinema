package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.management.relation.Role;

import tubes.models.Customer;
import tubes.models.Staff;
import tubes.models.User;
import tubes.models.enums.Genders;
import tubes.models.enums.Membership;
import tubes.models.enums.Roles;
import tubes.models.exceptions.EmptyListRepository;
import tubes.utils.Database;

public class ShowDataUser {
    private static final Connection conn;
    private List<User> users;

    static {
        conn = Database.connect();
    }

    public ShowDataUser() {
        users = new ArrayList<>();
    }

    public List<User> getAllUsers() throws EmptyListRepository {
        users.clear();

        try {
            String sql = "SELECT u.name, u.email, u.phone, u.date_of_birth, u.gender, u.role FROM users u WHERE u.deleted_at IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Roles roleEnum = Roles.valueOf(rs.getString("role"));
                Genders genderEnum = Genders.valueOf(rs.getString("gender"));
                if (roleEnum == Roles.CUSTOMER) {
                    User user = new Customer(rs.getString("name"), rs.getString("email"), "", rs.getString("phone"), rs.getString("date_of_birth"), genderEnum);
                    users.add(user);
                } else if (roleEnum == Roles.STAFF || roleEnum == Roles.MANAGER) {
                    User user = new Staff(rs.getString("name"), rs.getString("email"), "", rs.getString("phone"), rs.getString("date_of_birth"), genderEnum, roleEnum);
                    users.add(user);
                }
            }

            if (users.isEmpty()) {
                throw new EmptyListRepository("Show User");
            } 

            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
