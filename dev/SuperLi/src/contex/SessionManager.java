package contex;

import domain.Employee;
import domain.Role;

import java.util.List;

public class SessionManager {
    private static Employee currentEmployee;

    public static void login(Employee user) {
        currentEmployee = user;
    }

    public static void logout() {
        currentEmployee = null;
    }

    public static Employee getCurrentEmployee() {
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
