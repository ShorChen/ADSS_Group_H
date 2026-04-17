package presentation.ui;

import domain.entities.Role;
import domain.enums.JobScope;
import domain.enums.SalaryType;
import presentation.control.AuthController;
import presentation.control.EmployeeController;
import presentation.control.RoleController;
import presentation.model.EmployeePL;

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

            System.out.println("\n--- Options ---");
            System.out.println("1. Save Changes");
            System.out.println("2. Update a Field");
            System.out.println("3. Cancel");

            int choice = getNextInteger("Select an option:");

            if (choice == 1) {
                if (employeeId == null || e == null) RegisterEmployee(employee);
                else UpdateEmployee(employee);
            } else if (choice == 2) {
                updateFieldMenu();
            } else if (choice == 3) {
                System.out.println("Employee creation cancelled.");
                close();
            } else System.out.println("Invalid selection. Try again.");

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
        restDays();
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
        System.out.println("Constraints: " + (employee.getConstraints().isEmpty() ? "None" : employee.getConstraints()));

        System.out.print("Roles: ");
        if (employee.getQualifiedRoles().isEmpty()) {
            System.out.println("None");
        } else {
            employee.getQualifiedRoles().forEach(role -> System.out.print(role.getTag() + " ")); // Assuming Role has a getTag() or getName()
            System.out.println();
        }
    }

    private void updateFieldMenu() {
        System.out.println("\n--- Update Field ---");
        System.out.println("0. Back to Summary");
        System.out.println("1. Name");
        System.out.println("2. Bank Account");
        System.out.println("3. Salary & Salary Type");
        System.out.println("4. Date of Employment");
        System.out.println("5. Job Scope");
        System.out.println("6. Roles");
        System.out.println("7. Constraints");
        System.out.println("8. Rest Days");

        int choice = getNextInteger("Select field to update:");
        Runnable[] choices = new Runnable[]{() -> {}, this::name, this::bankAccount,
                this::salary, this::dateOfEmployment, this::jobScope,
                this::roles, this::constraints, this::restDays};
        if (choice >= 0 && choice < choices.length)
            choices[choice].run();
        else System.out.println("Invalid selection");
    }

    private void RegisterEmployee(EmployeePL employee) {
        try {
            System.out.println("Your password: " + employeeController.addEmployee(employee) +
                               " make sure to change it as soon as you get access to the system");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        close();
    }

    private void UpdateEmployee(EmployeePL employee) {
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
        String salTypeString = selectUntilResult("Enter Salary Type (UPPER_CASE):",
                SalaryType.HOURLY.name(),
                SalaryType.GLOBALLY.name());

        SalaryType salaryType = SalaryType.valueOf(salTypeString);
        double salary = getNextDouble("Enter Employee's Salary:");

        builder.salary(salary).salaryType(salaryType);
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
        String jobScope = selectUntilResult("Enter Job Scope (UPPER_CASE)",
                JobScope.FULL_TIME.name(),
                JobScope.PARTIAL.name());

        builder.jobScope(JobScope.valueOf(jobScope));
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

    private void restDays() {
        int yearlyRestDays = getNextInteger("Enter Yearly Rest Days:");
        builder.yearlyRestDays(yearlyRestDays);
    }

    private String selectUntilResult(String message, String... options) {
        String selection = getNextLine(message);
        for (String option : options) {
            if (selection.equals(option)) {
                return selection;
            }
        }
        System.out.println("Invalid selection. Please try again.");
        return selectUntilResult(message, options);
    }

    @Override
    public void close() {
        open = false;
    }
}