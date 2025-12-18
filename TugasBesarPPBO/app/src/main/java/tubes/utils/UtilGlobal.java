package tubes.utils;

import java.util.ArrayList;
import java.util.List;

import tubes.models.Seat;
import tubes.models.enums.Membership;
import tubes.models.enums.Roles;

public class UtilGlobal {

    private static String globalUUID;
    private static Roles role;
    private static Membership membership;
    private static String showUUID;

    public static void setGlobalUUID(String globalUUID) {
        UtilGlobal.globalUUID = globalUUID;
    }

    public static String getGlobalUUID() {
        return globalUUID;
    }

    public static void setRole(Roles role) {
        UtilGlobal.role = role;
    }

    public static Roles getRole() {
        return role;
    }

    public static void setMembership(Membership membership) {
        UtilGlobal.membership = membership;
    }
    public static Membership getMembership() {
        return membership;
    }

    public static String getShowUUID() {
        return showUUID;
    }
    public static void setShowUUID(String showUUID) {
        UtilGlobal.showUUID = showUUID;
    }

}
