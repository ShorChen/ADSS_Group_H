package Employees.Presentation.UIEmployee;

import Employees.Presentation.Controller.EmployeeAdditionalHoursController;
import Employees.Presentation.UIShared.ShiftsView;
import Employees.Presentation.UIShared.ViewCLI;
import Employees.Presentation.Utils.Option;

public class EmployeeAdditionalHoursUI extends ViewCLI {
    private boolean open;
    private ShiftsView shiftsView;
    private final EmployeeAdditionalHoursController controller;

    public EmployeeAdditionalHoursUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new EmployeeAdditionalHoursController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            shiftsView = new ShiftsView(0,
                    controller.getPastDayShiftsOfEmployeeThisWeekPredicate()
            );
            shiftsView.display();
            displayMenu(new Option.Builder("Report Additional Hours")
                    .append("Save and Back", onDismiss)
                    .append("Report to Shift", this::reportHours)
            );
        }
    }

    private void reportHours() {
        shiftsView.selectShift((day, type) -> {
            float hours = getNextFloat("Enter amount of hours");
            while (hours <= 0)
                hours = getNextFloat("Amount must be more than 0\nEnter amount of hours");
            controller.updateAdditionalHours(day, type, hours);
        });
    }

    @Override
    public void close() {
        open = false;
    }
}
