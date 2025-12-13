package tubes.models;

import tubes.models.enums.StudioTypes;

public class Studio {
    private String studioUUID;
    private String studioNumber;
    private int price;
    private StudioTypes studioType;

    public Studio(String studioNumber, StudioTypes studioType, int price) {
        this.studioNumber = studioNumber;
        this.studioType = studioType;
        this.price = price;
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

    public StudioTypes getStudioType() {
        return this.studioType;
    }

    public void setStudioType(StudioTypes studioType) {
        this.studioType = studioType;
    }

    @Override
    public String toString() {
        return this.studioNumber + " - " + this.studioType;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return this.price;
    }
}
