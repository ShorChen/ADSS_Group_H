package Employees.Presentation.UIShared;

import Employees.Presentation.Controller.FirstScreenController;

public class FirstScreenUI extends ViewCLI {
    private final FirstScreenController controller;

    public FirstScreenUI() {
        super(null);
        controller = new FirstScreenController();
    }

    @Override
    public void display() {
        if (controller.isFirstStarUp()) createStore();
        else login();

    }

    private void createStore() {
        CreateStoreUI createStoreUI = new CreateStoreUI(this::login);
        createStoreUI.display();
    }

    private void login(){
        LoginUI loginUI = new LoginUI(this::display);
        loginUI.display();
    }

    @Override
    public void close() {

    }

}