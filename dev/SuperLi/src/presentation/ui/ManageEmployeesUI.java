package presentation.ui;

import presentation.control.EmployeeController;
import presentation.model.EmployeePL;

public class ManageEmployeesUI extends View {
    private final EmployeeController controller;
    private boolean open = false;
    private final Runnable onBack;

    private static final StringBuilder employeesMenu = new StringBuilder(
            """
                    0. Back
                    1. Add Employee
                    2. Deactivate Employee
                    3. View Employee Details
                    4. Update Employee
                    """
    );

    public ManageEmployeesUI(Runnable onBack) {
        this.onBack = () -> {
            close();
            onBack.run();
        };

        this.controller = new EmployeeController();
    }

    @Override
    void display() {
        open = true;
        while (open) {
            System.out.print(employeesMenu.toString());
            int selection = getNextInteger("Select option (number)");
            handleSelection(selection,
                    onBack,
                    this::addEmployee,
                    this::deactivateEmployee,
                    this::getEmployeeDetails,
                    this::updateEmployeeDetails
            );
        }
    }

    private void addEmployee() {
        AddEmployeeView addEmployeeView = new AddEmployeeView(null);
        addEmployeeView.display();
    }

    private void deactivateEmployee() {
        String employeeId = getNextLine("Enter Employee ID:");
        boolean deactivated = controller.deactivateEmployee(employeeId);
        System.out.println(deactivated ? "Deactivated " + employeeId :
                "Employee was not found or already deactivated");
    }

    private void getEmployeeDetails() {
        EmployeePL employee = controller.getEmployeeDetails(getNextLine("Enter Employee ID:"));
        if (employee == null)
            System.out.println("Could not find employee");
        else {
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

    }

    private void updateEmployeeDetails() {
        String id = getNextLine("Enter employee id");
        AddEmployeeView addEmployeeView = new AddEmployeeView(id);
        addEmployeeView.display();
    }

    @Override
    void close() {
        open = false;
    }
}