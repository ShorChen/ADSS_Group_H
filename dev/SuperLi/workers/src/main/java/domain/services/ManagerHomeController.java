package domain.services;

import context.SessionManager;
import shared.WeekConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManagerHomeController {
    private final StoreDetailsService storeService;

    public ManagerHomeController() {
        storeService = new StoreDetailsService();
    }

    public void setDeadline(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
            if (!dateTime.minusDays(1).plusMinutes(1).isAfter(SessionManager.now()))
                throw new IllegalArgumentException("Deadline date must be at least one day from now");
            SessionManager.setDeadline(dateTime);

        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String issueReport() {
        // todo
        return null;
    }

    public boolean isFirstWeek() {
        LocalDate targetDate = SessionManager.now().toLocalDate();

        int year = targetDate.get(WeekConstants.WEEK_FIELDS.weekBasedYear());
        int week = targetDate.get(WeekConstants.WEEK_FIELDS.weekOfWeekBasedYear());
        int branchId = SessionManager.getCurrentEmployee().getBranchId();

        return storeService.isFirstWeek(branchId, year, week);
    }
}
