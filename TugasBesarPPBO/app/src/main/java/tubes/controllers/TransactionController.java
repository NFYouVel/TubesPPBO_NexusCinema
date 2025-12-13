package tubes.controllers;

import tubes.models.Transaction;
import tubes.repositories.TransactionRepository;

public class TransactionController {
    private TransactionRepository transactionRepository;

    public TransactionController() {
        transactionRepository = new TransactionRepository();
    }

    public void processTransaction(Transaction transaction, String paymentID, int grandTotal) {
        transactionRepository.processTransaction(transaction, paymentID, grandTotal);
    }
}
