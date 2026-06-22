package Workers.Presentation.UIManager;

import Workers.Context.SessionManager;
import Workers.Presentation.Controller.AddUpdateEmployeeController;
import Workers.Presentation.DTO.EmployeePL;
import Workers.Presentation.UIShared.ViewCLI;
import Workers.Presentation.Utils.Option;
import Workers.Shared.Enums.JobScope;
import Workers.Shared.Enums.SalaryType;
import Workers.Shared.Enums.WeekDay;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddUpdateEmployeeView extends ViewCLI {
    private final AddUpdateEmployeeController controller;
    private EmployeePL.Builder builder;
    private boolean open;
    private final String employeeId;
    private boolean isNewEmployee;

    public AddUpdateEmployeeView(String employeeId) {
        super(null);
        this.employeeId = employeeId;
        controller = new AddUpdateEmployeeController();
    }

    @Override
    public void display() {
        open = true;

        EmployeePL employee = controller.getEmployeeDetails(employeeId);
        isNewEmployee = employeeId == null || employee == null;

        if (!isNewEmployee) {
            System.out.println("\n--- Update Existing Employee ---");
            builder = new EmployeePL.Builder(employee);
        } else buildNewEmployee();

        while (open) {
            printSummary();

            displayMenu(new Option.Builder("--- Options ---")
                    .append("Cancel", this::close)
                    .append("Update a Field", this::updateFieldMenu)
                    .append("Save Changes", this::saveChanges)
            );

        }
    }

    private void buildNewEmployee() {
        if (employeeId == null) {
            System.out.println("\n--- Add New Employee ---");
            buildEmployee();
        } else {
            System.out.println("Could not find employee... Adding New Employee");
            buildEmployee();
        }
    }

    private void saveChanges() {
        EmployeePL employee = builder.build();
        if (isNewEmployee) registerEmployee(employee);
        else updateEmployee(employee);
    }

    private void buildEmployee() {
        id();
        name();
        bankAccount();
        salary();
        dateOfEmployment();
        jobScope();
        roles();
        constraints();
        yearlyRestDays();
        branch();
        if (isNewEmployee)
            weeklyRestDay();
    }

    private void branch() {
        builder.branchId(
                SessionManager.getSelectedBranchId()
        );
    }

    private void printSummary() {
        EmployeePL employee = builder.build();

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
            employee.getQualifiedRoles().forEach(role -> System.out.print(role + ", "));
            System.out.println();
        }
    }

    private void updateFieldMenu() {
        Option.Builder menuBuilder = new Option.Builder("---Update Field ---")
                .append("Back to Summary", null)
                .append("Name", this::name)
                .append("Bank Account", this::bankAccount)
                .append("Salary & Salary Type", this::salary)
                .append("Date of Employment", this::dateOfEmployment)
                .append("Job Scope", this::jobScope)
                .append("Roles", this::roles)
                .append("Constraints", this::constraints)
                .append("Yearly Rest Days", this::yearlyRestDays);
        if (isNewEmployee) menuBuilder.append("Weekly Rest Day", this::weeklyRestDay);


        displayMenu(menuBuilder);
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

        String password = getNextLine("To save changes, please enter employee password");
        boolean updated = controller.updateEmployee(employee, password);
        if (!updated) System.out.println("Could not update. Incorrect password");
        close();
    }


    private void id() {
        String id = getNextLine("Enter Employee Id:");
        while(controller.existsEmployeeId(id)){
            System.out.println("Employee with this id already exists in the system");
            id = getNextLine("Enter Employee Id:");
        }
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
                .append("Globally", () -> builder.salaryType(SalaryType.GLOBALLY))
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
                .append("Partial", () -> builder.jobScope(JobScope.PARTIAL))
        );
    }

    private void roles() {
        AtomicBoolean stop = new AtomicBoolean(false);
        Set<String> qualifiedRoles = builder.getQualifiedRoles();
        while (!stop.get()) {
            Option.Builder rolesMenu = new Option.Builder("--- Select Qualified Roles ---")
                    .append("done", () -> stop.set(true));

            controller.getAllRoles().forEach(role -> {
                boolean hasRole = qualifiedRoles.contains(role);
                rolesMenu.append(role + (hasRole ? " (Select to Delete) " : ""), () -> {
                    if (hasRole) {
                        qualifiedRoles.remove(role);
                        System.out.println("Role '" + role + "' removed.");
                    } else {
                        qualifiedRoles.add(role);
                        System.out.println("Role '" + role + "' added successfully.");
                    }
                });
            });
            rolesMenu.setEndMessage("Enter 0 to stop selection");
            displayMenu(rolesMenu);
        }

        builder.qualifiedRoles(new HashSet<>(qualifiedRoles));
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
        displayMenu(menuBuilder);

        builder.weeklyRestDay(day[0]);
    }


    @Override
    public void close() {
        open = false;
    }
}