package tubes.controllers;

import java.util.List;

import tubes.models.ShowTime;
import tubes.models.enums.StudioTypes;
import tubes.models.exceptions.EmptyListException;
import tubes.repositories.ShowTimeRepository;

public class ShowTimeController {
    private ShowTimeRepository showMoviesRepository;

    public ShowTimeController() {
        showMoviesRepository = new ShowTimeRepository();
    }

    public List<ShowTime> callShowMoviesListAll() throws EmptyListException {
        return showMoviesRepository.getAllMoviesList();
    }

    public List<ShowTime> callShowTimesFromOneMovies(String movies_UUID) throws EmptyListException {
        return showMoviesRepository.getAllShowTimesFromOneMovies(movies_UUID);
    }

    public List<ShowTime> callShowTimesFromOneMovies(String movies_UUID, StudioTypes type) throws EmptyListException {
        return showMoviesRepository.getAllShowTimesFromOneMovies(movies_UUID, type);
    }

    public ShowTime callShowTimeDetails(String showUUID) throws EmptyListException {
        return showMoviesRepository.getDetailsShowTime(showUUID);
    }
}
