package presentation.control;

import domain.enums.WeekDay;
import domain.services.ShiftService;

import java.util.List;

public class AddUpdateEmployeeController {
    private final ShiftService shiftService;

    public AddUpdateEmployeeController() {
        shiftService = new ShiftService();
    }

    public List<WeekDay> getClosedDays(){
        return shiftService.getClosedDays();
    }
}
