package Core.App;

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
        layout.setPadding(new Insets(20));
        Label titleLabel = new Label("Super-Lee Management System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        PasswordField codeField = new PasswordField();
        codeField.setPromptText("Enter Access Code");
        codeField.setMaxWidth(200);
        Button loginBtn = new Button("Login");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        loginBtn.setOnAction(e -> {
            try {
                Role role = ControllerFactory.getInstance().getAuthController().login(codeField.getText());
                routeUser(role);
            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage());
            }
        });
        layout.getChildren().addAll(titleLabel, codeField, loginBtn, errorLabel);
        scene = new Scene(layout, 400, 300);
    }

    private void routeUser(Role role) {
        switch (role) {
            case ORDER_MANAGER -> appNavigator.showOrderManagerDashboard();
            case INVENTORY_MANAGER -> appNavigator.showInventoryManagerDashboard();
            case SUPPLIER_MANAGER -> appNavigator.showSupplierManagerDashboard();
            case TRANSPORTATION_MANAGER -> appNavigator.showTransportManagerDashboard();
            default -> throw new IllegalStateException("Unexpected role: " + role);
        }
    }

    public Scene getScene() {
        return scene;
    }
}