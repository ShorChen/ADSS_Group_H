package Employees.Presentation.GUI;

import Core.Controller.ControllerFactory;
import Core.Domain.Role;
import Core.Navigation.AppNavigator;
import Employees.Presentation.Controller.BranchManagerController;
import Employees.Presentation.Controller.EmployeeController;
import Employees.Presentation.Controller.HRController;
import Employees.Presentation.DTO.EmployeePL;
import Employees.Presentation.DTO.ShiftPL;
import Employees.Domain.Entities.EmployeeDL;
import Employees.Domain.Entities.RequestDL;
import Employees.Domain.Entities.ShiftDL;
import Employees.Shared.Enums.JobScope;
import Employees.Shared.Enums.SalaryType;
import Employees.Shared.Enums.ShiftType;
import Employees.Shared.Enums.WeekDay;
import Core.Domain.SessionManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class EmployeesDashboard {
    private final Scene scene;
    private final AppNavigator navigator;

    private final EmployeeController employeeController;
    private final BranchManagerController branchController;
    private final HRController hrController;

    public EmployeesDashboard(AppNavigator navigator) {
        this.navigator = navigator;
        this.employeeController = ControllerFactory.getInstance().getEmployeeController();
        this.branchController = ControllerFactory.getInstance().getBranchManagerController();
        this.hrController = ControllerFactory.getInstance().getHRController();

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(createTopBar());
        mainLayout.setCenter(createTabPane());

        this.scene = new Scene(mainLayout, 1200, 800);
    }

    public Scene getScene() {
        return scene;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setStyle("-fx-background-color: #2c3e50;");

        Role currentRole = SessionManager.getInstance().getCurrentRole();
        Label roleLabel = new Label("Logged in as: " + currentRole.name());
        roleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutBtn.setOnAction(ignore -> {
            ControllerFactory.getInstance().getAuthController().logout();
            navigator.showLoginScreen();
        });

        topBar.getChildren().addAll(roleLabel, logoutBtn);
        return topBar;
    }

    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Role currentRole = SessionManager.getInstance().getCurrentRole();

        tabPane.getTabs().add(createMyProfileTab());

        if (currentRole == Role.BRANCH_MANAGER || currentRole == Role.HR_MANAGER || currentRole == Role.STORE_MANAGER) {
            tabPane.getTabs().add(createBranchScheduleTab());
        }

        if (currentRole == Role.HR_MANAGER || currentRole == Role.STORE_MANAGER) {
            tabPane.getTabs().add(createEmployeeManagementTab());
        }

        return tabPane;
    }

    // ==========================================
    // TAB 1: MY PROFILE (Worker Actions)
    // ==========================================
    private Tab createMyProfileTab() {
        Tab tab = new Tab("My Profile & Actions");
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        // --- Fetch Personal Details ---
        HBox fetchBox = new HBox(10);
        fetchBox.setAlignment(Pos.CENTER_LEFT);
        TextField myIdField = new TextField();
        myIdField.setPromptText("Enter your Employee ID");
        Button loadMeBtn = new Button("Load My Data");
        Label welcomeLabel = new Label();
        welcomeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2980b9;");

        loadMeBtn.setOnAction(ignore -> {
            try {
                EmployeeDL me = employeeController.getMyDetails(myIdField.getText());
                welcomeLabel.setText("Welcome, " + me.getName() + " | Job: " + me.getJobScope().name() + " | Salary: ₪" + me.getSalary());
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });
        fetchBox.getChildren().addAll(myIdField, loadMeBtn, welcomeLabel);

        // --- Request Shift Replacement Form ---
        VBox reqBox = new VBox(10);
        reqBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 15;");
        Label reqTitle = new Label("Request Shift Replacement");
        reqTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        GridPane reqGrid = new GridPane();
        reqGrid.setHgap(10); reqGrid.setVgap(10);
        TextField shiftIdField = new TextField(); shiftIdField.setPromptText("Shift ID");
        TextField newEmpIdField = new TextField(); newEmpIdField.setPromptText("Replacement Employee ID");
        Button submitReqBtn = getSubmitReqBtn(shiftIdField, myIdField, newEmpIdField);

        reqGrid.add(new Label("Target Shift ID:"), 0, 0); reqGrid.add(shiftIdField, 1, 0);
        reqGrid.add(new Label("Replacement Worker ID:"), 0, 1); reqGrid.add(newEmpIdField, 1, 1);
        reqGrid.add(submitReqBtn, 1, 2);
        reqBox.getChildren().addAll(reqTitle, reqGrid);

        layout.getChildren().addAll(new Label("Personal Dashboard"), fetchBox, reqBox);
        tab.setContent(layout);
        return tab;
    }

    private @NotNull Button getSubmitReqBtn(TextField shiftIdField, TextField myIdField, TextField newEmpIdField) {
        Button submitReqBtn = new Button("Submit Request");

        submitReqBtn.setOnAction(ignore -> {
            try {
                // Creating a dummy RequestDL to pass to the backend
                RequestDL req = new RequestDL(0, new ShiftDL(Integer.parseInt(shiftIdField.getText()), 1, 2026, 26, LocalDateTime.now(), WeekDay.SUNDAY, ShiftType.DAY, null, null),
                        myIdField.getText(), newEmpIdField.getText(), "", "WAITING", "WAITING", "WAITING", false);
                employeeController.submitRequest(req);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Replacement Request Submitted!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit request: " + ex.getMessage());
            }
        });
        return submitReqBtn;
    }

    // ==========================================
    // TAB 2: BRANCH SCHEDULE (Manager Actions)
    // ==========================================
    @SuppressWarnings("unchecked")
    private Tab createBranchScheduleTab() {
        Tab tab = new Tab("Branch Schedule");
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        // --- SHIFTS TABLE ---
        TableView<ShiftPL> table = new TableView<>();
        TableColumn<ShiftPL, String> idCol = new TableColumn<>("Shift ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().shiftId())));
        TableColumn<ShiftPL, String> dayCol = new TableColumn<>("Day");
        dayCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().day()));
        TableColumn<ShiftPL, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().shiftType()));
        table.getColumns().addAll(idCol, dayCol, typeCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox loadBox = new HBox(10);
        loadBox.setAlignment(Pos.CENTER_LEFT);
        TextField branchIdField = new TextField("1"); branchIdField.setPromptText("Branch ID"); branchIdField.setMaxWidth(60);
        TextField yearField = new TextField("2026"); yearField.setPromptText("Year"); yearField.setMaxWidth(60);
        TextField weekField = new TextField("26"); weekField.setPromptText("Week"); weekField.setMaxWidth(60);
        Button loadShiftsBtn = new Button("Load Branch Shifts");

        loadShiftsBtn.setOnAction(ignore -> {
            try {
                ObservableList<ShiftPL> shifts = FXCollections.observableArrayList(
                        branchController.getBranchShifts(Integer.parseInt(branchIdField.getText()), Integer.parseInt(yearField.getText()), Integer.parseInt(weekField.getText()))
                );
                table.setItems(shifts);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", ex.getMessage());
            }
        });
        loadBox.getChildren().addAll(new Label("Branch:"), branchIdField, new Label("Year:"), yearField, new Label("Week:"), weekField, loadShiftsBtn);

        // --- ADD SHIFT FORM ---
        HBox addShiftBox = new HBox(10);
        addShiftBox.setAlignment(Pos.CENTER_LEFT);
        ComboBox<WeekDay> dayCombo = new ComboBox<>(FXCollections.observableArrayList(WeekDay.values())); dayCombo.setPromptText("Day");
        ComboBox<ShiftType> typeCombo = new ComboBox<>(FXCollections.observableArrayList(ShiftType.values())); typeCombo.setPromptText("Type");
        Button publishShiftBtn = new Button("Publish New Shift");
        publishShiftBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        publishShiftBtn.setOnAction(ignore -> {
            try {
                ShiftDL newShift = new ShiftDL(0, Integer.parseInt(branchIdField.getText()), Integer.parseInt(yearField.getText()), Integer.parseInt(weekField.getText()),
                        LocalDateTime.now(), dayCombo.getValue(), typeCombo.getValue(), null, null);
                branchController.saveShift(newShift);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Shift Published!");
                loadShiftsBtn.fire(); // Reload table
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });
        addShiftBox.getChildren().addAll(dayCombo, typeCombo, publishShiftBtn);

        layout.getChildren().addAll(new Label("Weekly Shift Management"), loadBox, table, addShiftBox);
        tab.setContent(layout);
        return tab;
    }

    // ==========================================
    // TAB 3: EMPLOYEE MANAGEMENT (HR Actions)
    // ==========================================
    @SuppressWarnings("unchecked")
    private Tab createEmployeeManagementTab() {
        Tab tab = new Tab("HR: Manage Employees");
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        // --- EMPLOYEES TABLE ---
        TableView<EmployeePL> table = new TableView<>();
        TableColumn<EmployeePL, String> idCol = new TableColumn<>("ID"); idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().id()));
        TableColumn<EmployeePL, String> nameCol = new TableColumn<>("Name"); nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().name()));
        TableColumn<EmployeePL, String> scopeCol = new TableColumn<>("Job Scope"); scopeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().jobScope()));
        TableColumn<EmployeePL, String> salaryCol = new TableColumn<>("Salary"); salaryCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().salary())));
        table.getColumns().addAll(idCol, nameCol, scopeCol, salaryCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button loadBtn = new Button("Refresh Corporate Roster");
        loadBtn.setOnAction(ignore -> {
            try {
                ObservableList<EmployeePL> emps = FXCollections.observableArrayList(hrController.getAllEmployees());
                table.setItems(emps);
            } catch (Exception ex) { showAlert(Alert.AlertType.ERROR, "Database Error", ex.getMessage()); }
        });
        loadBtn.fire();

        // --- HIRE EMPLOYEE FORM ---
        GridPane addGrid = new GridPane();
        addGrid.setHgap(10); addGrid.setVgap(10); addGrid.setStyle("-fx-border-color: #bdc3c7; -fx-padding: 15;");
        TextField idField = new TextField(); idField.setPromptText("ID");
        TextField nameField = new TextField(); nameField.setPromptText("Full Name");
        TextField bankField = new TextField(); bankField.setPromptText("Bank Account");
        TextField salField = new TextField(); salField.setPromptText("Salary Amount");
        ComboBox<JobScope> scopeCombo = new ComboBox<>(FXCollections.observableArrayList(JobScope.values())); scopeCombo.setPromptText("Job Scope");
        ComboBox<SalaryType> typeCombo = new ComboBox<>(FXCollections.observableArrayList(SalaryType.values())); typeCombo.setPromptText("Salary Type");
        Button hireBtn = new Button("Hire Employee");
        hireBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");

        hireBtn.setOnAction(ignore -> {
            try {
                EmployeeDL newEmp = new EmployeeDL(idField.getText(), nameField.getText(), bankField.getText(), Double.parseDouble(salField.getText()),
                        typeCombo.getValue(), LocalDateTime.now(), scopeCombo.getValue(), null, "None", 14, WeekDay.SATURDAY, null, true, 1);
                hrController.addUpdateEmployee(newEmp);
                showAlert(Alert.AlertType.INFORMATION, "Success", nameField.getText() + " has been added to the system.");
                loadBtn.fire(); // Reload table
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to hire: " + ex.getMessage());
            }
        });

        addGrid.add(idField, 0, 0); addGrid.add(nameField, 1, 0); addGrid.add(bankField, 2, 0);
        addGrid.add(salField, 0, 1); addGrid.add(typeCombo, 1, 1); addGrid.add(scopeCombo, 2, 1);
        addGrid.add(hireBtn, 0, 2);

        layout.getChildren().addAll(loadBtn, table, new Label("Onboard New Employee"), addGrid);
        tab.setContent(layout);
        return tab;
    }

    // --- GENERIC ALERT SYSTEM ---
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}