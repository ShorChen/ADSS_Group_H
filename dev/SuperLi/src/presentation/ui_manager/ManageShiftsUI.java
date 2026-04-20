package presentation.ui_manager;

import presentation.control.HRManagerShiftController;
import presentation.ui_shared.ShiftsView;
import presentation.ui_shared.View;
import presentation.util.Option;

public class ManageShiftsUI extends View {
    private final HRManagerShiftController controller;
    private boolean open = false;
    private final Runnable onBack;

    public ManageShiftsUI(Runnable onBack) {
        this.onBack = () -> {
            close();
            onBack.run();
        };

        this.controller = new HRManagerShiftController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            displayMenu(new Option.Builder("--- Manage Shifts ---")
                    .append("Back", onBack)
                    .append("Place Employees to Shifts", this::placeEmployees)
                    .append("Set Submission Deadline", this::setDeadline)
                    .append("Handle Replacement Requests", this::handleReplacements)
                    .append("Create Shift Template (Not Implemented)", this::createTemplate)
                    .append("Set Default Template (Not Implemented)", this::setDefaultTemplate)
                    .append("Issue HR Report (Not Implemented)", this::issueReport), ""
            );
        }
    }

    private void createTemplate() {
        String name = getNextLine("Enter new template name:");
        controller.createShiftTemplate(name, null);
        System.out.println("Template '" + name + "' created successfully.");
    }

    private void setDefaultTemplate() {
        String name = getNextLine("Enter the name of the template to set as default:");
        controller.setShiftTemplateAsDefault(name);
        System.out.println("Default template updated.");
    }

    private void placeEmployees() {
        System.out.println("Initiating employee placement algorithm...");
        controller.placeToShifts(null, null);
        System.out.println("Employees placed in shifts.");

        ShiftsView shiftsView = new ShiftsView(0);

    }

    private void setDeadline() {
        boolean validDate = false;

        while (!validDate) {
            String date = getNextLine("Enter Date For Deadline (dd/MM/yyyy HH:mm) or type cancel to cancel: ");
            if (date.equalsIgnoreCase("cancel")) {
                System.out.println("Canceled");
                return;
            }
            try {
                controller.setDeadline(date);
                validDate = true;
                System.out.println("Deadline set to " + date);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void handleReplacements() {

    }

    private void issueReport() {
        System.out.println(controller.issueReport());
    }

    @Override
    public void close() {
        open = false;
    }
}