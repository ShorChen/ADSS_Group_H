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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public Scene getScene() { return scene; }

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
            tabPane.getTabs().add(createPastShiftsTab());
        }
        if (currentRole == Role.HR_MANAGER || currentRole == Role.STORE_MANAGER) {
            tabPane.getTabs().add(createEmployeeManagementTab());
            tabPane.getTabs().add(createGlobalSettingsTab());
        }
        return tabPane;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB 1 – My Profile & Actions
    // ─────────────────────────────────────────────────────────────────────────
    private Tab createMyProfileTab() {
        Tab tab = new Tab("My Profile & Actions");
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        // ── Welcome / load ──
        HBox fetchBox = new HBox(10);
        fetchBox.setAlignment(Pos.CENTER_LEFT);
        Button loadMeBtn = new Button("Load My Data");
        Label welcomeLabel = new Label();
        welcomeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2980b9;");
        loadMeBtn.setOnAction(ignore -> {
            try {
                String myId = SessionManager.getInstance().getLoggedInUserId();
                EmployeeDL me = employeeController.getMyDetails(myId);
                welcomeLabel.setText("Welcome, " + me.getName() + " | Job: " + me.getJobScope().name() + " | Salary: ₪" + me.getSalary());
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });
        fetchBox.getChildren().addAll(loadMeBtn, welcomeLabel);

        // ── Submit Unavailability (shifts I CANNOT work) ──
        VBox availBox = new VBox(10);
        availBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 15;");
        Label availTitle = new Label("Submit Unavailability – Shifts I Cannot Work");
        availTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label availNote = new Label("Select the shift you CANNOT work and submit. If you submit nothing, you are assumed available for all shifts.");
        availNote.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
        availNote.setWrapText(true);

        GridPane availGrid = new GridPane();
        availGrid.setHgap(10);
        availGrid.setVgap(10);

        // Branch/Year/Week for deadline check
        TextField availBranchField = new TextField("1");
        availBranchField.setMaxWidth(55);
        availBranchField.setPromptText("Branch");
        TextField availYearField = new TextField("2026");
        availYearField.setMaxWidth(60);
        availYearField.setPromptText("Year");
        TextField availWeekField = new TextField("27");
        availWeekField.setMaxWidth(55);
        availWeekField.setPromptText("Week");

        ComboBox<String> dayCombo = new ComboBox<>(FXCollections.observableArrayList(
                "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"));
        dayCombo.setPromptText("Select Day");
        ComboBox<String> shiftCombo = new ComboBox<>(FXCollections.observableArrayList("DAY", "EVENING"));
        shiftCombo.setPromptText("Select Shift");

        CheckBox cannotWorkCheck = new CheckBox("I CANNOT work this shift");
        cannotWorkCheck.setSelected(false);
        cannotWorkCheck.setStyle("-fx-font-weight: bold;");

        Label deadlineStatusLabel = new Label();
        deadlineStatusLabel.setStyle("-fx-text-fill: #c0392b;");

        Button checkDeadlineBtn = new Button("Check Deadline");
        checkDeadlineBtn.setOnAction(ignore -> {
            try {
                int branch = Integer.parseInt(availBranchField.getText());
                int year   = Integer.parseInt(availYearField.getText());
                int week   = Integer.parseInt(availWeekField.getText());
                String dl  = branchController.getDeadline(branch, year, week);
                if (dl == null) {
                    deadlineStatusLabel.setText("No deadline set – submission open.");
                    deadlineStatusLabel.setStyle("-fx-text-fill: #27ae60;");
                } else {
                    boolean open = branchController.isAvailabilitySubmissionOpen(branch, year, week);
                    if (open) {
                        deadlineStatusLabel.setText("Submission open until " + dl);
                        deadlineStatusLabel.setStyle("-fx-text-fill: #27ae60;");
                    } else {
                        deadlineStatusLabel.setText("Deadline passed (" + dl + ") – submission closed.");
                        deadlineStatusLabel.setStyle("-fx-text-fill: #c0392b;");
                    }
                }
            } catch (Exception ex) {
                deadlineStatusLabel.setText("Error: " + ex.getMessage());
            }
        });

        Button submitAvailBtn = new Button("Submit Unavailability");
        submitAvailBtn.setOnAction(ignore -> {
            try {
                if (dayCombo.getValue() == null || shiftCombo.getValue() == null)
                    throw new RuntimeException("Please select a day and shift type.");
                int branch = Integer.parseInt(availBranchField.getText());
                int year   = Integer.parseInt(availYearField.getText());
                int week   = Integer.parseInt(availWeekField.getText());
                // Enforce deadline
                boolean open = branchController.isAvailabilitySubmissionOpen(branch, year, week);
                if (!open) {
                    String dl = branchController.getDeadline(branch, year, week);
                    throw new RuntimeException("Availability submission deadline has passed (" + dl + "). Contact your manager.");
                }
                String myId = SessionManager.getInstance().getLoggedInUserId();
                EmployeeDL me = employeeController.getMyDetails(myId);
                // true = available, false = cannot work
                boolean available = !cannotWorkCheck.isSelected();
                Map<String, Boolean> shifts = new HashMap<>();
                shifts.put(dayCombo.getValue() + "_" + shiftCombo.getValue(), available);
                employeeController.updateAvailability(me, shifts);
                String msg = cannotWorkCheck.isSelected()
                        ? "Marked as UNAVAILABLE for " + dayCombo.getValue() + " " + shiftCombo.getValue() + " shift."
                        : "Marked as AVAILABLE for " + dayCombo.getValue() + " " + shiftCombo.getValue() + " shift.";
                showAlert(Alert.AlertType.INFORMATION, "Availability Updated", msg);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        availGrid.add(new Label("Branch:"), 0, 0);
        availGrid.add(availBranchField, 1, 0);
        availGrid.add(new Label("Year:"), 2, 0);
        availGrid.add(availYearField, 3, 0);
        availGrid.add(new Label("Week:"), 4, 0);
        availGrid.add(availWeekField, 5, 0);
        availGrid.add(checkDeadlineBtn, 6, 0);
        availGrid.add(deadlineStatusLabel, 0, 1, 7, 1);
        availGrid.add(new Label("Day:"), 0, 2);
        availGrid.add(dayCombo, 1, 2, 2, 1);
        availGrid.add(new Label("Shift:"), 3, 2);
        availGrid.add(shiftCombo, 4, 2);
        availGrid.add(cannotWorkCheck, 5, 2, 2, 1);
        availGrid.add(submitAvailBtn, 0, 3, 3, 1);
        availBox.getChildren().addAll(availTitle, availNote, availGrid);

        // ── Report Additional Hours (Day shifts only) ──
        VBox addHrsBox = new VBox(10);
        addHrsBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 15;");
        Label addHrsTitle = new Label("Report Additional Hours");
        addHrsTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label addHrsNote = new Label("You may only report additional hours for a DAY shift you worked.");
        addHrsNote.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");

        GridPane hrsGrid = new GridPane();
        hrsGrid.setHgap(10);
        hrsGrid.setVgap(10);
        TextField shiftIdForHrsField = new TextField();
        shiftIdForHrsField.setPromptText("Day Shift ID");
        shiftIdForHrsField.setMaxWidth(100);
        TextField hoursField = new TextField();
        hoursField.setPromptText("Additional Hours");
        hoursField.setMaxWidth(120);
        Button reportHrsBtn = new Button("Report Hours");
        reportHrsBtn.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white;");
        reportHrsBtn.setOnAction(ignore -> {
            try {
                String myId = SessionManager.getInstance().getLoggedInUserId();
                int shiftId = Integer.parseInt(shiftIdForHrsField.getText().trim());
                float hrs   = Float.parseFloat(hoursField.getText().trim());
                employeeController.reportAdditionalHours(myId, shiftId, hrs);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Additional hours reported successfully.");
            } catch (NumberFormatException nfe) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid shift ID and hours.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });
        hrsGrid.add(new Label("Shift ID:"), 0, 0);
        hrsGrid.add(shiftIdForHrsField, 1, 0);
        hrsGrid.add(new Label("Hours:"), 2, 0);
        hrsGrid.add(hoursField, 3, 0);
        hrsGrid.add(reportHrsBtn, 4, 0);
        addHrsBox.getChildren().addAll(addHrsTitle, addHrsNote, hrsGrid);

        // ── Request Shift Replacement ──
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
        Button submitReqBtn = getSubmitReqBtn(shiftIdField, newEmpIdField);
        reqGrid.add(new Label("Target Shift ID:"), 0, 0);
        reqGrid.add(shiftIdField, 1, 0);
        reqGrid.add(new Label("Replacement Worker ID:"), 0, 1);
        reqGrid.add(newEmpIdField, 1, 1);
        reqGrid.add(submitReqBtn, 1, 2);
        reqBox.getChildren().addAll(reqTitle, reqGrid);

        layout.getChildren().addAll(new Label("Personal Dashboard"), fetchBox, availBox, addHrsBox, reqBox);
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        return tab;
    }

    private @NotNull Button getSubmitReqBtn(TextField shiftIdField, TextField newEmpIdField) {
        Button submitReqBtn = new Button("Submit Request");
        submitReqBtn.setOnAction(ignore -> {
            try {
                String myId = SessionManager.getInstance().getLoggedInUserId();
                RequestDL req = new RequestDL(0,
                        new ShiftDL(Integer.parseInt(shiftIdField.getText()), 1, 2026, 26,
                                LocalDateTime.now(), WeekDay.SUNDAY, ShiftType.DAY,
                                new HashMap<>(), new HashMap<>()),
                        myId, newEmpIdField.getText(), "", "WAITING", "WAITING", "WAITING", false);
                employeeController.submitRequest(req);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Replacement Request Submitted!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit request: " + ex.getMessage());
            }
        });
        return submitReqBtn;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB 2 – Branch Schedule (current week)
    // ─────────────────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private Tab createBranchScheduleTab() {
        Tab tab = new Tab("Branch Schedule");
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        // ── Shift table ──
        TableView<ShiftPL> table = new TableView<>();
        TableColumn<ShiftPL, String> idCol  = new TableColumn<>("Shift ID");
        idCol.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().shiftId())));
        TableColumn<ShiftPL, String> dayCol = new TableColumn<>("Day");
        dayCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().day()));
        TableColumn<ShiftPL, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().shiftType()));
        table.getColumns().addAll(idCol, dayCol, typeCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        // ── Load shifts ──
        HBox loadBox = new HBox(10);
        loadBox.setAlignment(Pos.CENTER_LEFT);
        TextField branchIdField = new TextField("1");
        branchIdField.setMaxWidth(60);
        TextField yearField = new TextField("2026");
        yearField.setMaxWidth(60);
        TextField weekField = new TextField("26");
        weekField.setMaxWidth(60);
        Button loadShiftsBtn = new Button("Load Branch Shifts");
        loadShiftsBtn.setOnAction(ignore -> {
            try {
                ObservableList<ShiftPL> shifts = FXCollections.observableArrayList(
                        branchController.getBranchShifts(
                                Integer.parseInt(branchIdField.getText()),
                                Integer.parseInt(yearField.getText()),
                                Integer.parseInt(weekField.getText())));
                table.setItems(shifts);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", ex.getMessage());
            }
        });
        loadBox.getChildren().addAll(new Label("Branch:"), branchIdField,
                new Label("Year:"), yearField, new Label("Week:"), weekField, loadShiftsBtn);

        // ── Set Availability Deadline (must be at least 1 day in future) ──
        VBox deadlineBox = new VBox(8);
        deadlineBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 10;");
        Label dlTitle = new Label("Set Availability Submission Deadline");
        dlTitle.setStyle("-fx-font-weight: bold;");
        Label dlNote = new Label("Deadline must be set at least 1 day in the future.");
        dlNote.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
        HBox dlControls = new HBox(10);
        dlControls.setAlignment(Pos.CENTER_LEFT);
        TextField dYearField = new TextField("2026");
        dYearField.setMaxWidth(60);
        TextField dWeekField = new TextField("27");
        dWeekField.setMaxWidth(55);
        DatePicker deadlinePicker = new DatePicker();
        Button setDeadlineBtn = new Button("Set Deadline");
        setDeadlineBtn.setOnAction(ignore -> {
            try {
                if (deadlinePicker.getValue() == null)
                    throw new RuntimeException("Please select a deadline date.");
                int branch = Integer.parseInt(branchIdField.getText());
                int year   = Integer.parseInt(dYearField.getText());
                int week   = Integer.parseInt(dWeekField.getText());
                LocalDate dl = deadlinePicker.getValue();
                // Validation: must be at least 1 day in future
                branchController.setAvailabilityDeadline(branch, year, week, dl);
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Deadline set to " + dl + " for Year " + year + " Week " + week + ".");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });
        dlControls.getChildren().addAll(new Label("Year:"), dYearField, new Label("Week:"), dWeekField,
                new Label("Deadline Date:"), deadlinePicker, setDeadlineBtn);
        deadlineBox.getChildren().addAll(dlTitle, dlNote, dlControls);

        // ── Publish New Shift with Role Configuration ──
        VBox publishBox = new VBox(10);
        publishBox.setStyle("-fx-border-color: #27ae60; -fx-border-radius: 5; -fx-padding: 15;");
        Label publishTitle = new Label("Publish New Shift");
        publishTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        HBox shiftBasicRow = new HBox(10);
        shiftBasicRow.setAlignment(Pos.CENTER_LEFT);
        ComboBox<WeekDay> publishDayCombo = new ComboBox<>(FXCollections.observableArrayList(WeekDay.values()));
        publishDayCombo.setPromptText("Day");
        ComboBox<ShiftType> publishTypeCombo = new ComboBox<>(FXCollections.observableArrayList(ShiftType.values()));
        publishTypeCombo.setPromptText("Type");
        TextField managerIdField = new TextField();
        managerIdField.setPromptText("Shift Manager Employee ID");
        managerIdField.setMinWidth(180);
        shiftBasicRow.getChildren().addAll(new Label("Day:"), publishDayCombo, new Label("Type:"), publishTypeCombo,
                new Label("Shift Manager ID:"), managerIdField);

        // Dynamic role-requirement rows
        Label rolesLabel = new Label("Additional Required Roles:");
        rolesLabel.setStyle("-fx-font-weight: bold;");
        VBox rolesContainer = new VBox(5); // holds dynamic HBox rows
        List<HBox> roleRows = new ArrayList<>();

        HBox addRoleRow = new HBox(10);
        addRoleRow.setAlignment(Pos.CENTER_LEFT);
        TextField newRoleNameField = new TextField();
        newRoleNameField.setPromptText("Role name (e.g. Cashier)");
        newRoleNameField.setMaxWidth(160);
        TextField newRoleCountField = new TextField("1");
        newRoleCountField.setMaxWidth(50);
        Button addRoleToShiftBtn = new Button("+ Add Role");
        addRoleToShiftBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;");
        addRoleToShiftBtn.setOnAction(ignore -> {
            String rn = newRoleNameField.getText().trim();
            if (rn.isEmpty()) { showAlert(Alert.AlertType.WARNING, "Input", "Enter a role name."); return; }
            int count;
            try { count = Integer.parseInt(newRoleCountField.getText().trim()); }
            catch (NumberFormatException e) { showAlert(Alert.AlertType.WARNING, "Input", "Count must be a number."); return; }
            if (count < 1) { showAlert(Alert.AlertType.WARNING, "Input", "Count must be at least 1."); return; }
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);
            Label lbl = new Label(rn + " × " + count);
            lbl.setStyle("-fx-border-color: #bdc3c7; -fx-padding: 4 8; -fx-background-color: #ecf0f1;");
            Button removeBtn = new Button("✕");
            removeBtn.setStyle("-fx-font-size: 10px;");
            removeBtn.setOnAction(re -> { rolesContainer.getChildren().remove(row); roleRows.remove(row); });
            row.getChildren().addAll(lbl, removeBtn);
            row.setUserData(new String[]{rn, String.valueOf(count)});
            roleRows.add(row);
            rolesContainer.getChildren().add(row);
            newRoleNameField.clear();
            newRoleCountField.setText("1");
        });
        addRoleRow.getChildren().addAll(new Label("Role:"), newRoleNameField, new Label("Count:"), newRoleCountField, addRoleToShiftBtn);

        Button publishShiftBtn = new Button("Publish Shift");
        publishShiftBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        publishShiftBtn.setOnAction(ignore -> {
            try {
                if (publishDayCombo.getValue() == null || publishTypeCombo.getValue() == null)
                    throw new RuntimeException("Please select a day and shift type.");
                if (managerIdField.getText().trim().isEmpty())
                    throw new RuntimeException("Shift Manager ID is required.");

                ShiftDL newShift = new ShiftDL(0,
                        Integer.parseInt(branchIdField.getText()),
                        Integer.parseInt(yearField.getText()),
                        Integer.parseInt(weekField.getText()),
                        LocalDateTime.now(),
                        publishDayCombo.getValue(),
                        publishTypeCombo.getValue(),
                        new HashMap<>(), new HashMap<>());

                // Assign shift manager (always capacity 1)
                RoleDL smRole = new RoleDL("Shift Manager");
                newShift.setCapacity(smRole, 1);
                newShift.assignEmployeeToRole(smRole, managerIdField.getText().trim());

                // Assign all additionally configured roles
                for (HBox row : roleRows) {
                    String[] data = (String[]) row.getUserData();
                    RoleDL role = new RoleDL(data[0]);
                    int cap = Integer.parseInt(data[1]);
                    newShift.setCapacity(role, cap);
                }

                branchController.saveShift(newShift);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Shift published!");
                roleRows.clear();
                rolesContainer.getChildren().clear();
                loadShiftsBtn.fire();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        publishBox.getChildren().addAll(publishTitle, shiftBasicRow, rolesLabel, rolesContainer, addRoleRow, publishShiftBtn);

        // ── Assign Employee to Shift (with force-assign if unavailable) ──
        VBox assignBox = new VBox(10);
        assignBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 15;");
        Label assignTitle = new Label("Place / Assign Employee to Shift");
        assignTitle.setStyle("-fx-font-weight: bold;");
        Label assignNote = new Label("If an employee has declared unavailability you will be warned; you may still force-assign them.");
        assignNote.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
        assignNote.setWrapText(true);
        HBox assignControls = new HBox(10);
        TextField assignRoleField = new TextField();
        assignRoleField.setPromptText("Role (e.g. Cashier)");
        TextField assignEmpIdField = new TextField();
        assignEmpIdField.setPromptText("Employee ID");
        Button assignBtn = getAssignBtn(table, assignRoleField, assignEmpIdField);
        assignControls.getChildren().addAll(assignRoleField, assignEmpIdField, assignBtn);
        assignBox.getChildren().addAll(assignTitle, assignNote, assignControls);

        layout.getChildren().addAll(new Label("Weekly Shift Management"), deadlineBox, loadBox, table, publishBox, assignBox);
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        return tab;
    }

    /**
     * Assign button logic with force-assign fallback when employee is unavailable.
     */
    private @NotNull Button getAssignBtn(TableView<ShiftPL> table, TextField assignRoleField, TextField assignEmpIdField) {
        Button assignBtn = new Button("Assign");
        assignBtn.setOnAction(ignore -> {
            try {
                ShiftPL selected = table.getSelectionModel().getSelectedItem();
                if (selected == null) throw new RuntimeException("Please select a shift from the table first.");
                String empId = assignEmpIdField.getText().trim();
                String roleTag = assignRoleField.getText().trim();
                if (empId.isEmpty() || roleTag.isEmpty()) throw new RuntimeException("Employee ID and Role are required.");

                // Check availability – warn and offer force-assign if unavailable
                boolean available = branchController.isEmployeeAvailableForShift(empId, selected.day(), selected.shiftType());
                if (!available) {
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Unavailability Warning");
                    confirm.setHeaderText("Employee " + empId + " declared unavailability for " + selected.day() + " " + selected.shiftType() + " shift.");
                    confirm.setContentText("No employee is available for this role. Force-assign anyway?");
                    ButtonType forceBtn = new ButtonType("Force Assign", ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    confirm.getButtonTypes().setAll(forceBtn, cancelBtn);
                    java.util.Optional<ButtonType> result = confirm.showAndWait();
                    if (result.isEmpty() || result.get() == cancelBtn) return;
                }

                // Reload full shift state from DB before modifying
                ShiftDL shift = branchController.getShift(selected.branchId(), selected.year(), selected.week(), selected.day(), selected.shiftType());
                RoleDL targetRole = new RoleDL(roleTag);
                // Ensure capacity exists (default 1 if not set)
                if (shift.getCapacity(targetRole) == 0) shift.setCapacity(targetRole, 1);
                shift.assignEmployeeToRole(targetRole, empId);
                branchController.saveShift(shift);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee " + empId + " assigned to role '" + roleTag + "'.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Assignment Error", ex.getMessage());
            }
        });
        return assignBtn;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB 3 – Past Shifts
    // ─────────────────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private Tab createPastShiftsTab() {
        Tab tab = new Tab("Past Shifts");
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label title = new Label("Past Shifts Archive");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TableView<ShiftPL> table = new TableView<>();
        TableColumn<ShiftPL, String> idCol = new TableColumn<>("Shift ID");
        idCol.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().shiftId())));
        TableColumn<ShiftPL, String> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().year())));
        TableColumn<ShiftPL, String> weekCol = new TableColumn<>("Week");
        weekCol.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().week())));
        TableColumn<ShiftPL, String> dayCol = new TableColumn<>("Day");
        dayCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().day()));
        TableColumn<ShiftPL, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().shiftType()));
        TableColumn<ShiftPL, String> dateCol = new TableColumn<>("Start Date/Time");
        dateCol.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().startDate() != null ? d.getValue().startDate().toLocalDate().toString() : "N/A"));
        table.getColumns().addAll(idCol, yearCol, weekCol, dayCol, typeCol, dateCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        HBox loadRow = new HBox(10);
        loadRow.setAlignment(Pos.CENTER_LEFT);
        TextField pastBranchField = new TextField("1");
        pastBranchField.setMaxWidth(60);
        pastBranchField.setPromptText("Branch");
        Button loadPastBtn = new Button("Load All Shifts");
        loadPastBtn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");
        loadPastBtn.setOnAction(ignore -> {
            try {
                ObservableList<ShiftPL> shifts = FXCollections.observableArrayList(
                        branchController.getPastShifts(Integer.parseInt(pastBranchField.getText())));
                table.setItems(shifts);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });
        loadRow.getChildren().addAll(new Label("Branch ID:"), pastBranchField, loadPastBtn);

        Label infoLabel = new Label("Showing all recorded shifts, newest first.");
        infoLabel.setStyle("-fx-text-fill: #7f8c8d;");

        layout.getChildren().addAll(title, loadRow, infoLabel, table);
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        return tab;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB 4 – HR: Manage Employees
    // ─────────────────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private Tab createEmployeeManagementTab() {
        Tab tab = new Tab("HR: Manage Employees");
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        TableView<EmployeePL> table = new TableView<>();
        TableColumn<EmployeePL, String> idCol    = new TableColumn<>("ID");
        idCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().id()));
        TableColumn<EmployeePL, String> nameCol  = new TableColumn<>("Name");
        nameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().name()));
        TableColumn<EmployeePL, String> scopeCol = new TableColumn<>("Job Scope");
        scopeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().jobScope()));
        TableColumn<EmployeePL, String> salCol   = new TableColumn<>("Salary");
        salCol.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().salary())));
        table.getColumns().addAll(idCol, nameCol, scopeCol, salCol);
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

        // Hire box
        VBox hireBox = new VBox(10);
        hireBox.setStyle("-fx-border-color: #bdc3c7; -fx-padding: 15;");
        Label hireTitle = new Label("Onboard New Employee");
        hireTitle.setStyle("-fx-font-weight: bold;");
        GridPane addGrid = new GridPane();
        addGrid.setHgap(10);
        addGrid.setVgap(10);
        TextField idField = new TextField();             idField.setPromptText("ID");
        TextField nameField = new TextField();           nameField.setPromptText("Full Name");
        TextField bankField = new TextField();           bankField.setPromptText("Bank Account");
        TextField salField = new TextField();            salField.setPromptText("Salary Amount");
        ComboBox<JobScope>   scopeCombo = new ComboBox<>(FXCollections.observableArrayList(JobScope.values()));
        scopeCombo.setPromptText("Job Scope");
        ComboBox<SalaryType> typeCombo  = new ComboBox<>(FXCollections.observableArrayList(SalaryType.values()));
        typeCombo.setPromptText("Salary Type");
        TextField constraintsField = new TextField();    constraintsField.setPromptText("Constraints");
        TextField restDaysField = new TextField();       restDaysField.setPromptText("Yearly Rest Days");
        TextField branchIdField = new TextField();       branchIdField.setPromptText("Branch ID");
        Button hireBtn = new Button("Hire Employee");
        hireBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        hireBtn.setOnAction(ignore -> {
            try {
                EmployeeDL newEmp = new EmployeeDL(
                        idField.getText(), nameField.getText(), bankField.getText(),
                        Double.parseDouble(salField.getText()), typeCombo.getValue(),
                        LocalDateTime.now(), scopeCombo.getValue(), new ArrayList<>(),
                        constraintsField.getText().isEmpty() ? "None" : constraintsField.getText(),
                        restDaysField.getText().isEmpty() ? 14 : Integer.parseInt(restDaysField.getText()),
                        WeekDay.SATURDAY, null, true,
                        branchIdField.getText().isEmpty() ? 1 : Integer.parseInt(branchIdField.getText()));
                hrController.addUpdateEmployee(newEmp);
                showAlert(Alert.AlertType.INFORMATION, "Success", nameField.getText() + " has been added to the system.");
                loadBtn.fire();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to hire: " + ex.getMessage());
            }
        });
        addGrid.add(idField, 0, 0);       addGrid.add(nameField, 1, 0);    addGrid.add(bankField, 2, 0);
        addGrid.add(salField, 0, 1);      addGrid.add(typeCombo, 1, 1);    addGrid.add(scopeCombo, 2, 1);
        addGrid.add(constraintsField, 0, 2); addGrid.add(restDaysField, 1, 2); addGrid.add(branchIdField, 2, 2);
        addGrid.add(hireBtn, 0, 3);
        hireBox.getChildren().addAll(hireTitle, addGrid);

        // Update box
        VBox updateBox = new VBox(10);
        updateBox.setStyle("-fx-border-color: #bdc3c7; -fx-padding: 15;");
        Label updateTitle = new Label("Update Existing Employee");
        updateTitle.setStyle("-fx-font-weight: bold;");
        GridPane updateGrid = new GridPane();
        updateGrid.setHgap(10);
        updateGrid.setVgap(10);
        TextField searchIdField = new TextField();       searchIdField.setPromptText("Employee ID");
        Button searchBtn = new Button("Load Data");
        TextField upNameField = new TextField();         upNameField.setPromptText("Name");
        TextField upBankField = new TextField();         upBankField.setPromptText("Bank Account");
        TextField upSalField = new TextField();          upSalField.setPromptText("Salary");
        ComboBox<JobScope>   upScopeCombo = new ComboBox<>(FXCollections.observableArrayList(JobScope.values()));
        upScopeCombo.setPromptText("Job Scope");
        ComboBox<SalaryType> upTypeCombo  = new ComboBox<>(FXCollections.observableArrayList(SalaryType.values()));
        upTypeCombo.setPromptText("Salary Type");
        TextField upConstraintsField = new TextField(); upConstraintsField.setPromptText("Constraints");
        TextField upRestDaysField = new TextField();    upRestDaysField.setPromptText("Yearly Rest Days");
        TextField upBranchIdField = new TextField();    upBranchIdField.setPromptText("Branch ID");
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
        updateGrid.add(searchIdField, 0, 0);     updateGrid.add(searchBtn, 1, 0);
        updateGrid.add(upNameField, 0, 1);       updateGrid.add(upBankField, 1, 1);
        updateGrid.add(upSalField, 0, 2);        updateGrid.add(upTypeCombo, 1, 2);
        updateGrid.add(upScopeCombo, 0, 3);      updateGrid.add(upConstraintsField, 1, 3);
        updateGrid.add(upRestDaysField, 0, 4);   updateGrid.add(upBranchIdField, 1, 4);
        updateGrid.add(authPasswordField, 0, 5); updateGrid.add(updateBtn, 1, 5);
        updateBox.getChildren().addAll(updateTitle, updateGrid);

        formsBox.getChildren().addAll(hireBox, updateBox);
        layout.getChildren().addAll(loadBtn, table, formsBox);
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        return tab;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB 5 – Global Settings
    // ─────────────────────────────────────────────────────────────────────────
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