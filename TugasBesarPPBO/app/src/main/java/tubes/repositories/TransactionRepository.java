package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import tubes.models.Transaction;
import tubes.utils.Database;
import tubes.utils.UtilGlobal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tubes.models.AuditRecord;
import tubes.models.Transaction;
import tubes.models.enums.Membership;
import tubes.models.enums.PaymentMethods;
import tubes.models.enums.StudioTypes;
import tubes.models.exceptions.EmptyListException;
import tubes.utils.Database;

public class TransactionRepository {
    private static final Connection conn;

    static {
        conn = Database.connect();
    }

    public TransactionRepository() {
    }

    public void processTransaction(Transaction transaction, String paymentID, int grandTotal) {
        try {
            PreparedStatement psmt = conn.prepareStatement("INSERT INTO transactions (trans_UUID, cust_UUID, paymentID, payment_method, grand_total) VALUES (?, ?, ?, ?, ?);");
            psmt.setString(1, transaction.getTransactionUUID());
            psmt.setString(2, UtilGlobal.getGlobalUUID());
            psmt.setString(3, paymentID);
            psmt.setString(4, transaction.getPaymentMethods().toString());
            psmt.setInt(5, grandTotal);
            psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Transaction> getTransactionList() throws EmptyListException {
        List<Transaction> transactionList = new ArrayList<>();
        String sql = "SELECT tr.trans_UUID AS ID_Transaction, tr.created_at AS Payment_Date, u.name, c.membership, s.studio_number, s.type, m.title, se.seats_num, s.price, tr.payment_method FROM `ticket` t INNER JOIN transactions tr ON t.trans_UUID = tr.trans_UUID INNER JOIN show_time st ON t.show_UUID = st.show_UUID INNER JOIN studio s ON st.studio_UUID = s.studio_UUID INNER JOIN movies m ON st.movies_UUID = m.movies_UUID INNER JOIN customer c ON tr.cust_UUID = c.cust_UUID INNER JOIN users u ON c.user_UUID = u.user_UUID INNER JOIN seats se ON t.seats_UUID = se.seats_UUID WHERE t.deleted_at IS NULL AND tr.deleted_at IS NULL AND tr.status = 'paid' ORDER BY u.name, tr.trans_UUID, m.title ASC";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            boolean found = false;

            while (rs.next()) {
                found = true;
                PaymentMethods paymentMethod = null;
                String transactionID = rs.getString("ID_Transaction");
                String paymentDate = rs.getString("Payment_Date");
                String customerName = rs.getString("name");
                Membership membership = Membership.valueOf(rs.getString("membership"));
                String studioNumber = rs.getString("studio_number");
                StudioTypes studioType = StudioTypes.valueOf(rs.getString("type"));
                String movieTitle = rs.getString("title");
                String seatsNum = rs.getString("seats_num");
                int price = rs.getInt("price");
                if(rs.getString("payment_method").equalsIgnoreCase("E-Wallet")){
                    paymentMethod = PaymentMethods.valueOf("E_WALLET");
                }else{
                    paymentMethod = PaymentMethods.valueOf(rs.getString("payment_method"));
                }
                Transaction transaction = new Transaction(transactionID, paymentDate, studioNumber, studioType,
                        movieTitle, seatsNum, customerName, membership, paymentMethod, price);

                transactionList.add(transaction);
            }
            if(!found){
                throw new EmptyListException("No Transaction Found.");
            }
            return transactionList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // public List<Transaction> getTransactionListByMonth(String startDate, String endDate) {
    //     return null;
    // }
}
