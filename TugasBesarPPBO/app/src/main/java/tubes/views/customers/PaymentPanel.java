package tubes.views.customers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import tubes.controllers.ShowTimeController;
import tubes.controllers.TicketController;
import tubes.controllers.TransactionController;
import tubes.models.CardPayment;
import tubes.models.EWalletPayment;
import tubes.models.Seat;
import tubes.models.ShowTime;
import tubes.models.Transaction;
import tubes.models.enums.PaymentMethods;
import tubes.models.exceptions.EmptyListException;
import tubes.models.interfaces.PageNavigator;
import tubes.models.interfaces.Payment;
import tubes.utils.CLIUtils;
import tubes.utils.UtilUUIDGenerator;


public class PaymentPanel extends JPanel {
    private ShowTimeController showTimeController;
    private TicketController ticketController;
    private TransactionController transactionController;
    private PageNavigator navigator;

    public PaymentPanel(List<Seat> selectedSeats, String showUUID, PageNavigator navigator) throws EmptyListException {
        showTimeController = new ShowTimeController();
        transactionController = new TransactionController();
        ticketController = new TicketController();
        this.navigator = navigator;
        
        if (selectedSeats == null || selectedSeats.isEmpty()) {
                throw new EmptyListException("Selected seats list cannot be empty.");
        }
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 450));

        // Header
        JLabel title = new JLabel("Movie Ticket Payment", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 1, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        ShowTime showTime = showTimeController.callShowTimeDetails(showUUID);

        // Movie Detail
        JLabel movieLabel = new JLabel("Movie: " + showTime.getMovie().getTitle());

        JLabel ticketLabel = new JLabel("Number of Tickets: " + selectedSeats.size());

        JLabel paymentLabel = new JLabel("Payment Method:");
        String[] methods = {"CARD", "E-WALLET"};
        JComboBox<String> paymentBox = new JComboBox<>(methods);

        JLabel emailLabel = new JLabel("Email Address:");
        JTextField emailField = new JTextField();

        JButton payBtn = new JButton("Pay Now");

        payBtn.addActionListener((var e) -> {
            String email = emailField.getText();
            String paymentMethod = (String) paymentBox.getSelectedItem();
            String identifierNumber = "";
            Payment payment;
            boolean valid = false;

            if (paymentMethod.equals("CARD")) {
                identifierNumber = CLIUtils.showInputDialog("Enter your 16-digit card number:", "Card Payment");
                payment = new CardPayment(identifierNumber);
                valid = payment.validate();
            } else if (paymentMethod.equals("E-WALLET")) {
                identifierNumber = CLIUtils.showInputDialog("Enter your e-wallet ID:", "E-Wallet Payment");
                payment = new EWalletPayment(identifierNumber);
                valid = payment.validate();
            }

            // Payment Validation
            if (!valid) {
                CLIUtils.showErrorMessage("Invalid payment. Please check your payment ID and try again.", "Payment Error");
                return;
            }
            if (identifierNumber.equals("")) {
                return;
            }

            // Send to Database
            String transactionID = UtilUUIDGenerator.generateUUID();
            Transaction transaction = new Transaction(paymentMethod.equals("CARD") ? PaymentMethods.CARD : PaymentMethods.E_WALLET);
            transaction.setTransactionUUID(transactionID);
            transactionController.processTransaction(transaction, identifierNumber, selectedSeats.size() * showTime.getStudio().getPrice());

            ticketController.processTicket(transactionID, showUUID, selectedSeats);

            // OTP Generation and Email Sending
            String otpCode = CLIUtils.generateOTP();
            System.out.println("Generated OTP Code: " + otpCode); // For testing purposes
            // EmailSender.sendEmail(email, otpCode);
            CLIUtils.showInformationMessage("Payment of " + (selectedSeats.size() * showTime.getStudio().getPrice()) + " using " + paymentMethod + " was successful!\n" + "A confirmation email has been sent to " + email + ".", "Payment Successful");

            // OTP Confirmation
            boolean isConfirmed = false;
            for (int i = 0; i < 5; i++) {
                String otpCodeConfirmation = CLIUtils.showInputDialog("Enter the OTP code sent to your email to confirm your purchase:", "OTP Confirmation");
                while (otpCodeConfirmation == null || otpCodeConfirmation.isEmpty()) {
                    otpCodeConfirmation = CLIUtils.showInputDialog("OTP code cannot be empty. Please enter the OTP code sent to your email:", "OTP Confirmation");
                }
                if (otpCodeConfirmation.equals(otpCode)) {
                    CLIUtils.showInformationMessage("OTP confirmed! Don't forget your ticket!", "OTP Confirmed");
                    isConfirmed = true;
                    transactionController.callUpdateTransaction(transactionID);
                    ticketController.callUpdateTicket(transactionID);
                    break;
                } else {
                    CLIUtils.showErrorMessage("Incorrect OTP code. Please try again.", "OTP Error");
                }
                
            }
            if (!isConfirmed) {
                CLIUtils.showErrorMessage("Failed to confirm OTP after 5 attempts. Transaction cancelled.", "OTP Failed");
                transactionController.callDeleteTransaction(transactionID);
                ticketController.callDeleteTicket(transactionID);
                return;
            }
            navigator.showPage("PRINT_TICKET");

        });

        payBtn.setFont(new Font("Arial", Font.BOLD, 14));
        payBtn.setBackground(new Color(70, 130, 180));
        payBtn.setForeground(Color.WHITE);
        payBtn.setFocusPainted(false);

        mainPanel.add(movieLabel);
        mainPanel.add(ticketLabel);
        mainPanel.add(paymentLabel);
        mainPanel.add(paymentBox);
        mainPanel.add(emailLabel);
        mainPanel.add(emailField);

        add(mainPanel, BorderLayout.CENTER);
        add(payBtn, BorderLayout.SOUTH);
    }
}
