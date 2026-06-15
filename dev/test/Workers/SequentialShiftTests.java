package Workers;

import Workers.data_access.pools.ShiftPool;
import Workers.domain.entities.Shift;
import Workers.domain.entities.ShiftKey;
import Workers.domain.services.ShiftService;
import org.junit.jupiter.api.*;
import Workers.shared.enums.ShiftType;
import Workers.shared.enums.WeekDay;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SequentialShiftTests {

    private ShiftService shiftService;

    private final int branchId = 1;
    private final int year = 2026;
    private final int week = 24;
    private final WeekDay day = WeekDay.MONDAY;
    private final ShiftType type = ShiftType.DAY;

    @BeforeAll
    void setUp() {
        ShiftPool.free();
        shiftService = new ShiftService();
    }

    @AfterAll
    void tearDown() {
        ShiftPool.free();
    }

    @Test
    @Order(1)
    void step01_addUpdateShiftAndGetShiftsOfWeek() {
        String managerId = "MANAGER";
        Shift expectedShift = new Shift(day, type, managerId);
        shiftService.addUpdateShift(branchId, year, week, day.toString(), type.toString(), expectedShift);
        Map<ShiftKey, Shift> weekMap = shiftService.getShiftsOfWeek(branchId, year, week);
        assertNotNull(weekMap, "The returned shift map should not be null.");
        assertEquals(1, weekMap.size(), "The map should contain exactly one shift.");
        ShiftKey expectedKey = new ShiftKey(day, type);
        assertTrue(weekMap.containsKey(expectedKey), "The map should contain the expected ShiftKey.");
        Shift actualShift = weekMap.get(expectedKey);
        assertEquals(expectedShift.getDay(), actualShift.getDay());
        assertEquals(expectedShift.getShiftType(), actualShift.getShiftType());
    }

    @Test
    @Order(2)
    void step02_getShiftsOfWeek_ReturnsEmptyMapForEmptyWeek() {
        Map<ShiftKey, Shift> weekMap = shiftService.getShiftsOfWeek(branchId, year, 25);
        assertNotNull(weekMap);
        assertTrue(weekMap.isEmpty(), "The shift map should be empty for a week with no scheduled shifts.");
    }

    @Test
    @Order(3)
    void step03_closeShift_RemovesShiftSuccessfully() {
        shiftService.closeShift(branchId, year, week, day.toString(), type.toString());
        Map<ShiftKey, Shift> weekMap = shiftService.getShiftsOfWeek(branchId, year, week);
        ShiftKey key = new ShiftKey(day, type);
        assertFalse(weekMap.containsKey(key), "The shift should have been removed");
        assertTrue(weekMap.isEmpty(), "We should have removed all shifts");
    }
}