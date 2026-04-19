package tests;

import domain.entities.*;
import domain.enums.*;
import domain.services.HRManagerShiftService;

import java.time.LocalDateTime;
import java.util.*;

public class ShiftPlacementTest {

    public static void main(String[] args) {
        System.out.println("--- Starting Shift Placement Test ---");

        // 1. הכנת תאריך למשמרת (יום ראשון, 3 במאי 2026)
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.MAY, 3); 
        Date sundayDate = cal.getTime();

        // 2. יצירת המשמרת
        Shift sundayMorningShift = new Shift(
                sundayDate, ShiftType.DAY, new HashMap<>(), new HashMap<>(), null, 1L
        );
        
        // 3. הגדרת התפקידים הנדרשים כ-Objects (מחלקה רגילה ולא Enum)
        Role cashierRole = new Role("Cashier"); // שים לב: אם הבנאי שלך שונה, תקן את המחרוזת כאן
        Role managerRole = new Role("Manager");

        Map<Role, Integer> requiredJobs = new HashMap<>();
        requiredJobs.put(cashierRole, 1); // צריכים קופאי 1
        requiredJobs.put(managerRole, 1); // צריכים מנהל 1
        sundayMorningShift.setRequiredJobs(requiredJobs);

        // 4. יצירת רשימת העובדים
        List<Employee> mockEmployees = new ArrayList<>();

        // עובד 1: אליס - קופאית, פעילה ופנויה (אמורה להשתבץ בהצלחה)
        Employee emp1 = createDummyEmployee("111", "Alice", true);
        emp1.getQualifiedRoles().add(cashierRole); 
        mockEmployees.add(emp1);

        // עובד 2: בוב - מנהל, פעיל, אבל הגיש אילוץ שהוא *לא* יכול לעבוד בראשון בבוקר!
        Employee emp2 = createDummyEmployee("222", "Bob", true);
        emp2.getQualifiedRoles().add(managerRole);
        Map<WeekDay, Set<ShiftType>> bobUnavailable = new HashMap<>();
        bobUnavailable.put(WeekDay.SUNDAY, new HashSet<>(Arrays.asList(ShiftType.DAY)));
        emp2.setUnavailableShifts(bobUnavailable);
        mockEmployees.add(emp2);

        // עובד 3: צ'רלי - קופאי, פנוי, אבל הסטטוס שלו הוא "לא פעיל"
        Employee emp3 = createDummyEmployee("333", "Charlie", false);
        emp3.getQualifiedRoles().add(cashierRole);
        mockEmployees.add(emp3);

        // 5. הרצת אלגוריתם השיבוץ!
        HRManagerShiftService service = new HRManagerShiftService();
        List<Shift> mockShifts = new ArrayList<>();
        mockShifts.add(sundayMorningShift);
        
        List<String> warnings = service.placeToShifts(mockEmployees, mockShifts);

        // 6. הדפסת התוצאות לבקרה
        System.out.println("\n--- Placement Results ---");
        System.out.println("Employees successfully assigned to shift:");
        for (Map.Entry<Employee, Role> entry : sundayMorningShift.getEmployees().entrySet()) {
            // אם במחלקה Role אין לך מתודת getName(), תחליף למה שמחזיר את השם אצלך (למשל getTag)
            System.out.println("- " + entry.getKey().getName() + " assigned as " + entry.getValue().getTag());
        }

        System.out.println("\n--- Warnings ---");
        if (warnings.isEmpty()) {
            System.out.println("No warnings. Perfect placement!");
        } else {
            for (String w : warnings) {
                System.out.println(w);
            }
        }
    }

    // פונקציית עזר לייצור עובדים מהיר לבדיקה מבלי להקליד את כל הבנאי הארוך כל פעם
    private static Employee createDummyEmployee(String id, String name, boolean isActive) {
        Employee emp = new Employee(
                id, name, "123456", 50.0, SalaryType.HOURLY, LocalDateTime.now(),
                JobScope.FULL_TIME, new ArrayList<>(), "No constraints", 12,
                WeekDay.SATURDAY, false, new HashMap<>()
        );
        emp.setActive(isActive);
        return emp;
    }
}