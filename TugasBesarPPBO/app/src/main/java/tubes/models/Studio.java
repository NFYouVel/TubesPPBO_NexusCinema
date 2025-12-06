package tubes.models;

import tubes.models.enums.MovieTypes;

public class Studio {
    private String studioUUID;
    private String studioNumber;
    private MovieTypes studioType;

    public Studio(String studioNumber, MovieTypes studioType) {
        this.studioNumber = studioNumber;
        this.studioType = studioType;
    }

    public String getStudioUUID() {
        return this.studioUUID;
    }

    public void setStudioUUID(String studioUUID) {
        this.studioUUID = studioUUID;
    }

    public String getStudioNumber() {
        return this.studioNumber;
    }

    public void setStudioNumber(String studioNumber) {
        this.studioNumber = studioNumber;
    }

    public MovieTypes getStudioType() {
        return this.studioType;
    }

    public void setStudioType(MovieTypes studioType) {
        this.studioType = studioType;
    }

    @Override
    public String toString() {
        return this.studioNumber + " - " + this.studioType;
    }
}
