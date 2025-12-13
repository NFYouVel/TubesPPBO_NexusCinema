package tubes.controllers;

import java.util.List;

import tubes.models.Seat;
import tubes.models.exceptions.EmptyListException;
import tubes.repositories.SeatRepository;

public class SeatController {

    private SeatRepository seatRepository;

    public SeatController() {
        seatRepository = new SeatRepository();
    }

    public List<Seat> callAllAvailableSeats(String showUUID) throws EmptyListException {
        return seatRepository.getAllAvailableSeats(showUUID);
    }

    public List<Seat> callAllOccupiedSeats(String showUUID) {
        try {
            return seatRepository.getAllOccupiedSeats(showUUID);
        } catch (EmptyListException e) {
            return null;
        }
    }

    public List<Seat> setSeatAvailability(List<Seat> availableSeats, List<Seat> occupiedSeats) {
        for (Seat seat : availableSeats) {
            boolean isOccupied = false;

            for (Seat oc : occupiedSeats) {
                if (seat.getSeatNumber().equals(oc.getSeatNumber())) {
                    isOccupied = true;
                    break;
                }
            }

            seat.setIsAvailable(!isOccupied);
        }

        return availableSeats;
    }

    public List<Seat> setSeatAvailability(List<Seat> availableSeats) {
        List<Seat> updatedSeats = availableSeats;
        for (Seat updatedSeat : updatedSeats) {
            updatedSeat.setIsAvailable(true);
        }
        return updatedSeats;
    }
}
