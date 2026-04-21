import context.SessionManager;
import domain.entities.Employee;
import domain.entities.Role;
import domain.entities.Shift;
import domain.enums.JobScope;
import domain.enums.SalaryType;
import domain.enums.ShiftType;
import domain.enums.WeekDay;
import domain.services.HRManagerShiftService;
import util.BoundedSet;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class ShiftPlacementTest {

    public static void main(String[] args) {
        System.out.println("--- Starting Shift Placement Test ---");

        // 1. הכנת תאריך למשמרת (יום ראשון, 3 במאי 2026)
        LocalDateTime dateTime = LocalDateTime.of(2006, 5, 3, 12, 0);

        // 3. הגדרת התפקידים הנדרשים כ-Objects (מחלקה רגילה ולא Enum)
        Role cashierRole = Role.Cashier; // שים לב: אם הבנאי שלך שונה, תקן את המחרוזת כאן
        Role managerRole = Role.MANAGER;

        Map<Role, BoundedSet<String>> requiredJobs = new HashMap<>();
        requiredJobs.put(cashierRole, new BoundedSet<>(1)); // צריכים קופאי 1
        requiredJobs.put(managerRole, new BoundedSet<>(1)); // צריכים מנהל 1

        // 2. יצירת המשמרת
        Shift sundayMorningShift = new Shift(
                dateTime, WeekDay.SUNDAY, ShiftType.DAY, requiredJobs,
                null
        );

        // 4. יצירת רשימת העובדים
        List<Employee> mockEmployees = new ArrayList<>();

        // עובד 1: אליס - קופאית, פעילה ופנויה (אמורה להשתבץ בהצלחה)
        Employee alice = createDummyEmployee("111", "Alice", true);
        alice.getQualifiedRoles().add(cashierRole);
        sundayMorningShift.assign(cashierRole, alice);

        // עובד 2: בוב - מנהל, פעיל, אבל הגיש אילוץ שהוא *לא* יכול לעבוד בראשון בבוקר!
        Employee bob = createDummyEmployee("222", "Bob", true);
        bob.getQualifiedRoles().add(managerRole);
        Map<WeekDay, Set<ShiftType>> bobUnavailable = new HashMap<>();
        bobUnavailable.put(WeekDay.SUNDAY, new HashSet<>(Arrays.asList(ShiftType.DAY)));
        bob.setUnavailableShifts(bobUnavailable);
        sundayMorningShift.assign(managerRole, bob);
        mockEmployees.add(bob);

        // עובד 3: צ'רלי - קופאי, פנוי, אבל הסטטוס שלו הוא "לא פעיל"
        Employee charlie = createDummyEmployee("333", "Charlie", false);
        charlie.getQualifiedRoles().add(cashierRole);
        sundayMorningShift.assign(cashierRole, charlie);

        // 5. הרצת אלגוריתם השיבוץ!
        HRManagerShiftService service = new HRManagerShiftService();
        List<Shift> mockShifts = new ArrayList<>();
        mockShifts.add(sundayMorningShift);

//        List<String> warnings = service.placeToShifts(mockEmployees, mockShifts);
//
//        // 6. הדפסת התוצאות לבקרה
//        System.out.println("\n--- Placement Results ---");
//        System.out.println("Employees successfully assigned to shift:");
//        for (Map.Entry<Employee, Role> entry : sundayMorningShift.getEmployees().entrySet()) {
//            // אם במחלקה Role אין לך מתודת getName(), תחליף למה שמחזיר את השם אצלך (למשל getTag)
//            System.out.println("- " + entry.getKey().getName() + " assigned as " + entry.getValue().getTag());
//        }
//
//        System.out.println("\n--- Warnings ---");
//        if (warnings.isEmpty()) {
//            System.out.println("No warnings. Perfect placement!");
//        } else {
//            for (String w : warnings) {
//                System.out.println(w);
//            }
//        }
    }

    private static Employee createDummyEmployee(String id, String name, boolean isActive) {
        return new Employee(
                id, name, "123456", 50.0, SalaryType.HOURLY,
                SessionManager.now(),
                JobScope.FULL_TIME, new ArrayList<>(), "No constraints", 12,
                WeekDay.SATURDAY, false, new HashMap<>(), isActive
        );
    }
}