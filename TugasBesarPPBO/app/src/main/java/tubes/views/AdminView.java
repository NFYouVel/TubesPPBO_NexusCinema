package tubes.views;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;

import tubes.controllers.AdminController;
import tubes.models.Movie;
import tubes.models.ShowTime;
import tubes.models.Studio;
import tubes.models.enums.MovieTypes;
import tubes.models.enums.Ratings;

public class AdminView {

    private AdminController adminController;
    private JFrame frame;
    private JPanel navigationDiv;
    private JPanel mainContentPanel;

    // Navigation Buttons
    private JButton btnManageMovies;
    private JButton btnManageShowtimes;

    public AdminView() {
        adminController = new AdminController();
        frame = new JFrame("Admin Dashboard");
        
        // Setup Frame
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        initNavigation();
        
        // Default view
        showMovieManagementPanel();

        frame.setVisible(true);
    }

    // --- UTILS ---
    private void refreshContentPanel(JPanel newPanel) {
        if (mainContentPanel != null) {
            frame.remove(mainContentPanel);
        }
        mainContentPanel = newPanel;
        frame.add(mainContentPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void initNavigation() {
        navigationDiv = new JPanel();
        navigationDiv.setBackground(Color.DARK_GRAY);
        navigationDiv.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        btnManageMovies = new JButton("Manage Movies");
        btnManageShowtimes = new JButton("Manage Showtimes");

        styleNavButton(btnManageMovies);
        styleNavButton(btnManageShowtimes);

        btnManageMovies.addActionListener(e -> showMovieManagementPanel());
        btnManageShowtimes.addActionListener(e -> showShowtimeManagementPanel());

        navigationDiv.add(btnManageMovies);
        navigationDiv.add(btnManageShowtimes);

        frame.add(navigationDiv, BorderLayout.NORTH);
    }

    private void styleNavButton(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 40));
    }

    // ==========================================
    // 1. MANAGE MOVIES PANEL
    // ==========================================
    // ==========================================
    // 1. MANAGE MOVIES PANEL (UPDATED)
    // ==========================================
    private void showMovieManagementPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Form Components ---
        JTextField tfTitle = new JTextField(20);
        JTextField tfDuration = new JTextField(20);
        JTextField tfGenre = new JTextField(20);
        JComboBox<Ratings> cbRating = new JComboBox<>(Ratings.values());
        
        // Field UUID (Kita taruh agak bawah atau kasih label jelas)
        JTextField tfUUID = new JTextField(20); 
        
        // Buttons
        JButton btnAdd = new JButton("Add New Movie"); // Tombol Add Sendiri
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnRestore = new JButton("Restore");

        // --- Layouting Form ---
        
        // Input Data Film (Untuk Add & Update)
        addFormRow(panel, gbc, 0, "Title:", tfTitle);
        addFormRow(panel, gbc, 1, "Duration (Minutes):", tfDuration);
        addFormRow(panel, gbc, 2, "Genre:", tfGenre);
        addFormRow(panel, gbc, 3, "Rating:", cbRating);
        
        // Tombol Add ditaruh langsung di bawah form isian
        gbc.gridx = 1; gbc.gridy = 4;
        btnAdd.setBackground(Color.GREEN); // Biar kelihatan beda
        panel.add(btnAdd, gbc);

        // --- Separator Visual (Garis Batas) ---
        JSeparator sep = new JSeparator();
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(sep, gbc);

        // --- Area Khusus Edit/Hapus ---
        JLabel lblInfo = new JLabel("<html><b>Area Edit / Hapus</b><br>(Wajib isi UUID target di bawah ini)</html>");
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(lblInfo, gbc);

        gbc.gridwidth = 1; // Reset width
        addFormRow(panel, gbc, 7, "Target UUID:", tfUUID);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.add(btnUpdate);
        actionPanel.add(btnDelete);
        actionPanel.add(btnRestore);

        gbc.gridx = 1; gbc.gridy = 8;
        panel.add(actionPanel, gbc);

        // --- Action Listeners ---

