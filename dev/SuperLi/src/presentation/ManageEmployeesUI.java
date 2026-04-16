package presentation;

import domain.entities.Employee;
import domain.services.EmployeeService;
import presentation.controllers.EmployeeController;

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

    public ManageEmployeesUI(Runnable onBack, EmployeeController controller) {
        this.onBack = () -> {
            close();
            onBack.run();
        };

        this.controller = controller;
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            System.out.println(employeesMenu.toString());
            int selection = getNextInteger("Select option (number):");
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
        String id = getNextLine("Enter new Employee ID:");
        String result = controller.addEmployee(id);
        System.out.println(result);
    }

    private void deactivateEmployee() {
        String id = getNextLine("Enter Employee ID to deactivate:");
        String result = controller.deactivateEmployee(id);
        System.out.println(result);
    }

    private void getEmployeeDetails() {
        String id = getNextLine("Enter Employee ID:");
        String result = controller.getEmployeeDetails(id);
        System.out.println(result);
    }

    private void updateEmployeeDetails() {
         System.out.println("Method not yet implemented");
    }

    @Override
    void close() {
        open = false;
    }
}