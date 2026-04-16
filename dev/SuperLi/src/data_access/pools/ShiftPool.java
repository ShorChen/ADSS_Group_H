package data_access.pools;

import domain.entities.Shift;
import java.util.ArrayList;
import java.util.List;

public class ShiftPool {
    private final List<Shift> shifts;

    public ShiftPool() {
        this.shifts = new ArrayList<>();
    }

    public void addShift(Shift shift) {
        shifts.add(shift);
    }

    public List<Shift> getAllShifts() {
        return shifts;
    }
}