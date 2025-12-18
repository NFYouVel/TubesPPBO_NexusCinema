package tubes.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import tubes.controllers.SeatController;
import tubes.controllers.ShowTimeController;
import tubes.controllers.TicketController;
import tubes.models.Movie;
import tubes.models.Seat;
import tubes.models.ShowTime;
import tubes.models.exceptions.EmptyListException;
import tubes.models.interfaces.PageNavigator;
import tubes.utils.UtilGlobal;
import tubes.utils.UtilJavaSwing;
import tubes.views.customers.BackgroundPanel;
import tubes.views.customers.MoviesPanel;
import tubes.views.customers.PaymentPanel;
import tubes.views.customers.StudioTypePanel;

public class CustomerView implements PageNavigator {

    // Controller
    private final ShowTimeController showMoviesController;
    private final SeatController seatController;
    private final TicketController ticketController;
    private final boolean isImages;
    private List<ShowTime> showTimes;

    // GUI Java Swing 
    private final JFrame frame;
    private CardLayout cardLayout;
    private BackgroundPanel backgroundCustomer;
    // Main Panel
    private JPanel navbarContent;
    private JPanel mainContent;
    // Navbar Content
    private JButton moviesButton;
    private JButton historyButton;
    private JButton logoutButton;

    // Pages
    private JPanel defaultPage; // Empty Page just for default
    private JScrollPane moviesPage; // Page List of Movies
    private JPanel showTimePage; // Page List of Show Times from one movies
    private JPanel seatSelectionPage; // Page for Seat Selection
    private JPanel paymentPage; // Page for Payment
    private JPanel printTicketPage; // Page for Print Ticket
    private JPanel historyPage;

    // Seat Selection Page Components
    private List<Seat> selectedSeats;

    public CustomerView(boolean isImages) {
        // Frame
        frame = UtilJavaSwing.generateFrame("Customer View", 1024, 800);
        // Controller
        showMoviesController = new ShowTimeController();
        seatController = new SeatController();
        ticketController = new TicketController();
        // Pages
        defaultPage = new JPanel();
        showTimePage = new JPanel();
        paymentPage = new JPanel();
        seatSelectionPage = new JPanel();
        printTicketPage = new JPanel();
        historyPage = new JPanel();
        // Other Attributes
        this.isImages = isImages;
    }

    // <------------------------------------------- START ------------------------------------------->
    public void startCustomerView() {
        backgroundCustomer = new BackgroundPanel("/assets/images/home_page.png");
        backgroundCustomer.setLayout(new BorderLayout());
        setNavbarContent();
        setMainContent();

        backgroundCustomer.add(navbarContent, BorderLayout.NORTH);
        backgroundCustomer.add(mainContent, BorderLayout.CENTER);

        // Set Page Of Index
        handleMoviesButton();

        // Adding Page
        mainContent.add(defaultPage, "DEFAULT");
        mainContent.add(moviesPage, "HOME");
        mainContent.add(showTimePage, "MOVIES");
        mainContent.add(seatSelectionPage, "SEAT_SELECTION");
        mainContent.add(paymentPage, "PAYMENT");
        mainContent.add(printTicketPage, "PRINT_TICKET");
        mainContent.add(historyPage, "HISTORY");
        showPage("DEFAULT");

        handleNavigationActionButton();

        frame.setContentPane(backgroundCustomer);
        frame.revalidate(); 
        frame.repaint();    
        frame.setVisible(true);

    }

