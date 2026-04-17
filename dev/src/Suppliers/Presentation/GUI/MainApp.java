package Suppliers.Presentation.GUI;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Super-Lee Management System");
        showLoginScreen();
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void showLoginScreen() {
        LoginScreen login = new LoginScreen();
        switchScene(login.getScene());
    }

    public static void showSupplierManagerDashboard() {
        SupplierDashboard dashboard = new SupplierDashboard();
        switchScene(dashboard.getScene());
    }

    public static void showOrderManagerDashboard() {
        OrderDashboard dashboard = new OrderDashboard();
        switchScene(dashboard.getScene());
    }

    public static void switchScene(Scene newScene) {
        if (primaryStage.getScene() == null) {
            primaryStage.setScene(newScene);
        } else {
            Parent root = newScene.getRoot();
            newScene.setRoot(new Pane());
            primaryStage.getScene().setRoot(root);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}