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
        shiftsView.selectShift((day, type) -> {
            List<String> roles = controller.getRoles();
            Option.Builder builder = new Option.Builder("Roles");
            String[] selectedRole = new String[1];
            roles.forEach(role -> builder.append(role, () -> selectedRole[0] = role));
            displayMenu(builder, "");

            List<EmployeePL> employees = controller.getAvailableEmployees(day, type, selectedRole[0]);
            checkNoAvailableEmpty(employees);

            Option.Builder employeeMenu = new Option.Builder("---Available Employees---");
            EmployeePL[] selectedEmployee = new EmployeePL[1];
            employees.forEach(employeePL ->
                    employeeMenu.append(employeePL.getId(), () ->
                            selectedEmployee[0] = employeePL));

            displayMenu(employeeMenu, "");


            controller.assignToShift(day, type, selectedEmployee[0], selectedRole[0]);
        });
        onDismiss.run();

    }


    private void checkNoAvailableEmpty(List<EmployeePL> employees) {
        if (employees.isEmpty()) {
            displayMenu(new Option.Builder("No available employee. Request Exceptional Placement?")
                            .append("Yes", this::requestReplacements)
                            .append("No", () -> {
                            })
                    , "");
        }
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
