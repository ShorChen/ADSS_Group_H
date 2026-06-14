package presentation.ui_shared;

import context.Debugger;
import context.SessionManager;
import domain.entities.store.Branch;
import shared.enums.WeekDay;
import presentation.control.CreateStoreController;
import presentation.model.BranchPL;
import presentation.model.StoreDetailsPL;
import presentation.util.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CreateStoreUI extends ViewCLI {

    private final CreateStoreController controller;
    private boolean open = false;
    private final List<WeekDay> closeDays = new ArrayList<>();
    private final List<Branch> branches;
    private String id;

    public CreateStoreUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new CreateStoreController();
        branches = new ArrayList<>();
    }


    @Override
    public void display() {
        open = true;
        while (open) {
            displayMenu(new Option.Builder("--- Init System ---")
                    .append("Register Store Manager", this::createStore)
                    .append("Load Data", this::loadData)
                    .append("Debug Mode", this::setDebugMode));
        }
    }

    private void loadData() {
        controller.load();
    }

    private void setDebugMode() {
        boolean debug = getNextBoolean("Enter debug mode (y/n)");
        if (debug) SessionManager.setDebugger(new Debugger());
    }

    private void setClosedDays() {
        boolean[] set = {closeDays.size() < 6};
        while (set[0]) {
            List<WeekDay> days = new ArrayList<>();
            WeekDay[] values = WeekDay.values();
            for (WeekDay value : values) {
                if (!closeDays.contains(value)) days.add(value);
            }

            Option.Builder builder = new Option.Builder("Select days to close the store at");
            if (!closeDays.isEmpty())
                builder.append("Back", () -> set[0] = false);
            days.forEach(day ->
                    builder.append(day.day, () -> closeDays.add(day)));

            displayMenu(builder.setEndMessage("Closed Days: " + closeDays));
            controller.setClosedDays(closeDays);
            if (closeDays.size() == 6) set[0] = false;
        }
        System.out.println(closeDays);
        if (closeDays.size() == 6)
            System.out.println("Can not close all days, going back.\n");


    }

    private void createStore() {
        registerManager();
        createBranch();
        AtomicBoolean keep = new AtomicBoolean(true);
        while (keep.get()) {
            displayMenu(
                    new Option.Builder("---Add additional branches---")
                            .append("Back", () -> keep.set(false))
                            .append("Add Branch", this::createBranch)
            );
        }
        controller.setStoreDetails(new StoreDetailsPL(branches, id, closeDays));
        onDismiss.run();
    }


    private void registerManager() {
        id = getNextLine("Enter Manager Id:");
        String name = getNextLine("Enter Manager Name:");
        String bankAccount = getNextLine("Enter Manager Bank Account:");
        if (closeDays.isEmpty()) {
            System.out.println("Please close the store for at least 1 day\n");
            setClosedDays();
        }

        String restDay = selectionMenuOf("Select Rest Day",
                closeDays, d -> d.day, Enum::name);

        System.out.println("Your password: " +
                           controller.registerManager(id, name, bankAccount, restDay) +
                           " You can change your password any time you like.");
    }

    private void createBranch() {
        System.out.println("---Create Branch---");
        String location = getNextLine("Enter Location: ");
        if (!controller.containsBranch(location))
            controller.addBranch(
                    new BranchPL(id, location, new ArrayList<>())
            );
        else System.out.println("Store already has a branch is " + location);
    }

    @Override
    public void close() {
        open = false;
    }
}
