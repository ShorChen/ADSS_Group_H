package presentation.ui_manager;

import presentation.control.PlaceEmployeesController;
import presentation.model.EmployeePL;
import presentation.ui_employee.RequestReplacementUI;
import presentation.ui_shared.ShiftsView;
import presentation.ui_shared.View;
import presentation.util.Option;

import java.util.List;

public class PlaceEmployeesUI extends View {

    private final PlaceEmployeesController controller;

    public PlaceEmployeesUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new PlaceEmployeesController();
    }

    @Override
    public void display() {
        ShiftsView shiftsView = new ShiftsView(0);
        shiftsView.selectShift(this::placeEmployeeFlow);
        onDismiss.run();

    }

    private void placeEmployeeFlow(int day, int type) {

        String selectedRole = selectionMenuOf("Roles", controller.getRoles());
        EmployeePL selectedEmployee = selectEmployeeFromMenu(day, type, selectedRole);

        try {
            if (selectedEmployee == null) displayOnEmptyMenu();
            else controller.assignToShift(day, type, selectedEmployee, selectedRole);
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        } catch (Exception _) {

        }
    }

    private EmployeePL selectEmployeeFromMenu(int day, int type, String role) {
        List<EmployeePL> employees = controller.getAvailableEmployees(day, type, role);
        if (employees.isEmpty()) return null;
        return selectionMenuOf("---Available Employees---",
                employees, EmployeePL::getId);
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
