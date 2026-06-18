package Employees.Presentation.UIShared;

import javafx.scene.Scene;

public abstract class ViewGUI extends View {

    protected Scene scene;
    public ViewGUI(Runnable onDismiss) {
        super(onDismiss);
    }

    public Scene getScene() {
        return scene;
    }
}
