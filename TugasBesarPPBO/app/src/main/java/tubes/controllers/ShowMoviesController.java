package tubes.controllers;

import java.util.List;

import tubes.models.ShowTime;
import tubes.repositories.ShowMoviesRepository;

public class ShowMoviesController {
    private ShowMoviesRepository showMoviesRepository;

    public ShowMoviesController() {
        showMoviesRepository = new ShowMoviesRepository();
    }

    public List<ShowTime> callShowMoviesListAll() {
        return showMoviesRepository.showMoviesListAll();
    }
}
