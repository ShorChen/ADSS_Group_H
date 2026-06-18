package Employees.Presentation.GUI;

import Core.Controller.ControllerFactory;
import Core.Navigation.AppNavigator;
import Employees.Context.SessionManager;
import Employees.Domain.Entities.Employee;
import Employees.Domain.Service.EmployeeService;
import Employees.Shared.Enums.JobScope;
import Employees.Shared.Enums.SalaryType;
import Employees.Shared.Enums.WeekDay;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkersDashboard {
    private final Scene scene;
    private final AppNavigator appNavigator;
    private final EmployeeService employeeService;

    public WorkersDashboard(AppNavigator appNavigator) {
        this.appNavigator = appNavigator;
        this.employeeService = new EmployeeService();
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> logout());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBar = new HBox(15, spacer, logoutBtn);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #333333;");
        TabPane tabPane = new TabPane();
        Tab employeesTab = new Tab("Employees", createEmployeesView());
        employeesTab.setClosable(false);
        Tab shiftsTab = new Tab("Shifts", createPlaceholderView("Shift Management System"));
        shiftsTab.setClosable(false);
        Tab rolesTab = new Tab("Roles & Store", createPlaceholderView("Store Details & Roles System"));
        rolesTab.setClosable(false);
        tabPane.getTabs().addAll(employeesTab, shiftsTab, rolesTab);
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);
        mainLayout.setCenter(tabPane);
        this.scene = new Scene(mainLayout, 1000, 700);
    }

    private void logout() {
        try {
            ControllerFactory.getInstance().getAuthController().logout();
            appNavigator.showLoginScreen();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Logout Error", ex.getMessage());
        }
    }

    private VBox createPlaceholderView(String title) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        Label label = new Label(title + " coming soon...");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: gray;");
        layout.getChildren().add(label);
        return layout;
    }

    private VBox createEmployeesView() {
        TableView<Employee> employeeTable = new TableView<>();
        ObservableList<Employee> tableData = FXCollections.observableArrayList();
        HBox searchBox = getSearchBox(tableData);
        TableColumn<Employee, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        TableColumn<Employee, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        TableColumn<Employee, String> scopeCol = new TableColumn<>("Job Scope");
        scopeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJobScope().toString()));
        TableColumn<Employee, String> salaryCol = new TableColumn<>("Salary/Hr");
        salaryCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getSalary())));
        TableColumn<Employee, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ? "Active" : "Inactive"));
        employeeTable.getColumns().addAll(List.of(idCol, nameCol, scopeCol, salaryCol, statusCol));
        employeeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        employeeTable.setItems(tableData);
        VBox.setVgrow(employeeTable, Priority.ALWAYS);
        HBox actionBox = getHBox(tableData, employeeTable);
        VBox layout = new VBox(15, searchBox, employeeTable, actionBox);
        layout.setPadding(new Insets(20));
        return layout;
    }

    private HBox getSearchBox(ObservableList<Employee> tableData) {
        TextField searchField = new TextField();
        searchField.setPromptText("Enter Employee ID...");
        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> {
            if (searchField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Required", "Enter an ID.");
                return;
            }
            Employee emp = employeeService.getEmployeeDetails(searchField.getText().trim());
            tableData.clear();
            if (emp != null) tableData.add(emp);
            else showAlert(Alert.AlertType.INFORMATION, "Not Found", "No employee found.");
        });
        HBox searchBox = new HBox(10, new Label("Find Employee:"), searchField, searchBtn);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        return searchBox;
    }

    private HBox getHBox(ObservableList<Employee> tableData, TableView<Employee> employeeTable) {
        Button addBtn = new Button("Add New Employee");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addBtn.setOnAction(e -> openEmployeeForm(null, tableData));
        Button updateBtn = new Button("Edit Selected");
        updateBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        updateBtn.setOnAction(e -> {
            Employee selected = employeeTable.getSelectionModel().getSelectedItem();
            if (selected != null) openEmployeeForm(selected, tableData);
            else showAlert(Alert.AlertType.WARNING, "Error", "Select an employee to edit.");
        });
        Button deactivateBtn = getDeactivateBtn(tableData, employeeTable);
        HBox actionBox = new HBox(15, addBtn, updateBtn, deactivateBtn);
        actionBox.setAlignment(Pos.CENTER);
        return actionBox;
    }

    private Button getDeactivateBtn(ObservableList<Employee> tableData, TableView<Employee> employeeTable) {
        Button deactivateBtn = new Button("Deactivate Selected");
        deactivateBtn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        deactivateBtn.setOnAction(e -> {
            Employee selected = employeeTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    if (employeeService.deactivateEmployee(selected.getId())) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Deactivated.");
                        tableData.clear();
                        tableData.add(employeeService.getEmployeeDetails(selected.getId()));
                    }
                } catch (Exception ex) { showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage()); }
            }
        });
        return deactivateBtn;
    }

    private void openEmployeeForm(Employee existing, ObservableList<Employee> tableData) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(existing == null ? "Add Employee" : "Update Employee");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20)); grid.setHgap(15); grid.setVgap(15);
        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField salaryField = new TextField();
        ComboBox<JobScope> scopeBox = new ComboBox<>(FXCollections.observableArrayList(JobScope.values()));
        ComboBox<SalaryType> salaryTypeBox = new ComboBox<>(FXCollections.observableArrayList(SalaryType.values()));
        TextField passField = new TextField(); passField.setPromptText("Current password to authorize");
        if (existing != null) {
            idField.setText(existing.getId()); idField.setDisable(true);
            nameField.setText(existing.getName());
            salaryField.setText(String.valueOf(existing.getSalary()));
            scopeBox.setValue(existing.getJobScope());
            salaryTypeBox.setValue(existing.getSalaryType());
        }
        grid.addRow(0, new Label("ID:"), idField);
        grid.addRow(1, new Label("Name:"), nameField);
        grid.addRow(2, new Label("Salary:"), salaryField);
        grid.addRow(3, new Label("Scope:"), scopeBox);
        grid.addRow(4, new Label("Salary Type:"), salaryTypeBox);
        if (existing != null) grid.addRow(5, new Label("Auth Password:"), passField);
        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(e -> {
            try {
                Employee emp = new Employee(idField.getText().trim(), nameField.getText().trim(), "temp",
                        Double.parseDouble(salaryField.getText().trim()), salaryTypeBox.getValue(),
                        SessionManager.now(), scopeBox.getValue(), new ArrayList<>(), "None", 12, WeekDay.SUNDAY, false, new HashMap<>(), true, 1);

                if (existing == null) {
                    String genPass = employeeService.addEmployee(emp);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Employee added! Password: " + genPass);
                } else {
                    if (employeeService.updateEmployee(emp, passField.getText())) showAlert(Alert.AlertType.INFORMATION, "Success", "Updated.");
                    else throw new IllegalArgumentException("Invalid password.");
                }
                tableData.clear();
                tableData.add(employeeService.getEmployeeDetails(emp.getId()));
                stage.close();
            } catch (Exception ex) { showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage()); }
        });
        VBox layout = new VBox(20, grid, saveBtn);
        layout.setAlignment(Pos.CENTER); layout.setPadding(new Insets(10));
        stage.setScene(new Scene(layout, 400, existing == null ? 350 : 400));
        stage.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(msg);
        alert.showAndWait();
    }

    @SuppressWarnings("unused")
    public Scene getScene() { return scene; }
}