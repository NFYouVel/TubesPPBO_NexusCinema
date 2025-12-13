package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tubes.models.Movie;
import tubes.models.ShowTime;
import tubes.models.Studio;
import tubes.models.enums.Ratings;
import tubes.models.enums.StudioTypes;
import tubes.models.exceptions.EmptyListException;
import tubes.utils.Database;

public class ShowTimeRepository {
    private final List<ShowTime> showTimes;
    private static final Connection conn;

    static {
        conn = Database.connect();
    }

    public ShowTimeRepository() {
        showTimes = new ArrayList<>();
    }

    public ShowTime getDetailsShowTime(String showUUID) throws EmptyListException {
        try {
            PreparedStatement stmt = conn.prepareStatement("""
                                                           SELECT \r
                                                               st.show_UUID, \r
                                                               m.movies_UUID, \r
                                                               m.title, \r
                                                               m.duration, \r
                                                               m.genre, \r
                                                               m.rating, \r
                                                               st.show_time, \r
                                                               s.studio_number, \r
                                                               s.price,\r
                                                               s.type\r
                                                           FROM show_time AS st \r
                                                           JOIN movies AS m ON st.movies_UUID = m.movies_UUID \r
                                                           JOIN studio AS s ON st.studio_UUID = s.studio_UUID\r
                                                           WHERE st.show_UUID = ? ;""");
            stmt.setString(1, showUUID);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EmptyListException("Show Movies");
            } else {
                ShowTime temp = new ShowTime(rs.getString("show_time"));
                temp.setShowtimeUUID(rs.getString("show_UUID"));
                temp.setStudio(new Studio(rs.getString("studio_number"), StudioTypes.valueOf(rs.getString("type")), rs.getInt("price")));
                temp.setMovie(new Movie(rs.getString("title"), rs.getInt("duration"), rs.getString("genre"), Ratings.valueOf(rs.getString("rating"))));
                return temp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ShowTime> getAllShowTimesFromOneMovies(String movies_UUID) throws EmptyListException {
        try {
            showTimes.clear();

            PreparedStatement stmt = conn.prepareStatement("""
                                                           SELECT \r
                                                               st.show_UUID, \r
                                                               m.movies_UUID, \r
                                                               m.title, \r
                                                               m.duration, \r
                                                               m.genre, \r
                                                               m.rating, \r
                                                               st.show_time, \r
                                                               s.studio_number, \r
                                                               s.price,\r
                                                               s.type\r
                                                           FROM show_time AS st \r
                                                           JOIN movies AS m ON st.movies_UUID = m.movies_UUID \r
                                                           JOIN studio AS s ON st.studio_UUID = s.studio_UUID\r
                                                           WHERE m.movies_UUID = ? GROUP BY s.type ORDER BY st.show_time ASC ;""");
            stmt.setString(1, movies_UUID);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EmptyListException("Show Movies");
            } else {
                do {
                    ShowTime temp = new ShowTime(rs.getString("show_time"));
                    temp.setShowtimeUUID(rs.getString("show_UUID"));
                    temp.setStudio(new Studio(rs.getString("studio_number"), StudioTypes.valueOf(rs.getString("type")), rs.getInt("price")));
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

    public List<ShowTime> getAllShowTimesFromOneMovies(String movies_UUID, StudioTypes type) throws EmptyListException {
        try {
            showTimes.clear();

            PreparedStatement stmt = conn.prepareStatement("""
                                                           SELECT \r
                                                               st.show_UUID, \r
                                                               m.movies_UUID, \r
                                                               m.title, \r
                                                               m.duration, \r
                                                               m.genre, \r
                                                               m.rating, \r
                                                               st.show_time, \r
                                                               s.studio_number, \r
                                                               s.price,\r
                                                               s.type\r
                                                           FROM show_time AS st \r
                                                           JOIN movies AS m ON st.movies_UUID = m.movies_UUID \r
                                                           JOIN studio AS s ON st.studio_UUID = s.studio_UUID\r
                                                           WHERE m.movies_UUID = ? AND s.type = ? ORDER BY st.show_time ASC ;""");
            stmt.setString(1, movies_UUID);
            stmt.setString(2, type.toString());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new EmptyListException("Show Movies");
            } else {
                do {
                    ShowTime temp = new ShowTime(rs.getString("show_time"));
                    temp.setShowtimeUUID(rs.getString("show_UUID"));
                    temp.setStudio(new Studio(rs.getString("studio_number"), StudioTypes.valueOf(rs.getString("type")), rs.getInt("price")));
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

    public List<ShowTime> getAllMoviesList() throws EmptyListException {
        try {
            showTimes.clear();

            PreparedStatement stmt = conn.prepareStatement("""
                                                           SELECT \r
                                                               st.show_UUID, \r
                                                               m.movies_UUID, \r
                                                               m.title, \r
                                                               m.duration, \r
                                                               m.genre, \r
                                                               m.rating, \r
                                                               st.show_time \r
                                                           FROM show_time AS st \r
                                                           JOIN movies AS m ON st.movies_UUID = m.movies_UUID \r
                                                           GROUP BY m.movies_UUID ORDER BY m.title ASC;""");
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                throw new EmptyListException("Show Movies");
            } else {
                do {
                    ShowTime temporary = new ShowTime(rs.getString("show_time"));
                    temporary.setShowtimeUUID(rs.getString("show_UUID"));

                    Movie tempMovie = new Movie(rs.getString("title"), rs.getInt("duration"), rs.getString("genre"), Ratings.valueOf(rs.getString("rating")));
                    tempMovie.setMoviesUUID(rs.getString("movies_UUID"));
                    temporary.setMovie(tempMovie);
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
