// // ... imports
//     import tubes.models.enums.Ratings;

//     // ... variable class
//     private JTextField tfTitle, tfDuration, tfGenre;
//     private JComboBox<Ratings> cbRating;
//     private JTable movieTable;
//     private DefaultTableModel tableModel;

//     private void showMovieManagementPanel() {
//         JPanel panel = new JPanel(new BorderLayout(10, 10));
//         panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

//         // --- 1. BAGIAN FORM INPUT (ADD MOVIE) ---
//         JPanel formPanel = new JPanel(new GridBagLayout());
//         formPanel.setBorder(BorderFactory.createTitledBorder("Add New Movie"));
//         GridBagConstraints gbc = new GridBagConstraints();
//         gbc.insets = new Insets(5, 5, 5, 5);
//         gbc.fill = GridBagConstraints.HORIZONTAL;

//         // Init Components
//         tfTitle = new JTextField(20);
//         tfDuration = new JTextField(20);
//         tfGenre = new JTextField(20);
        
//         // ComboBox untuk Rating biar admin ga salah ketik
//         cbRating = new JComboBox<>(Ratings.values());

//         JButton btnAdd = new JButton("Add Movie");
//         btnAdd.setBackground(new Color(100, 200, 100)); // Warna hijau biar fresh
//         btnAdd.setForeground(Color.WHITE);

//         // Layout Form
//         addFormRow(formPanel, gbc, 0, "Movie Title:", tfTitle);
//         addFormRow(formPanel, gbc, 1, "Duration (Minutes):", tfDuration);
//         addFormRow(formPanel, gbc, 2, "Genre:", tfGenre);
//         addFormRow(formPanel, gbc, 3, "Rating:", cbRating);

//         // Tombol Add ditaruh di bawah form
//         gbc.gridx = 1; gbc.gridy = 4;
//         gbc.anchor = GridBagConstraints.EAST; // Rata kanan
//         formPanel.add(btnAdd, gbc);

//         // --- 2. BAGIAN TABEL (UNTUK LIHAT HASIL) ---
//         String[] columnNames = {"Title", "Duration", "Genre", "Rating", "UUID"};
//         tableModel = new DefaultTableModel(columnNames, 0);
//         movieTable = new JTable(tableModel);
//         JScrollPane scrollPane = new JScrollPane(movieTable);
//         scrollPane.setPreferredSize(new Dimension(800, 250));

//         // --- LOGIC TOMBOL ADD ---
//         btnAdd.addActionListener(e -> {
//             // 1. Validasi Input Kosong
//             if (tfTitle.getText().isEmpty() || tfDuration.getText().isEmpty() || tfGenre.getText().isEmpty()) {
//                 JOptionPane.showMessageDialog(frame, "Semua data harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
//                 return;
//             }

//             try {
//                 // 2. Ambil Data
//                 String title = tfTitle.getText();
//                 int duration = Integer.parseInt(tfDuration.getText()); // Bisa error kalau bukan angka
//                 String genre = tfGenre.getText();
//                 Ratings rating = (Ratings) cbRating.getSelectedItem();

//                 // 3. Bungkus ke Object Movie (Tanpa UUID, karena auto-generate)
//                 Movie newMovie = new Movie(title, duration, genre, rating);

//                 // 4. Kirim ke Controller
//                 String result = adminController.addMovies(newMovie);

//                 // 5. Cek Hasil & Feedback ke User
//                 if (result.contains("successfully")) {
//                     JOptionPane.showMessageDialog(frame, "Berhasil Menambahkan Film!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
//                     // 6. Refresh Tabel & Reset Form
//                     loadTableData(); 
//                     clearForm();
//                 } else {
//                     JOptionPane.showMessageDialog(frame, result, "Gagal", JOptionPane.ERROR_MESSAGE);
//                 }

//             } catch (NumberFormatException ex) {
//                 JOptionPane.showMessageDialog(frame, "Durasi harus berupa angka (menit)!", "Input Error", JOptionPane.ERROR_MESSAGE);
//             }
//         });

//         // Load data awal
//         loadTableData();

//         // Susun Layout Utama
//         panel.add(formPanel, BorderLayout.NORTH); // Form di atas
//         panel.add(scrollPane, BorderLayout.CENTER); // Tabel di bawah

//         refreshContentPanel(panel);
//     }

//     // --- Helper Methods ---

//     private void clearForm() {
//         tfTitle.setText("");
//         tfDuration.setText("");
//         tfGenre.setText("");
//         cbRating.setSelectedIndex(0);
//     }

//     private void loadTableData() {
//         tableModel.setRowCount(0); // Hapus data lama
//         List<Movie> movies = adminController.getAllMovies(); // Pastikan controller punya ini
        
//         for (Movie m : movies) {
//             tableModel.addRow(new Object[]{
//                 m.getTitle(),
//                 m.getDuration() + " min",
//                 m.getGenre(),
//                 m.getRating(),
//                 m.getMoviesUUID() // UUID tetap ditampilkan di tabel buat info
//             });
//         }
//     }

//     // Helper bikin baris form rapi
//     private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent component) {
//         gbc.gridx = 0; 
//         gbc.gridy = row;
//         gbc.weightx = 0.1;
//         gbc.anchor = GridBagConstraints.WEST;
//         panel.add(new JLabel(labelText), gbc);

//         gbc.gridx = 1;
//         gbc.weightx = 0.9;
//         gbc.fill = GridBagConstraints.HORIZONTAL;
//         panel.add(component, gbc);
//     }