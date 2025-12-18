package tubes.views;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// Import Project Classes
import tubes.controllers.AdminController;
import tubes.models.Movie;
import tubes.models.Studio;
import tubes.models.ShowTime;
import tubes.models.enums.Ratings;

public class AdminView {

    // --- 1. Global Variables ---
    private AdminController adminController;
    private JFrame frame;
    private JPanel navigationDiv;
    private JPanel mainContentPanel;

    // Components untuk Movie Panel
    private JTextField tfTitle, tfDuration, tfGenre;
    private JComboBox<Ratings> cbRating;
    private JTable movieTable;
    private DefaultTableModel movieTableModel;

    // Components untuk Showtime Panel
    private JComboBox<Movie> cbMovieSelection;
    private JComboBox<Studio> cbStudioSelection;
    private JTextField tfShowTimeInput; // Input Tanggal Manual

    // Buttons Navigation
    private JButton btnManageMovies;
    private JButton btnManageShowtimes;
    private JButton btnSignUpStaff;

    // --- 2. Constructor ---
    public AdminView() {
        adminController = new AdminController();
        frame = new JFrame("Admin Dashboard");

        // Setup Frame Dasar
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Init Navigation Bar
        initNavigation();

        // Tampilan awal langsung ke Manage Movies
        showMovieManagementPanel();

        frame.setVisible(true);
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public void dispose() {
        frame.dispose();
    }

    // --- 3. Navigation Setup ---
    // --- 3. Navigation Setup (REVISI) ---
    private void initNavigation() {
        navigationDiv = new JPanel();
        navigationDiv.setBackground(Color.DARK_GRAY);
        // Ganti Layout utama menjadi BorderLayout
        navigationDiv.setLayout(new BorderLayout());

        // 1. Buat Panel untuk Tombol Kiri (Movies & Showtimes)
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        leftPanel.setOpaque(false); // Supaya background DARK_GRAY tetap terlihat

        btnManageMovies = new JButton("Manage Movies");
        btnManageShowtimes = new JButton("Manage Showtimes");

        // Styling & Listener
        styleNavButton(btnManageMovies);
        styleNavButton(btnManageShowtimes);
        btnManageMovies.addActionListener(e -> showMovieManagementPanel());
        btnManageShowtimes.addActionListener(e -> showShowtimeManagementPanel());

        // Masukkan ke panel kiri
        leftPanel.add(btnManageMovies);
        leftPanel.add(btnManageShowtimes);

        // 2. Buat Panel untuk Tombol Kanan (Sign Up Staff)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        rightPanel.setOpaque(false); // Supaya background DARK_GRAY tetap terlihat

        btnSignUpStaff = new JButton("Sign Up Staff");

        btnSignUpStaff.addActionListener(e ->  {
            frame.setVisible(false);; // sembunyikan login
            new SignupUI(this); // kirim reference
        });

        // Styling
        styleNavButton(btnSignUpStaff);
        // Tambahkan listener btnSignUpStaff di sini nanti (jika sudah ada logic-nya)

        // Masukkan ke panel kanan
        rightPanel.add(btnSignUpStaff);

        // 3. Gabungkan Panel Kiri dan Kanan ke Navigation Div Utama
        navigationDiv.add(leftPanel, BorderLayout.WEST);
        navigationDiv.add(rightPanel, BorderLayout.EAST);

        frame.add(navigationDiv, BorderLayout.NORTH);
    }

    private void styleNavButton(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    // ==========================================
    // 4. MAIN PANEL A: MANAGE MOVIES
    // ==========================================
    private void showMovieManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- BAGIAN FORM INPUT (ADD MOVIE) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Movie"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Init Components
        tfTitle = new JTextField(20);
        tfDuration = new JTextField(20);
        tfGenre = new JTextField(20);
        cbRating = new JComboBox<>(Ratings.values());

        JButton btnAdd = new JButton("Add Movie");
        btnAdd.setBackground(new Color(100, 200, 100)); // Hijau
        btnAdd.setForeground(Color.WHITE);

        // Layout Form
        addFormRow(formPanel, gbc, 0, "Movie Title:", tfTitle);
        addFormRow(formPanel, gbc, 1, "Duration (Minutes):", tfDuration);
        addFormRow(formPanel, gbc, 2, "Genre:", tfGenre);
        addFormRow(formPanel, gbc, 3, "Rating:", cbRating);

        // Tombol Add ditaruh di kanan bawah form
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(btnAdd, gbc);

        // --- BAGIAN TABEL (UNTUK LIHAT HASIL) ---
        String[] columnNames = { "Title", "Duration", "Genre", "Rating", "UUID" };
        movieTableModel = new DefaultTableModel(columnNames, 0);
        movieTable = new JTable(movieTableModel);
        JScrollPane scrollPane = new JScrollPane(movieTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));

        // --- LOGIC TOMBOL ADD MOVIE ---
        btnAdd.addActionListener(e -> {
            // 1. Validasi Input Kosong
            if (tfTitle.getText().isEmpty() || tfDuration.getText().isEmpty() || tfGenre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Semua data harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // 2. Ambil Data
                String title = tfTitle.getText();
                int duration = Integer.parseInt(tfDuration.getText());
                String genre = tfGenre.getText();
                Ratings rating = (Ratings) cbRating.getSelectedItem();

                // 3. Bungkus ke Object Movie
                Movie newMovie = new Movie(title, duration, genre, rating);

                // 4. Kirim ke Controller
                String result = adminController.addMovies(newMovie);

                // 5. Cek Hasil
                if (result.contains("successfully")) {
                    JOptionPane.showMessageDialog(frame, "Berhasil Menambahkan Film!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadMovieTableData();
                    clearMovieForm();
                } else {
                    JOptionPane.showMessageDialog(frame, result, "Gagal", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Durasi harus berupa angka (menit)!", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Load data awal
        loadMovieTableData();

        // Susun Layout Utama
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshContentPanel(panel);
    }

    // ==========================================
    // 5. MAIN PANEL B: MANAGE SHOWTIMES
    // ==========================================
    private void showShowtimeManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- FORM INPUT JADWAL (ACC JADWAL) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Atur Jadwal Tayang (ACC Showtime)"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spasi agak lega
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Load Data untuk Dropdown
        List<Movie> movies = adminController.getAllMovies();
        List<Studio> studios = adminController.getAllStudios();

        // 2. Init Components
        cbMovieSelection = new JComboBox<>(movies.toArray(new Movie[0]));
        cbStudioSelection = new JComboBox<>(studios.toArray(new Studio[0]));

        tfShowTimeInput = new JTextField();
        tfShowTimeInput.setBorder(BorderFactory.createTitledBorder("Format: yyyy-MM-dd HH:mm"));

        JButton btnAcc = new JButton("ACC JADWAL");
        btnAcc.setBackground(Color.BLUE);
        btnAcc.setForeground(Color.WHITE);
        btnAcc.setFont(btnAcc.getFont().deriveFont(Font.BOLD));
        btnAcc.setPreferredSize(new Dimension(150, 40));

        // 3. Layouting Form
        addFormRow(formPanel, gbc, 0, "Pilih Film:", cbMovieSelection);
        addFormRow(formPanel, gbc, 1, "Pilih Studio:", cbStudioSelection);
        addFormRow(formPanel, gbc, 3, "Waktu Tayang:", tfShowTimeInput);

        // Tombol ACC
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(btnAcc, gbc);

        // --- LOGIC TOMBOL ACC ---
        btnAcc.addActionListener(e -> {
            try {
                // Ambil Data
                Movie selectedMovie = (Movie) cbMovieSelection.getSelectedItem();
                Studio selectedStudio = (Studio) cbStudioSelection.getSelectedItem();
                String timeText = tfShowTimeInput.getText();

                // Validasi
                if (selectedMovie == null || selectedStudio == null || timeText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Semua data harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime showTimeDate = LocalDateTime.parse(timeText, formatter);

                // Bungkus Object
                ShowTime newShow = new ShowTime(timeText);
                newShow.setMovie(selectedMovie);
                newShow.setStudio(selectedStudio);

                // Kirim ke Controller
                String result = adminController.addShowTime(newShow, showTimeDate);

                // Feedback
                if (result.toLowerCase().contains("success")) {
                    String msg = "Sukses!\nFilm: " + selectedMovie.getTitle() +
                            "\nStudio: " + selectedStudio.getStudioNumber() +
                            "\nJam: " + timeText;
                    JOptionPane.showMessageDialog(frame, msg, "Berhasil", JOptionPane.INFORMATION_MESSAGE);

                    tfShowTimeInput.setText(""); // Reset tanggal saja
                } else {
                    JOptionPane.showMessageDialog(frame, result, "Gagal", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Harga harus angka!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Format Tanggal Salah!\nGunakan: yyyy-MM-dd HH:mm", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        // Masukkan Form ke Panel Utama (Posisi di Atas)
        panel.add(formPanel, BorderLayout.NORTH);

        refreshContentPanel(panel);
    }

    // ==========================================
    // 6. HELPER METHODS
    // ==========================================

    // Method ganti panel (penting untuk navigasi)
    private void refreshContentPanel(JPanel newPanel) {
        if (mainContentPanel != null) {
            frame.remove(mainContentPanel);
        }
        mainContentPanel = newPanel;
        frame.add(mainContentPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void clearMovieForm() {
        tfTitle.setText("");
        tfDuration.setText("");
        tfGenre.setText("");
        cbRating.setSelectedIndex(0);
    }

    private void loadMovieTableData() {
        movieTableModel.setRowCount(0); // Hapus data lama
        List<Movie> movies = adminController.getAllMovies();

        for (Movie m : movies) {
            movieTableModel.addRow(new Object[] {
                    m.getTitle(),
                    m.getDuration() + " min",
                    m.getGenre(),
                    m.getRating(),
                    m.getMoviesUUID()
            });
        }
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
    }
}