package presentation.ui_employee;

import domain.enums.ShiftType;
import domain.enums.WeekDay;
import presentation.ui_shared.ShiftsView;
import presentation.ui_shared.View;
import presentation.util.Option;

public class RequestChangeUI extends View {
    private boolean open;

    @Override
    public void display() {
        open = true;
        while (open) {
            ShiftsView shiftsView = new ShiftsView(0);
            shiftsView.display();


            // todo: should know which shifts/day are actually open
            WeekDay[] day = new WeekDay[1];
            displayMenu(new Option.Builder("Enter Shift's Week Day")
                    .append("Sunday", () -> day[0] = WeekDay.SUNDAY)
                    .append("Monday", () -> day[0] = WeekDay.MONDAY)
                    .append("Tuesday", () -> day[0] = WeekDay.TUESDAY)
                    .append("Wednesday", () -> day[0] = WeekDay.WEDNESDAY)
                    .append("Thursday", () -> day[0] = WeekDay.THURSDAY)
                    .append("Friday", () -> day[0] = WeekDay.FRIDAY)
                    .append("Saturday", () -> day[0] = WeekDay.SATURDAY), ""
            );


            ShiftType[] shiftType = new ShiftType[1];
            displayMenu(new Option.Builder("Enter Shift's Week Day")
                    .append("Day", () -> shiftType[0] = ShiftType.DAY)
                    .append("Evening", () -> shiftType[0] = ShiftType.EVENING), ""
            );



            String otherId = getNextLine("Enter employee's id to replace shift with ");


        }
    }

    @Override
    public void close() {
        open = false;
    }
}
