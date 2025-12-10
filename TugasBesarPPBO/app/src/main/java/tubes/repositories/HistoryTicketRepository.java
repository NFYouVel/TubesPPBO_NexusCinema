package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tubes.models.Movie;
import tubes.models.ShowTime;
import tubes.models.Ticket;
import tubes.models.Seat;
import tubes.models.Studio;
import tubes.models.enums.MovieTypes;
import tubes.models.enums.Ratings;
import tubes.models.enums.TicketStatus;
import tubes.models.exceptions.EmptyListRepository;
import tubes.utils.Database;

public class HistoryTicketRepository {
    private static final Connection conn;
    private List<Ticket> ticketOrdered;

    static {
        conn = Database.connect();
    }

    public HistoryTicketRepository() {
        ticketOrdered = new ArrayList<>();
    }

    public List<Ticket> getTicketOrdered(String custUUID) throws EmptyListRepository {
        try {
            String sql = "SELECT t.*, m.*, s.*, st.*, se.* FROM ticket t JOIN show_time st ON t.show_UUID = st.show_UUID "
                    +
                    "JOIN movies m ON st.movies_UUID = m.movies_UUID JOIN seats se ON t.seats_UUID = se.seats_UUID JOIN studio s ON st.studio_UUID = s.studio_UUID JOIN transactions tr ON tr.trans_UUID = t.trans_UUID "
                    +
                    "WHERE t.status='PAID' AND tr.cust_UUID = ? ORDER BY t.created_at DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, custUUID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ShowTime showTime = new ShowTime(rs.getString("show_time"), rs.getInt("show_price"));
                showTime.setMovie(new Movie(rs.getString("title"), rs.getInt("duration"), rs.getString("genre"), Ratings.valueOf(rs.getString("rating"))));
                showTime.setStudio(new Studio(rs.getString("studio_number"), MovieTypes.valueOf(rs.getString("type"))));

                Ticket historyTicket = new Ticket(TicketStatus.PAID);
                historyTicket.setSeats(new Seat(rs.getString("seats_num"), rs.getInt("seats_row"), rs.getInt("seats_column")));
                historyTicket.setShowTime(showTime);

                ticketOrdered.add(historyTicket);
            }

            if (ticketOrdered.isEmpty()) {
                throw new EmptyListRepository("no ticket ordered");
            }

            return ticketOrdered;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
