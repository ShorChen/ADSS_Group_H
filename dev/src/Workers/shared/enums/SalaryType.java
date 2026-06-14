package Workers.shared.enums;

public enum SalaryType {
    HOURLY("Hourly"), GLOBALLY("Globally");

    public final String type;
    SalaryType(String type) {
        this.type = type;
    }

    public static SalaryType fromArgs(String salaryType){
        return valueOf(salaryType.toUpperCase());
    }

}
