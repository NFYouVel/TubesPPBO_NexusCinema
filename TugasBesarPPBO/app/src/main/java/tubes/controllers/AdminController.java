package tubes.controllers;

import java.time.LocalDateTime;

import tubes.models.Movie;
import tubes.models.ShowTime;
import tubes.repositories.AdminRepository;

public class AdminController {
    private AdminRepository adminRepository;

    public AdminController() {
        adminRepository = new AdminRepository();
    }

    public String addMovies(Movie movie) {
        return adminRepository.getAddMovies(movie);
    }

    public String deleteMovie(String movieuUID) {
        return adminRepository.getDeleteMovie(movieuUID);
    }

    public String restoreMovie(String movieuUID) {
        return adminRepository.getRestoreMovie(movieuUID);
    }

    public String updateMovie(Movie movie) {
        return adminRepository.getUpdateMovie(movie);
    }

    public String addShowTime(ShowTime show, LocalDateTime inputShowStart) {
        return adminRepository.getAddShowTime(show, inputShowStart);
    }
}
