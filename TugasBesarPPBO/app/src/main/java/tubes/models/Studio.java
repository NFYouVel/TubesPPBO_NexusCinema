package tubes.models;

import java.sql.Types;

public class Studio {
    private String studioUUID;
    private String studioNumber;
    private Types studioType;

    public Studio(String studioUUID, String studioNumber, Types studioType) {
        this.studioUUID = studioUUID;
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

    public Types getStudioType() {
        return this.studioType;
    }

    public void setStudioType(Types studioType) {
        this.studioType = studioType;
    }
}
