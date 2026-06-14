package presentation.ui_manager;

import presentation.control.PlaceEmployeesController;
import presentation.model.EmployeePL;
import presentation.ui_employee.RequestReplacementUI;
import presentation.ui_shared.ShiftsView;
import presentation.ui_shared.ViewCLI;
import presentation.util.Option;

import java.util.List;
import java.util.Objects;

public class PlaceEmployeesUI extends ViewCLI {

    private final PlaceEmployeesController controller;

    public PlaceEmployeesUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new PlaceEmployeesController();
    }

    @Override
    public void display() {
        ShiftsView shiftsView = new ShiftsView(0);
        shiftsView.selectShift((day, type) -> placeEmployeeFlow(
                day.toString(),
                type.toString()
        ));
        onDismiss.run();

    }

    private void placeEmployeeFlow(String day, String type) {

        String selectedRole = selectionMenuOf("Roles", controller.getRoles(), Objects::toString, o -> o);
        EmployeePL selectedEmployee = selectEmployeeFromMenu(day, type, selectedRole);

        try {
            if (selectedEmployee == null) displayOnEmptyMenu();
            else controller.assignToShift(day, type, selectedEmployee, selectedRole);
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        } catch (Exception _) {

        }
    }

    private EmployeePL selectEmployeeFromMenu(String day, String type, String role) {
        List<EmployeePL> employees = controller.getAvailableEmployees(day, type, role);
        if (employees.isEmpty()) return null;
        return selectionMenuOf("---Available Employees---",
                employees, EmployeePL::getId, o -> o);
    }

    private void displayOnEmptyMenu() {
        displayMenu(new Option.Builder("No available employee. Request Exceptional Placement?")
                .append("Yes", this::requestReplacements)
                .append("No", () -> {
                })
        );
    }

    private void requestReplacements() {
        RequestReplacementUI requestUI = new RequestReplacementUI(this::display);
        close();
        requestUI.display();
    }

    @Override
    public void close() {

    }
}
