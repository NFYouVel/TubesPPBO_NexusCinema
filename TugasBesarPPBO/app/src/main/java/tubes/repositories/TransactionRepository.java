package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import tubes.models.Transaction;
import tubes.utils.Database;
import tubes.utils.UtilGlobal;

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
}
