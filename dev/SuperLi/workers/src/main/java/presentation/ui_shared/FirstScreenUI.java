package presentation.ui_shared;

public class FirstScreenUI extends View {
    public FirstScreenUI() {
        super(null);
    }

    @Override
    public void display() {
        CreateStoreUI createStoreUI = new CreateStoreUI();
        createStoreUI.display();

        LoginUI loginUI;
        loginUI = new LoginUI();
        loginUI.display();
    }

    @Override
    public void close() {

    }

}