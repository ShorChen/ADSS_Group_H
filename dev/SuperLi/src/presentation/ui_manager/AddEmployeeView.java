package presentation.ui_manager;

import domain.entities.Role;
import domain.enums.JobScope;
import domain.enums.SalaryType;
import domain.enums.WeekDay;
import presentation.control.EmployeeController;
import presentation.control.RoleController;
import presentation.model.EmployeePL;
import presentation.ui_shared.View;
import presentation.util.Option;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AddEmployeeView extends View {

    private final EmployeeController employeeController;
    private final RoleController roleController;
    private EmployeePL.Builder builder;
    private boolean open;
    private final String employeeId;

    public AddEmployeeView(String employeeId) {
        employeeController = new EmployeeController();
        roleController = new RoleController();
        this.employeeId = employeeId;
    }

    @Override
    public void display() {
        open = true;

        EmployeePL e = new EmployeeController().getEmployeeDetails(employeeId);
        if (employeeId == null)
            buildEmployee("\n--- Add New Employee ---");
        else if (e == null)
            buildEmployee("Could not find employee... Adding New Employee");
        else
            System.out.println("\n--- Update Existing Employee ---");

        while (open) {
            EmployeePL employee = builder.build();
            printSummary(employee);

            displayMenu("--- Options ---", "",
                    new Option("Cancel", this::close),
                    new Option("Update a Field", this::updateFieldMenu),
                    new Option("Save Changes", () -> {
                        if (employeeId == null || e == null) registerEmployee(employee);
                        else updateEmployee(employee);
                    })
            );

        }
    }

    private void buildEmployee(String message) {
        System.out.println(message);
        id();
        name();
        bankAccount();
        salary();
        dateOfEmployment();
        jobScope();
        roles();
        constraints();
        yearlyRestDays();
        weeklyRestDay();
    }

    private void printSummary(EmployeePL employee) {
        System.out.println("\n--- Employee Details ---");
        System.out.println("ID: " + employee.getId());
        System.out.println("Name: " + employee.getName());
        System.out.println("Salary: " + employee.getSalary() + " (" + employee.getSalaryType() + ")");
        System.out.println("Bank Account: " + employee.getBankAccount());
        System.out.println("Date of Employment: " + employee.getDateOfEmployment());
        System.out.println("Job Scope: " + employee.getJobScope());
        System.out.println("Yearly Rest Days: " + employee.getYearlyRestDays());
        System.out.println("Weekly Rest Day: " + employee.getWeeklyRestDay());
        System.out.println("Constraints: " + (employee.getConstraints().isEmpty() ? "None" : employee.getConstraints()));

        System.out.print("Roles: ");
        if (employee.getQualifiedRoles().isEmpty()) {
            System.out.println("None");
        } else {
            employee.getQualifiedRoles().forEach(role -> System.out.print(role.getTag() + " "));
            System.out.println();
        }
    }

    private void updateFieldMenu() {
        displayMenu("---Update Field ---", "",
                new Option("Back to Summary", () -> {
                }),
                new Option("Name", this::name),
                new Option("Bank Account", this::bankAccount),
                new Option("Salary & Salary Type", this::salary),
                new Option("Date of Employment", this::dateOfEmployment),
                new Option("Job Scope", this::jobScope),
                new Option("Roles", this::roles),
                new Option("Constraints", this::constraints),
                new Option("Yearly Rest Days", this::yearlyRestDays)
        );
    }

    private void registerEmployee(EmployeePL employee) {
        try {
            System.out.println("Your password: " + employeeController.addEmployee(employee) +
                               " make sure to change it as soon as you get access to the system");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        close();
    }

    private void updateEmployee(EmployeePL employee) {
        try {
            String password = getNextLine("To save changes, please enter password");
            employeeController.updateEmployee(employee, password);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        close();
    }


    private void id() {
        String id = getNextLine("Enter Employee Id:");
        builder = new EmployeePL.Builder(id);
    }

    private void name() {
        String name = getNextLine("Enter Employee name:");
        builder.name(name);
    }

    private void bankAccount() {
        String bankAccount = getNextLine("Enter Bank Account Number:");
        builder.bankAccount(bankAccount);
    }

    private void salary() {
        displayMenu("Enter Salary Type", "",
                new Option("Hourly", () -> builder.salaryType(SalaryType.HOURLY)),
                new Option("Globally", () -> builder.salaryType(SalaryType.GLOBALLY))
        );

        double salary = getNextDouble("Enter Employee's Salary:");
        builder.salary(salary);
    }

    private void dateOfEmployment() {
        boolean validDate = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (!validDate) {
            String dateStr = getNextLine("Enter Date of Employment (dd/MM/yyyy):");
            try {
                LocalDate date = LocalDate.parse(dateStr, formatter);
                builder.dateOfEmployment(date.atStartOfDay());
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd/MM/yyyy exactly.");
            }
        }
    }

    private void jobScope() {
        displayMenu("Enter Job Scope", "",
                new Option("Full Time", () -> builder.jobScope(JobScope.FULL_TIME)),
                new Option("Partial", () -> builder.jobScope(JobScope.PARTIAL))
        );
    }

    private void roles() {
        Set<Role> qualifiedRoles = new HashSet<>();
        System.out.println("\n--- Add Qualified Roles ---");
        System.out.println("Roles: " + roleController.getAllRoles());
        boolean stop = false;
        while (!stop) {
            String roleName = getNextLine("Enter a qualified role name (or type '0' to finish):");
            if (roleName.equals("0"))
                stop = true;
            else if (!roleName.trim().isEmpty()) {
                Role role = roleController.getRole(roleName);

                if (role != null) {
                    qualifiedRoles.add(role);
                    System.out.println("Role '" + roleName + "' added successfully.");
                } else {
                    System.out.println("Error: Role '" + roleName + "' does not exist in the system.");
                }
            }
        }

        builder.qualifiedRoles(new ArrayList<>(qualifiedRoles));
    }

    private void constraints() {
        String constraints = getNextLine("Enter Employee Constraints (or press Enter to skip):");
        builder.constraints(constraints);
    }

    private void yearlyRestDays() {
        int yearlyRestDays = getNextInteger("Enter Yearly Rest Days:");
        builder.yearlyRestDays(yearlyRestDays);
    }

    private void weeklyRestDay() {
        WeekDay[] day = new WeekDay[1];
        displayMenu("Enter Weekly Rest Day", "",
                new Option("Sunday", () -> day[0] = WeekDay.SUNDAY),
                new Option("Monday", () -> day[0] = WeekDay.MONDAY),
                new Option("Tuesday", () -> day[0] = WeekDay.TUESDAY),
                new Option("Wednesday", () -> day[0] = WeekDay.WEDNESDAY),
                new Option("Thursday", () -> day[0] = WeekDay.THURSDAY),
                new Option("Friday", () -> day[0] = WeekDay.FRIDAY),
                new Option("Saturday", () -> day[0] = WeekDay.SATURDAY)
        );
        builder.weeklyRestDay(day[0]);
    }


    @Override
    public void close() {
        open = false;
    }
}