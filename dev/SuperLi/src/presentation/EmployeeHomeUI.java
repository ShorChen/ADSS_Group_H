package presentation;

public class EmployeeHomeUI extends View {
    public static final StringBuilder employeeMenu = new StringBuilder(
            """
                    Actions for Employee:
                    0. Logout
                    1. View Schedule & Submit Availability
                    2. Set Preferred Rest Day
                    3. Request Shift Replacement
                    """
    );
    private boolean open = false;
    private final Runnable onLogout;

    public EmployeeHomeUI(Runnable onLogout) {
        this.onLogout = onLogout;
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            System.out.println(employeeMenu.toString());
            int selection = getNextInteger("Select option (number)");
            handleSelection(selection,
                    onLogout,
                    this::chooseShifts,
                    this::setRestDay,
                    this::requestReplacement
            );
        }
    }


    private void chooseShifts() {
        EmployeeChooseShiftsUI shiftsUI = new EmployeeChooseShiftsUI(this::display);
        close();
        shiftsUI.display();
    }

    private void setRestDay() {
        System.out.println("not implemented");

    }

    private void requestReplacement() {
        System.out.println("not implemented");
    }

    @Override
    public void close() {
        open = false;
    }
}