package Workers;

import Workers.data_access.pools.ShiftPool;
import Workers.domain.entities.Shift;
import Workers.domain.entities.ShiftKey;
import Workers.domain.services.ShiftService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Workers.shared.enums.ShiftType;
import Workers.shared.enums.WeekDay;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShiftServiceTest {

    private ShiftService shiftService;

    // Test constants
    private final int branchId = 1;
    private final int year = 2026;
    private final int week = 24;
    private final WeekDay day = WeekDay.MONDAY;
    private final ShiftType type = ShiftType.DAY;
    private final String managerId = "MANAGER";

    @BeforeEach
    void setUp() {
        // Reset the singleton instance to ensure a clean slate for the test
        ShiftPool.free();
        shiftService = new ShiftService();
    }

    @AfterEach
    void tearDown() {
        // Clean up the singleton pool state after the test completes
        ShiftPool.free();
    }

    @Test
    void testAddUpdateShiftAndGetShiftsOfWeek() {
        // Arrange
        // Create a real Shift instance. (Pass whatever arguments your Shift constructor requires)
        Shift expectedShift = new Shift(day, type, managerId);

        // Act
        shiftService.addUpdateShift(branchId, year, week, day.toString(), type.toString(), expectedShift);
        Map<ShiftKey, Shift> weekMap = shiftService.getShiftsOfWeek(branchId, year, week);

        // Assert
        assertNotNull(weekMap, "The returned shift map should not be null.");
        assertEquals(1, weekMap.size(), "The map should contain exactly one shift.");

        // Verify the map contains the correct key and value
        ShiftKey expectedKey = new ShiftKey(day, type);
        assertTrue(weekMap.containsKey(expectedKey), "The map should contain the expected ShiftKey.");

        Shift actualShift = weekMap.get(expectedKey);
        assertEquals(expectedShift.getDay(), actualShift.getDay());
        assertEquals(expectedShift.getShiftType(), actualShift.getShiftType());
    }

    @Test
    void testGetShiftsOfWeek_ReturnsEmptyMapWhenNoShiftsExist() {
        // Act
        Map<ShiftKey, Shift> weekMap = shiftService.getShiftsOfWeek(branchId, year, week);

        // Assert
        assertNotNull(weekMap);
        assertTrue(weekMap.isEmpty(), "The shift map should be empty for a week with no scheduled shifts.");
    }

    @Test
    void testCloseShift_ReturnsEmptyMap() {
        // Arrange: Populate the pool with an active shift first
        Shift activeShift = new Shift(day, type, managerId);
        shiftService.addUpdateShift(branchId, year, week, day.toString(), type.toString(), activeShift);

        // Act: Close the shift
        shiftService.closeShift(branchId, year, week, day.toString(), type.toString());
        Map<ShiftKey, Shift> weekMap = shiftService.getShiftsOfWeek(branchId, year, week);

        // Assert
        ShiftKey key = new ShiftKey(day, type);
        assertFalse(weekMap.containsKey(key), "The shift should have been removed");
        assertTrue(weekMap.isEmpty(), "We should have removed all shifts");
    }
}