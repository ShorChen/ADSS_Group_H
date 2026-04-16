package presentation.ui;

import domain.enums.SalaryType;

public class AddEmployeeView extends View {
/*
*
*
* private String id;
    private String name;
    private String bankAccount;
    private double salary;
    private SalaryType salaryType;
    private Date dateOfEmployment;
    private JobScope jobScope;
    private List<Role> qualifiedRoles;
    private String constraints;
    private int yearlyRestDays;
*
*
*
*
*
* */

    @Override
    void display() {
        String id = getNextLine("Enter Employee Id:");
        String name = getNextLine("Enter Employee name:");
        String salTypeString = selectUntilResult("Enter Salary Type:",
                SalaryType.HOURLY.type,
                SalaryType.GLOBALLY.type);

        SalaryType salaryType = salTypeString.equalsIgnoreCase("hourly") ?
                SalaryType.HOURLY : SalaryType.GLOBALLY;

        double salary = getNextDouble("Enter Employee's Salary");


        // todo

    }


    private <T, R> String selectUntilResult(String message, String... options) {
        String selection = getNextLine(message);
        for (String option : options) {
            if (selection.equals(option))
                return selection;
        }
        return selectUntilResult(message, options);
    }

    @Override
    void close() {

    }
}
