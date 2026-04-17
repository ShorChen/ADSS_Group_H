package Suppliers.Presentation.GUI;

import Suppliers.Presentation.Controller.ControllerFactory;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class OrderDashboard {
    private final Scene scene;

    public OrderDashboard() {
        Label welcome = new Label("Welcome, Order Manager!");
        welcome.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button logoutBtn = new Button("Logout");

        // Wire up the logout button to talk to your backend
        logoutBtn.setOnAction(e -> {
            try {
                ControllerFactory.getInstance().getAuthController().logout();
                MainApp.showLoginScreen(); // Send them back to login
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(20, welcome, logoutBtn);
        layout.setAlignment(Pos.CENTER);
        this.scene = new Scene(layout, 800, 600);
    }

    public Scene getScene() {
        return scene;
    }
}