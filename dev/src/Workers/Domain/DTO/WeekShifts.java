package Workers.Domain.DTO;

import Workers.DataAccess.Entities.ShiftEntity;
import Workers.DataAccess.Entities.WeekShiftsEntity;
import Workers.Shared.Enums.ShiftType;
import Workers.Shared.Enums.WeekDay;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class WeekShifts {
    private LocalDate date;
    private Map<WeekDay, Map<ShiftType, ShiftSL>> shifts;
    private final int id;

    public WeekShifts(LocalDate date, Map<WeekDay, Map<ShiftType, ShiftSL>> shifts) {
        this.date = date;
        this.id = WeekShiftsEntity.NO_ID;
        setShifts(shifts);
    }

    public WeekShifts(WeekShiftsEntity shiftsEntity) {
        this.date = shiftsEntity.getDate();
        this.id = shiftsEntity.getId();
        this.shifts = new HashMap<>();
        shiftsEntity.getShifts().forEach((day, _shiftTypeToShift) -> {
            Map<ShiftType, ShiftSL> shiftTypeToShift = new HashMap<>();
            _shiftTypeToShift.forEach((type, shift) ->
                    shiftTypeToShift.put(ShiftType.fromType(type), new ShiftSL(shift))
            );
            shifts.put(WeekDay.fromArgs(day), shiftTypeToShift);
        });

    }

    public WeekShiftsEntity toEntity() {
        Map<String, Map<String, ShiftEntity>> shifts = new HashMap<>();
        this.shifts.forEach((weekDay, shiftTypeShiftMap) -> {
            Map<String, ShiftEntity> shiftTypeToShiftEntity = new HashMap<>();
            shiftTypeShiftMap.forEach((shiftType, shift) ->
                    shiftTypeToShiftEntity.put(shiftType.toString(), shift.toEntity()));
            shifts.put(weekDay.toString(), shiftTypeToShiftEntity);
        });
        return new WeekShiftsEntity(id, date, shifts);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setShifts(Map<WeekDay, Map<ShiftType, ShiftSL>> shifts) {
        this.shifts = new HashMap<>();
        shifts.forEach((day, _shiftTypeToShift) -> {
            Map<ShiftType, ShiftSL> shiftTypeToShift = new HashMap<>();
            _shiftTypeToShift.forEach((type, shift) ->
                    shiftTypeToShift.put(type, new ShiftSL(shift))
            );
            shifts.put(day, shiftTypeToShift);
        });
    }

    public ShiftSL getShift(WeekDay day, ShiftType shiftType) {
        if (!shifts.containsKey(day)) return null;
        if (!shifts.get(day).containsKey(shiftType)) return null;
        return new ShiftSL(
                shifts.get(day).get(shiftType)
        );
    }

    public boolean isEmpty() {
        return shifts.isEmpty();
    }

    public void addUpdateShift(ShiftSL shift) {
        WeekDay day = shift.getDay();
        ShiftType shiftType = shift.getShiftType();

        if (!shifts.containsKey(day)) shifts.put(day, new HashMap<>());
        shifts.get(day).put(shiftType, new ShiftSL(shift));
        //shift.assignToWeek(id);
    }
}
