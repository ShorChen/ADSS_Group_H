package presentation.ui;

import domain.services.ShiftService;
import presentation.control.HRManagerShiftController;

public class ManageShiftsUI extends View {
    private final HRManagerShiftController controller;
    private boolean open = false;
    private final Runnable onBack;

    private static final StringBuilder shiftsMenu = new StringBuilder(
            """
                    --- Manage Shifts ---
                    0. Back
                    1. Create Shift Template
                    2. Set Default Template
                    3. Place Employees to Shifts
                    4. Set Submission Deadline
                    5. Handle Replacement Requests
                    6. Issue HR Report
                    """
    );

    public ManageShiftsUI(Runnable onBack) {
        this.onBack = () -> {
            close();
            onBack.run();
        };

        this.controller = new HRManagerShiftController(new ShiftService());
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            System.out.print(shiftsMenu.toString());
            int selection = getNextInteger("Select option (number):");

            handleSelection(selection,
                    onBack,
                    this::createTemplateFlow,
                    this::setDefaultTemplateFlow,
                    this::placeEmployeesFlow,
                    this::setDeadlineFlow,
                    this::handleReplacementsFlow,
                    this::issueReportFlow
            );
        }
    }

    private void createTemplateFlow() {
        String name = getNextLine("Enter new template name:");
        // TODO: this is a test implementation
        controller.createShiftTemplate(name, null);
        System.out.println("Template '" + name + "' created successfully.");
    }

    private void setDefaultTemplateFlow() {
        String name = getNextLine("Enter the name of the template to set as default:");
        controller.setShiftTemplateAsDefault(name);
        System.out.println("Default template updated.");
    }

    private void placeEmployeesFlow() {
        System.out.println("Initiating employee placement algorithm...");
        controller.placeToShifts(null, null);
        System.out.println("Employees placed in shifts.");
    }

    private void setDeadlineFlow() {
        // service.setDeadline(date);
    }

    private void handleReplacementsFlow() {
        // service.handleReplacementRequest(request, approve);
    }

    private void issueReportFlow() {
        System.out.println(controller.issueReport());
    }

    @Override
    public void close() {
        open = false;
    }
}