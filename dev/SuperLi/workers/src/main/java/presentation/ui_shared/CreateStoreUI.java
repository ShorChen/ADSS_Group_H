package presentation.ui_shared;

import context.Debugger;
import context.SessionManager;
import domain.enums.WeekDay;
import presentation.ui_manager.CreateStoreController;
import presentation.util.Option;

import java.util.ArrayList;
import java.util.List;

public class CreateStoreUI extends View {

    private CreateStoreController controller;
    private boolean open = false;
    private List<WeekDay> closeDays = new ArrayList<>();

    public CreateStoreUI() {
        super(null);
        controller = new CreateStoreController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            displayMenu(new Option.Builder("--- Init System ---")
                    .append("Register Store Manager", this::registerManager)
                    .append("Set Closed Days", this::setClosedDays)
                    .append("Debug Mode", this::isDebugMode), "");
        }

    }


    private void isDebugMode() {
        boolean debug = getNextBoolean("Enter debug mode (y/n)");
        if (debug) SessionManager.setDebugger(new Debugger());
    }

    private void setClosedDays() {
        boolean[] set = {true};
        while (set[0]) {
            List<WeekDay> days = new ArrayList<>();
            WeekDay[] values = WeekDay.values();
            for (WeekDay value : values) {
                if (!closeDays.contains(value)) days.add(value);
            }

            Option.Builder builder = new Option.Builder("Select days to close the store at");
            builder.append("Back", () -> set[0] = false);
            days.forEach(day ->
                    builder.append(day.day, () -> closeDays.add(day)));

            displayMenu(builder, "Closed Days: " + closeDays);
            controller.setClosedDays(closeDays);
        }


    }

    private void registerManager() {
        String id = getNextLine("Enter Manager Id:");
        String name = getNextLine("Enter Manager Name:");
        String bankAccount = getNextLine("Enter Manager Bank Account:");
        while (closeDays.isEmpty()) {
            System.out.println("Please close the store for at least 1 day");
            setClosedDays();
        }

        String[] restDay = new String[1];
        Option.Builder builder = new Option.Builder("Select Rest Day");
        closeDays.forEach(day -> builder.append(day.day, () ->
                restDay[0] = day.name()));
        displayMenu(builder, "");


        System.out.println("Your password: " +
                           controller.registerManager(id, name, bankAccount, restDay[0]) +
                           " You can change your password any time you like.");
        close();
    }

    @Override
    public void close() {
        open = false;
    }
}
