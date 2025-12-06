package tubes.models;

import tubes.models.enums.TicketStatus;

public class Ticket {
    private String ticketUUID;
    private TicketStatus status;
    private Transaction transaction;
    private ShowTime showTime;
    private Seat seats;

    public Ticket(TicketStatus status) {
        this.status = status;
    }

    public String getTicketUUID() {
        return this.ticketUUID;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketStatus getStatus() {
        return this.status;
    }

    public void setTicketUUID(String ticketUUID) {
        this.ticketUUID = ticketUUID;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public ShowTime getShowTime() {
        return this.showTime;
    }

    public void setShowTime(ShowTime showTime) {
        this.showTime = showTime;
    }

    public Seat getSeats() {
        return this.seats;
    }

    public void setSeats(Seat seats) {
        this.seats = seats;
    }
}
