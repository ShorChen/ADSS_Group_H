package presentation.ui_shared;

import presentation.control.FirstScreenController;

public class FirstScreenUI extends View {
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
        CreateStoreUI createStoreUI = new CreateStoreUI(this::onStoreCreated);
        createStoreUI.display();
    }

    private void onStoreCreated(){
        controller.setIsFirstStartUpAsFalse();
        login();
    }
    private void login(){
        LoginUI loginUI = new LoginUI(this::display);
        loginUI.display();
    }

    @Override
    public void close() {

    }

}