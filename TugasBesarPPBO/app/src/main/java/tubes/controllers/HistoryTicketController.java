package tubes.controllers;

import java.util.List;

import tubes.models.Ticket;
import tubes.models.exceptions.EmptyListException;
import tubes.repositories.HistoryTicketRepository;

public class HistoryTicketController {
    private HistoryTicketRepository historyTicketRepository;

    public HistoryTicketController() {
        this.historyTicketRepository = new HistoryTicketRepository();
    }

    public List<Ticket> TicketHistoryListAll(String custUUID) throws EmptyListException{
        return historyTicketRepository.getTicketOrdered(custUUID);
    }
}
