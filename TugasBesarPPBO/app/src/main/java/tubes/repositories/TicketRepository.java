package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import tubes.models.AuditRecord;
import tubes.models.enums.StudioTypes;
import tubes.models.exceptions.LoginFailedException;
import tubes.utils.Database;

public class TicketRepository {
    private static final Connection conn;

    static {
        conn = Database.connect();
    }

    public List<AuditRecord> getTicketListByMonth(String startDate, String endDate) {
        List<AuditRecord> auditList = new ArrayList<>();
        String sql = "SELECT s.studio_number, s.type, st.show_price, COUNT(t.ticket_UUID) AS ticketCount, SUM(st.show_price) AS totalIncome FROM `ticket` t INNER JOIN show_time st ON t.show_UUID = st.show_UUID INNER JOIN studio s ON st.studio_UUID = s.studio_UUID WHERE t.created_at >= ? AND t.created_at <= ? GROUP BY s.studio_number, st.show_price ORDER BY s.studio_number, st.show_price;";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            ResultSet rs = pstmt.executeQuery();

            // if (!rs.next()) {
            // throw new LoginFailedException("No Ticket Sold for '" + month + "'.");
            // }

            while (rs.next()) {
                String studioNumber = rs.getString("studio_number");
                StudioTypes studioType = StudioTypes.valueOf(rs.getString("type"));
                int price = rs.getInt("show_price");
                int countTicket = rs.getInt("ticketCount");
                int totalIncome = rs.getInt("totalIncome");

                AuditRecord record = new AuditRecord(studioNumber, studioType, price, countTicket, totalIncome);

                auditList.add(record);
            }
            return auditList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
