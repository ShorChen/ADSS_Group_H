package Workers.Presentation.UIManager;

import Workers.Context.SessionManager;
import Workers.Presentation.Controller.BranchController;
import Workers.Presentation.DTO.BranchPL;
import Workers.Presentation.UIShared.ViewCLI;
import Workers.Presentation.Utils.Option;

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
                    .append("Back", () -> {
                        SessionManager.unselectBranch();
                        onDismiss.run();
                    })
                    .append("Create Branch", this::createBranch);
            controller.getBranches().forEach(b ->
                    builder.append("Manage " + b.getLocation(), () -> gotoBranch(b))
            );
            displayMenu(builder);
        }
    }

    private void gotoBranch(BranchPL branch) {
        ViewBranchUI viewBranchUI = new ViewBranchUI(this::display);
        SessionManager.setSelectedBranchId(branch.getBranchId());
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
