package presentation.ui;

import presentation.control.HRManagerShiftController;

public class ManageShiftsUI extends View {
    private final HRManagerShiftController controller;
    private boolean open = false;
    private final Runnable onBack;

    private static final StringBuilder shiftsMenu = new StringBuilder(
            """
                    --- Manage Shifts ---
                    0. Back
                    1. Create Shift Template (Not Implemented)
                    2. Set Default Template (Not Implemented)
                    3. Place Employees to Shifts
                    4. Set Submission Deadline
                    5. Handle Replacement Requests
                    6. Issue HR Report (Not Implemented)
                    """
    );

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
            System.out.print(shiftsMenu.toString());
            int selection = getNextInteger("Select option (number):");

            handleSelection(selection,
                    onBack,
                    this::createTemplate,
                    this::setDefaultTemplate,
                    this::placeEmployees,
                    this::setDeadline,
                    this::handleReplacements,
                    this::issueReport
            );
        }
    }

    private void createTemplate() {
        String name = getNextLine("Enter new template name:");
        // TODO: this is a test implementation
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