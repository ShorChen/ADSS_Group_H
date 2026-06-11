package domain;

import context.SessionManager;
import data_access.pools.EmployeePool;
import domain.entities.Employee;
import domain.entities.Role;
import domain.entities.ShiftKey;
import domain.services.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import shared.enums.JobScope;
import shared.enums.SalaryType;
import shared.enums.ShiftType;
import shared.enums.WeekDay;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private final EmployeeService employeeService = new EmployeeService();

    @AfterEach
    void setUp() {
        EmployeePool.free();
    }

    // ==============
    //  addEmployee
    // ==============

    @Test
    void addEmployee_ValidNewEmployee_ReturnsGeneratedPasswordAndSaves() {
        Employee emp = createTestEmployee("123", "Dani", true);
        String pass = employeeService.addEmployee(emp);

        assertFalse(pass.isEmpty(), "Password should not be empty");
        assertTrue(employeeService.containsEmployee("123"));
    }

    @Test
    void addEmployee_ExistingId_ThrowsIllegalArgumentException() {
        Employee emp1 = createTestEmployee("123", "Dani", true);
        employeeService.addEmployee(emp1);

        Employee emp2 = createTestEmployee("123", "Yossi", true);
        assertThrows(IllegalArgumentException.class, () -> employeeService.addEmployee(emp2));

    }

    // ===================
    // deactivateEmployee
    // ===================

    @Test
    void deactivateEmployee_ActiveEmployee_ReturnsTrueAndSetsInactive() {
        employeeService.addEmployee(createTestEmployee("111", "Rina", true));

        employeeService.deactivateEmployee("111");
        assertFalse(employeeService.getEmployeeDetails("111").isActive(), "Employee status should be false");
    }

    @Test
    void deactivateEmployee_AlreadyInactiveEmployee_ReturnsFalse() {
        employeeService.addEmployee(createTestEmployee("222", "Bob", false));
        assertThrows(IllegalArgumentException.class, () -> employeeService.deactivateEmployee("222"));
    }

    @Test
    void deactivateEmployee_NonExistentEmployee_ReturnsFalse() {
        boolean result = employeeService.deactivateEmployee("999");
        assertFalse(result);
    }

    // ====================
    //  updateAvailability
    // ====================

    @Test
    void updateAvailability_ValidEmployee_UpdatesSuccessfully() {
        employeeService.addEmployee(createTestEmployee("333", "Shir", true));

        Set<ShiftKey> unavailable = new HashSet<>();
        unavailable.add(new ShiftKey(WeekDay.SUNDAY, ShiftType.EVENING));

        assertDoesNotThrow(() ->
                employeeService.updateAvailability("333", unavailable, true));

        Employee updated = employeeService.getEmployeeDetails("333");
        assertTrue(updated.isWorkingDoubles());
        assertTrue(updated.getUnavailableShifts().containsKey(WeekDay.SUNDAY));
    }

    @Test
    void updateAvailability_NonExistentEmployee_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.updateAvailability("404", new HashSet<>(), false);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    // ====================
    //  getEmployeeDetails
    // ====================

    @Test
    void getEmployeeDetails_ExistingEmployee_ReturnsEmployee() {
        Employee emp = createTestEmployee("444", "Alice", true);
        employeeService.addEmployee(emp);

        Employee retrieved = employeeService.getEmployeeDetails("444");

        assertNotNull(retrieved, "Employee should be found");
        assertEquals("444", retrieved.getId());
        assertEquals("Alice", retrieved.getName());
    }

    @Test
    void getEmployeeDetails_NonExistentEmployee_ReturnsNull() {
        Employee retrieved = employeeService.getEmployeeDetails("999");

        assertNull(retrieved, "Non-existent employee should return null");
    }

    // ================
    //  updateEmployee
    // ================

    @Test
    void updateEmployee_ExistingEmployee_UpdatesSuccessfully() {
        Employee emp = createTestEmployee("555", "Bob", true);
        String pass = employeeService.addEmployee(emp);

        Employee updatedEmp = new Employee(
                "555", "Bob Updated", "123456", 60.0, SalaryType.HOURLY,
                SessionManager.now(), JobScope.FULL_TIME, new ArrayList<>(),
                "None", 12, WeekDay.SUNDAY,
                false, new HashMap<>(), true, 1
        );

        boolean result = employeeService.updateEmployee(updatedEmp, pass);

        assertTrue(result, "Update should succeed");
        Employee retrieved = employeeService.getEmployeeDetails("555");
        assertEquals("Bob Updated", retrieved.getName());
        assertEquals(60.0, retrieved.getSalary());

    }

    @Test
    void updateEmployee_NonExistentEmployee_ReturnsFalse() {
        Employee emp = createTestEmployee("666", "Charlie", true);

        boolean result = employeeService.updateEmployee(emp, "password");

        assertFalse(result, "Update should fail for non-existent employee");
    }

    // =======================
    //  getAvailableEmployees
    // =======================

    @Test
    void getAvailableEmployees_EmployeesWithRoles_ReturnsAvailableOnes() {
        // Create employees with roles
        Employee emp1 = createTestEmployeeWithRoles("777", "David", Role.Cashier);
        Employee emp2 = createTestEmployeeWithRoles("888", "Eve", Role.Cashier);
        employeeService.addEmployee(emp1);
        employeeService.addEmployee(emp2);

        // Make emp2 unavailable on Sunday for DAY shift
        Set<ShiftKey> unavailable = new HashSet<>();
        unavailable.add(new ShiftKey(WeekDay.SUNDAY, ShiftType.DAY));
        employeeService.updateAvailability("888", unavailable, false);

        List<Employee> available = employeeService.getAvailableEmployees(WeekDay.SUNDAY, ShiftType.DAY, Role.Cashier);

        assertEquals(1, available.size(), "Only one employee should be available");
        assertEquals("777", available.getFirst().getId());
    }

    @Test
    void getAvailableEmployees_NoEmployeesWithRole_ReturnsEmptyList() {
        List<Employee> available = employeeService.getAvailableEmployees(WeekDay.MONDAY, ShiftType.EVENING, Role.MANAGER);

        assertTrue(available.isEmpty(), "No employees with manager role should be available");
    }

    // ===============
    //  containsRole
    // ===============

    @Test
    void containsRole_EmployeeHasRole_ReturnsTrue() {
        Employee emp = createTestEmployeeWithRoles("999", "Frank", Role.Storekeeper);
        employeeService.addEmployee(emp);

        boolean result = employeeService.containsRole("999", Role.Storekeeper);

        assertTrue(result, "Employee should have the storekeeper role");
    }

    @Test
    void containsRole_EmployeeDoesNotHaveRole_ReturnsFalse() {
        Employee emp = createTestEmployeeWithRoles("101", "Grace", Role.Cashier);
        employeeService.addEmployee(emp);

        boolean result = employeeService.containsRole("101", Role.MANAGER);

        assertFalse(result, "Employee should not have the manager role");
    }

    @Test
    void containsRole_NonExistentEmployee_ReturnsFalse() {
        assertThrows(IllegalArgumentException.class, () -> employeeService.containsRole("202", Role.Cashier));
    }

    private Employee createTestEmployee(String id, String name, boolean isActive) {
        return new Employee(
                id, name, "123456", 50.0, SalaryType.HOURLY,
                SessionManager.now(), JobScope.FULL_TIME, new ArrayList<>(),
                "None", 12, WeekDay.SUNDAY, false, new HashMap<>(), isActive, 1
        );
    }

    private Employee createTestEmployeeWithRoles(String id, String name, Role... roles) {
        return new Employee(
                id, name, "123456", 50.0, SalaryType.HOURLY,
                SessionManager.now(), JobScope.FULL_TIME, Arrays.asList(roles),
                "None", 12, WeekDay.SUNDAY, false,
                new HashMap<>(), true, 1
        );
    }
}