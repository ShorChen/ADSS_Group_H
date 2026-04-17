package Suppliers.Presentation.GUI;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Super-Lee Management System");
        showLoginScreen();
        primaryStage.show();
    }

    public static void showLoginScreen() {
        primaryStage.setScene(new LoginScreen().getScene());
    }

    public static void showSupplierManagerDashboard() {
        primaryStage.setScene(new SupplierDashboard().getScene());
    }

    public static void showOrderManagerDashboard() {
        primaryStage.setScene(new OrderDashboard().getScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}