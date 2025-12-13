package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import tubes.models.Seat;
import tubes.utils.Database;

public class TicketRepository {
    private static final Connection conn;

    static {
        conn = Database.connect();
    }

    public void processTicket(String transUUID, String showUUID, List<Seat> selectedSeats) {
        try {
            PreparedStatement psmt = conn.prepareStatement("INSERT INTO ticket (show_UUID, seats_UUID, status, trans_UUID) VALUES (?,?,?,?);");
            
            for(Seat seat : selectedSeats) {
                psmt.setString(1, showUUID);
                psmt.setString(2, seat.getSeatsUUID());
                psmt.setString(3, "BOOKED");
                psmt.setString(4, transUUID);
                psmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }   
}
