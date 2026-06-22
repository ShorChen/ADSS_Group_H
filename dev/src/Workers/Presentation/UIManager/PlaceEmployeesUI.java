package Workers.Presentation.UIManager;

import Workers.Presentation.Controller.PlaceEmployeesController;
import Workers.Presentation.DTO.EmployeePL;
import Workers.Presentation.UIEmployee.RequestReplacementUI;
import Workers.Presentation.UIShared.ShiftsView;
import Workers.Presentation.UIShared.ViewCLI;
import Workers.Presentation.Utils.Option;
import Workers.Shared.Enums.ShiftType;
import Workers.Shared.Enums.WeekDay;

import java.util.List;
import java.util.Objects;

public class PlaceEmployeesUI extends ViewCLI {
    private final PlaceEmployeesController controller;
    private ShiftsView shiftsView;
    boolean isFirstWeek;

    public PlaceEmployeesUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new PlaceEmployeesController();
    }

    @Override
    public void display() {
        shiftsView = new ShiftsView(-1);
        isFirstWeek = controller.isFirstWeek();
        if (isFirstWeek) onFirstWeek();

        shiftsView.selectShift(this::placeEmployeeFlow);
        onDismiss.run();

    }

    private void onFirstWeek() {
        Option.Builder builder = new Option.Builder("Select Week");
        builder.append("Back", onDismiss);
        builder.append("This Week", () -> shiftsView = new ShiftsView(0));
        builder.append("Upcoming Week", () -> shiftsView = new ShiftsView(-1));
        displayMenu(builder);
    }

    private void placeEmployeeFlow(WeekDay day, ShiftType type) {

        String selectedRole = selectionMenuOf("Roles", controller.getRoles(), Objects::toString, o -> o);
        EmployeePL selectedEmployee = selectEmployeeFromMenu(day, type, selectedRole);

        try {
            if (selectedEmployee == null) displayOnEmptyMenu();
            else controller.assignToShift(day, type, selectedEmployee, selectedRole, isFirstWeek);
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        } catch (Exception _) {

        }
    }

    private EmployeePL selectEmployeeFromMenu(WeekDay day, ShiftType type, String role) {
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
