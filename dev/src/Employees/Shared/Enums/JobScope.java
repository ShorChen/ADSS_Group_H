package Employees.Shared.Enums;

public enum JobScope {
    PARTIAL, FULL_TIME;

    public static JobScope fromArgs(String jobScope) {
        return "Partial".equalsIgnoreCase(jobScope) ? PARTIAL :
                "Full Time".equalsIgnoreCase(jobScope) ? FULL_TIME : null;
    }
}
