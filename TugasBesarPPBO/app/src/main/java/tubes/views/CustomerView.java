package tubes.views;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import tubes.controllers.ShowMoviesController;
import tubes.models.ShowTime;

public class CustomerView {

    //Controller
    private ShowMoviesController showMoviesController;

    // GUI
    private JFrame frame;

    // Panel
    private JPanel navigationDiv;
    private JPanel contentShowMoviesDiv;

    // Input Type
    private JButton showMoviesButton;
    private JButton orderTicketButton;

    public CustomerView() {
        showMoviesController = new ShowMoviesController();
        this.frame = new JFrame();
        setNavigationBar();
    }

    public void showMoviesList() {
        List<ShowTime> showTimes = showMoviesController.callShowMoviesListAll();
        contentShowMoviesDiv = new JPanel();
        contentShowMoviesDiv.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));
        contentShowMoviesDiv.setLayout(new GridLayout(showTimes.size() + 1, 8, 0, 0));

        // Title
        contentShowMoviesDiv.add(makeHeaderCell(("Title")));
        contentShowMoviesDiv.add(makeHeaderCell(("Duration")));
        contentShowMoviesDiv.add(makeHeaderCell(("Genre")));
        contentShowMoviesDiv.add(makeHeaderCell(("Rating")));
        contentShowMoviesDiv.add(makeHeaderCell(("Showtime")));
        contentShowMoviesDiv.add(makeHeaderCell(("Price")));
        contentShowMoviesDiv.add(makeHeaderCell(("Studio Number")));
        contentShowMoviesDiv.add(makeHeaderCell(("Studio Type")));
        for (ShowTime showTime : showTimes) {
            contentShowMoviesDiv.add(makeCell(showTime.getMovie().getTitle()));
            contentShowMoviesDiv.add(makeCell(showTime.getMovie().getDuration() + " Menit"));
            contentShowMoviesDiv.add(makeCell(showTime.getMovie().getGenre()));
            contentShowMoviesDiv.add(makeCell(showTime.getMovie().getRating().toString()));
            contentShowMoviesDiv.add(makeCell(showTime.getshowtimeDateTime()));
            contentShowMoviesDiv.add(makeCell("Rp" + Integer.toString(showTime.getPrice())));
            contentShowMoviesDiv.add(makeCell(showTime.getStudio().getStudioNumber()));
            contentShowMoviesDiv.add(makeCell(showTime.getStudio().getStudioType().toString()));
        }

        frame.add(contentShowMoviesDiv, BorderLayout.CENTER);
        frame.setVisible(true);

    }

    public void setNavigationBar() {
        frame.setSize(1024, 1024);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        navigationDiv = new JPanel();
        navigationDiv.setBackground(Color.decode("#c6c6c6"));
        navigationDiv.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        navigationDiv.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));
        navigationDiv.setBounds(0, 0, 1024, 100);

        showMoviesButton = new JButton("Show Movies");
        showMoviesButton.setPreferredSize(new Dimension(150, 50));
        orderTicketButton = new JButton("Order Ticket");
        orderTicketButton.setPreferredSize(new Dimension(150, 50));

        navigationDiv.add(showMoviesButton);
        navigationDiv.add(orderTicketButton);

        frame.add(navigationDiv, BorderLayout.NORTH);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        showMoviesButton.addActionListener(e -> {
            showMoviesList();
        });
    }

    private JLabel makeCell(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // padding biar ga mepet
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // kasih min width dikit biar ga jadi "..."
        lbl.setPreferredSize(new Dimension(90, 20));

        return lbl;
    }

    private JLabel makeHeaderCell(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);

        // Bold biar beda dari data biasa
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));

        // Background header (harus opaque)
        lbl.setOpaque(true);
        lbl.setBackground(Color.decode("#eacf64")); // abu2 terang

        // Border rapi
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // min width biar ga ngejepit
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 16f));
        lbl.setPreferredSize(new Dimension(90, 25));

        return lbl;
    }

}
