package Workers;

import Workers.Context.SessionManager;
import Workers.DataAccess.Pools.EmployeePool;
import Workers.Domain.DTO.EmployeeSL;
import Workers.Domain.DTO.RoleSL;
import Workers.Domain.DTO.ShiftKey;
import Workers.Service.EmployeeService;
import Workers.Shared.Enums.JobScope;
import Workers.Shared.Enums.SalaryType;
import Workers.Shared.Enums.ShiftType;
import Workers.Shared.Enums.WeekDay;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SequentialEmployeeTests {

    private final EmployeeService employeeService = new EmployeeService();
    private final int branchId = 1;

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
        EmployeeSL emp = createTestEmployee("123", "Dani", true);
        String currentPassword = employeeService.addEmployee(emp);
        assertFalse(currentPassword.isEmpty(), "Password should not be empty");
        assertTrue(employeeService.containsEmployee("123"));
    }

    @Test
    @Order(2)
    void step02_addEmployee_ExistingId_ThrowsException() {
        EmployeeSL emp2 = createTestEmployee("123", "Yossi", true);
        assertThrows(IllegalArgumentException.class, () -> employeeService.addEmployee(emp2));
    }

    @Test
    @Order(3)
    void step03_deactivateEmployee_ActiveEmployee() {
        employeeService.addEmployee(createTestEmployee("111", "Rina", true));
        employeeService.deactivateEmployee(branchId,"111");
        assertFalse(employeeService.getEmployeeDetails("111").isActive(), "Employee status should be false");
    }

    @Test
    @Order(4)
    void step04_deactivateEmployee_AlreadyInactive_ThrowsException() {
        employeeService.addEmployee(createTestEmployee("222", "Bob", false));
        assertThrows(IllegalArgumentException.class, () -> employeeService.deactivateEmployee(branchId,"222"));
    }

    @Test
    @Order(5)
    void step05_deactivateEmployee_NonExistent_ReturnsFalse() {
        boolean result = employeeService.deactivateEmployee(branchId,"999");
        assertFalse(result);
    }

    @Test
    @Order(6)
    void step06_updateAvailability_ValidEmployee() {
        employeeService.addEmployee(createTestEmployee("333", "Shir", true));
        Set<ShiftKey> unavailable = new HashSet<>();
        unavailable.add(new ShiftKey(WeekDay.SUNDAY, ShiftType.EVENING));
        assertDoesNotThrow(() -> employeeService.updateAvailability("333", unavailable, true));
        EmployeeSL updated = employeeService.getEmployeeDetails("333");
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
        EmployeeSL emp = createTestEmployee("444", "Alice", true);
        employeeService.addEmployee(emp);
        EmployeeSL retrieved = employeeService.getEmployeeDetails("444");
        assertNotNull(retrieved, "Employee should be found");
        assertEquals("444", retrieved.getId());
        assertEquals("Alice", retrieved.getName());
    }

    @Test
    @Order(9)
    void step09_getEmployeeDetails_NonExistent_ReturnsNull() {
        EmployeeSL retrieved = employeeService.getEmployeeDetails("9999");
        assertNull(retrieved, "Non-existent employee should return null");
    }

    @Test
    @Order(10)
    void step10_updateEmployee_ExistingEmployee() {
        EmployeeSL emp = createTestEmployee("555", "Bob", true);
        String pass = employeeService.addEmployee(emp);
        EmployeeSL updatedEmp = new EmployeeSL(
                "555", "Bob Updated", "123456", 60.0, SalaryType.HOURLY,
                SessionManager.now(), JobScope.FULL_TIME, new ArrayList<>(),
                "None", 12, WeekDay.SUNDAY,
                false, new HashMap<>(), true, 1
        );
        boolean result = employeeService.updateEmployee(updatedEmp, pass);
        assertTrue(result, "Update should succeed");
        EmployeeSL retrieved = employeeService.getEmployeeDetails("555");
        assertEquals("Bob Updated", retrieved.getName());
        assertEquals(60.0, retrieved.getSalary());
    }

    @Test
    @Order(11)
    void step11_updateEmployee_NonExistent_ReturnsFalse() {
        EmployeeSL emp = createTestEmployee("666", "Charlie", true);
        boolean result = employeeService.updateEmployee(emp, "password");
        assertFalse(result, "Update should fail for non-existent employee");
    }

    @Test
    @Order(12)
    void step12_getAvailableEmployees_WithRoles() {
        EmployeeSL emp1 = createTestEmployeeWithRoles("777", "David", RoleSL.Cashier);
        EmployeeSL emp2 = createTestEmployeeWithRoles("888", "Eve", RoleSL.Cashier);
        employeeService.addEmployee(emp1);
        employeeService.addEmployee(emp2);
        Set<ShiftKey> unavailable = new HashSet<>();
        unavailable.add(new ShiftKey(WeekDay.SUNDAY, ShiftType.DAY));
        employeeService.updateAvailability("888", unavailable, false);
        List<EmployeeSL> available = employeeService.getAvailableEmployees(WeekDay.SUNDAY, ShiftType.DAY, RoleSL.Cashier);
        assertEquals(1, available.size(), "Only one employee should be available");
        assertEquals("777", available.getFirst().getId());
    }

    @Test
    @Order(13)
    void step13_getAvailableEmployees_NoEmployeesWithRole_ReturnsEmpty() {
        List<EmployeeSL> available = employeeService.getAvailableEmployees(WeekDay.MONDAY, ShiftType.EVENING, RoleSL.MANAGER);
        assertTrue(available.isEmpty(), "No employees with manager role should be available");
    }

    @Test
    @Order(14)
    void step14_containsRole_EmployeeHasRole() {
        EmployeeSL emp = createTestEmployeeWithRoles("999", "Frank", RoleSL.Storekeeper);
        employeeService.addEmployee(emp);
        assertTrue(employeeService.containsRole("999", RoleSL.Storekeeper), "Employee should have the storekeeper role");
    }

    @Test
    @Order(15)
    void step15_containsRole_EmployeeDoesNotHaveRole() {
        EmployeeSL emp = createTestEmployeeWithRoles("101", "Grace", RoleSL.Cashier);
        employeeService.addEmployee(emp);
        assertFalse(employeeService.containsRole("101", RoleSL.MANAGER), "Employee should not have the manager role");
    }

    @Test
    @Order(16)
    void step16_containsRole_NonExistentEmployee_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> employeeService.containsRole("202", RoleSL.Cashier));
    }

    private EmployeeSL createTestEmployee(String id, String name, boolean isActive) {
        return new EmployeeSL(
                id, name, "123456", 50.0, SalaryType.HOURLY,
                SessionManager.now(), JobScope.FULL_TIME, new ArrayList<>(),
                "None", 12, WeekDay.SUNDAY, false, new HashMap<>(), isActive, 1
        );
    }

    private EmployeeSL createTestEmployeeWithRoles(String id, String name, RoleSL... roles) {
        return new EmployeeSL(
                id, name, "123456", 50.0, SalaryType.HOURLY,
                SessionManager.now(), JobScope.FULL_TIME, Arrays.asList(roles),
                "None", 12, WeekDay.SUNDAY, false, new HashMap<>(), true, 1
        );
    }
}