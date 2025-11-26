package tubes.models;

import tubes.models.enums.Ratings;

public class Movie {
    private String moviesUUID;
    private String title;
    private int duration;
    private String genre;
    private Ratings rating;

    public Movie(String title, int duration, String genre, Ratings rating) {
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.rating = rating;
    }

    public String getMoviesUUID() {
        return this.moviesUUID;
    }

    public void setMoviesUUID(String moviesUUID) {
        this.moviesUUID = moviesUUID;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Ratings getRating() {
        return this.rating;
    }

    public void setRating(Ratings rating) {
        this.rating = rating;
    }
}
