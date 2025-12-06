package tubes.views;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import tubes.controllers.HistoryTicketController;
import tubes.controllers.ShowMoviesController;
import tubes.models.ShowTime;
import tubes.models.exceptions.EmptyListRepository;
import tubes.utils.UtilJavaSwing;

public class CustomerView {

    // Controller
    private ShowMoviesController showMoviesController;
    private List<ShowTime> showTimes;
    private boolean isImages;

    // GUI Java Swing
    private JFrame frame;
    private CardLayout cardLayout;
    private BackgroundPanel backgroundCustomer;
    // Main Panel
    private JPanel navbarContent;
    private JPanel mainContent;
    // Navbar Content
    private JButton showNowButton;

    // Pages
    private JScrollPane showNowPage;
    private JPanel defaultPage;
    private JPanel orderPage;
    private List<JButton> orderButtons;

    public CustomerView(boolean isImages) {
        frame = UtilJavaSwing.generateFrame("Customer View", 1024, 700);
        orderButtons = new ArrayList<>();
        showMoviesController = new ShowMoviesController();
        defaultPage = new JPanel();
        orderPage = new JPanel();
        this.isImages = isImages;
    }

    public void startCustomerView() {
        backgroundCustomer = new BackgroundPanel("/assets/images/home_page.png");
        backgroundCustomer.setLayout(new BorderLayout());
        setNavbarContent();
        setMainContent();

        backgroundCustomer.add(navbarContent, BorderLayout.NORTH);
        backgroundCustomer.add(mainContent, BorderLayout.CENTER);

        // Set Page Of Index
        setHomePageView();

        // Adding Page
        mainContent.add(defaultPage, "DEFAULT");
        mainContent.add(showNowPage, "HOME");
        mainContent.add(orderPage, "ORDER");
        showPage("DEFAULT");

        handleActionListener();

        frame.setContentPane(backgroundCustomer);
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
        showNowButton = UtilJavaSwing.generateButton("Show Now");
        showNowButton.setFont(new Font("Arial", Font.BOLD, 16));

        // Adding Content
        navbarContent.add(showNowButton);

        // Adding Navigation Bar Content
        frame.add(navbarContent, BorderLayout.CENTER);
    }

    public void handleActionListener() {
        // Action Button
        showNowButton.addActionListener(e -> showPage("HOME"));
    }

    public void setMainContent() {
        mainContent = new JPanel();
        mainContent.setOpaque(false);

        cardLayout = new CardLayout();
        mainContent.setLayout(cardLayout);
    }

    private void handleOrderTicketButton(String movies_UUID) {
        showPage("ORDER");

        // Bersihin dulu page-nya biar gak double
        orderPage.removeAll();

        try {
            showTimes.clear();
            showTimes = showMoviesController.callShowTimesFromOneMovies(movies_UUID);
            System.out.println(showTimes.size());
        } catch (EmptyListRepository e) {
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
                JLabel iconMovie = getMovieIconLabel(showTime.getMovie().getTitle());
                movieCard.add(iconMovie, BorderLayout.NORTH);
            }

            // DETAILS PANEL
            JPanel detailsMovie = new JPanel();
            detailsMovie.setOpaque(false);
            detailsMovie.setLayout(new BoxLayout(detailsMovie, BoxLayout.Y_AXIS));
            detailsMovie.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

            JLabel title = new JLabel(showTime.getMovie().getTitle());
            title.setFont(new Font("Arial", Font.BOLD, 24));

            JLabel studioNum = new JLabel("Studio Number: " + showTime.getStudio().getStudioNumber());
            studioNum.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel studioType = new JLabel("Studio Type: " + showTime.getStudio().getStudioType());
            studioType.setFont(new Font("Arial", Font.BOLD, 16));

            detailsMovie.add(title);
            detailsMovie.add(studioNum);
            detailsMovie.add(studioType);

            movieCard.add(detailsMovie, BorderLayout.CENTER);

            // ADD CARD TO PAGE
            moviesCards.add(movieCard);
        }

        // ADD MOVIES TO PAGE
        orderPage.add(moviesCards);

        // Refresh page
        orderPage.revalidate();
        orderPage.repaint();

    }

    private void showPage(String text) {
        cardLayout.show(mainContent, text);
    }

    private void setHomePageView() {
        try {
            showTimes = showMoviesController.callShowMoviesListAll();
            System.out.println("Show Times: " + showTimes.size());
        } catch (EmptyListRepository e) {
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
                JLabel iconMovie = getMovieIconLabel(showTime.getMovie().getTitle());
                movieCard.add(iconMovie, BorderLayout.NORTH);
            }
            
            // DETAILS PANEL
            JPanel detailsMovie = new JPanel();
            detailsMovie.setOpaque(false);
            detailsMovie.setLayout(new BoxLayout(detailsMovie, BoxLayout.Y_AXIS));
            detailsMovie.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

            JLabel title = new JLabel(showTime.getMovie().getTitle());
            title.setFont(new Font("Arial", Font.BOLD, 24));

            JLabel studio = new JLabel("Studio: " + showTime.getStudio().getStudioType());
            studio.setFont(new Font("Arial", Font.BOLD, 16));
            JLabel duration = new JLabel("Duration: " + showTime.getMovie().getDuration() + " mins");
            duration.setFont(new Font("Arial", Font.BOLD, 16));

            detailsMovie.add(title);
            detailsMovie.add(studio);
            detailsMovie.add(duration);

            movieCard.add(detailsMovie, BorderLayout.CENTER);

            // BUTTON
            JButton btn = UtilJavaSwing.generateButton("Order Ticket");
            btn.putClientProperty("movies_UUID", showTime.getMovie().getMoviesUUID());
            btn.setPreferredSize(new Dimension(150, 40));

            btn.addActionListener(e -> {
                JButton clickedBtn = (JButton) e.getSource();
                String movies_UUID = (String) clickedBtn.getClientProperty("movies_UUID");

                handleOrderTicketButton(movies_UUID);
            });

            orderButtons.add(btn);

            JPanel btnPanel = new JPanel();
            btnPanel.setOpaque(false);
            btnPanel.add(btn);

            movieCard.add(btnPanel, BorderLayout.SOUTH);

            // ADD CARD TO PAGE
            moviesCards.add(movieCard);
        }

        showNowPage = new JScrollPane(moviesCards);
        showNowPage.setOpaque(false);
        showNowPage.getViewport().setOpaque(false);
    }

    private JLabel getMovieIconLabel(String movieTitle) {
        String imagePath;
        switch (movieTitle) {
            case "Kimetsu No Yaiba: Infinity Castle Arc":
                imagePath = "/assets/images/kimetsu_1.png";
                break;
            case "Pabrik Gula":
                imagePath = "/assets/images/pabrik_gula.png";
                break;
            case "Top Gun: Maverick":
                imagePath = "/assets/images/top_gun_maverick.png";
                break;
            case "Mission: Impossible - Dead Reckoning":
                imagePath = "/assets/images/mission_impossible.png";
                break;
            default:
                return new JLabel("No Image");
        }
        return UtilJavaSwing.generateImage(imagePath, 300, 400);
    }
}