    private void setNavbarContent() {
        // Navigation Bar Settings
        navbarContent = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbarContent.setOpaque(true);
        navbarContent.setBackground(new Color(130, 130, 130, 150));
        navbarContent.setBorder(BorderFactory.createEmptyBorder(16, 16, 0, 0));
        navbarContent.setPreferredSize(new Dimension(1024, 100));

        // Navigation Bar Content
        moviesButton = UtilJavaSwing.generateButton("MOVIES");
        moviesButton.setFont(new Font("Arial", Font.BOLD, 16));

        historyButton = UtilJavaSwing.generateButton("HISTORY");
        historyButton.setFont(new Font("Arial", Font.BOLD, 16));

        logoutButton = UtilJavaSwing.generateButton("LOGOUT");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));

        // Adding Content
        navbarContent.add(moviesButton);
        navbarContent.add(Box.createHorizontalStrut(20));
        navbarContent.add(historyButton);
        navbarContent.add(Box.createHorizontalStrut(20));
        navbarContent.add(logoutButton);

        // Adding Navigation Bar Content
        frame.add(navbarContent, BorderLayout.CENTER);
    }

    private void handleNavigationActionButton() {
        // Action Button
        moviesButton.addActionListener(e -> {
            navbarContent.removeAll();
            navbarContent.add(moviesButton);
            navbarContent.add(historyButton);
            navbarContent.add(logoutButton);
            navbarContent.revalidate();
            navbarContent.repaint();

            clearFrameContent();
            showPage("HOME");
        });

        historyButton.addActionListener(e -> {
            historyPage.removeAll();
            clearFrameContent();

            JPanel historyContent = new HistoryTicketView(UtilGlobal.getGlobalUUID());
            historyPage.add(historyContent, BorderLayout.CENTER);

            navbarContent.removeAll();
            navbarContent.add(moviesButton);
            navbarContent.add(historyButton);
            navbarContent.add(logoutButton);
            navbarContent.revalidate();
            navbarContent.repaint();

            showPage("HISTORY");
        });

        logoutButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                frame.dispose();
                UtilGlobal.setGlobalUUID(null);
                UserView loginView = new UserView();
                loginView.showMenuLogin();
            }
        });
    }

    private void setMainContent() {
        mainContent = new JPanel();
        mainContent.setOpaque(false);

        cardLayout = new CardLayout();
        mainContent.setLayout(cardLayout);
    }

    @Override
    public void showPage(String text) {
        cardLayout.show(mainContent, text);
    }
    @Override
    public void viewStudioType(String moviesUUID) {
        handleViewStudioTypeButton(moviesUUID);
    }
    @Override
    public void goToSeatPage(String showtimeUUID) {
        try {
            handleSelectSeatsButton(showtimeUUID);
        } catch (EmptyListException e) {
            System.out.println(e.getMessage());
        }
    }

    private void clearFrameContent() {
        frame.revalidate();
        frame.repaint();
    }

    // <------------------------------------------- HANDLE BUTTONS ------------------------------------------->
    private void handleMoviesButton() {
        JPanel movieContent = new MoviesPanel(isImages, this);
        moviesPage = new JScrollPane(movieContent);

        moviesPage.repaint();
        moviesPage.revalidate();
        clearFrameContent();
    }

    private void handleViewStudioTypeButton(String moviesUUID) {
        showPage("MOVIES");
        showTimePage.removeAll();
        
        JPanel studioTypeContent = new StudioTypePanel(moviesUUID, isImages, this);
        showTimePage.add(studioTypeContent, BorderLayout.CENTER);
        
        // Refresh page
        showTimePage.repaint();
        showTimePage.revalidate();
        clearFrameContent();
    }

    private void handleSelectSeatsButton(String showUUID) throws EmptyListException {
        clearFrameContent();
        showPage("SEAT_SELECTION");
        seatSelectionPage.removeAll();
        selectedSeats = new ArrayList<>();

        // Add refresh seat selection page
        JButton refreshSeatsButton = UtilJavaSwing.generateButton("Refresh Seats");
        navbarContent.add(refreshSeatsButton);
        // Handle refresh seats action

        JButton confirmSeatsButton = UtilJavaSwing.generateButton("Confirm Seats");
        navbarContent.add(confirmSeatsButton);
        // Handle confirm seats action
        confirmSeatsButton.addActionListener(e -> {
            if (selectedSeats != null && !selectedSeats.isEmpty()) {

                int result = JOptionPane.showConfirmDialog(frame, "You selected " + selectedSeats.size() + " seat(s).\nConfirm?", "Confirm Seats", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    for (Seat seat : selectedSeats) {
                        System.out.println("Selected Seat: " + seat.getSeatsUUID());
                    }
                    JOptionPane.showMessageDialog(frame, "Seats confirmed! Proceeding to payment...");
                    navbarContent.remove(refreshSeatsButton);
                    navbarContent.remove(confirmSeatsButton);
                    clearFrameContent();
                    try {
                        handleConfirmSeatsButton(selectedSeats, showUUID);
                    } catch (EmptyListException e1) {
                        JOptionPane.showMessageDialog(frame, "Error processing tickets: " + e1.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No seats selected. Please select at least one seat.");
            }
        });

        refreshSeatsButton.addActionListener(e -> {
            navbarContent.remove(refreshSeatsButton);
            navbarContent.remove(confirmSeatsButton);
            clearFrameContent();
            try {
                handleSelectSeatsButton(showUUID);
            } catch (EmptyListException e1) {
                System.out.println(e1.getMessage());
            }
        });

        List<Seat> occupiedSeats = seatController.callAllOccupiedSeats(showUUID);
        List<Seat> availableSeats = seatController.callAllAvailableSeats(showUUID);

        if (occupiedSeats == null) {
            availableSeats = seatController.setSeatAvailability(availableSeats);
        } else {
            availableSeats = seatController.setSeatAvailability(availableSeats, occupiedSeats);
        }

        JPanel seatPageContent = new JPanel(new BorderLayout());

        JPanel seatsPanel = new JPanel();
        seatsPanel.setOpaque(false);
        seatsPanel.setLayout(new GridLayout(0, 12, 8, 8));

        int tempRow = 0;
        for (Seat seat : availableSeats) {
            JButton seatButton = UtilJavaSwing.generateButton(seat.getSeatNumber(), 60, 60);
            seatButton.putClientProperty("seatUUID", seat.getSeatsUUID());
            seatButton.putClientProperty("seatNumber", seat.getSeatNumber());
            seatButton.putClientProperty("seatRow", seat.getSeatRow());
            seatButton.putClientProperty("seatColumn", seat.getSeatColumn());

            seatButton.setFont(new Font("Arial", Font.BOLD, 12));
            seatButton.setForeground(new Color(68, 75, 77));
            if (seat.getIsAvailable()) {
                seatButton.setBackground(new Color(84, 247, 55));
            } else {
                seatButton.setBackground(new Color(247, 55, 55));
                seatButton.setEnabled(false);
            }
            if (tempRow == 0) {
                String rowLabel = String.valueOf(seat.getSeatNumber().charAt(0));
                JLabel rowLabelPanel = new JLabel(rowLabel, SwingConstants.CENTER);
                rowLabelPanel.setFont(new Font("Arial", Font.BOLD, 16));
                rowLabelPanel.setForeground(new Color(50, 50, 50));
                rowLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                rowLabelPanel.setOpaque(false);

                seatsPanel.add(rowLabelPanel);
            }

            seatsPanel.add(seatButton);

            tempRow++;

            if (tempRow % 10 == 0) {
                String rowLabel = String.valueOf(seat.getSeatNumber().charAt(0));
                JLabel rowLabelPanel = new JLabel(rowLabel, SwingConstants.CENTER);
                rowLabelPanel.setFont(new Font("Arial", Font.BOLD, 16));
                rowLabelPanel.setForeground(new Color(50, 50, 50));
                rowLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                rowLabelPanel.setOpaque(false);

                tempRow = 0;
                seatsPanel.add(rowLabelPanel);
            }

            seatButton.addActionListener(evt -> {
                JButton clickedBtn = (JButton) evt.getSource();
                String seatUUID = (String) clickedBtn.getClientProperty("seatUUID");

                String seatNumber = (String) clickedBtn.getClientProperty("seatNumber");
                int seatRow = (int) clickedBtn.getClientProperty("seatRow");
                int seatColumn = (int) clickedBtn.getClientProperty("seatColumn");

                Seat selectedSeat = new Seat(seatNumber, seatRow, seatColumn);
                selectedSeat.setSeatsUUID(seatUUID);
                selectedSeats.add(selectedSeat);
                System.out.println(selectedSeats.size());
                seatButton.setEnabled(false);
                seatButton.setBackground(new Color(48, 197, 214));

            });

        }
        JPanel screenPanel = new JPanel();
        screenPanel.setOpaque(false);
        screenPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        JLabel screenLabel = new JLabel("SCREEN");
        screenLabel.setFont(new Font("Arial", Font.BOLD, 20));
        screenPanel.add(screenLabel);

        seatPageContent.add(seatsPanel, BorderLayout.CENTER);
        seatPageContent.add(screenPanel, BorderLayout.SOUTH);

        seatSelectionPage.add(seatPageContent, BorderLayout.CENTER);

        seatSelectionPage.revalidate();
        seatSelectionPage.repaint();
    }

    private void handleConfirmSeatsButton(List<Seat> selectedSeats, String showUUID) throws EmptyListException {
        showPage("PAYMENT");
        paymentPage.removeAll();

        PaymentPanel paymentPanel = new PaymentPanel(selectedSeats, showUUID, this);
        paymentPage.add(paymentPanel, BorderLayout.CENTER);
    }

}
