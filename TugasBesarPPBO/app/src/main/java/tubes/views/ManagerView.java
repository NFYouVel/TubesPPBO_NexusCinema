package tubes.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.util.Date;
import java.text.SimpleDateFormat;

import tubes.controllers.AuditRecordController;
import tubes.controllers.ManagerController;
import tubes.controllers.TransactionController;
import tubes.models.User;
import tubes.models.enums.Roles;
import tubes.models.exceptions.EmptyListException;
import tubes.models.exceptions.InvalidDateException;

public class ManagerView {
    private AuditRecordController auditController;
    private TransactionController transactionController;
    private ManagerController managerController;

    public ManagerView() {
        auditController = new AuditRecordController();
        transactionController = new TransactionController();
        managerController = new ManagerController();
    }

    public void showMainMenu() {
        JFrame frame = new JFrame("Nexus Cinema - Manager Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Manager Main Menu", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(title, BorderLayout.NORTH);

        // Panel tombol menu
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton auditBtn = new JButton("Audit Report");
        JButton transactionBtn = new JButton("Transaction History");
        JButton dataUserBtn = new JButton("Show Data User");
        JButton exitBtn = new JButton("Exit / Sign Out");

        // Event tombol
        auditBtn.addActionListener(e -> {
            frame.dispose();
            showAuditMenu();
        });

        transactionBtn.addActionListener(e -> {
            frame.dispose();
            try {
                showTransactionHistory();
            } catch (EmptyListException ex) {
                showMessage(frame, ex.getMessage(), "Empty List Transaction");
                showMainMenu();
            }
        });

        dataUserBtn.addActionListener(e -> {
            frame.dispose();
            try {
                showUserList();
            } catch (EmptyListException ex) {
                showMessage(frame, ex.getMessage(), "Empty List User");
                showMainMenu();
            }
        });

        exitBtn.addActionListener(e -> {
            frame.dispose();
            UserView start = new UserView();
            start.showMenuLogin();
        });

        panel.add(auditBtn);
        panel.add(transactionBtn);
        panel.add(dataUserBtn);
        panel.add(exitBtn);

        frame.add(panel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void showAuditMenu() {
        JFrame frame = new JFrame("Nexus Cinema Audit Report");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // ----------------------------------- TOP PANEL (DATE PICKER CONTROL)
        // -----------------------------------
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

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
        JButton exitBtn = new JButton("Exit");
        Color dangerRed = new Color(220, 53, 69);
        Color dangerDark = new Color(200, 35, 51);

        exitBtn.setBackground(dangerRed);
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        exitBtn.setBorderPainted(false);
        exitBtn.setOpaque(true);

        // Hover effect
        exitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitBtn.setBackground(dangerDark);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitBtn.setBackground(dangerRed);
            }
        });

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

        exitBtn.addActionListener(e -> {
            frame.dispose();
            showMainMenu();
        });

        // Tambahkan ke panel
        leftPanel.add(new JLabel("Calendar:"));
        leftPanel.add(dateSpinner);

        leftPanel.add(new JLabel("Start:"));
        leftPanel.add(startDate);
        leftPanel.add(setStartBtn);

        leftPanel.add(new JLabel("End:"));
        leftPanel.add(endDate);
        leftPanel.add(setEndBtn);

        leftPanel.add(filterBtn);

        rightPanel.add(exitBtn);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        // ------------------------------------ CENTER PANEL (AUDIT RESULT)
        // ------------------------------------
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Filter logic
        filterBtn.addActionListener(e -> {
            try {
                String result = auditController.getAuditListByMonth(startDate.getText(), endDate.getText());
                resultArea.setText(result);
            } catch (InvalidDateException ex) {
                showMessage(frame, ex.getMessage(), "Invalid Date Range");
            } catch (EmptyListException ex) {
                showMessage(frame, ex.getMessage(), "Empty List Audit");
            }
        });

        // ----------------------------------------- ADD TO FRAME
        // -----------------------------------------
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void showTransactionHistory() throws EmptyListException {
        JFrame frame = new JFrame("Nexus Cinema - Transaction History");
        frame.setSize(1100, 600);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exitBtn = new JButton("Exit");
        Color dangerRed = new Color(220, 53, 69);
        Color dangerDark = new Color(200, 35, 51);

        exitBtn.setBackground(dangerRed);
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        exitBtn.setBorderPainted(false);
        exitBtn.setOpaque(true);

        // Hover effect
        exitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitBtn.setBackground(dangerDark);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitBtn.setBackground(dangerRed);
            }
        });

        rightPanel.add(exitBtn);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        // Table Container
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);

        // Load Data First Time
        loadTableData(table);

        exitBtn.addActionListener(e -> {
            frame.dispose();
            showMainMenu();
        });

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void showUserList() throws EmptyListException {
        JFrame frame = new JFrame("Nexus Cinema - Data User");
        frame.setSize(500, 650);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(245, 245, 245));
        frame.setLayout(new BorderLayout());

        // ===== TOP PANEL (TITLE + EXIT)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        topPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("User List");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
                JButton exitBtn = new JButton("Exit");
        Color dangerRed = new Color(220, 53, 69);
        Color dangerDark = new Color(200, 35, 51);

        exitBtn.setBackground(dangerRed);
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        exitBtn.setBorderPainted(false);
        exitBtn.setOpaque(true);

        // Hover effect
        exitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitBtn.setBackground(dangerDark);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitBtn.setBackground(dangerRed);
            }
        });

        exitBtn.addActionListener(e -> {
            frame.dispose();
            showMainMenu();
        });

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(exitBtn, BorderLayout.EAST);

        // ===== LIST PANEL
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(245, 245, 245));
        listPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        for (User u : managerController.getAllUsers()) {
            JPanel card = createUserCard(u);
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel createUserCard(User u) {
        JPanel card = new JPanel();

        // Pakai BoxLayout Y_AXIS biar elemen di dalam kartu susun ke bawah rapi
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)));
        card.setBackground(Color.WHITE);

        // Ukuran Kartu (Maksimal lebar, tinggi secukupnya)
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // --- ISI KARTU ---

        // Warna Role
        String roleText = u.getRole().toString();
        Color roleColor = Color.GRAY;
        if (u.getRole() == Roles.CUSTOMER)
            roleColor = new Color(0, 120, 215);
        else if (u.getRole() == Roles.STAFF)
            roleColor = new Color(34, 139, 34);
        else if (u.getRole() == Roles.MANAGER)
            roleColor = new Color(220, 20, 60);

        // Elemen-elemen
        JLabel lblName = new JLabel(u.getName());
        lblName.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel lblRole = new JLabel(roleText);
        lblRole.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblRole.setForeground(roleColor);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5)); // Batasi tinggi garis
        sep.setForeground(Color.LIGHT_GRAY);

        JLabel lblEmail = new JLabel("📧 " + u.getEmail());
        JLabel lblPhone = new JLabel("📞 " + u.getPhone());
        JLabel lblBio = new JLabel("👤 " + u.getGender() + " | 🎂 " + u.getDob());

        Font dataFont = new Font("SansSerif", Font.PLAIN, 13);
        lblEmail.setFont(dataFont);
        lblPhone.setFont(dataFont);
        lblBio.setFont(dataFont);

        // --- SUSUN (Rata Kiri) ---
        JComponent[] components = { lblName, lblRole, sep, lblEmail, lblPhone, lblBio };

        for (JComponent c : components) {
            c.setAlignmentX(Component.LEFT_ALIGNMENT); // Rata Kiri
            card.add(c);
            if (c != sep)
                card.add(Box.createRigidArea(new Dimension(0, 5))); // Jarak dikit
            else
                card.add(Box.createRigidArea(new Dimension(0, 10))); // Jarak abis garis
        }

        return card;
    }

    private void loadTableData(JTable table) throws EmptyListException {
        String[][] data = transactionController.getTransactionTableData();
        String[] columns = transactionController.getTransactionTableColumns();

        table.setModel(new javax.swing.table.DefaultTableModel(data, columns));

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(25);
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