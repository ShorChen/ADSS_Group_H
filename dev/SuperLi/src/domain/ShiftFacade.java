package domain;

import java.util.Date;
import java.util.List;

public class ShiftFacade {
    private List<Shift> shifts;
    private Date constraintDeadLine;

    public ShiftFacade(List<Shift> shifts) {
        this.shifts = shifts;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void createShift(Date date, ShiftType type) {
        Shift newShift = new Shift(date, type);
        shifts.add(newShift);
    }

    public void setConstraintDeadLine(Date constraintDeadLine) {
        this.constraintDeadLine = constraintDeadLine;
    }

    public Date getConstraintDeadLine() {
        return constraintDeadLine;
    }

    
}
