package Transportation.Presentation.GUI;

import Core.Navigation.AppNavigator;
import Core.Controller.ControllerFactory;
import Transportation.Presentation.Controller.TransportationController;
import Transportation.Presentation.DTO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;

public class TransportationDashboard {
    private final Scene scene;
    private final TransportationController transportationController;
    private final ObservableList<DestinationPL> draftDestinations;
    private final ListView<String> deliveryListView = new ListView<>();
    private final ListView<String> truckList = new ListView<>();
    private final ListView<String> driverList = new ListView<>();
    private final ListView<String> siteList = new ListView<>();
    private final AppNavigator appNavigator;

    public TransportationDashboard(AppNavigator appNavigator) {
        this.appNavigator = appNavigator;
        transportationController = ControllerFactory.getInstance().getTransportController();
        draftDestinations = FXCollections.observableArrayList();
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> logout());
        HBox topBar = new HBox(15, new Region(), logoutBtn);
        HBox.setHgrow(topBar.getChildren().get(0), Priority.ALWAYS);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #333333;");
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                new Tab("Trucks & Drivers", createResourcesView()),
                new Tab("Sites", createSitesView()),
                new Tab("Deliveries", createDeliveriesView())
        );
        tabPane.getTabs().forEach(t -> t.setClosable(false));
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);
        mainLayout.setCenter(tabPane);
        scene = new Scene(mainLayout, 1000, 700);
        refreshDeliveries();
        refreshResources();
        refreshSites();
    }

    private void logout() {
        try {
            ControllerFactory.getInstance().getAuthController().logout();
            appNavigator.showLoginScreen();
        } catch (Exception ex) {
            showAlert("Logout Error", ex.getMessage());
        }
    }

    private VBox createResourcesView() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        HBox truckBox = new HBox(10);
        TextField licField = new TextField(); licField.setPromptText("License Number");
        TextField modField = new TextField(); modField.setPromptText("Model");
        TextField netField = new TextField(); netField.setPromptText("Net Weight");
        TextField maxField = new TextField(); maxField.setPromptText("Max Weight");
        CheckBox refCheck = new CheckBox("Refrigerated");
        Button addTruckBtn = new Button("Add Truck");
        addTruckBtn.setOnAction(e -> {
            try {
                transportationController.addTruck(licField.getText(), modField.getText(), Double.parseDouble(netField.getText()), Double.parseDouble(maxField.getText()), refCheck.isSelected());
                refreshResources();
            } catch (Exception ex) { showAlert("Error", ex.getMessage()); }
        });
        truckBox.getChildren().addAll(licField, modField, netField, maxField, refCheck, addTruckBtn);
        HBox driverBox = new HBox(10);
        TextField idField = new TextField(); idField.setPromptText("Driver ID");
        TextField nameField = new TextField(); nameField.setPromptText("Name");
        ComboBox<String> typeBox = new ComboBox<>(); typeBox.getItems().addAll("STANDARD", "HEAVY");
        DatePicker expPicker = new DatePicker(); expPicker.setPromptText("License Expiry");
        Button addDriverBtn = new Button("Add Driver");
        addDriverBtn.setOnAction(e -> {
            try {
                transportationController.addDriver(idField.getText(), nameField.getText(), typeBox.getValue(), expPicker.getValue());
                refreshResources();
            } catch (Exception ex) { showAlert("Error", ex.getMessage()); }
        });
        driverBox.getChildren().addAll(idField, nameField, typeBox, expPicker, addDriverBtn);
        layout.getChildren().addAll(new Label("Manage Trucks"), truckBox, truckList, new Label("Manage Drivers"), driverBox, driverList);
        return layout;
    }

    private VBox createSitesView() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        HBox formBox = new HBox(10);
        TextField nameField = new TextField(); nameField.setPromptText("Site Name");
        TextField addrField = new TextField(); addrField.setPromptText("Address");
        TextField contactField = new TextField(); contactField.setPromptText("Contact Person");
        TextField phoneField = new TextField(); phoneField.setPromptText("Phone");
        Button addBtn = new Button("Add Site");
        addBtn.setOnAction(e -> {
            try {
                transportationController.addSite(nameField.getText(), addrField.getText(), contactField.getText(), phoneField.getText());
                refreshSites();
            } catch (Exception ex) { showAlert("Error", ex.getMessage()); }
        });
        formBox.getChildren().addAll(nameField, addrField, contactField, phoneField, addBtn);
        layout.getChildren().addAll(new Label("Manage Sites (Stores/Warehouses)"), formBox, siteList);
        return layout;
    }

    private VBox createDeliveriesView() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        HBox topBox = new HBox(10);
        DatePicker datePicker = new DatePicker(); datePicker.setPromptText("Delivery Date");
        TextField timeField = new TextField(); timeField.setPromptText("Time (HH:MM)");
        TextField originField = new TextField(); originField.setPromptText("Origin Site Name");
        TextField truckField = new TextField(); truckField.setPromptText("Truck License");
        TextField driverField = new TextField(); driverField.setPromptText("Driver ID");
        topBox.getChildren().addAll(datePicker, timeField, originField, truckField, driverField);
        HBox destBox = new HBox(10);
        TextField destField = new TextField(); destField.setPromptText("Destination Site Name");
        Button addDestBtn = new Button("Add Empty Destination");
        ListView<String> draftList = new ListView<>();
        addDestBtn.setOnAction(e -> {
            draftDestinations.add(new DestinationPL(destField.getText(), new ArrayList<>()));
            refreshDraftList(draftList);
        });
        destBox.getChildren().addAll(destField, addDestBtn);
        HBox cargoBox = new HBox(10);
        ComboBox<String> destCombo = new ComboBox<>();
        destCombo.setOnMouseClicked(e -> destCombo.getItems().setAll(draftDestinations.stream().map(DestinationPL::destinationSite).toList()));
        TextField itemField = new TextField(); itemField.setPromptText("Item Name");
        TextField weightField = new TextField(); weightField.setPromptText("Weight");
        TextField qtyField = new TextField(); qtyField.setPromptText("Quantity");
        Button addCargoBtn = new Button("Add Cargo to Selected Dest");
        addCargoBtn.setOnAction(e -> {
            try {
                String target = destCombo.getValue();
                DestinationPL match = draftDestinations.stream().filter(d -> d.destinationSite().equals(target)).findFirst().orElseThrow(() -> new Exception("Select destination"));
                match.items().add(new CargoItemPL(itemField.getText(), Double.parseDouble(weightField.getText()), Integer.parseInt(qtyField.getText())));
                refreshDraftList(draftList);
            } catch (Exception ex) { showAlert("Error", ex.getMessage()); }
        });
        cargoBox.getChildren().addAll(destCombo, itemField, weightField, qtyField, addCargoBtn);
        HBox authBox = new HBox(10);
        TextField managerField = new TextField(); managerField.setPromptText("Authorizing Manager ID");
        Button createBtn = new Button("Finalize & Create Delivery");
        createBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        createBtn.setOnAction(e -> {
            try {
                if (managerField.getText().isEmpty()) throw new IllegalArgumentException("Manager ID is required.");
                transportationController.createDelivery(datePicker.getValue(), timeField.getText(), truckField.getText(), driverField.getText(), originField.getText(), new ArrayList<>(draftDestinations), managerField.getText());
                draftDestinations.clear();
                refreshDraftList(draftList);
                refreshDeliveries();
                showAlert("Success", "Delivery created!");
            } catch (Exception ex) { showAlert("Error", ex.getMessage()); }
        });
        authBox.getChildren().addAll(managerField, createBtn);
        HBox actionBox = new HBox(10);
        TextField modifyIdField = new TextField(); modifyIdField.setPromptText("Delivery ID");
        TextField newTruckField = new TextField(); newTruckField.setPromptText("New Truck");
        TextField newDriverField = new TextField(); newDriverField.setPromptText("New Driver");
        Button replanBtn = getReplanBtn(modifyIdField, newTruckField, newDriverField);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        cancelBtn.setOnAction(e -> {
            try {
                transportationController.cancelDelivery(Integer.parseInt(modifyIdField.getText()));
                refreshDeliveries();
            } catch (Exception ex) { showAlert("Error", ex.getMessage()); }
        });
        actionBox.getChildren().addAll(modifyIdField, newTruckField, newDriverField, replanBtn, cancelBtn);
        layout.getChildren().addAll(new Label("1. Setup Route"), topBox, new Label("2. Build Destinations"), destBox, new Label("3. Load Cargo"), cargoBox, draftList, authBox, new Label("Manage Active/Historical"), actionBox, deliveryListView);
        return layout;
    }

    private Button getReplanBtn(TextField modifyIdField, TextField newTruckField, TextField newDriverField) {
        Button replanBtn = new Button("Replan");
        replanBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        replanBtn.setOnAction(e -> {
            try {
                transportationController.replanDelivery(Integer.parseInt(modifyIdField.getText()), newTruckField.getText(), newDriverField.getText());
                refreshDeliveries();
                showAlert("Success", "Delivery replanned!");
            } catch (Exception ex) { showAlert("Error", ex.getMessage()); }
        });
        return replanBtn;
    }

    private void refreshResources() {
        truckList.getItems().setAll(transportationController.getAllTrucks().stream().map(t -> t.licenseNumber() + " - " + t.model() + " (Max: " + t.maxWeight() + ")").toList());
        driverList.getItems().setAll(transportationController.getAllDrivers().stream().map(d -> d.employeeId() + " - " + d.name() + " (" + d.licenseType() + ")").toList());
    }

    private void refreshSites() {
        siteList.getItems().setAll(transportationController.getAllSites().stream().map(SitePL::siteName).toList());
    }

    private void refreshDraftList(ListView<String> list) {
        list.getItems().setAll(draftDestinations.stream().map(d -> "DEST: " + d.destinationSite() + " | Items: " + d.items().size()).toList());
    }

    private void refreshDeliveries() {
        try {
            deliveryListView.getItems().setAll(transportationController.getAllDeliveries().stream().map(d -> "ID: " + d.deliveryId() + " | Date: " + d.date() + " | Truck: " + d.truckLicense() + " | Status: " + d.status()).toList());
        } catch (Exception ignored) {}
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() { return scene; }
}