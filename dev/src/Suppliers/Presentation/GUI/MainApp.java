package Suppliers.Presentation.GUI;

import Suppliers.Presentation.Controller.ControllerFactory;
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
        if (primaryStage.getScene() == null)
            primaryStage.setScene(newScene);
        else {
            Parent root = newScene.getRoot();
            newScene.setRoot(new Pane());
            primaryStage.getScene().setRoot(root);
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--auto")) {
            System.out.println("Starting Headless Automatic Order Execution...");
            try {
                ControllerFactory.getInstance().getAuthController().login("ORD123");
                int count = ControllerFactory.getInstance().getOrderController().executeAutomaticOrders();
                System.out.println("SUCCESS: Generated " + count + " automatic orders for today.");
            } catch (Exception e) {
                System.err.println("CRITICAL ERROR executing automatic orders: " + e.getMessage());
            }
            System.exit(0);
        } else {
            launch(args);
        }
    }
}