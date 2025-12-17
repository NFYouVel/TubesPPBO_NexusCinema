package tubes.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import tubes.controllers.HistoryTicketController;
import tubes.models.Ticket;
import tubes.models.exceptions.EmptyListException;
import tubes.repositories.HistoryTicketRepository;

// UBAH: Sekarang extends JPanel, bukan JFrame
public class HistoryTicketView extends JPanel {

    private JPanel contentPanel;
    private HistoryTicketRepository repo;
    private String custUUID;

    public HistoryTicketView(String custUUID) {
        this.custUUID = custUUID;
        this.repo = new HistoryTicketRepository();

        // Setup Layout Panel ini
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- 1. Header Bagian Atas ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(30, 144, 255)); // Biru
        
        JLabel lblHeader = new JLabel("My Booking History");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        headerPanel.add(lblHeader);
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Area Konten (List Tiket) ---
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 245)); // Background abu muda
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll halus
        
        add(scrollPane, BorderLayout.CENTER);

        // Load data saat panel dibuat
        loadData();
    }

    private void loadData() {
        contentPanel.removeAll();
        
        HistoryTicketController historyController = new HistoryTicketController();
        try {
            List<Ticket> tickets = historyController.TicketHistoryListAll(custUUID);
            for (Ticket t : tickets) {
                // Panggil Inner Class TicketCard
                TicketCard card = new TicketCard(t);
                
                contentPanel.add(card);
                contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Jarak antar card
            }
            
        } catch (EmptyListException e) {
            JLabel lblEmpty = new JLabel("Belum ada riwayat pemesanan.");
            lblEmpty.setFont(new Font("SansSerif", Font.ITALIC, 16));
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            contentPanel.add(Box.createVerticalGlue());
            contentPanel.add(lblEmpty);
            contentPanel.add(Box.createVerticalGlue());
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage());
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // (Tampilan per tiket)
    private class TicketCard extends JPanel {

        public TicketCard(Ticket ticket) {
            setLayout(new BorderLayout(15, 15));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    new EmptyBorder(15, 15, 15, 15)
            ));
            
            // Set ukuran
            setPreferredSize(new Dimension(550, 100));
            setMaximumSize(new Dimension(800, 100));
            
            // KIRI: Info Film
            JPanel leftPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            leftPanel.setBackground(Color.WHITE);
            
            JLabel lblTitle = new JLabel(ticket.getShowTime().getMovie().getTitle());
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
            
            String metaInfo = ticket.getShowTime().getMovie().getGenre() + " | " + 
                              ticket.getShowTime().getMovie().getDuration() + " mins";
            JLabel lblMeta = new JLabel(metaInfo);
            lblMeta.setForeground(Color.GRAY);

            leftPanel.add(lblTitle);
            leftPanel.add(lblMeta);

            // TENGAH: Info Jadwal
            JPanel centerPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            centerPanel.setBackground(Color.WHITE);

            // PASTIKAN GETTER INI SUDAH ADA DI MODEL KAMU YA
            String studioInfo = "Studio " + ticket.getShowTime().getStudio().getStudioNumber() + 
                                " (" + ticket.getShowTime().getStudio().getStudioType() + ")";
            JLabel lblStudio = new JLabel("📍 " + studioInfo);
            JLabel lblTime = new JLabel("🕒 " + ticket.getShowTime().getshowtimeDateTime());
            JLabel lblSeat = new JLabel("💺 Seat: " + ticket.getSeats().getSeatNumber());
            lblSeat.setFont(new Font("SansSerif", Font.BOLD, 14));
            lblSeat.setForeground(new Color(0, 102, 204));

            centerPanel.add(lblStudio);
            centerPanel.add(lblTime);
            centerPanel.add(lblSeat);

            // KANAN: Harga & Status
            JPanel rightPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            rightPanel.setBackground(Color.WHITE);
            
            JLabel lblStatus = new JLabel("PAID");
            lblStatus.setOpaque(true);
            lblStatus.setBackground(new Color(220, 255, 220));
            lblStatus.setForeground(new Color(0, 128, 0));
            lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
            
            JLabel lblPrice = new JLabel("Rp " + ticket.getShowTime().getStudio().getPrice());
            lblPrice.setFont(new Font("SansSerif", Font.BOLD, 14));
            lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);

            rightPanel.add(lblStatus);
            rightPanel.add(lblPrice);

            add(leftPanel, BorderLayout.WEST);
            add(centerPanel, BorderLayout.CENTER);
            add(rightPanel, BorderLayout.EAST);
        }
    }
}