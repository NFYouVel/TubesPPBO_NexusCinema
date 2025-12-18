package tubes.views.customers;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import tubes.controllers.ShowTimeController;
import tubes.models.ShowTime;
import tubes.models.exceptions.EmptyListException;
import tubes.models.interfaces.PageNavigator;
import tubes.utils.UtilJavaSwing;

public class StudioTypePanel extends JPanel {
    private List<ShowTime> showTimes;
    private ShowTimeController showMoviesController;
    private PageNavigator navigator;

    public StudioTypePanel(String moviesUUID, boolean isImages, PageNavigator navigator) {
        showTimes = new ArrayList<>();
        showMoviesController = new ShowTimeController();
        this.navigator = navigator;

        try {
            showTimes.clear();
            showTimes = showMoviesController.callShowTimesFromOneMovies(moviesUUID);
        } catch (EmptyListException ex1) {
            System.out.println(ex1.getMessage());
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
                } catch (EmptyListException ex2) {
                    System.out.println(ex2.getMessage());
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

                        revalidate();
                        repaint();
                        
                        navigator.goToSeatPage(showUUID);
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
        add(moviesCards);
    }
}
