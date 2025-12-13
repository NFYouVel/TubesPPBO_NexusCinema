package tubes.controllers;

import java.util.List;

import tubes.models.Transaction;
import tubes.models.exceptions.EmptyListException;
import tubes.repositories.TransactionRepository;

public class TransactionController {
    TransactionRepository transactionRepository;

    public TransactionController() {
        transactionRepository = new TransactionRepository();
    }

    public void processTransaction(Transaction transaction, String paymentID, int grandTotal) {
        transactionRepository.processTransaction(transaction, paymentID, grandTotal);
    }
    
    public String[][] getTransactionTableData() throws EmptyListException {
        List<Transaction> list = transactionRepository.getTransactionList();

        String[] columns = getTransactionTableColumns();
        String[][] data = new String[list.size()][columns.length];

        for (int i = 0; i < list.size(); i++) {
            Transaction t = list.get(i);

            data[i][0] = t.getTransactionUUID();
            data[i][1] = t.getTransactionDateTime();
            data[i][2] = t.getCustomerName();
            data[i][3] = t.getMembership().toString();
            data[i][4] = t.getStudioNumber() + " / " + t.getStudioType();
            data[i][5] = t.getMovieTitle();
            data[i][6] = t.getSeatNumber();
            data[i][7] = String.valueOf(t.getPrice());
            data[i][8] = t.getPaymentMethods().toString();
        }
        return data;
    }

    public String[] getTransactionTableColumns() {
        return new String[] {
                "Transaction ID",
                "Payment Date",
                "Customer",
                "Membership",
                "Studio",
                "Movie Title",
                "Seat",
                "Price",
                "Payment Method"
        };
    }
}