        // LOGIC ADD: Tidak peduli isi tfUUID
        btnAdd.addActionListener(e -> {
            try {
                String title = tfTitle.getText();
                int duration = Integer.parseInt(tfDuration.getText());
                String genre = tfGenre.getText();
                Ratings rating = (Ratings) cbRating.getSelectedItem();

                // Constructor Movie tanpa UUID, karena nanti digenerate di Repo
                Movie newMovie = new Movie(title, duration, genre, rating);
                
                String result = adminController.addMovies(newMovie);
                JOptionPane.showMessageDialog(frame, result);
                
                // Kosongkan form setelah add
                tfTitle.setText("");
                tfDuration.setText("");
                tfGenre.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Duration harus angka!");
            }
        });

        // LOGIC UPDATE: Wajib baca tfUUID
        btnUpdate.addActionListener(e -> {
            String targetUUID = tfUUID.getText();
            if(targetUUID.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Mohon isi UUID film yang akan diedit!");
                return;
            }
            try {
                Movie movie = new Movie(tfTitle.getText(), Integer.parseInt(tfDuration.getText()), tfGenre.getText(), (Ratings) cbRating.getSelectedItem());
                movie.setMoviesUUID(targetUUID); // Set UUID target dari inputan
                
                String result = adminController.updateMovie(movie);
                JOptionPane.showMessageDialog(frame, result);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Data tidak valid: " + ex.getMessage());
            }
        });

        // LOGIC DELETE
        btnDelete.addActionListener(e -> {
            String targetUUID = tfUUID.getText();
            if(targetUUID.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Mohon isi UUID film yang akan dihapus!");
                return;
            }
            String result = adminController.deleteMovie(targetUUID);
            JOptionPane.showMessageDialog(frame, result);
        });

        // LOGIC RESTORE
        btnRestore.addActionListener(e -> {
            String targetUUID = tfUUID.getText();
            if(targetUUID.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Mohon isi UUID film yang akan direstore!");
                return;
            }
            String result = adminController.restoreMovie(targetUUID);
            JOptionPane.showMessageDialog(frame, result);
        });

        refreshContentPanel(panel);
    }   

    // ==========================================
    // 2. MANAGE SHOWTIME PANEL
    // ==========================================
    private void showShowtimeManagementPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField tfMovieUUID = new JTextField(25);
        JTextField tfStudioUUID = new JTextField(25);
        JTextField tfPrice = new JTextField(15);
        
        // Input Tanggal Manual (Format: yyyy-MM-dd HH:mm)
        JTextField tfDateTime = new JTextField("2025-12-31 14:00"); 

        JButton btnAddShow = new JButton("Add Show Time");

        addFormRow(panel, gbc, 0, "Movie UUID:", tfMovieUUID);
        addFormRow(panel, gbc, 1, "Studio UUID:", tfStudioUUID);
        addFormRow(panel, gbc, 2, "Price (Rp):", tfPrice);
        addFormRow(panel, gbc, 3, "Start Time (yyyy-MM-dd HH:mm):", tfDateTime);

        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(btnAddShow, gbc);

        btnAddShow.addActionListener(e -> {
            try {
                // 1. Siapkan Object Movie (Cukup UUID nya saja utk bridging)
                Movie m = new Movie("", 0, "", Ratings.PG13); 
                m.setMoviesUUID(tfMovieUUID.getText());

                // 2. Siapkan Object Studio
                // Type studio dummy saja karena di repo yg dicek UUID
                Studio s = new Studio(tfStudioUUID.getText(), MovieTypes.STARIUM); 
                s.setStudioUUID(tfStudioUUID.getText());

                int price = Integer.parseInt(tfPrice.getText());

                // 3. Parsing Tanggal
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime startTime = LocalDateTime.parse(tfDateTime.getText(), formatter);

                // 4. Bungkus ke ShowTime
                ShowTime show = new ShowTime("", price); // show_time UUID di generate repo
                show.setMovie(m);
                show.setStudio(s);

                // 5. Panggil Controller
                String result = adminController.addShowTime(show, startTime);
                JOptionPane.showMessageDialog(frame, result);

            } catch (DateTimeParseException dtpe) {
                JOptionPane.showMessageDialog(frame, "Format Tanggal Salah! Gunakan: yyyy-MM-dd HH:mm");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(frame, "Price harus angka!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        refreshContentPanel(panel);
    }

    // Helper untuk bikin baris form rapi
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent component) {
        gbc.gridx = 0; 
        gbc.gridy = row;
        gbc.weightx = 0.1;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        panel.add(component, gbc);
    }
}