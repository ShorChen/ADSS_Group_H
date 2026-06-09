package domain;

import data_access.pools.ShiftPool;
import domain.entities.Shift;
import domain.entities.WeekShifts;
import shared.enums.ShiftType;
import shared.enums.WeekDay;
import domain.services.ShiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShiftServiceTest {

    private ShiftService shiftService;

    @BeforeEach
    void setUp() {
        ShiftPool.Instance().clear();
        shiftService = new ShiftService();
    }

    // ================
    //  getNWeeksAgo
    // ================

    @Test
    void getNWeeksAgo_WeekExists_ReturnsCorrectWeekShifts() {
        LocalDate tuesday = LocalDate.of(2026, 5, 5);
        WeekShifts weekToSave = new WeekShifts(tuesday, new HashMap<>());
        shiftService.updateWeek(weekToSave);

        WeekShifts result = shiftService.getNWeeksAgo(tuesday);

        assertNotNull(result, "WeekShifts should not be null");
    }

    @Test
    void getNWeeksAgo_WeekDoesNotExist_ReturnsEmptyWeekShiftsObject() {
        LocalDate randomDate = LocalDate.of(2020, 1, 1);

        WeekShifts result = shiftService.getNWeeksAgo(randomDate);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty WeekShifts object");
    }

    // ========================
    //  ClosedDays (Integration)
    // =========================

    @Test
    void setAndGetClosedDays_ValidDays_ReturnsMappedEnums() {
        List<String> closedDaysInput = new ArrayList<>(List.of("FRIDAY", "SATURDAY"));

        shiftService.setClosedDays(closedDaysInput);
        List<WeekDay> result = shiftService.getClosedDays();

        assertEquals(2, result.size());
        assertTrue(result.contains(WeekDay.FRIDAY));
        assertTrue(result.contains(WeekDay.SATURDAY));
    }

    @Test
    void getClosedDays_EmptyList_ReturnsEmptyList() {
        shiftService.setClosedDays(new ArrayList<>());
        List<WeekDay> result = shiftService.getClosedDays();

        assertTrue(result.isEmpty(), "Closed days list should be empty");
    }

    // =============
    //  updateWeek
    // =============

    @Test
    void updateWeek_ValidWeekShifts_UpdatesSuccessfully() {
        LocalDate sunday = LocalDate.of(2024, 1, 7); // Sunday
        WeekShifts week = new WeekShifts(sunday, new HashMap<>());
        shiftService.updateWeek(week);

        WeekShifts retrieved = shiftService.getNWeeksAgo(sunday.plusDays(1)); // Monday of that week

        assertNotNull(retrieved, "Week should be updated and retrievable");
        assertEquals(sunday, retrieved.getDate());
    }

    // =============
    //  updateShift
    // =============

    @Test
    void updateShift_AddDayShift_UpdatesSuccessfully() {
        LocalDate monday = LocalDate.of(2026, 5, 5); // Monday
        LocalDate weekStart = monday.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        WeekShifts emptyWeek = new WeekShifts(weekStart, new HashMap<>());
        shiftService.updateWeek(emptyWeek);

        Shift shift = new Shift(WeekDay.MONDAY, ShiftType.DAY, "manager123");
        shiftService.updateShift(monday, WeekDay.MONDAY, ShiftType.DAY, shift);

        WeekShifts week = shiftService.getNWeeksAgo(monday);
        Shift retrievedShift = week.getShift(WeekDay.MONDAY, ShiftType.DAY);

        assertNotNull(retrievedShift, "Shift should be added");
        assertEquals(ShiftType.DAY, retrievedShift.getShiftType());
        assertEquals("manager123", retrievedShift.getShiftManager());
    }

    @Test
    void updateShift_AddNightShift_UpdatesSuccessfully() {
        LocalDate tuesday = LocalDate.of(2026, 5, 6); // Tuesday
        LocalDate weekStart = tuesday.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        WeekShifts emptyWeek = new WeekShifts(weekStart, new HashMap<>());
        shiftService.updateWeek(emptyWeek);

        Shift shift = new Shift(WeekDay.TUESDAY, ShiftType.EVENING, "manager456");
        shiftService.updateShift(tuesday, WeekDay.TUESDAY, ShiftType.EVENING, shift);

        WeekShifts week = shiftService.getNWeeksAgo(tuesday);
        Shift retrievedShift = week.getShift(WeekDay.TUESDAY, ShiftType.EVENING);

        assertNotNull(retrievedShift, "Night shift should be added");
        assertEquals(ShiftType.EVENING, retrievedShift.getShiftType());
    }

    @Test
    void updateShift_RemoveShiftBySettingNull_RemovesSuccessfully() {
        LocalDate wednesday = LocalDate.of(2026, 5, 7); // Wednesday
        LocalDate weekStart = wednesday.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        WeekShifts emptyWeek = new WeekShifts(weekStart, new HashMap<>());
        shiftService.updateWeek(emptyWeek);

        Shift shift = new Shift(WeekDay.WEDNESDAY, ShiftType.DAY, "manager789");
        shiftService.updateShift(wednesday, WeekDay.WEDNESDAY, ShiftType.DAY, shift);

        // Now remove it
        shiftService.updateShift(wednesday, WeekDay.WEDNESDAY, ShiftType.DAY, Shift.EMPTY_SHIFT);

        WeekShifts week = shiftService.getNWeeksAgo(wednesday);
        Shift retrievedShift = week.getShift(WeekDay.WEDNESDAY, ShiftType.DAY);

        assertNull(retrievedShift, "Shift should be removed");
    }

    // Additional test for getNWeeksAgo with different dates
    @Test
    void getNWeeksAgo_DifferentDaysInSameWeek_ReturnsSameWeek() {
        LocalDate sunday = LocalDate.of(2024, 1, 7);
        LocalDate monday = LocalDate.of(2024, 1, 8);
        WeekShifts weekSun = new WeekShifts(sunday, new HashMap<>());
        shiftService.updateWeek(weekSun);

        WeekShifts weekMon = shiftService.getNWeeksAgo(monday);

        assertEquals(sunday, weekMon.getDate(), "Should return the same week for any day in the week");
    }
}