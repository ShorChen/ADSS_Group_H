package Core.App;

import Core.Controller.AuthController;
import Core.Controller.ControllerFactory;
import Core.Domain.Role;
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
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        loginButton.setOnAction(e -> {
            try {
                AuthController authController = ControllerFactory.getInstance().getAuthController();
                Role role = authController.login(codeField.getText());
                if (role == Role.SUPPLIER_MANAGER)
                    MainApp.showSupplierManagerDashboard();
                else if (role == Role.ORDER_MANAGER)
                    MainApp.showOrderManagerDashboard();
                else if (role == Role.INVENTORY_MANAGER)
                    MainApp.showInventoryManagerDashboard();
            } catch (Exception ex) {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText(ex.getMessage());
            }
        });
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, codeField, loginButton, errorLabel);
        scene = new Scene(layout, 400, 300);
    }

    public Scene getScene() {
        return scene;
    }
}