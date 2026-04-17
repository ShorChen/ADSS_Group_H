package data_access.pools;

import domain.entities.Shift;

import java.util.ArrayList;
import java.util.List;

public class ShiftPool {
    private final List<Shift> shifts;

    private static ShiftPool instance;

    public static ShiftPool Instance() {
        if (instance == null)
            instance = new ShiftPool();
        return instance;
    }

    private ShiftPool() {
        this.shifts = new ArrayList<>();
    }

    public void addShift(Shift shift) {
        shifts.add(shift);
    }

    public List<Shift> getAllShifts() {
        return shifts;
    }

    public Shift getshift(long shiftId) {
        for (Shift s : shifts)
            if (s.getShiftId() == shiftId)
                return s;
        return null;
    }
}
