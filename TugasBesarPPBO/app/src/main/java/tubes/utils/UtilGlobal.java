package tubes.utils;

import tubes.models.enums.Roles;

public class UtilGlobal {
    private static String globalUUID;
    private static Roles role;

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
}
