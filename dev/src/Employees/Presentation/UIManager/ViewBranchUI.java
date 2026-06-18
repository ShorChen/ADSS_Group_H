package Employees.Presentation.UIManager;

import Employees.Presentation.Controller.ViewBranchController;
import Employees.Presentation.Model.BranchPL;
import Employees.Presentation.UIShared.ViewCLI;
import Employees.Presentation.Utils.Option;

public class ViewBranchUI extends ViewCLI {
    private ViewBranchController controller;
    private boolean open;

    public ViewBranchUI(BranchPL branch, Runnable onDismiss) {
        super(onDismiss);
        controller = new ViewBranchController(branch);
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            displayMenu(new Option.Builder(" --- Manage Branch --- ")
                    .append("Back", onDismiss)
                    .append("View Shift History", this::viewShiftHistory)
                    .append("Manage shifts", this::manageShifts)
                    .append("Manage Employees", this::manageEmployees)
            );
        }
    }

    private void manageEmployees() {
        ManageEmployeesUI manageEmployeesUI = new ManageEmployeesUI(this::display);
        close();
        manageEmployeesUI.display();
    }

    private void manageShifts() {
        ManageShiftsUI manageShiftsUI = new ManageShiftsUI(this::display);
        close();
        manageShiftsUI.display();
    }

    private void viewShiftHistory() {
        ViewShiftHistoryUI historyUI = new ViewShiftHistoryUI(this::display);
        close();
        historyUI.display();
    }

    @Override
    public void close() {
        open = false;
    }
}
