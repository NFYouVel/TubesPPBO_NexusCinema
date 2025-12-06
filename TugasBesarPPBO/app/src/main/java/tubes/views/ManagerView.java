package tubes.views;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.text.SimpleDateFormat;

import tubes.controllers.TicketController;
import tubes.models.exceptions.InvalidDateException;

public class ManagerView {
    private TicketController ticketController;

    public ManagerView(){
        ticketController = new TicketController();
    }

    public void showAuditMenu(){
        JFrame frame = new JFrame("Nexus Cinema Audit Report");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // ----------------------------------- TOP PANEL (DATE PICKER CONTROL) -----------------------------------
        JPanel topPanel = new JPanel(new FlowLayout());

        // Spinner kalender
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));

        // Textfield hasil
        JTextField startDate = new JTextField(10);
        JTextField endDate = new JTextField(10);

        // Tombol
        JButton setStartBtn = new JButton("Set Start");
        JButton setEndBtn = new JButton("Set End");
        JButton filterBtn = new JButton("Filter");

        // Formatter untuk output
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Action tombol
        setStartBtn.addActionListener(e -> {
            Date date = (Date) dateSpinner.getValue();
            startDate.setText(sdf.format(date));
        });

        setEndBtn.addActionListener(e -> {
            Date date = (Date) dateSpinner.getValue();
            endDate.setText(sdf.format(date));
        });

        // Tambahkan ke panel
        topPanel.add(new JLabel("Calendar:"));
        topPanel.add(dateSpinner);
        topPanel.add(new JLabel("Start:"));
        topPanel.add(startDate);
        topPanel.add(setStartBtn);

        topPanel.add(new JLabel("End:"));
        topPanel.add(endDate);
        topPanel.add(setEndBtn);

        topPanel.add(filterBtn);

        // ------------------------------------ CENTER PANEL (AUDIT RESULT) ------------------------------------
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Filter logic
        filterBtn.addActionListener(e -> {
            try {
                String result = ticketController.getTicketListByMonth(startDate.getText(), endDate.getText());
                resultArea.setText(result);
            } catch (InvalidDateException ex) {
                showMessage(frame, ex.getMessage(), "Invalid Date Range");
            }
        });

        // ----------------------------------------- ADD TO FRAME -----------------------------------------
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Improved showMessage dialog
    private void showMessage(JFrame parent, String message, String title) {
        JDialog dialog = new JDialog(parent, title, true);
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());

        JPanel panelButton = new JPanel();
        panelButton.add(okButton);

        dialog.add(label, BorderLayout.CENTER);
        dialog.add(panelButton, BorderLayout.SOUTH);

        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}