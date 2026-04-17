package context;

import domain.entities.Role;
import presentation.model.EmployeePL;

import java.util.List;

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

    public static List<Role> getContext() {
        if (!hasContext()) return null;
        return currentEmployee.getQualifiedRoles();
    }
}
