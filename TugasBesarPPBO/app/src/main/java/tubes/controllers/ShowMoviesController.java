package tubes.controllers;

import java.util.List;

import tubes.models.ShowTime;
import tubes.models.exceptions.EmptyListRepository;
import tubes.repositories.ShowMoviesRepository;

public class ShowMoviesController {
    private ShowMoviesRepository showMoviesRepository;

    public ShowMoviesController() {
        showMoviesRepository = new ShowMoviesRepository();
    }

    public List<ShowTime> callShowMoviesListAll() throws EmptyListRepository {
        return showMoviesRepository.getShowMoviesListAll();
    }

    public List<ShowTime> callShowTimesFromOneMovies(String movies_UUID) throws EmptyListRepository {
        return showMoviesRepository.getAllShowTimesFromOneMovies(movies_UUID);
    }
}
