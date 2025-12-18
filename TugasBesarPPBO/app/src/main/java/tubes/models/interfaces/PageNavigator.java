package tubes.models.interfaces;

import java.util.List;

import tubes.models.Seat;

public interface PageNavigator {
    void showPage(String pageName);
    void viewStudioType(String moviesUUID);
    void goToSeatPage(String showtimeUUID);
}
