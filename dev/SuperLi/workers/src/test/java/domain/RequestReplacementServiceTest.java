package domain;

import data_access.pools.RequestsPool;
import data_access.pools.ShiftPool;
import domain.entities.Request;
import domain.entities.Shift;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import domain.services.RequestReplacementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestReplacementServiceTest {

    private RequestReplacementService service;

    @BeforeEach
    void setUp() {
        ShiftPool.Instance().clear();
        RequestsPool.Instance().clear();
        service = new RequestReplacementService();
    }

    // =============
    //  getShift
    // =============

    @Test
    void getShift_ExistingShift_ReturnsShift() {
        LocalDate monday = LocalDate.of(2024, 1, 8); // Monday
        LocalDate weekStart = monday.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        Shift shift = new Shift(WeekDay.MONDAY, ShiftType.DAY, "manager123");
        ShiftPool.Instance().addDayShift(weekStart, shift.toEntity());

        Shift retrieved = service.getShift(monday, WeekDay.MONDAY, ShiftType.DAY);

        assertNotNull(retrieved);
        assertEquals(ShiftType.DAY, retrieved.getShiftType());
    }

    @Test
    void getShift_NonExistingShift_ThrowsNullPointerException() {
        LocalDate date = LocalDate.of(2024, 1, 8);

        assertThrows(NullPointerException.class, () -> {
            service.getShift(date, WeekDay.MONDAY, ShiftType.DAY);
        });
    }

    // ====================
    //  requestReplacement
    // ====================

    @Test
    void requestReplacement_ValidRequest_ReturnsTrue() {
        Shift shift = new Shift(WeekDay.TUESDAY, ShiftType.EVENING, "manager456");
        Request request = new Request(shift, "emp1", "emp2");

        boolean result = service.requestReplacement(request);

        assertTrue(result);
    }

    @Test
    void requestReplacement_NullRequest_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.requestReplacement(null);
        });
    }

    // =====================
    //  getPendingRequests
    // =====================

    @Test
    void getPendingRequests_ExistingRequest_ReturnsRequest() {
        Shift shift = new Shift(WeekDay.WEDNESDAY, ShiftType.DAY, "manager789");
        Request request = new Request(shift, "emp1", "emp2");
        
        service.requestReplacement(request);

        List<Request> pending = service.getPendingRequests("emp1");

        assertTrue(!pending.isEmpty(), "Should have pending request for emp1");
        assertTrue(pending.stream().anyMatch(r -> r.getPrevEmployee().equals("emp1")));
    }

    @Test
    void getPendingRequests_NoRequests_ReturnsEmptyList() {
        List<Request> pending = service.getPendingRequests("nonexistent");

        assertTrue(pending.isEmpty());
    }

    // ==========
    //  approve
    // ==========

    @Test
    void approve_ValidRequest_DoesNotThrow() {
        Shift shift = new Shift(WeekDay.THURSDAY, ShiftType.EVENING, "manager101");
        Request request = new Request(shift, "emp1", "emp2", "manager101", false, false, true, false);
        service.requestReplacement(request);

        assertDoesNotThrow(() -> {
            service.approve(request, "emp1");
        });
    }

    @Test
    void approve_ValidApprovalByNewEmployee_SetsApproval() {
        Shift shift = new Shift(WeekDay.FRIDAY, ShiftType.DAY, "manager202");
        Request request = new Request(shift, "emp1", "emp2", "manager202", true, false, false, false);

        assertDoesNotThrow(() -> {
            service.approve(request, "emp2");
        });
        // approve() for newEmployee doesn't return true, but sets newApproved flag
        assertTrue(request.isNewApproved());
    }

    @Test
    void approve_DeniedRequest_ReturnsFalse() {
        Shift shift = new Shift(WeekDay.SATURDAY, ShiftType.EVENING, "manager303");
        Request request = new Request(shift, "emp1", "emp2", "manager303", true, true, true, true);

        boolean result = service.approve(request, "emp1");

        assertFalse(result);
    }

    @Test
    void approve_NullRequest_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.approve(null, "emp1"));
    }

    // ========
    //  deny
    // ========

    @Test
    void deny_ValidDenyByPrevEmployee_ReturnsTrue() {
        Shift shift = new Shift(WeekDay.SUNDAY, ShiftType.DAY, "manager404");
        Request request = new Request(shift, "emp1", "emp2", "manager404", true, false, false, false);

        boolean result = service.deny(request, "emp1");

        assertTrue(result);
        assertTrue(request.isDenied());
    }

    @Test
    void deny_AlreadyApprovedRequest_ReturnsFalse() {
        Shift shift = new Shift(WeekDay.MONDAY, ShiftType.EVENING, "manager505");
        Request request = new Request(shift, "emp1", "emp2", "manager505", true, true, true, false);

        boolean result = service.deny(request, "emp1");

        assertFalse(result);
    }

    @Test
    void deny_AlreadyDeniedRequest_ReturnsFalse() {
        Shift shift = new Shift(WeekDay.TUESDAY, ShiftType.DAY, "manager606");
        Request request = new Request(shift, "emp1", "emp2", "manager606", false, false, false, true);

        boolean result = service.deny(request, "emp1");

        assertFalse(result);
    }

    @Test
    void deny_NullRequest_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.deny(null, "emp1");
        });
    }

    // Edge cases
    @Test
    void requestReplacement_RequestWithNullNewEmployee_Succeeds() {
        Shift shift = new Shift(WeekDay.WEDNESDAY, ShiftType.EVENING, "manager707");
        Request request = new Request(shift, "emp1", null);

        boolean result = service.requestReplacement(request);

        assertTrue(result);
    }

    @Test
    void approve_RequestWithNullNewEmployee_IsApproved() {
        Shift shift = new Shift(WeekDay.THURSDAY, ShiftType.DAY, "manager808");

        Request request = new Request(shift, "emp1", null);
        request.approve("manager808");

        boolean result = service.doAllSidesApprove(request);

        assertTrue(result);
        assertTrue(request.isApproved());
    }
}
