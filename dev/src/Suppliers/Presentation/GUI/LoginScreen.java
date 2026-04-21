package Suppliers.Presentation.GUI;

import Suppliers.Presentation.Controller.AuthController;
import Suppliers.Presentation.Controller.ControllerFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

class LoginScreen {
    private final Scene scene;

    LoginScreen() {
        Label titleLabel = new Label("Super-Lee Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        PasswordField codeField = new PasswordField();
        codeField.setPromptText("Enter your access code...");
        codeField.setMaxWidth(200);
        Button loginButton = new Button("Login");
        Button loadDataButton = new Button("Load Example Data");
        loadDataButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        loginButton.setOnAction(e -> {
            try {
                AuthController authController = ControllerFactory.getInstance().getAuthController();
                String roleName = authController.login(codeField.getText());
                if (roleName.equals("SUPPLIER_MANAGER"))
                    MainApp.showSupplierManagerDashboard();
                else if (roleName.equals("ORDER_MANAGER"))
                    MainApp.showOrderManagerDashboard();
            } catch (Exception ex) {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText(ex.getMessage());
            }
        });
        loadDataButton.setOnAction(e -> {
            try {
                ControllerFactory.getInstance().getSupplierController().loadExampleSuppliers();
                ControllerFactory.getInstance().getOrderController().loadExampleOrders();
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("System data loaded successfully!");
                loadDataButton.setDisable(true);
            } catch (Exception ex) {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText("Error loading data: " + ex.getMessage());
            }
        });
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, codeField, loginButton, loadDataButton, errorLabel);
        scene = new Scene(layout, 400, 300);
    }

    public Scene getScene() {
        return scene;
    }
}