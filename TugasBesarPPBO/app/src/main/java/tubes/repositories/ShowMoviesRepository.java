package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import tubes.models.Movie;
import tubes.models.ShowTime;
import tubes.models.Studio;
import tubes.models.enums.Ratings;
import tubes.models.enums.MovieTypes;
import tubes.models.exception.EmptyListRepository;
import tubes.utils.Database;

public class ShowMoviesRepository {
    private LocalDateTime now;
    private List<ShowTime> showTimes;
    private static final Connection conn;

    static {
        conn = Database.connect();
    }

    public ShowMoviesRepository() {
        now = LocalDateTime.now();
        showTimes = new ArrayList<>();
    }

    public List<ShowTime> getAllShowTimesFromOneMovies(String movies_UUID) throws EmptyListRepository {
        try {
            showTimes.clear();

            PreparedStatement stmt = conn.prepareStatement("SELECT \r\n" + 
                                "    st.show_UUID, \r\n" + 
                                "    m.movies_UUID, \r\n" + 
                                "    m.title, \r\n" + 
                                "    m.duration, \r\n" + 
                                "    m.genre, \r\n" + 
                                "    m.rating, \r\n" + 
                                "    st.show_time, \r\n" + 
                                "    st.show_price, \r\n" + 
                                "    s.studio_number, \r\n" + 
                                "    s.type\r\n" + 
                                "FROM show_time AS st \r\n" + 
                                "JOIN movies AS m ON st.movies_UUID = m.movies_UUID \r\n" + 
                                "JOIN studio AS s ON st.studio_UUID = s.studio_UUID\r\n" + 
                                "WHERE m.movies_UUID = ?;");
            stmt.setString(1, movies_UUID);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EmptyListRepository("Show Movies");
            } else {
                do {
                    ShowTime temp = new ShowTime(rs.getString("show_time"), rs.getInt("show_price"));
                    temp.setStudio(new Studio(rs.getString("studio_number"), MovieTypes.valueOf(rs.getString("type"))));
                    temp.setMovie(new Movie(rs.getString("title"), rs.getInt("duration"), rs.getString("genre"), Ratings.valueOf(rs.getString("rating"))));
                    showTimes.add(temp);
                } while (rs.next());
            }
            
            return showTimes;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ShowTime> getShowMoviesListAll() throws EmptyListRepository {
        try {
            showTimes.clear();

            PreparedStatement stmt = conn.prepareStatement("SELECT \r\n" + 
                                "    st.show_UUID, \r\n" + 
                                "    m.movies_UUID, \r\n" + 
                                "    m.title, \r\n" + 
                                "    m.duration, \r\n" + 
                                "    m.genre, \r\n" + 
                                "    m.rating, \r\n" + 
                                "    st.show_time, \r\n" + 
                                "    st.show_price, \r\n" + 
                                "    s.studio_number, \r\n" + 
                                "    s.type\r\n" + 
                                "FROM show_time AS st \r\n" + 
                                "JOIN movies AS m ON st.movies_UUID = m.movies_UUID \r\n" + 
                                "JOIN studio AS s ON st.studio_UUID = s.studio_UUID\r\n" + 
                                "GROUP BY m.movies_UUID ORDER BY m.title ASC;");
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                throw new EmptyListRepository("Show Movies");
            } else {
                do {
                    ShowTime temporary = new ShowTime(rs.getString("show_time"), rs.getInt("show_price"));
                    temporary.setShowtimeUUID(rs.getString("show_UUID"));

                    Movie tempMovie = new Movie(rs.getString("title"), rs.getInt("duration"), rs.getString("genre"), Ratings.valueOf(rs.getString("rating")));
                    tempMovie.setMoviesUUID(rs.getString("movies_UUID"));
                    temporary.setMovie(tempMovie);
                    temporary.setStudio(new Studio(rs.getString("studio_number"), MovieTypes.valueOf(rs.getString("type"))));
                    showTimes.add(temporary);
                } while(rs.next());

            }
            return showTimes;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
