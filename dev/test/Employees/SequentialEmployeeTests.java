package Employees;

import Core.Domain.Role;
import Core.Domain.SessionManager;
import Employees.Domain.Entities.AvailabilitySubmissionDL;
import Employees.Domain.Entities.EmployeeDL;
import Employees.Domain.Entities.RoleDL;
import Employees.Domain.Entities.ShiftDL;
import Employees.Shared.Enums.JobScope;
import Employees.Shared.Enums.SalaryType;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SequentialEmployeeTests {
    private EmployeeDL currentEmployee;
    private ShiftDL currentShift;
    private RoleDL cashierRole;
    private RoleDL shiftManagerRole;

    @BeforeAll
    void setUp() {
        SessionManager.getInstance().login("ADMIN", Role.HR_MANAGER);
        currentEmployee = new EmployeeDL("EMP123", "Dani", "12-345", 50.0, SalaryType.HOURLY, LocalDateTime.now(), JobScope.FULL_TIME, null, "None", 12, WeekDay.SUNDAY, null, true, 1);
        currentShift = new ShiftDL(1, 1, 2026, 26, LocalDateTime.now(), WeekDay.MONDAY, ShiftType.DAY, new HashMap<>(), new HashMap<>());
        cashierRole = new RoleDL("Cashier");
        shiftManagerRole = new RoleDL("Shift Manager");
    }

    @Test
    @Order(1)
    void test01_addRolesToEmployee_Success() {
        currentEmployee.addQualifiedRoles(cashierRole, shiftManagerRole);
        assertEquals(2, currentEmployee.getQualifiedRoles().size());
        assertEquals("Cashier", currentEmployee.getQualifiedRoles().getFirst().getTag());
    }

    @Test
    @Order(2)
    void test02_addDuplicateRoleToEmployee_Ignored() {
        currentEmployee.addQualifiedRoles(cashierRole);
        assertEquals(2, currentEmployee.getQualifiedRoles().size());
    }

    @Test
    @Order(3)
    void test03_verifyAvailabilityLogic_SpecificShiftFalse() {
        Map<String, Boolean> shiftsMap = new HashMap<>();
        shiftsMap.put("MONDAY_DAY", false);
        AvailabilitySubmissionDL availability = new AvailabilitySubmissionDL(currentEmployee.getId(), shiftsMap, false);
        currentEmployee.setAvailabilitySubmission(availability);
        assertFalse(currentEmployee.getAvailabilitySubmission().getShift("MONDAY_DAY"));
    }

    @Test
    @Order(4)
    void test04_verifyAvailabilityLogic_UnspecifiedShiftDefaultsTrue() {
        assertTrue(currentEmployee.getAvailabilitySubmission().getShift("TUESDAY_EVENING"));
    }

    @Test
    @Order(5)
    void test05_shiftCapacity_SetAndRetrieve() {
        currentShift.setCapacity(cashierRole, 1);
        assertEquals(1, currentShift.getCapacity(cashierRole));
    }

    @Test
    @Order(6)
    void test06_shiftAssignEmployee_Success() {
        currentShift.assignEmployeeToRole(cashierRole, currentEmployee.getId());
        assertTrue(currentShift.doesEmployeeWork(currentEmployee.getId()));
        assertTrue(currentShift.shiftRequiresRole(cashierRole));
    }

    @Test
    @Order(7)
    void test07_shiftAssignEmployee_ExceedsCapacity_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> currentShift.assignEmployeeToRole(cashierRole, "EMP999"));
    }

    @Test
    @Order(8)
    void test08_shiftReplaceEmployee_Success() {
        currentShift.replaceEmployees(cashierRole, currentEmployee.getId(), "EMP555");
        assertTrue(currentShift.doesEmployeeWork("EMP555"));
        assertFalse(currentShift.doesEmployeeWork(currentEmployee.getId()));
    }

    @Test
    @Order(9)
    void test09_shiftReplaceEmployee_NotWorking_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> currentShift.replaceEmployees(cashierRole, "EMP_GHOST", "EMP_NEW"));
    }

    @Test
    @Order(10)
    void test10_verifySessionSecurity() {
        SessionManager.getInstance().logout();
        assertThrows(SecurityException.class, () -> SessionManager.getInstance().getCurrentRole());
    }
}