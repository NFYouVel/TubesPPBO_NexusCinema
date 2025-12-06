package tubes.controllers;

import java.time.LocalDateTime;
import java.util.List;

import tubes.models.Movie;
import tubes.models.ShowTime;
import tubes.models.Studio;
import tubes.repositories.AdminRepository;

public class AdminController {
    private AdminRepository adminRepository;

    public AdminController() {
        adminRepository = new AdminRepository();
    }

    public String addMovies(Movie movie) {
        return adminRepository.getAddMovies(movie);
    }

    public String deleteMovie(String movieUUID) {
        return adminRepository.getDeleteMovie(movieUUID);
    }

    public String restoreMovie(String movieUUID) {
        return adminRepository.getRestoreMovie(movieUUID);
    }

    public String updateMovie(Movie movie) {
        return adminRepository.getUpdateMovie(movie);
    }

    public String addShowTime(ShowTime show, LocalDateTime inputShowStart) {
        return adminRepository.getAddShowTime(show, inputShowStart);
    }

    public List<Studio> getAllStudios() {
        // Asumsi di AdminRepository sudah ada method getAllStudios()
        // Kalau belum, buat method SELECT * FROM studio di Repository ya!
        return adminRepository.getAllStudios();
    }

    // Pastikan juga getAllMovies sudah ada
    public List<Movie> getAllMovies() {
        return adminRepository.getAllMovies();
    }
}
