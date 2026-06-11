package presentation.ui_manager;

import presentation.control.BranchController;
import presentation.model.BranchPL;
import presentation.ui_shared.View;
import presentation.ui_shared.ViewCLI;
import presentation.util.Option;

public class ManageBranchesUI extends ViewCLI {
    private boolean open;
    private final BranchController controller;

    public ManageBranchesUI(Runnable onDismiss) {
        super(onDismiss);
        controller = new BranchController();
    }

    @Override
    public void display() {
        open = true;
        while (open) {
            Option.Builder builder = new Option.Builder(" --- Manage Branches --- ")
                    .append("Back", onDismiss)
                    .append("Create Branch", this::createBranch);
            controller.getBranches().forEach(b ->
                    builder.append("Manage " + b.getLocation(), () -> gotoBranch(b))
            );
            displayMenu(builder);
        }
    }

    private void gotoBranch(BranchPL branch) {
        ViewBranchUI viewBranchUI = new ViewBranchUI(branch, this::display);
        close();
        viewBranchUI.display();
    }

    private void createBranch() {
        System.out.println("---Create Branch---");
        String location = getNextLine("Enter Location: ");
        if (!controller.containsBranch(location))
            controller.addBranch(location);
        else System.out.println("Store already has a branch is " + location);
    }

    @Override
    public void close() {
        open = false;
    }
}
