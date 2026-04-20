package domain.enums;

public enum SalaryType {
    HOURLY("Hourly"), GLOBALLY("Globally");

    public final String type;
    SalaryType(String type) {
        this.type = type;
    }

}
