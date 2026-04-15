package domain.in_memory;

import domain.entities.Shift;

import java.util.List;

public class ShiftPool {
    private final List<Shift> shifts;

    public ShiftPool(List<Shift> shifts) {
        this.shifts = shifts;
    }


    public Shift getEmployee(long shiftId) {
        for (Shift s : shifts)
            if (s.getShiftId() == shiftId)
                return s;
        return null;
    }
}
