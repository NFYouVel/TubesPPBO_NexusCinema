package tubes.views;

import javax.swing.*;

import java.text.SimpleDateFormat;
import tubes.controllers.UserController;
import tubes.models.Customer;
import tubes.models.User;
import tubes.models.enums.Genders;

import java.awt.*;
import java.util.Date;

public class SignupUI extends JFrame {

    private UserController userController;

    private JTextField namaField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JSpinner dobSpinner;
    private JComboBox<String> genderCombo;
    private JButton registerButton;
    private JButton cancelButton;

    private UserView loginUI;

    public SignupUI(UserView loginUI) {
        super("Signup Form");
        this.loginUI = loginUI;
        userController = new UserController();
        initUI();
    }

    private void initUI() {
        setSize(500, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        Font inputFont = new Font("VERDANA", Font.BOLD, 14);

        JLabel namaLabel = new JLabel("Nama:");
        namaField = new JTextField();
        namaField.setFont(inputFont);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        emailField.setFont(inputFont);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        passwordField.setFont(inputFont);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField();
        phoneField.setFont(inputFont);

        JLabel dobLabel = new JLabel("Date of Birth:");
        dobSpinner = new JSpinner(new SpinnerDateModel());
        dobSpinner.setEditor(new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd"));

        JLabel genderLabel = new JLabel("Gender:");
    genderCombo = new JComboBox<>(new String[]{Genders.PRIA.toString(), Genders.WANITA.toString()});

        registerButton = new JButton("Register");
        registerButton.setFont(inputFont);
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setForeground(Color.WHITE);

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(inputFont);
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);

        cancelButton.addActionListener(e -> {
            dispose();
            loginUI.setVisible(true);
        });
        registerButton.addActionListener(e -> handleRegister());

        panel.add(namaLabel);
        panel.add(namaField);

        panel.add(emailLabel);
        panel.add(emailField);

        panel.add(passwordLabel);
        panel.add(passwordField);

        panel.add(phoneLabel);
        panel.add(phoneField);

        panel.add(dobLabel);
        panel.add(dobSpinner);

        panel.add(genderLabel);
        panel.add(genderCombo);

        panel.add(registerButton);
        panel.add(cancelButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Getter untuk controller
    public String getNama() {
        return namaField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public String getPhone() {
        return phoneField.getText();
    }

    public Date getDateOfBirth() {
        return (Date) dobSpinner.getValue();
    }

    public String getGender() {
        return genderCombo.getSelectedItem().toString();
    }

    public void addRegisterListener() {
        registerButton.addActionListener(e -> handleRegister());
    }

    private void handleRegister() {
        String nama = getNama();
        String email = getEmail();
        String password = new String(getPassword());
        String phone = getPhone();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dob = sdf.format(getDateOfBirth());
        String gender = getGender();

        // Validasi sederhana
        if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            showMessage("Pastikan semua field telah diisi!", "Error");
            return;
        }

        // Buat object User
        User user = new Customer(nama, email, password, phone, dob, Genders.valueOf(gender));

        try {
            userController.signupUser(user);
            showMessage("Pendaftaran berhasil!", "Sukses");

            dispose(); // tutup signup
            loginUI.setVisible(true); // munculkan login lama

        } catch (Exception e) {
            showMessage("Terjadi kesalahan: " + e.getMessage(), "Error");
        }
    }

    private void showMessage(String message, String title) {
        JDialog dialog = new JDialog(this, title, true);
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
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

}
