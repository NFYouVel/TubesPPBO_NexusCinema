package tubes.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import tubes.models.Movie;
import tubes.utils.Database;
import tubes.models.ShowTime;
import tubes.utils.UtilUUIDGenerator;

public class AdminRepository {
    private static final Connection conn;

    static {
        conn = Database.connect();
    }

    public String getAddMovies(Movie movie) {
        String sql = "INSERT INTO movies (movies_UUID, title, duration, genre, rating) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, UtilUUIDGenerator.generateUUID());
            ps.setString(2, movie.getTitle());
            ps.setInt(3, movie.getDuration());
            ps.setString(4, movie.getGenre());
            ps.setString(5, movie.getRating().name());
            ps.executeUpdate();
            return "Movie added successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to add movie.";
        }
    }

    public String getDeleteMovie(String movieUUID) {
        String sql = "UPDATE movies SET deleted_at = CURRENT_TIMESTAMP WHERE movies_UUID = ? AND deleted_at IS NULL";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, movieUUID);
            ps.executeUpdate();
            return "Movie deleted successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to delete movie.";
        }
    }

    public String getRestoreMovie(String movieUUID) {
        String sql = "UPDATE movies SET deleted_at = NULL WHERE movies_UUID = ? AND deleted_at IS NOT NULL";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, movieUUID);
            ps.executeUpdate();
            return "Movie restored successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to restore movie.";
        }
    }

    public String getUpdateMovie(Movie movie) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to update movie.";
        }
    } 

    public String getAddShowTime(ShowTime show, LocalDateTime inputShowStart) {
        try {
            String sqlDurasi = "SELECT duration FROM movies WHERE movies_UUID = ?";
            PreparedStatement psDurasi = conn.prepareStatement(sqlDurasi);
            psDurasi.setString(1, show.getMovie().getMoviesUUID()); 
            ResultSet rsDurasi = psDurasi.executeQuery();

            if (rsDurasi.next()) {
                int newDuration = rsDurasi.getInt("duration");
                LocalDateTime inputShowEnd = inputShowStart.plusMinutes(newDuration + 15); //15 menit untuk cleaning studio

                //check film sbelumnya selesai jam berapa
                String sqlCheck = "SELECT st.show_time, m.duration FROM show_time st JOIN movies m ON st.movies_UUID = m.movies_UUID WHERE st.studio_UUID = ? AND st.deleted_at IS NULL";
                PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
                psCheck.setString(1, show.getStudio().getStudioUUID());
                ResultSet rsCheck = psCheck.executeQuery();

                //cek bentrok/ga
                while (rsCheck.next()) {
                    LocalDateTime existingStart = rsCheck.getTimestamp("show_time").toLocalDateTime(); 
                    int existingDuration = rsCheck.getInt("duration");

                    LocalDateTime existingEnd = existingStart.plusMinutes(existingDuration + 15);

                    //kalo movieBaru mulai < movieLama selesai && movieBaru selesai > movieLama mulai
                    if (inputShowStart.isBefore(existingEnd) && inputShowEnd.isAfter(existingStart)) {
                        return "gagal! jadwal bentrok dengan film lain di studio ini";
                    }
                }

                String sqlInsert = "INSERT INTO show_time (show_UUID, movies_UUID, studio_UUID, show_time, show_price) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
                psInsert.setString(1, UtilUUIDGenerator.generateUUID());
                psInsert.setString(2, show.getMovie().getMoviesUUID());
                psInsert.setString(3, show.getStudio().getStudioUUID());
                psInsert.setTimestamp(4, Timestamp.valueOf(inputShowStart));
                psInsert.setInt(5, show.getPrice());
                psInsert.executeUpdate();
                return "Showtime added successfully.";
            } else {
                return "Error! Movie not found.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to add showtime.";
        }
    }
      
}