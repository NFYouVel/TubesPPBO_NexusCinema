package tubes.models;

public class Seat {
    private String seatsUUID;
    private String seatNumber;
    private int seatRow;
    private int seatColumn;
    
    public Seat(String seatNumber, int seatRow, int seatColumn) {
        this.seatNumber = seatNumber;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
    }
    
    public String getSeatsUUID() {
        return this.seatsUUID;
    }
    
    public void setSeatsUUID(String seatsUUID) {
        this.seatsUUID = seatsUUID;
    }
    
    public String getSeatNumber() {
        return this.seatNumber;
    }
    
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public int getSeatRow() {
        return this.seatRow;
    }
    
    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }
    
    public int getSeatColumn() {
        return this.seatColumn;
    }
    
    public void setSeatColumn(int seatColumn) {
        this.seatColumn = seatColumn;
    }
}
