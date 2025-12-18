package tubes.views.customers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tubes.controllers.ShowTimeController;
import tubes.models.ShowTime;
import tubes.models.exceptions.EmptyListException;
import tubes.models.interfaces.PageNavigator;
import tubes.utils.UtilJavaSwing;

public class MoviesPanel extends JPanel {
    private List<ShowTime> showTimes;
    private ShowTimeController showMoviesController = new ShowTimeController();
    private PageNavigator navigator;
    private JPanel moviesContent;

    public MoviesPanel(boolean isImages, PageNavigator navigator) {
        showTimes = new ArrayList<>();
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

                navigator.goToStudioTypePage(movies_UUID);
            });

            JPanel btnPanel = new JPanel();
            btnPanel.setOpaque(false);
            btnPanel.add(viewStudioTypeButton);

            movieCard.add(btnPanel, BorderLayout.SOUTH);

            // ADD CARD TO PAGE
            moviesCards.add(movieCard);
        }

        add(moviesCards);
    }
}
