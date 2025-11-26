package tubes.models;

import tubes.models.enums.TicketStatus;

public class Ticket {
    private String ticketUUID;
    private TicketStatus status;

    public Ticket(String ticketUUID, TicketStatus status) {
        this.ticketUUID = ticketUUID;
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
}
