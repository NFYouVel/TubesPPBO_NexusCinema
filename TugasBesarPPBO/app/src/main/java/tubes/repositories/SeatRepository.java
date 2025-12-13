package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tubes.models.Seat;
import tubes.models.exceptions.EmptyListException;
import tubes.utils.Database;

public class SeatRepository {

    private List<Seat> seats;
    private static final Connection conn;

    static {
        conn = Database.connect();
    }

    public SeatRepository() {
        seats = new ArrayList<>();
    }

    public List<Seat> getAllOccupiedSeats(String showUUID) throws EmptyListException {

        List<Seat> resultSeats = new ArrayList<>();

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT t.show_UUID, t.seats_UUID, s.seats_num, s.seats_row, s.seats_column FROM ticket AS t JOIN seats AS s ON t.seats_UUID = s.seats_UUID WHERE t.show_UUID = ? AND t.deleted_at IS NULL AND s.deleted_at IS NULL;");
            stmt.setString(1, showUUID);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EmptyListException("Get Occupied Seat");
            } else {
                // seats.clear(); // <-- HAPUS BARIS INI
                do {
                    Seat temp = new Seat(rs.getString("seats_num"), rs.getInt("seats_row"), rs.getInt("seats_column"));
                    // Gunakan List lokal
                    resultSeats.add(temp);
                } while (rs.next());
                System.out.println("Occupied Seats: " + resultSeats.size());
                return resultSeats; // <-- Kembalikan List lokal baru
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Seat> getAllAvailableSeats(String showUUID) throws EmptyListException {
        int maxCapacity = getMaximumCapacity(showUUID);

        List<Seat> resultSeats = new ArrayList<>();

        try {

            PreparedStatement stmt = conn.prepareStatement("""
                                                           SELECT seats_UUID, seats_num, seats_row, seats_column\r
                                                           FROM seats \r
                                                           WHERE (seats_row < ? OR (seats_row <= ? AND seats_column < ?)) AND seats.deleted_at IS NULL ORDER BY seats_row ASC, seats_column ASC;""" //

            );
            stmt.setInt(1, maxCapacity / 10);
            stmt.setInt(2, maxCapacity / 10);
            stmt.setInt(3, maxCapacity % 10);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EmptyListException("Get Available Seat");
            } else {
                do {
                    Seat temp = new Seat(rs.getString("seats_num"), rs.getInt("seats_row"), rs.getInt("seats_column"));
                    temp.setSeatsUUID(rs.getString("seats_UUID"));
                    // Gunakan List lokal
                    resultSeats.add(temp);
                } while (rs.next());
                System.out.println("Available Seats: " + resultSeats.size());
                return resultSeats; // <-- Kembalikan List lokal baru
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getMaximumCapacity(String showUUID) throws EmptyListException {
        try {
            seats.clear();

            PreparedStatement stmt = conn.prepareStatement("SELECT st.show_UUID, st.studio_UUID, seats.seats_num, seats.seats_row, seats.seats_column FROM show_time as st JOIN studio s ON st.studio_UUID = s.studio_UUID JOIN seats ON seats.seats_UUID = s.seats_UUID WHERE st.show_UUID = ? AND st.deleted_at IS NULL AND seats.deleted_at IS NULL;");
            stmt.setString(1, showUUID);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EmptyListException("Seat");
            } else {
                System.out.println((rs.getInt("seats_row")) * 10 + rs.getInt("seats_column") + 1);
                return ((rs.getInt("seats_row")) * 10 + rs.getInt("seats_column") + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
