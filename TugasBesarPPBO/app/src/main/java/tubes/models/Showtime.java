package tubes.models;

public class Showtime {
    private String showtimeUUID;
    private String showtimeDateTime;
    private String price;
    
    public Showtime(String showtimeUUID, String showtimeDateTime, String price) {
        this.showtimeUUID = showtimeUUID;
        this.showtimeDateTime = showtimeDateTime;
        this.price = price;
    }
    
    public String getShowtimeUUID() {
        return this.showtimeUUID;
    }

    public void setShowtimeUUID(String showtimeUUID) {
        this.showtimeUUID = showtimeUUID;
    }
    
    public String getshowtimeDateTime() {
        return this.showtimeDateTime;
    }

    public void setshowtimeDateTime(String showtimeDateTime) {
        this.showtimeDateTime = showtimeDateTime;
    }
    
    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
