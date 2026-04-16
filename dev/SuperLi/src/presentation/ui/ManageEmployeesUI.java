package presentation.ui;

import domain.entities.Employee;
import domain.services.EmployeeService;
import presentation.control.EmployeeController;

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

        this.controller = new EmployeeController(new EmployeeService()); // todo replace facade
    }

    @Override
    void display() {
        open = true;
        while (open) {
            System.out.println(employeesMenu.toString());
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
        boolean added = controller.addEmployee(new Employee("dummy employee"));
        System.out.println(added ? "Added a new employee" : "Could not add the new employee");
    }

    private void deactivateEmployee() {
        String employeeId = getNextLine("Enter Employee ID:");
        boolean deactivated = controller.deactivateEmployee(employeeId);
        System.out.println(deactivated ? "Deactivated " + employeeId :
                "Employee was not found or already deactivated");
    }

    private void getEmployeeDetails() {
        Employee employee = controller.getEmployeeDetails(getNextLine("Enter Employee ID:"));
        System.out.println(employee == null ? "Could not find employee" : "found " + employee.getId());
    }

    private void updateEmployeeDetails() {
        Employee employee = new Employee("updated employee");
        String id = getNextLine("Enter employee id");
        boolean updated = controller.updateEmployee(id, employee);
        System.out.println(updated? "updated employee" : "could not update employee");
    }

    @Override
    void close() {
        open = false;
    }
}