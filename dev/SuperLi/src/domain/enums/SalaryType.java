package domain.enums;

public enum SalaryType {
    HOURLY("hourly"), GLOBALLY("globally");

    public final String type;
    SalaryType(String type) {
        this.type = type;
    }
}
