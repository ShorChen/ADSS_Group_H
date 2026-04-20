package presentation.ui_manager;

import presentation.control.EmployeeController;
import presentation.model.EmployeePL;
import presentation.ui_shared.View;
import presentation.util.Option;

public class ManageEmployeesUI extends View {
    private final EmployeeController controller;
    private boolean open = false;
    private final Runnable onBack;

    public ManageEmployeesUI(Runnable onBack) {
        this.onBack = () -> {
            close();
            onBack.run();
        };

        this.controller = new EmployeeController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            displayMenu(new Option.Builder("---Managing Employees---")
                    .append("Back", onBack)
                    .append("Add Employee", this::addEmployee)
                    .append("Deactivate Employee", this::deactivateEmployee)
                    .append("View Employee Details", this::getEmployeeDetails)
                    .append("Update Employee", this::updateEmployeeDetails), ""
            );
        }
    }

    private void addEmployee() {
        AddUpdateEmployeeView addUpdateEmployeeView = new AddUpdateEmployeeView(null);
        addUpdateEmployeeView.display();
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
            System.out.println("Weekly Rest Day: " + employee.getWeeklyRestDay());
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
        AddUpdateEmployeeView addUpdateEmployeeView = new AddUpdateEmployeeView(id);
        addUpdateEmployeeView.display();
    }

    @Override
    public void close() {
        open = false;
    }
}