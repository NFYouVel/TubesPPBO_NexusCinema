package tubes.models;

import tubes.models.enums.StudioTypes;

public class AuditRecord {
    private String studioNumber;
    private StudioTypes studioType;
    private int ticketPrice;
    private int ticketCount;
    private int totalIncome;

    public AuditRecord(String studioNumber, StudioTypes studioType, int ticketPrice, int ticketCount, int totalIncome) {
        this.studioNumber = studioNumber;
        this.studioType = studioType;
        this.ticketPrice = ticketPrice;
        this.ticketCount = ticketCount;
        this.totalIncome = totalIncome;
    }

    public String getStudioNumber() {
        return studioNumber;
    }

    public StudioTypes getStudioType() {
        return studioType;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public int getTotalIncome() {
        return totalIncome;
    }

    @Override
    public String toString() {
        return "AuditRecord{" +
                "studioNumber='" + studioNumber + '\'' +
                ", studioType=" + studioType +
                ", ticketPrice=" + ticketPrice +
                ", ticketCount=" + ticketCount +
                ", totalIncome=" + totalIncome +
                '}';
    }
}
