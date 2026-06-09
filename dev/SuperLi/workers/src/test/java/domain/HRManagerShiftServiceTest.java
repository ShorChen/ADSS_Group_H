package domain;

import data_access.pools.ShiftPool;
import domain.entities.Role;
import domain.entities.Shift;
import shared.enums.ShiftType;
import shared.enums.WeekDay;
import domain.services.HRManagerShiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HRManagerShiftServiceTest {

    private HRManagerShiftService hrManagerShiftService;

    @BeforeEach
    void setUp() {
        ShiftPool.Instance().clear();
        hrManagerShiftService = new HRManagerShiftService();
    }

    // ================
    //  setJobsToShift
    // ================

    @Test
    void setJobsToShift_ValidShiftAndJobs_AddsJobsSuccessfully() {
        Shift shift = new Shift(WeekDay.MONDAY, ShiftType.DAY, "manager123");
        Map<Role, Integer> jobs = new HashMap<>();
        jobs.put(Role.Cashier, 2);
        jobs.put(Role.Storekeeper, 1);

        assertDoesNotThrow(() -> {
            hrManagerShiftService.setJobsToShift(shift, jobs);
        });

        assertTrue(shift.getEmployees().containsKey(Role.Cashier), "Cashier role should be added");
        assertTrue(shift.getEmployees().containsKey(Role.Storekeeper), "Storekeeper role should be added");
        assertEquals(2, shift.getEmployees().get(Role.Cashier).size(), "Cashier capacity should be 2");
        assertEquals(1, shift.getEmployees().get(Role.Storekeeper).size(), "Storekeeper capacity should be 1");
    }

    @Test
    void setJobsToShift_NullShift_ThrowsIllegalArgumentException() {
        Map<Role, Integer> jobs = new HashMap<>();
        jobs.put(Role.Cashier, 1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            hrManagerShiftService.setJobsToShift(null, jobs);
        });

        assertEquals("Shift or jobs cannot be null", exception.getMessage());
    }

    @Test
    void setJobsToShift_NullJobs_ThrowsIllegalArgumentException() {
        Shift shift = new Shift(WeekDay.TUESDAY, ShiftType.EVENING, "manager456");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            hrManagerShiftService.setJobsToShift(shift, null);
        });

        assertEquals("Shift or jobs cannot be null", exception.getMessage());
    }

    @Test
    void setJobsToShift_EmptyJobsMap_AddsNothing() {
        Shift shift = new Shift(WeekDay.WEDNESDAY, ShiftType.DAY, "manager789");
        Map<Role, Integer> jobs = new HashMap<>();

        assertDoesNotThrow(() -> {
            hrManagerShiftService.setJobsToShift(shift, jobs);
        });

        assertEquals(1, shift.getEmployees().size(), "Only ShiftManager should be present");
        assertTrue(shift.getEmployees().containsKey(Role.ShiftManager), "ShiftManager should be present");
    }

    @Test
    void setJobsToShift_UpdateExistingJob_IncreasesCapacity() {
        Shift shift = new Shift(WeekDay.THURSDAY, ShiftType.EVENING, "manager101");
        Map<Role, Integer> jobs = new HashMap<>();
        jobs.put(Role.Cashier, 1);

        hrManagerShiftService.setJobsToShift(shift, jobs);

        Map<Role, Integer> updateJobs = new HashMap<>();
        updateJobs.put(Role.Cashier, 3);

        hrManagerShiftService.setJobsToShift(shift, updateJobs);

        assertEquals(3, shift.getEmployees().get(Role.Cashier).size(), "Capacity should be updated to 3");
    }

    @Test
    void setJobsToShift_JobsWithZeroCapacity_AddsWithZeroCapacity() {
        Shift shift = new Shift(WeekDay.FRIDAY, ShiftType.DAY, "manager202");
        Map<Role, Integer> jobs = new HashMap<>();
        jobs.put(Role.Cashier, 0);

        hrManagerShiftService.setJobsToShift(shift, jobs);

        assertTrue(shift.getEmployees().containsKey(Role.Cashier), "Cashier role should be added");
        assertEquals(0, shift.getEmployees().get(Role.Cashier).size(), "Capacity should be 0");
    }

    @Test
    void setJobsToShift_AddShiftManagerRole_UpdatesCapacity() {
        Shift shift = new Shift(WeekDay.SATURDAY, ShiftType.EVENING, "manager303");
        Map<Role, Integer> jobs = new HashMap<>();
        jobs.put(Role.ShiftManager, 2); // ShiftManager already exists with capacity 1

        hrManagerShiftService.setJobsToShift(shift, jobs);

        assertEquals(2, shift.getEmployees().get(Role.ShiftManager).size(), "ShiftManager capacity should be updated to 2");
    }

    @Test
    void setJobsToShift_JobsWithNullValue_ThrowsException() {
        Shift shift = new Shift(WeekDay.SUNDAY, ShiftType.DAY, "manager404");
        Map<Role, Integer> jobs = new HashMap<>();
        jobs.put(Role.Cashier, null); // Null value

        assertThrows(NullPointerException.class, () -> {
            hrManagerShiftService.setJobsToShift(shift, jobs);
        });
    }

    @Test
    void setJobsToShift_LargeCapacity_AddsSuccessfully() {
        Shift shift = new Shift(WeekDay.MONDAY, ShiftType.EVENING, "manager505");
        Map<Role, Integer> jobs = new HashMap<>();
        jobs.put(Role.Storekeeper, 1000); // Large capacity

        assertDoesNotThrow(() -> {
            hrManagerShiftService.setJobsToShift(shift, jobs);
        });

        assertEquals(1000, shift.getEmployees().get(Role.Storekeeper).size(), "Large capacity should be set");
    }
}
