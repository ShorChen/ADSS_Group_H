package Workers.Context;

import Workers.Presentation.Model.EmployeePL;

import java.time.LocalDateTime;

public abstract class SessionManager {
    protected static boolean debugMode = false;

    private static Debugger debugger;
    private static EmployeePL currentEmployee;
    private static LocalDateTime deadline;

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

    public static LocalDateTime now() {
        return LocalDateTime.now().plusHours(debugger == null? 0 : debugger.getHours());
    }

    public static void debug(long hoursForward){
        if (debugger != null) debugger.setHours(hoursForward);
    }

    public static void setDebugger(Debugger debugger) {
        SessionManager.debugger = debugger;
    }

    public static boolean isDebugMode() {
        return debugger != null;
    }

    public static void setDeadline(LocalDateTime deadline) {
        SessionManager.deadline = deadline;
    }

    public static LocalDateTime getDeadline() {
        if (deadline == null)
            return now().plusDays(1);
        return deadline;
    }
}
