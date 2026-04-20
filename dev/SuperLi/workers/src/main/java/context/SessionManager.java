package context;

import presentation.model.EmployeePL;

public class SessionManager {
    private static EmployeePL currentEmployee;

    public static void login(EmployeePL user) {
        currentEmployee = user;
    }

    public static void logout() {
        currentEmployee = null;
    }

    public static EmployeePL getCurrentEmployee() {
        return currentEmployee;
    }

    public static boolean hasContext() {
        return currentEmployee != null;
    }

}
