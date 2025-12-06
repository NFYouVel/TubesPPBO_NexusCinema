package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import tubes.models.Customer;
import tubes.models.Staff;
import tubes.models.User;
import tubes.models.enums.Genders;
import tubes.models.enums.Membership;
import tubes.models.enums.Roles;
import tubes.models.exceptions.LoginFailedException;
import tubes.utils.Database;
import tubes.utils.UtilUUIDGenerator;

public class UserRepository {
    private static final Connection conn;

    static {
        conn = Database.connect();
    }

    // Login
    public User getUser(String email) throws LoginFailedException {
        Roles role = null;
        String sqlRole = "SELECT u.* FROM users u WHERE u.email = ? AND u.deleted_at IS NULL";
        String sqlStaff = "SELECT u.*, s.* FROM users u INNER JOIN staff s ON u.user_UUID = s.user_UUID WHERE u.deleted_at IS NULL AND u.email = ?";
        String sqlCustomer = "SELECT u.*, c.* FROM users u INNER JOIN customer c ON u.user_UUID = c.user_UUID WHERE u.deleted_at IS NULL AND u.email = ?";
        try {
            PreparedStatement pstmtRole = conn.prepareStatement(sqlRole);
            pstmtRole.setString(1, email);
            ResultSet rsRole = pstmtRole.executeQuery();

            if (!rsRole.next()) {
                throw new LoginFailedException("User with email '" + email + "' not found.");
            }

            role = Roles.valueOf(rsRole.getString("role"));
            Genders gender = Genders.valueOf(rsRole.getString("gender"));

            if (role.equals(Roles.CUSTOMER)) {
                PreparedStatement pstmt = conn.prepareStatement(sqlCustomer);
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.next()) {
                    throw new LoginFailedException("Customer with email '" + email + "' not found.");
                }

                Membership membership = Membership.valueOf(rs.getString("membership"));
                Customer customer = new Customer(rs.getString("name"), rs.getString("email"), rs.getString("password"),
                        rs.getString("phone"), rs.getString("date_of_birth"), gender);
                customer.setCustomerUUID(rs.getString("cust_UUID"));
                customer.setPoint(rs.getInt("point"));
                customer.setMembership(membership);
                return customer;
            } else {
                PreparedStatement pstmt = conn.prepareStatement(sqlStaff);
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.next()) {
                    throw new LoginFailedException("Staff with email '" + email + "' not found.");
                }

                Staff staff = new Staff(rs.getString("name"), rs.getString("email"), rs.getString("password"),
                        rs.getString("phone"), rs.getString("date_of_birth"), gender, role);
                        staff.setStaffUUID(rs.getString("staff_UUID"));
                staff.setEin(rs.getString("EIN"));
                staff.setSalary(rs.getDouble("salary"));
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
        try {
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
    public void insertUser(User user, double salary) {
        String sqlUser = "INSERT INTO users (user_UUID, name, email, password, phone, date_of_birth, gender, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlSortEIN = "SELECT EIN FROM staff WHERE EIN LIKE ? ORDER BY EIN DESC LIMIT 1";
        String sqlStaff = "INSERT INTO staff (staff_UUID, ein, salary, user_UUID) VALUES (?, ?, ?, ?)";
        String userUUID = UtilUUIDGenerator.generateUUID();
        String staffUUID = UtilUUIDGenerator.generateUUID();
        try {
            PreparedStatement pstmtUser = conn.prepareStatement(sqlUser);
            pstmtUser.setString(1, userUUID);
            pstmtUser.setString(2, user.getName());
            pstmtUser.setString(3, user.getEmail());
            pstmtUser.setString(4, user.getPassword());
            pstmtUser.setString(5, user.getPhone());
            pstmtUser.setString(6, user.getDob());
            pstmtUser.setString(7, user.getGender().toString());
            pstmtUser.setString(8, user.getRole().toString());
            pstmtUser.executeUpdate();

            String ein = null;
            PreparedStatement pstmtEIN = conn.prepareStatement(sqlSortEIN);
            if (user.getRole() == Roles.STAFF) {
                pstmtEIN.setString(1, "11%");
                ResultSet rs = pstmtEIN.executeQuery();
                if (rs.next()) {
                    ein = rs.getString("EIN");
                    ein = String.valueOf(Integer.parseInt(ein) + 1);
                } else {
                    ein = "11001";
                }
            } else {
                pstmtEIN.setString(1, "22%");
                ResultSet rs = pstmtEIN.executeQuery();
                if (rs.next()) {
                    ein = rs.getString("EIN");
                    ein = String.valueOf(Integer.parseInt(ein) + 1);
                } else {
                    ein = "22001";
                }
            }

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
