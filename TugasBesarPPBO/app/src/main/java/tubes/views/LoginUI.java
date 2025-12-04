package tubes.views;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class LoginUI extends JFrame {

    private JPanel panelLogin;
    private JPanel panelHeader;
    private JLabel loginHeaderLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private Font headerFont;
    private Font inputFont;

    public LoginUI() {
        super("Login UI View For Admin");
        headerFont = new Font("SansSerif", Font.BOLD, 28);
        inputFont = new Font("VERDANA", Font.BOLD, 18);
    }

    public void showMenuLogin() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 1024); // Ukuran lebih besar agar gambar terlihat
        
        JLabel backgroundLabel = addLoginBackground();
        if (backgroundLabel == null) {
            backgroundLabel = new JLabel();
            backgroundLabel.setBackground(Color.LIGHT_GRAY);
            backgroundLabel.setOpaque(true);
        }

        // Inisialisasi JLayeredPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));

        // Tambahkan Gambar Latar Belakang ke Layer Bawah (Layer 0)
        backgroundLabel.setBounds(0, 0, 800, 600);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // Header
        panelHeader = new JPanel();
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        loginHeaderLabel = new JLabel("WELCOME TO NEXUS CINEMA", SwingConstants.CENTER);
        loginHeaderLabel.setFont(headerFont);
        loginHeaderLabel.setForeground(Color.decode("#c6c6c6"));
        panelHeader.setBackground(new Color(0, 0, 0, 150));
        panelHeader.setOpaque(true);
        panelHeader.add(loginHeaderLabel);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panelHeader.setBounds(0, 0, 800, 100);
        layeredPane.add(panelHeader, JLayeredPane.PALETTE_LAYER);


        // Inisialisasi Panel Login (Form)
        panelLogin = new JPanel();
        panelLogin.setLayout(new GridLayout(3, 2, 10, 20));
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelLogin.setBackground(new Color(255, 255, 255, 100));
        panelLogin.setOpaque(true); 
        // Komponen Label 
        emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE); 
        emailLabel.setFont(inputFont);
        passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE); 
        passwordLabel.setFont(inputFont);
        // Komponen Input Type
        emailField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("VERDANA", Font.BOLD, 14));

        // Tambahkan komponen ke panelLogin
        panelLogin.add(emailLabel);
        panelLogin.add(emailField);
        panelLogin.add(passwordLabel);
        panelLogin.add(passwordField);
        panelLogin.add(new JLabel()); // Placeholder
        panelLogin.add(loginButton);

        // 5. Atur Posisi dan Ukuran panelLogin di Layer Atas (Layer 1)
        // Kita atur panelLogin di tengah jendela, dengan ukuran yang sesuai (misal 300x150)
        int panelWidth = 400;
        int panelHeight = 150;
        int x = (800 - panelWidth) / 2; // Hitung posisi X tengah
        int y = (600 - panelHeight) / 2; // Hitung posisi Y tengah
        // 200 makin kecil spacenya --> 300 (maksudnya padding 300 atas 300 bawah makanya makin sempit)
        // 400 makin lebar buat si PALLETTE --> 200 (maksudnya padding 200 kiri 200 kanan makanya makin luas)
        panelLogin.setBounds(x, y, panelWidth, panelHeight);
        layeredPane.add(panelLogin, JLayeredPane.PALETTE_LAYER); // Letakkan di layer di atas default

        add(layeredPane); 
        pack(); // ngatur ukuran frame agar sesuai dengan layeredPane
        setVisible(true);
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

    public final JLabel addLoginBackground() {
        ImageIcon bgImage;
        JLabel displayImage;
        try {
            bgImage = new ImageIcon(getClass().getResource("/assets/images/login_ui.png"));
            Image image = bgImage.getImage(); 
            Image newImage = image.getScaledInstance(800, 600, Image.SCALE_SMOOTH); 
            displayImage = new JLabel(new ImageIcon(newImage));
            return displayImage;
        } catch (Exception e) {
            System.out.println("Image not found! Error: " + e.getMessage());
            return null;
        }
    }
}