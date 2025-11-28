package tubes.models;

public class ShowTime {
    private String showtimeUUID;
    private Movie movie;
    private Studio studio;
    private String showtimeDateTime;
    private int price;
    
    public ShowTime(String showtimeDateTime, int price) {
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
    
    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public Movie getMovie() {
        return this.movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    
    public Studio getStudio() {
        return this.studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }
}
