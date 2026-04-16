package presentation;
import domain.enums.ShiftType;
import presentation.controllers.ShiftController;

public class EmployeeChooseShiftsUI extends View {
    private ShiftsView shiftsView = new ShiftsView(); // todo generally we should get the actual shift schedule of the next week
    private boolean open = false;
    private final ShiftController shiftController;
    private Runnable onBack;
    private String menu = """
            0 - Back to Menu
            1 - Mark Shift as Unavailable
            2 - Submit Availability
            """;

    public EmployeeChooseShiftsUI(Runnable onBack, ShiftController shiftController) {
        this.onBack = () -> {
            close();
            onBack.run();
        };
        this.shiftController = shiftController;
    }

    @Override
    public void display() {
        open = true;
        while(open) {
            System.out.println("Viewing Schedule of next week");
            shiftsView.display();
            System.out.print(menu);
            int selection = getNextInteger("Select option:");

            if (selection == 0) {
                close();
                onBack.run();
            } else if (selection == 1) {
                int dayIdx = getNextInteger("Enter day (0=SUN, 1=MON, 2=TUE, 3=WED, 4=THU, 5=FRI, 6=SAT):");
                int shiftIdx = getNextInteger("Enter shift (0=DAY, 1=NIGHT):");

                if (dayIdx >= 0 && dayIdx <= 6 && shiftIdx >= 0 && shiftIdx <= 1) {
                    WeekDay day = WeekDay.values()[dayIdx];
                    ShiftType type = shiftIdx == 0 ? ShiftType.DAY : ShiftType.EVENING;
                    ShiftsView.setMarked(day, type, 'X');
                } else {
                    System.out.println("Invalid selection.");
                }
            } else if (selection == 2) {
                System.out.println("Availability Submitted.");
                close();
                onBack.run();
            }
        }
    }

    @Override
    public void close() {
        open = false;
    }
}