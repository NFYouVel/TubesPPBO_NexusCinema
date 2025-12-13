package tubes.utils;

import java.util.ArrayList;
import java.util.List;

import tubes.models.Seat;
import tubes.models.enums.Roles;

public class UtilGlobal {

    private static String globalUUID;
    private static Roles role;
    private static List<Seat> selectedSeats = new ArrayList<>();
    private static String showUUID;

    public static void setGlobalUUID(String globalUUID) {
        UtilGlobal.globalUUID = globalUUID;
    }

    public static String getGlobalUUID() {
        return "355ac86a-84b5-493f-b31d-5e4883d75a1c";
    }

    public static void setRole(Roles role) {
        UtilGlobal.role = role;
    }

    public static Roles getRole() {
        return role;
    }

    public static void setSelectedSeats(List<Seat> seats) {
        selectedSeats = seats;
    }
    
    public static List<Seat> getSelectedSeats() {
        return selectedSeats;
    }

    public static String getShowUUID() {
        return showUUID;
    }
    public static void setShowUUID(String showUUID) {
        UtilGlobal.showUUID = showUUID;
    }

}
