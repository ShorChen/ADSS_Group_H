package presentation.ui_manager;

import domain.enums.JobScope;
import domain.enums.SalaryType;
import domain.enums.WeekDay;
import presentation.control.AddUpdateEmployeeController;
import presentation.model.EmployeePL;
import presentation.ui_shared.View;
import presentation.util.Option;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddUpdateEmployeeView extends View {


    private final AddUpdateEmployeeController controller;
    private boolean addEmployee;

    private EmployeePL.Builder builder;
    private boolean open;
    private final String employeeId;

    public AddUpdateEmployeeView(String employeeId) {
        super(null);
        this.employeeId = employeeId;
        controller = new AddUpdateEmployeeController();
    }

    @Override
    public void display() {
        open = true;

        EmployeePL e = controller.getEmployeeDetails(employeeId);
        if (employeeId == null) {
            buildEmployee("\n--- Add New Employee ---");
            addEmployee = true;
        } else if (e == null) {
            buildEmployee("Could not find employee... Adding New Employee");
            addEmployee = true;
        } else {
            System.out.println("\n--- Update Existing Employee ---");
            addEmployee = false;
            builder = new EmployeePL.Builder(employeeId);
        }

        while (open) {
            EmployeePL employee = builder.build();
            printSummary(employee);

            displayMenu(new Option.Builder("--- Options ---")
                    .append("Cancel", this::close)
                    .append("Update a Field", this::updateFieldMenu)
                    .append("Save Changes", () -> {
                        if (employeeId == null || e == null) registerEmployee(employee);
                        else updateEmployee(employee);
                    }), ""
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
        if (addEmployee)
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
        System.out.println("Constraints: " + (employee.getConstraints() == null ||
                                              employee.getConstraints().isEmpty() ?
                "None" : employee.getConstraints()));

        System.out.print("Roles: ");
        if (employee.getQualifiedRoles().isEmpty()) {
            System.out.println("None");
        } else {
            employee.getQualifiedRoles().forEach(role -> System.out.print(role + " "));
            System.out.println();
        }
    }

    private void updateFieldMenu() {
        displayMenu(new Option.Builder("---Update Field ---")
                .append("Back to Summary", () -> {
                })
                .append("Name", this::name)
                .append("Bank Account", this::bankAccount)
                .append("Salary & Salary Type", this::salary)
                .append("Date of Employment", this::dateOfEmployment)
                .append("Job Scope", this::jobScope)
                .append("Roles", this::roles)
                .append("Constraints", this::constraints)
                .append("Yearly Rest Days", this::yearlyRestDays), ""
        );
    }

    private void registerEmployee(EmployeePL employee) {
        try {
            System.out.println("Your password: " + controller.addEmployee(employee) +
                               " make sure to change it as soon as you get access to the system");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        close();
    }

    private void updateEmployee(EmployeePL employee) {

        String password = getNextLine("To save changes, please enter password");
        boolean updated = controller.updateEmployee(employee, password);
        if (!updated) System.out.println("Could not update. Incorrect password");
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
        displayMenu(new Option.Builder("Enter Salary Type")
                .append("Hourly", () -> builder.salaryType(SalaryType.HOURLY))
                .append("Globally", () -> builder.salaryType(SalaryType.GLOBALLY)), ""
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
        displayMenu(new Option.Builder("Enter Job Scope")
                .append("Full Time", () -> builder.jobScope(JobScope.FULL_TIME))
                .append("Partial", () -> builder.jobScope(JobScope.PARTIAL)), ""
        );
    }

    private void roles() {
        Set<String> qualifiedRoles = new HashSet<>();
        System.out.println("\n--- Add Qualified Roles ---");
        System.out.println("Roles: " + controller.getAllRoles());
        boolean stop = false;
        while (!stop) {
            String roleName = getNextLine("Enter a qualified role name (or type '0' to finish):");
            if (roleName.equals("0"))
                stop = true;
            else if (!roleName.trim().isEmpty()) {
                String role = controller.getRole(roleName);

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
        System.out.println("Enter Employee Constraints (or press Enter to skip):");
        String constraints = reader.nextLine();
        builder.constraints(constraints);
    }

    private void yearlyRestDays() {
        int yearlyRestDays = getNextInteger("Enter Yearly Rest Days:");
        builder.yearlyRestDays(yearlyRestDays);
    }

    private void weeklyRestDay() {
        WeekDay[] day = new WeekDay[1];
        Option.Builder menuBuilder = new Option.Builder("Enter Weekly Rest Day");
        List<WeekDay> closedDays = controller.getClosedDays();
        closedDays.forEach(d -> menuBuilder.append(d.day, () -> day[0] = d));
        displayMenu(menuBuilder, "");

        builder.weeklyRestDay(day[0]);
    }


    @Override
    public void close() {
        open = false;
    }
}