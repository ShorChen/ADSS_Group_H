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
import Employees.Domain.Entities.RoleDL;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        this.scene = new Scene(mainLayout, 1200, 850);
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
            tabPane.getTabs().add(createGlobalSettingsTab());
        }
        return tabPane;
    }

    private Tab createMyProfileTab() {
        Tab tab = new Tab("My Profile & Actions");
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
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
        VBox availBox = new VBox(10);
        availBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 15;");
        Label availTitle = new Label("Submit Next Week's Availability");
        availTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        GridPane availGrid = new GridPane();
        availGrid.setHgap(10);
        availGrid.setVgap(10);
        ComboBox<String> dayCombo = new ComboBox<>(FXCollections.observableArrayList("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"));
        dayCombo.setPromptText("Select Day");
        ComboBox<String> shiftCombo = new ComboBox<>(FXCollections.observableArrayList("DAY", "EVENING"));
        shiftCombo.setPromptText("Select Shift");
        CheckBox availableCheck = new CheckBox("I am AVAILABLE to work this shift");
        availableCheck.setSelected(true);
        Button submitAvailBtn = new Button("Submit Availability");
        submitAvailBtn.setOnAction(ignore -> {
            try {
                EmployeeDL me = employeeController.getMyDetails(myIdField.getText());
                Map<String, Boolean> shifts = new HashMap<>();
                shifts.put(dayCombo.getValue() + "_" + shiftCombo.getValue(), availableCheck.isSelected());
                employeeController.updateAvailability(me, shifts);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Availability Updated!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update availability: " + ex.getMessage());
            }
        });
        availGrid.add(dayCombo, 0, 0);
        availGrid.add(shiftCombo, 1, 0);
        availGrid.add(availableCheck, 2, 0);
        availGrid.add(submitAvailBtn, 0, 1);
        availBox.getChildren().addAll(availTitle, availGrid);
        VBox reqBox = new VBox(10);
        reqBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 15;");
        Label reqTitle = new Label("Request Shift Replacement");
        reqTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        GridPane reqGrid = new GridPane();
        reqGrid.setHgap(10);
        reqGrid.setVgap(10);
        TextField shiftIdField = new TextField();
        shiftIdField.setPromptText("Shift ID");
        TextField newEmpIdField = new TextField();
        newEmpIdField.setPromptText("Replacement Employee ID");
        Button submitReqBtn = getSubmitReqBtn(shiftIdField, myIdField, newEmpIdField);
        reqGrid.add(new Label("Target Shift ID:"), 0, 0);
        reqGrid.add(shiftIdField, 1, 0);
        reqGrid.add(new Label("Replacement Worker ID:"), 0, 1);
        reqGrid.add(newEmpIdField, 1, 1);
        reqGrid.add(submitReqBtn, 1, 2);
        reqBox.getChildren().addAll(reqTitle, reqGrid);
        layout.getChildren().addAll(new Label("Personal Dashboard"), fetchBox, availBox, reqBox);
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        return tab;
    }

    private @NotNull Button getSubmitReqBtn(TextField shiftIdField, TextField myIdField, TextField newEmpIdField) {
        Button submitReqBtn = new Button("Submit Request");
        submitReqBtn.setOnAction(ignore -> {
            try {
                RequestDL req = new RequestDL(0, new ShiftDL(Integer.parseInt(shiftIdField.getText()), 1, 2026, 26, LocalDateTime.now(), WeekDay.SUNDAY, ShiftType.DAY, new HashMap<>(), new HashMap<>()), myIdField.getText(), newEmpIdField.getText(), "", "WAITING", "WAITING", "WAITING", false);
                employeeController.submitRequest(req);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Replacement Request Submitted!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit request: " + ex.getMessage());
            }
        });
        return submitReqBtn;
    }

    @SuppressWarnings("unchecked")
    private Tab createBranchScheduleTab() {
        Tab tab = new Tab("Branch Schedule");
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        TableView<ShiftPL> table = new TableView<>();
        TableColumn<ShiftPL, String> idCol = new TableColumn<>("Shift ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().shiftId())));
        TableColumn<ShiftPL, String> dayCol = new TableColumn<>("Day");
        dayCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().day()));
        TableColumn<ShiftPL, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().shiftType()));
        table.getColumns().addAll(idCol, dayCol, typeCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        HBox loadBox = new HBox(10);
        loadBox.setAlignment(Pos.CENTER_LEFT);
        TextField branchIdField = new TextField("1");
        branchIdField.setPromptText("Branch");
        branchIdField.setMaxWidth(60);
        TextField yearField = new TextField("2026");
        yearField.setPromptText("Year");
        yearField.setMaxWidth(60);
        TextField weekField = new TextField("26");
        weekField.setPromptText("Week");
        weekField.setMaxWidth(60);
        Button loadShiftsBtn = new Button("Load Branch Shifts");
        loadShiftsBtn.setOnAction(ignore -> {
            try {
                ObservableList<ShiftPL> shifts = FXCollections.observableArrayList(branchController.getBranchShifts(Integer.parseInt(branchIdField.getText()), Integer.parseInt(yearField.getText()), Integer.parseInt(weekField.getText())));
                table.setItems(shifts);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", ex.getMessage());
            }
        });
        loadBox.getChildren().addAll(new Label("Branch:"), branchIdField, new Label("Year:"), yearField, new Label("Week:"), weekField, loadShiftsBtn);
        HBox deadlineBox = new HBox(10);
        deadlineBox.setAlignment(Pos.CENTER_LEFT);
        deadlineBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 10;");
        TextField dYearField = new TextField("2026");
        dYearField.setMaxWidth(60);
        TextField dWeekField = new TextField("26");
        dWeekField.setMaxWidth(60);
        DatePicker deadlinePicker = new DatePicker();
        Button setDeadlineBtn = new Button("Set Deadline");
        setDeadlineBtn.setOnAction(ignore -> {
            try {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Availability deadline set for Year: " + dYearField.getText() + " Week: " + dWeekField.getText());
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });
        deadlineBox.getChildren().addAll(new Label("Set Availability Deadline - Year:"), dYearField, new Label("Week:"), dWeekField, new Label("Date:"), deadlinePicker, setDeadlineBtn);
        HBox addShiftBox = new HBox(10);
        addShiftBox.setAlignment(Pos.CENTER_LEFT);
        ComboBox<WeekDay> dayCombo = new ComboBox<>(FXCollections.observableArrayList(WeekDay.values()));
        dayCombo.setPromptText("Day");
        ComboBox<ShiftType> typeCombo = new ComboBox<>(FXCollections.observableArrayList(ShiftType.values()));
        typeCombo.setPromptText("Type");
        TextField managerIdField = new TextField();
        managerIdField.setPromptText("Shift Manager ID");
        Button publishShiftBtn = new Button("Publish New Shift");
        publishShiftBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        publishShiftBtn.setOnAction(ignore -> {
            try {
                if (managerIdField.getText().trim().isEmpty()) throw new RuntimeException("Shift Manager ID is required to publish a shift.");
                ShiftDL newShift = new ShiftDL(0, Integer.parseInt(branchIdField.getText()), Integer.parseInt(yearField.getText()), Integer.parseInt(weekField.getText()), LocalDateTime.now(), dayCombo.getValue(), typeCombo.getValue(), new HashMap<>(), new HashMap<>());
                RoleDL smRole = new RoleDL("Shift Manager");
                newShift.setCapacity(smRole, 1);
                newShift.assignEmployeeToRole(smRole, managerIdField.getText());
                branchController.saveShift(newShift);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Shift Published with Manager!");
                loadShiftsBtn.fire();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });
        addShiftBox.getChildren().addAll(dayCombo, typeCombo, managerIdField, publishShiftBtn);
        VBox assignBox = new VBox(10);
        assignBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 15;");
        Label assignTitle = new Label("Place / Assign Employee to Shift");
        assignTitle.setStyle("-fx-font-weight: bold;");
        HBox assignControls = new HBox(10);
        TextField assignRoleField = new TextField();
        assignRoleField.setPromptText("Role (e.g. Cashier)");
        TextField assignEmpIdField = new TextField();
        assignEmpIdField.setPromptText("Employee ID");
        Button assignBtn = getAssignBtn(table, assignRoleField, assignEmpIdField);
        assignControls.getChildren().addAll(assignRoleField, assignEmpIdField, assignBtn);
        assignBox.getChildren().addAll(assignTitle, assignControls);
        layout.getChildren().addAll(new Label("Weekly Shift Management"), deadlineBox, loadBox, table, addShiftBox, assignBox);
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        return tab;
    }

    private @NotNull Button getAssignBtn(TableView<ShiftPL> table, TextField assignRoleField, TextField assignEmpIdField) {
        Button assignBtn = new Button("Assign");
        assignBtn.setOnAction(ignore -> {
            try {
                ShiftPL selected = table.getSelectionModel().getSelectedItem();
                if (selected == null) throw new RuntimeException("Please select a shift from the table first.");
                ShiftDL shift = branchController.getShift(selected.branchId(), selected.year(), selected.week(), selected.day(), selected.shiftType());
                RoleDL targetRole = new RoleDL(assignRoleField.getText());
                shift.setCapacity(targetRole, 5);
                shift.assignEmployeeToRole(targetRole, assignEmpIdField.getText());
                branchController.saveShift(shift);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee Assigned!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Assignment Error", ex.getMessage());
            }
        });
        return assignBtn;
    }

    @SuppressWarnings("unchecked")
    private Tab createEmployeeManagementTab() {
        Tab tab = new Tab("HR: Manage Employees");
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        TableView<EmployeePL> table = new TableView<>();
        TableColumn<EmployeePL, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().id()));
        TableColumn<EmployeePL, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().name()));
        TableColumn<EmployeePL, String> scopeCol = new TableColumn<>("Job Scope");
        scopeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().jobScope()));
        TableColumn<EmployeePL, String> salaryCol = new TableColumn<>("Salary");
        salaryCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().salary())));
        table.getColumns().addAll(idCol, nameCol, scopeCol, salaryCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
        Button loadBtn = new Button("Refresh Corporate Roster");
        loadBtn.setOnAction(ignore -> {
            try {
                ObservableList<EmployeePL> emps = FXCollections.observableArrayList(hrController.getAllEmployees());
                table.setItems(emps);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", ex.getMessage());
            }
        });
        loadBtn.fire();
        HBox formsBox = new HBox(20);
        VBox hireBox = new VBox(10);
        hireBox.setStyle("-fx-border-color: #bdc3c7; -fx-padding: 15;");
        Label hireTitle = new Label("Onboard New Employee");
        hireTitle.setStyle("-fx-font-weight: bold;");
        GridPane addGrid = new GridPane();
        addGrid.setHgap(10);
        addGrid.setVgap(10);
        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField bankField = new TextField();
        bankField.setPromptText("Bank Account");
        TextField salField = new TextField();
        salField.setPromptText("Salary Amount");
        ComboBox<JobScope> scopeCombo = new ComboBox<>(FXCollections.observableArrayList(JobScope.values()));
        scopeCombo.setPromptText("Job Scope");
        ComboBox<SalaryType> typeCombo = new ComboBox<>(FXCollections.observableArrayList(SalaryType.values()));
        typeCombo.setPromptText("Salary Type");
        TextField constraintsField = new TextField();
        constraintsField.setPromptText("Constraints");
        TextField restDaysField = new TextField();
        restDaysField.setPromptText("Yearly Rest Days");
        CheckBox doublesCheck = new CheckBox("Works Doubles");
        TextField branchIdField = new TextField();
        branchIdField.setPromptText("Branch ID");
        Button hireBtn = new Button("Hire Employee");
        hireBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        hireBtn.setOnAction(ignore -> {
            try {
                EmployeeDL newEmp = new EmployeeDL(idField.getText(), nameField.getText(), bankField.getText(), Double.parseDouble(salField.getText()), typeCombo.getValue(), LocalDateTime.now(), scopeCombo.getValue(), new ArrayList<>(), constraintsField.getText().isEmpty() ? "None" : constraintsField.getText(), restDaysField.getText().isEmpty() ? 14 : Integer.parseInt(restDaysField.getText()), WeekDay.SATURDAY, null, doublesCheck.isSelected(), branchIdField.getText().isEmpty() ? 1 : Integer.parseInt(branchIdField.getText()));
                hrController.addUpdateEmployee(newEmp);
                showAlert(Alert.AlertType.INFORMATION, "Success", nameField.getText() + " has been added to the system.");
                loadBtn.fire();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to hire: " + ex.getMessage());
            }
        });
        addGrid.add(idField, 0, 0);
        addGrid.add(nameField, 1, 0);
        addGrid.add(bankField, 2, 0);
        addGrid.add(salField, 0, 1);
        addGrid.add(typeCombo, 1, 1);
        addGrid.add(scopeCombo, 2, 1);
        addGrid.add(constraintsField, 0, 2);
        addGrid.add(restDaysField, 1, 2);
        addGrid.add(branchIdField, 2, 2);
        addGrid.add(doublesCheck, 0, 3);
        addGrid.add(hireBtn, 1, 3);
        hireBox.getChildren().addAll(hireTitle, addGrid);
        VBox updateBox = new VBox(10);
        updateBox.setStyle("-fx-border-color: #bdc3c7; -fx-padding: 15;");
        Label updateTitle = new Label("Update Existing Employee");
        updateTitle.setStyle("-fx-font-weight: bold;");
        GridPane updateGrid = new GridPane();
        updateGrid.setHgap(10);
        updateGrid.setVgap(10);
        TextField searchIdField = new TextField();
        searchIdField.setPromptText("Employee ID");
        Button searchBtn = new Button("Load Data");
        TextField upNameField = new TextField();
        upNameField.setPromptText("Name");
        TextField upBankField = new TextField();
        upBankField.setPromptText("Bank Account");
        TextField upSalField = new TextField();
        upSalField.setPromptText("Salary");
        ComboBox<JobScope> upScopeCombo = new ComboBox<>(FXCollections.observableArrayList(JobScope.values()));
        upScopeCombo.setPromptText("Job Scope");
        ComboBox<SalaryType> upTypeCombo = new ComboBox<>(FXCollections.observableArrayList(SalaryType.values()));
        upTypeCombo.setPromptText("Salary Type");
        TextField upConstraintsField = new TextField();
        upConstraintsField.setPromptText("Constraints");
        TextField upRestDaysField = new TextField();
        upRestDaysField.setPromptText("Yearly Rest Days");
        CheckBox upDoublesCheck = new CheckBox("Works Doubles");
        TextField upBranchIdField = new TextField();
        upBranchIdField.setPromptText("Branch ID");
        PasswordField authPasswordField = new PasswordField();
        authPasswordField.setPromptText("Employee Password");
        Button updateBtn = new Button("Update Details");
        updateBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;");
        updateBtn.setDisable(true);
        final EmployeeDL[] loadedEmp = new EmployeeDL[1];
        searchBtn.setOnAction(ignore -> {
            try {
                EmployeeDL emp = employeeController.getMyDetails(searchIdField.getText());
                upNameField.setText(emp.getName());
                upBankField.setText(emp.getBankAccount());
                upSalField.setText(String.valueOf(emp.getSalary()));
                upScopeCombo.setValue(emp.getJobScope());
                upTypeCombo.setValue(emp.getSalaryType());
                upConstraintsField.setText(emp.getConstraints());
                upRestDaysField.setText(String.valueOf(emp.getYearlyRestDays()));
                upDoublesCheck.setSelected(emp.isWorkingDoubles());
                upBranchIdField.setText(String.valueOf(emp.getBranchId()));
                loadedEmp[0] = emp;
                updateBtn.setDisable(false);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Employee not found.");
            }
        });
        updateBtn.setOnAction(ignore -> {
            try {
                if (authPasswordField.getText().isEmpty()) throw new RuntimeException("Employee password is required for authorization.");
                EmployeeDL empToUpdate = loadedEmp[0];
                empToUpdate.setName(upNameField.getText());
                empToUpdate.setBankAccount(upBankField.getText());
                empToUpdate.setSalary(Double.parseDouble(upSalField.getText()));
                empToUpdate.setJobScope(upScopeCombo.getValue());
                empToUpdate.setSalaryType(upTypeCombo.getValue());
                empToUpdate.setConstraints(upConstraintsField.getText());
                empToUpdate.setYearlyRestDays(Integer.parseInt(upRestDaysField.getText()));
                empToUpdate.setWorkingDoubles(upDoublesCheck.isSelected());
                empToUpdate.setBranchId(Integer.parseInt(upBranchIdField.getText()));
                hrController.updateEmployeeWithPassword(empToUpdate, authPasswordField.getText());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee updated successfully.");
                loadBtn.fire();
                authPasswordField.clear();
                updateBtn.setDisable(true);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Update Failed", ex.getMessage());
            }
        });
        updateGrid.add(searchIdField, 0, 0);
        updateGrid.add(searchBtn, 1, 0);
        updateGrid.add(upNameField, 0, 1);
        updateGrid.add(upBankField, 1, 1);
        updateGrid.add(upSalField, 0, 2);
        updateGrid.add(upTypeCombo, 1, 2);
        updateGrid.add(upScopeCombo, 0, 3);
        updateGrid.add(upConstraintsField, 1, 3);
        updateGrid.add(upRestDaysField, 0, 4);
        updateGrid.add(upBranchIdField, 1, 4);
        updateGrid.add(upDoublesCheck, 0, 5);
        updateGrid.add(authPasswordField, 0, 6);
        updateGrid.add(updateBtn, 1, 6);
        updateBox.getChildren().addAll(updateTitle, updateGrid);
        formsBox.getChildren().addAll(hireBox, updateBox);
        layout.getChildren().addAll(loadBtn, table, formsBox);
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        return tab;
    }

    private Tab createGlobalSettingsTab() {
        Tab tab = new Tab("Store Settings & Roles");
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        VBox roleBox = new VBox(10);
        roleBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 15;");
        Label roleTitle = new Label("Add System Role");
        roleTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        HBox roleControls = new HBox(10);
        TextField newRoleField = new TextField();
        newRoleField.setPromptText("Enter New Role (e.g. Cleaner)");
        Button addRoleBtn = getAddRoleBtn(newRoleField);
        roleControls.getChildren().addAll(newRoleField, addRoleBtn);
        roleBox.getChildren().addAll(roleTitle, roleControls);
        layout.getChildren().addAll(new Label("Global HR Settings"), roleBox);
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        return tab;
    }

    private @NotNull Button getAddRoleBtn(TextField newRoleField) {
        Button addRoleBtn = new Button("Add Role");
        addRoleBtn.setOnAction(ignore -> {
            try {
                if (newRoleField.getText().trim().isEmpty()) throw new Exception("Role name cannot be empty.");
                hrController.addRole(newRoleField.getText().trim());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Role '" + newRoleField.getText() + "' registered in the database.");
                newRoleField.clear();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });
        return addRoleBtn;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}