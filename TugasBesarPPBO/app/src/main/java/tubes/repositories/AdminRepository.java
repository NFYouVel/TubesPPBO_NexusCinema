package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.mysql.cj.util.Util;

import tubes.models.Movie;
import tubes.utils.Database;
import tubes.utils.UtilUUIDGenerator;

public class AdminRepository {
    private static final Connection conn;

    static {
        conn = Database.connect();
    }

    public static void addMovies(Movie movie) {
        String sql = "INSERT INTO movies (movies_UUID, title, duration, genre, rating) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, UtilUUIDGenerator.generateUUID());
            ps.setString(2, movie.getTitle());
            ps.setInt(3, movie.getDuration());
            ps.setString(4, movie.getGenre());
            ps.setString(5, movie.getRating().name());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String deleteMovie(String movieUUID) {
        String sql = "UPDATE movies SET deleted_at = CURRENT_TIMESTAMP WHERE movies_UUID = ? AND deleted_at IS NULL";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, movieUUID);
            ps.executeUpdate();
            return "Movie deleted successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to delete movie.";
        }
    }

    public static String restoreMovie(String movieUUID) {
        String sql = "UPDATE movies SET deleted_at = NULL WHERE movies_UUID = ? AND deleted_at IS NOT NULL";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, movieUUID);
            ps.executeUpdate();
            return "Movie restored successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to restore movie.";
        }
    }

    public static String updateMovie(Movie movie) {
        String sql = "UPDATE movies SET title = ?, duration = ?, genre = ?, rating = ? WHERE movies_UUID = ? AND deleted_at IS NULL";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getDuration());
            ps.setString(3, movie.getGenre());
            ps.setString(4, movie.getRating().name());
            ps.setString(5, movie.getMoviesUUID());
            ps.executeUpdate();
            return "Movie updated successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update movie.";
        }
    } 
      
}