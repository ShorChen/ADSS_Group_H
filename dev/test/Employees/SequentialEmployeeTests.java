package Employees;

import Employees.Context.SessionManager;
import Employees.DataAccess.Pools.EmployeePool;
import Employees.Domain.Entities.Employee;
import Employees.Domain.Entities.Role;
import Employees.Domain.Entities.ShiftKey;
import Employees.Service.EmployeeService;
import org.junit.jupiter.api.*;
import Employees.Shared.Enums.JobScope;
import Employees.Shared.Enums.SalaryType;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SequentialEmployeeTests {

    private final EmployeeService employeeService = new EmployeeService();

    @BeforeAll
    void setUp() {
        EmployeePool.free();
    }

    @AfterAll
    void tearDown() {
        EmployeePool.free();
    }

    @Test
    @Order(1)
    void step01_addEmployee_ValidNewEmployee() {
        Employee emp = createTestEmployee("123", "Dani", true);
        String currentPassword = employeeService.addEmployee(emp);
        assertFalse(currentPassword.isEmpty(), "Password should not be empty");
        assertTrue(employeeService.containsEmployee("123"));
    }

    @Test
    @Order(2)
    void step02_addEmployee_ExistingId_ThrowsException() {
        Employee emp2 = createTestEmployee("123", "Yossi", true);
        assertThrows(IllegalArgumentException.class, () -> employeeService.addEmployee(emp2));
    }

    @Test
    @Order(3)
    void step03_deactivateEmployee_ActiveEmployee() {
        employeeService.addEmployee(createTestEmployee("111", "Rina", true));
        employeeService.deactivateEmployee("111");
        assertFalse(employeeService.getEmployeeDetails("111").isActive(), "Employee status should be false");
    }

    @Test
    @Order(4)
    void step04_deactivateEmployee_AlreadyInactive_ThrowsException() {
        employeeService.addEmployee(createTestEmployee("222", "Bob", false));
        assertThrows(IllegalArgumentException.class, () -> employeeService.deactivateEmployee("222"));
    }

    @Test
    @Order(5)
    void step05_deactivateEmployee_NonExistent_ReturnsFalse() {
        boolean result = employeeService.deactivateEmployee("999");
        assertFalse(result);
    }

    @Test
    @Order(6)
    void step06_updateAvailability_ValidEmployee() {
        employeeService.addEmployee(createTestEmployee("333", "Shir", true));
        Set<ShiftKey> unavailable = new HashSet<>();
        unavailable.add(new ShiftKey(WeekDay.SUNDAY, ShiftType.EVENING));
        assertDoesNotThrow(() -> employeeService.updateAvailability("333", unavailable, true));
        Employee updated = employeeService.getEmployeeDetails("333");
        assertTrue(updated.isWorkingDoubles());
        assertTrue(updated.getUnavailableShifts().containsKey(WeekDay.SUNDAY));
    }

    @Test
    @Order(7)
    void step07_updateAvailability_NonExistent_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeService.updateAvailability("404", new HashSet<>(), false);
        });
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @Order(8)
    void step08_getEmployeeDetails_ExistingEmployee() {
        Employee emp = createTestEmployee("444", "Alice", true);
        employeeService.addEmployee(emp);
        Employee retrieved = employeeService.getEmployeeDetails("444");
        assertNotNull(retrieved, "Employee should be found");
        assertEquals("444", retrieved.getId());
        assertEquals("Alice", retrieved.getName());
    }

    @Test
    @Order(9)
    void step09_getEmployeeDetails_NonExistent_ReturnsNull() {
        Employee retrieved = employeeService.getEmployeeDetails("9999");
        assertNull(retrieved, "Non-existent employee should return null");
    }

    @Test
    @Order(10)
    void step10_updateEmployee_ExistingEmployee() {
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
    @Order(11)
    void step11_updateEmployee_NonExistent_ReturnsFalse() {
        Employee emp = createTestEmployee("666", "Charlie", true);
        boolean result = employeeService.updateEmployee(emp, "password");
        assertFalse(result, "Update should fail for non-existent employee");
    }

    @Test
    @Order(12)
    void step12_getAvailableEmployees_WithRoles() {
        Employee emp1 = createTestEmployeeWithRoles("777", "David", Role.Cashier);
        Employee emp2 = createTestEmployeeWithRoles("888", "Eve", Role.Cashier);
        employeeService.addEmployee(emp1);
        employeeService.addEmployee(emp2);
        Set<ShiftKey> unavailable = new HashSet<>();
        unavailable.add(new ShiftKey(WeekDay.SUNDAY, ShiftType.DAY));
        employeeService.updateAvailability("888", unavailable, false);
        List<Employee> available = employeeService.getAvailableEmployees(WeekDay.SUNDAY, ShiftType.DAY, Role.Cashier);
        assertEquals(1, available.size(), "Only one employee should be available");
        assertEquals("777", available.getFirst().getId());
    }

    @Test
    @Order(13)
    void step13_getAvailableEmployees_NoEmployeesWithRole_ReturnsEmpty() {
        List<Employee> available = employeeService.getAvailableEmployees(WeekDay.MONDAY, ShiftType.EVENING, Role.MANAGER);
        assertTrue(available.isEmpty(), "No employees with manager role should be available");
    }

    @Test
    @Order(14)
    void step14_containsRole_EmployeeHasRole() {
        Employee emp = createTestEmployeeWithRoles("999", "Frank", Role.Storekeeper);
        employeeService.addEmployee(emp);
        assertTrue(employeeService.containsRole("999", Role.Storekeeper), "Employee should have the storekeeper role");
    }

    @Test
    @Order(15)
    void step15_containsRole_EmployeeDoesNotHaveRole() {
        Employee emp = createTestEmployeeWithRoles("101", "Grace", Role.Cashier);
        employeeService.addEmployee(emp);
        assertFalse(employeeService.containsRole("101", Role.MANAGER), "Employee should not have the manager role");
    }

    @Test
    @Order(16)
    void step16_containsRole_NonExistentEmployee_ThrowsException() {
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
                "None", 12, WeekDay.SUNDAY, false, new HashMap<>(), true, 1
        );
    }
}