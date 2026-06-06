package Core.App;

import Core.Navigation.AppNavigator;
import Inventory.Presentation.GUI.InventoryDashboard;
import Suppliers.Presentation.GUI.OrderDashboard;
import Suppliers.Presentation.GUI.SupplierDashboard;
import Transportation.Presentation.GUI.TransportDashboard;
import Core.Controller.ControllerFactory;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application implements AppNavigator {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Super-Lee Management System");
        primaryStage.setOnCloseRequest(event -> {
            javafx.application.Platform.exit();
            System.exit(0);
        });
        showLoginScreen();
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    @Override
    public void showLoginScreen() {
        LoginScreen login = new LoginScreen(this);
        switchScene(login.getScene());
    }

    @Override
    public void showSupplierManagerDashboard() {
        SupplierDashboard dashboard = new SupplierDashboard(this);
        switchScene(dashboard.getScene());
    }

    @Override
    public void showOrderManagerDashboard() {
        OrderDashboard dashboard = new OrderDashboard(this);
        switchScene(dashboard.getScene());
    }

    @Override
    public void showInventoryManagerDashboard() {
        InventoryDashboard id = new InventoryDashboard(this);
        switchScene(id.getScene());
    }

    @Override
    public void showTransportManagerDashboard() {
        TransportDashboard td = new TransportDashboard(this);
        switchScene(td.getScene());
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