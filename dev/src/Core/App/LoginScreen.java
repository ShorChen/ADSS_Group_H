package Core.App;

import Core.Controller.AuthController;
import Core.Controller.ControllerFactory;
import Core.Domain.Role;
import Core.Navigation.AppNavigator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginScreen {
    private final Scene scene;
    private final AppNavigator appNavigator;

    public LoginScreen(AppNavigator appNavigator) {
        this.appNavigator = appNavigator;
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        Label title = new Label("Super-Lee System Login");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        TextField idField = new TextField();
        idField.setPromptText("Enter Employee ID");
        idField.setMaxWidth(250);
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter Password");
        passField.setMaxWidth(250);
        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-weight: bold;");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        loginBtn.setOnAction(e -> {
            try {
                AuthController authController = ControllerFactory.getInstance().getAuthController();
                Role userRole = authController.login(idField.getText(), passField.getText());
                routeUser(userRole);

            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        layout.getChildren().addAll(title, idField, passField, loginBtn, errorLabel);
        scene = new Scene(layout, 400, 400);
    }

    private void routeUser(Role role) {
        switch (role) {
            case INVENTORY_MANAGER:
                appNavigator.showInventoryDashboard();
                break;
            case SUPPLIER_MANAGER:
                appNavigator.showSuppliersDashboard();
                break;
            case ORDER_MANAGER:
                appNavigator.showOrderDashboard();
                break;
            case TRANSPORTATION_MANAGER:
                appNavigator.showTransportationDashboard();
                break;
            case STORE_MANAGER:
            case BRANCH_MANAGER:
            case HR_MANAGER:
            case DRIVER:
            case EMPLOYEE:
                appNavigator.showEmployeesDashboard();
                break;
            default:
                throw new IllegalStateException("Unknown role: " + role);
        }
    }

    public Scene getScene() {
        return scene;
    }
}