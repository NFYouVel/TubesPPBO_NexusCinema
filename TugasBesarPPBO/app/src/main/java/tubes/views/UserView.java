package tubes.views;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import tubes.controllers.UserController;
import tubes.models.Staff;
import tubes.models.User;
import tubes.models.exceptions.LoginFailedException;

public class UserView extends JFrame {
    private UserController userController;
    private CustomerView customerView;

    private JDialog dialog;

    private JPanel panelLogin;
    private JPanel panelHeader;

    private JLabel loginHeaderLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JLabel backgroundLabel;
    private JLabel label;

    private JLayeredPane layeredPane;

    private JTextField emailField;
    private JPasswordField passwordField;

    private JButton loginButton;
    private JButton signupButton;
    private JButton okButton;

    private Font headerFont;
    private Font inputFont;

    private Image image;
    private Image newImage;

    private GridBagConstraints gbc;

    public UserView() {
        super("Login UI View For Admin");
        userController = new UserController();
        headerFont = new Font("SansSerif", Font.BOLD, 28);
        inputFont = new Font("VERDANA", Font.BOLD, 18);
    }

    public boolean showMenuLogin() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 1024);

        backgroundLabel = addLoginBackground();
        if (backgroundLabel == null) {
            backgroundLabel = new JLabel();
            backgroundLabel.setBackground(Color.LIGHT_GRAY);
            backgroundLabel.setOpaque(true);
        }

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));

        backgroundLabel.setBounds(0, 0, 800, 600);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // ---------------- HEADER ----------------
        panelHeader = new JPanel();
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelHeader.setLayout(new GridBagLayout());
        panelHeader.setBackground(new Color(0, 0, 0, 150));

        loginHeaderLabel = new JLabel("WELCOME TO NEXUS CINEMA", SwingConstants.CENTER);
        loginHeaderLabel.setFont(headerFont);
        loginHeaderLabel.setForeground(Color.decode("#c6c6c6"));

        panelHeader.add(loginHeaderLabel);
        panelHeader.setBounds(0, 0, 800, 100);
        layeredPane.add(panelHeader, JLayeredPane.PALETTE_LAYER);

        // ---------------- PANEL LOGIN FORM ----------------
        panelLogin = new JPanel();
        panelLogin.setLayout(new GridBagLayout());
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelLogin.setBackground(new Color(255, 255, 255, 100));
        panelLogin.setOpaque(true);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // LABELS
        emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(inputFont);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(inputFont);

        // FIELDS
        emailField = new JTextField(15);
        passwordField = new JPasswordField(15);

        // BUTTON LOGIN
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("VERDANA", Font.BOLD, 14));
        loginButton.setEnabled(false);
        loginButton.setBackground(Color.GRAY);
        loginButton.setForeground(Color.WHITE);

        // BUTTON SIGNUP
        signupButton = new JButton("Signup");
        signupButton.setFont(new Font("VERDANA", Font.BOLD, 14));
        signupButton.setBackground(new Color(0, 123, 255));
        signupButton.setForeground(Color.WHITE);

        signupButton.addActionListener(e -> {
            this.setVisible(false); // sembunyikan login
            new SignupUI(this); // kirim reference
        });

        // ADD COMPONENTS WITH GBC
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelLogin.add(emailLabel, gbc);

        gbc.gridx = 1;
        panelLogin.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelLogin.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panelLogin.add(passwordField, gbc);

        // Panel untuk dua tombol sejajar
        JPanel panelButtons = new JPanel(new GridLayout(1, 2, 15, 0));
        panelButtons.setOpaque(false);
        panelButtons.add(loginButton);
        panelButtons.add(signupButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelLogin.add(panelButtons, gbc);

        // Document Listener
        emailField.getDocument().addDocumentListener(new InputListener());
        passwordField.getDocument().addDocumentListener(new InputListener());

        // Set posisi panelLogin di tengah
        int panelWidth = 450;
        int panelHeight = 250;
        int x = (800 - panelWidth) / 2;
        int y = (600 - panelHeight) / 2;

        panelLogin.setBounds(x, y, panelWidth, panelHeight);
        layeredPane.add(panelLogin, JLayeredPane.PALETTE_LAYER);

        add(layeredPane);
        pack();
        setVisible(true);

        loginButton.addActionListener(e -> handleLogin());

        return true;
    }

    private void showMessageDialog(String title, String message) {
        dialog = new JDialog(this, title, true);
        dialog.setLayout(new BorderLayout());

        label = new JLabel(message, SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialog.add(label, BorderLayout.CENTER);

        okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());
        dialog.add(okButton, BorderLayout.SOUTH);

        dialog.setSize(500, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private class InputListener implements javax.swing.event.DocumentListener {

        private void checkFields() {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            boolean filled = !email.isEmpty() && !password.isEmpty();

            if (filled) {
                loginButton.setEnabled(true);
                loginButton.setBackground(new Color(0, 123, 255)); // biru
                loginButton.setForeground(Color.WHITE);
            } else {
                loginButton.setEnabled(false);
                loginButton.setBackground(Color.GRAY); // abu-abu
                loginButton.setForeground(Color.LIGHT_GRAY);
            }
        }

        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            checkFields();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            checkFields();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            checkFields();
        }

    }

    // Getter Setter for Input Type
    public String getEmail() {
        return emailField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public void addLoginListener(ActionListener listerForLoginButton) {
        loginButton.addActionListener(listerForLoginButton);
    }

    private void handleLogin() {
        String email = getEmail();
        String password = new String(getPassword());

        try {
            User user = userController.loginVerification(email, password);

            showMessageDialog("Login Successful",
                    "Login successful! Welcome " + user.getName());

            // TODO: pindah ke halaman berikutnya
            customerView = new CustomerView(true);
            customerView.startCustomerView();
            dispose();

        } catch (LoginFailedException e) {

            showMessageDialog("Login Failed", e.getMessage());

        } catch (Exception e) {

            showMessageDialog("Terjadi Kesalahan", e.getMessage());
        }
    }

    public final JLabel addLoginBackground() {
        ImageIcon bgImage;
        JLabel displayImage;
        try {
            bgImage = new ImageIcon(getClass().getResource("login_ui.png"));
            image = bgImage.getImage();
            newImage = image.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
            displayImage = new JLabel(new ImageIcon(newImage));
            return displayImage;
        } catch (Exception e) {
            System.out.println("Image not found! Error: " + e.getMessage());
            return null;
        }
    }
}