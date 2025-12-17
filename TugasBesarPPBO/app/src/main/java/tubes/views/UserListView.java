package tubes.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import tubes.controllers.ShowDataController;
import tubes.models.User;
import tubes.models.enums.Roles;
import tubes.repositories.ShowDataUser;

public class UserListView extends JFrame {

    public UserListView() {
        // 1. SETUP FRAME
        setTitle("Data User");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        // 2. PANEL UTAMA
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS)); // Susun ke bawah
        listPanel.setBackground(new Color(245, 245, 245));
        listPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 3. AMBIL DATA
        ShowDataController showDataController = new ShowDataController();
        try {
            List<User> users = showDataController.getShowAllUser();
            // System.out.println("Berhasil ambil " + users.size() + " data.");

            for (User u : users) {
                JPanel card = createUserCard(u);
                
                // Biar kartu rata tengah & ga melar aneh
                card.setAlignmentX(Component.LEFT_ALIGNMENT); 
                
                listPanel.add(card);
                listPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Jarak antar kartu
            }

        } catch (Exception e) {
            e.printStackTrace();
            listPanel.add(new JLabel("Gagal load data: " + e.getMessage()));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        setVisible(true);
    }

    private JPanel createUserCard(User u) {
        JPanel card = new JPanel();
        
        // Pakai BoxLayout Y_AXIS biar elemen di dalam kartu susun ke bawah rapi
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        
        // Ukuran Kartu (Maksimal lebar, tinggi secukupnya)
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // --- ISI KARTU ---
        
        // Warna Role
        String roleText = u.getRole().toString();
        Color roleColor = Color.GRAY;
        if (u.getRole() == Roles.CUSTOMER) roleColor = new Color(0, 120, 215);
        else if (u.getRole() == Roles.STAFF) roleColor = new Color(34, 139, 34);
        else if (u.getRole() == Roles.MANAGER) roleColor = new Color(220, 20, 60);

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
        JLabel lblBio   = new JLabel("👤 " + u.getGender() + " | 🎂 " + u.getDob());

        Font dataFont = new Font("SansSerif", Font.PLAIN, 13);
        lblEmail.setFont(dataFont);
        lblPhone.setFont(dataFont);
        lblBio.setFont(dataFont);

        // --- SUSUN (Rata Kiri) ---
        JComponent[] components = {lblName, lblRole, sep, lblEmail, lblPhone, lblBio};
        
        for (JComponent c : components) {
            c.setAlignmentX(Component.LEFT_ALIGNMENT); // Rata Kiri
            card.add(c);
            if (c != sep) card.add(Box.createRigidArea(new Dimension(0, 5))); // Jarak dikit
            else card.add(Box.createRigidArea(new Dimension(0, 10))); // Jarak abis garis
        }

        return card;
    }
}