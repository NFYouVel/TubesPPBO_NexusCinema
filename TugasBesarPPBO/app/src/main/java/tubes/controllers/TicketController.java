package tubes.controllers;

import java.util.List;

import tubes.models.Seat;
import tubes.repositories.TicketRepository;

public class TicketController {
    private TicketRepository ticketRepository;

    public TicketController() {
        ticketRepository = new TicketRepository();
    }

    public void processTicket(String transUUID, String showUUID, List<Seat> selectedSeats) {
        ticketRepository.processTicket(transUUID, showUUID, selectedSeats);
    }
}
