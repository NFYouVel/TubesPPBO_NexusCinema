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
import tubes.models.Seat;
import tubes.models.ShowTime;
import tubes.models.exceptions.EmptyListException;
import tubes.models.interfaces.PageNavigator;
import tubes.utils.UtilGlobal;
import tubes.utils.UtilJavaSwing;
import tubes.views.customers.PaymentPanel;


public class CustomerView implements PageNavigator{

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
        frame.revalidate(); // penting
        frame.repaint();    // penting
        frame.setVisible(true);

    }

    public void setNavbarContent() {
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
            navbarContent.revalidate();
            navbarContent.repaint();

            clearFrameContent();
            showPage("HOME");
        });

        historyButton.addActionListener(e -> {
            JPanel historyContent = new HistoryTicketView(UtilGlobal.getGlobalUUID());
            historyPage.add(historyContent, BorderLayout.CENTER);

            navbarContent.removeAll();
            navbarContent.add(moviesButton);
            navbarContent.revalidate();
            navbarContent.repaint();

            clearFrameContent();
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

    public void setMainContent() {
        mainContent = new JPanel();
        mainContent.setOpaque(false);

        cardLayout = new CardLayout();
        mainContent.setLayout(cardLayout);
    }

    @Override
    public void showPage(String text) {
        cardLayout.show(mainContent, text);
    }

    private void clearFrameContent() {
        frame.revalidate();
        frame.repaint();
    }

    // <------------------------------------------- HANDLE BUTTONS ------------------------------------------->
    private void handleMoviesButton() {
        try {
            showTimes = showMoviesController.callShowMoviesListAll();
        } catch (EmptyListException e) {
            System.out.println(e.getMessage());
        }

        JPanel moviesCards = new JPanel();
        moviesCards.setOpaque(false);
        moviesCards.setLayout(new BoxLayout(moviesCards, BoxLayout.X_AXIS));

        for (ShowTime showTime : showTimes) {

            // CARD WRAPPER
            JPanel movieCard = new JPanel();
            movieCard.setOpaque(false);
            movieCard.setLayout(new BorderLayout());
            movieCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // IMAGE
            if (isImages) {
                JLabel iconMovie = UtilJavaSwing.getMovieIconLabel(showTime.getMovie().getTitle());
                movieCard.add(iconMovie, BorderLayout.NORTH);
            }

            // DETAILS PANEL
            JPanel detailsMovie = new JPanel();
            detailsMovie.setOpaque(false);
            detailsMovie.setLayout(new BoxLayout(detailsMovie, BoxLayout.Y_AXIS));
            detailsMovie.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

            JLabel title = new JLabel(showTime.getMovie().getTitle());
            title.setFont(new Font("Arial", Font.BOLD, 24));

            JLabel duration = new JLabel("Duration: " + showTime.getMovie().getDuration() + " mins");
            JLabel genre = new JLabel("Genre: " + showTime.getMovie().getGenre());
            JLabel ratings = new JLabel("Ratings: " + showTime.getMovie().getRating().name());

            detailsMovie.add(title);
            detailsMovie.add(genre);
            detailsMovie.add(ratings);
            detailsMovie.add(duration);

            movieCard.add(detailsMovie, BorderLayout.CENTER);

            // BUTTON
            JButton viewStudioTypeButton = UtilJavaSwing.generateButton("View Studio Type");
            viewStudioTypeButton.putClientProperty("movies_UUID", showTime.getMovie().getMoviesUUID());
            viewStudioTypeButton.setPreferredSize(new Dimension(150, 40));

            viewStudioTypeButton.addActionListener(e -> {
                JButton clickedBtn = (JButton) e.getSource();
                String movies_UUID = (String) clickedBtn.getClientProperty("movies_UUID");

                clearFrameContent();
                handleViewStudioTypeButton(movies_UUID);
            });

            JPanel btnPanel = new JPanel();
            btnPanel.setOpaque(false);
            btnPanel.add(viewStudioTypeButton);

            movieCard.add(btnPanel, BorderLayout.SOUTH);

            // ADD CARD TO PAGE
            moviesCards.add(movieCard);
        }

        moviesPage = new JScrollPane(moviesCards);
        moviesPage.setOpaque(false);
        moviesPage.getViewport().setOpaque(false);
    }

    private void handleViewStudioTypeButton(String moviesUUID) {
        showPage("MOVIES");

        showTimePage.removeAll();

        try {
            showTimes.clear();
            showTimes = showMoviesController.callShowTimesFromOneMovies(moviesUUID);
        } catch (EmptyListException e) {
            System.out.println(e.getMessage());
        }

        JPanel moviesCards = new JPanel();
        moviesCards.setOpaque(false);
        moviesCards.setLayout(new BoxLayout(moviesCards, BoxLayout.X_AXIS));

        for (ShowTime showTime : showTimes) {

            // CARD WRAPPER
            JPanel movieCard = new JPanel();
            movieCard.setOpaque(false);
            movieCard.setLayout(new BoxLayout(movieCard, BoxLayout.Y_AXIS));
            movieCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            movieCard.setAlignmentY(Component.TOP_ALIGNMENT);

            // IMAGE
            if (isImages) {
                JLabel iconMovie = UtilJavaSwing.getMovieIconLabel(showTime.getMovie().getTitle());
                movieCard.add(iconMovie, BorderLayout.NORTH);
            }

            // DETAILS PANEL
            JPanel detailsMovie = new JPanel();
            detailsMovie.setOpaque(false);
            detailsMovie.setLayout(new BoxLayout(detailsMovie, BoxLayout.Y_AXIS));
            detailsMovie.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            detailsMovie.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

            JLabel title = new JLabel(showTime.getMovie().getTitle());
            title.setFont(new Font("Arial", Font.BOLD, 24));

            JLabel studioType = new JLabel("Studio Type: " + showTime.getStudio().getStudioType());
            studioType.setFont(new Font("Arial", Font.BOLD, 16));
            studioType.setBorder(new EmptyBorder(10, 0, 10, 0));

            JButton showtimesButton = UtilJavaSwing.generateButton("Showtimes");
            showtimesButton.putClientProperty("movies_UUID", showTime.getMovie().getMoviesUUID());
            showtimesButton.setPreferredSize(new Dimension(150, 40));

            detailsMovie.add(title);
            detailsMovie.add(studioType);
            detailsMovie.add(showtimesButton);

            // SHOWTIMES BUTTON ACTION
            showtimesButton.addActionListener(e -> {
                detailsMovie.remove(showtimesButton);

                try {
                    showTimes.clear();
                    showTimes = showMoviesController.callShowTimesFromOneMovies(moviesUUID, showTime.getStudio().getStudioType());
                } catch (EmptyListException ex) {
                    System.out.println(ex.getMessage());
                }

                JPanel showTimesPanel = new JPanel();
                showTimesPanel.setOpaque(false);
                showTimesPanel.setLayout(new BoxLayout(showTimesPanel, BoxLayout.Y_AXIS));

                for (ShowTime st : showTimes) {

                    // PANEL SATU BARIS (jam + tombol)
                    JPanel row = new JPanel();
                    row.setOpaque(false);
                    row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
                    row.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JLabel times = new JLabel(st.getshowtimeDateTime());
                    JButton selectSeatsButton = new JButton("Select Seats");
                    selectSeatsButton.putClientProperty("showUUID", st.getShowtimeUUID());

                    row.add(times);
                    row.add(Box.createHorizontalStrut(10));
                    row.add(selectSeatsButton);

                    showTimesPanel.add(row);
                    showTimesPanel.add(Box.createVerticalStrut(8));

                    selectSeatsButton.addActionListener(evt -> {
                        JButton clickedBtn = (JButton) evt.getSource();
                        String showUUID = (String) clickedBtn.getClientProperty("showUUID");

                        clearFrameContent();
                        try {
                            handleSelectSeatsButton(showUUID);
                        } catch (EmptyListException ex) {
                            System.out.println(ex.getMessage());
                        }
                    });
                }

                // WRAP KE DALAM SCROLLPANE
                JScrollPane scrollPane = new JScrollPane(showTimesPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                scrollPane.getViewport().setOpaque(false);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
                scrollPane.setPreferredSize(new Dimension(250, 150));
                scrollPane.setMaximumSize(new Dimension(250, 150));
                scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
                scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

                showTimesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

                // Debug
                detailsMovie.add(scrollPane);
                detailsMovie.revalidate();
                detailsMovie.repaint();
            });

            movieCard.add(detailsMovie);

            // ADD CARD TO PAGE
            moviesCards.add(movieCard);

            detailsMovie.revalidate();
            detailsMovie.repaint();

        }

        // ADD MOVIES TO PAGE
        showTimePage.add(moviesCards);

        // Refresh page
        showTimePage.repaint();

    }

    private void handleSelectSeatsButton(String showUUID) throws EmptyListException {
        selectedSeats = new ArrayList<>();
        showPage("SEAT_SELECTION");
        seatSelectionPage.removeAll();

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
