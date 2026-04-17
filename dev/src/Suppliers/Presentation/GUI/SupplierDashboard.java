package Suppliers.Presentation.GUI;

import Suppliers.Presentation.Controller.ControllerFactory;
import Suppliers.Presentation.Controller.SupplierController;
import Suppliers.Presentation.SupplierPL;
import Suppliers.Presentation.ContactPersonPL;
import Suppliers.Presentation.AgreementPL;
import Suppliers.Presentation.ProductLinePL;
import Suppliers.Presentation.DiscountBracketPL;
import Suppliers.Presentation.DeliveryTermsPL;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class SupplierDashboard {
    private final Scene scene;
    private final SupplierController supplierController;
    private ListView<SupplierPL> supplierListView;
    private VBox detailsPane;
    private TabPane mainTabPane;
    private String currentSupplierId = null;
    private Integer activeAgreementId = null;
    private Integer activeProductId = null;
    private boolean isRebuilding = false;

    public SupplierDashboard() {
        this.supplierController = ControllerFactory.getInstance().getSupplierController();
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> logout());
        Button addSupplierBtn = new Button("+ Add New Supplier");
        addSupplierBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addSupplierBtn.setOnAction(e -> showAddSupplierDialog());
        HBox topBar = new HBox(15, addSupplierBtn, logoutBtn);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #333333;");
        supplierListView = new ListView<>();
        supplierListView.setPrefWidth(250);
        supplierListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(SupplierPL item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getName() + " (" + item.getBusinessNumber() + ")");
            }
        });
        supplierListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (currentSupplierId == null || !currentSupplierId.equals(newVal.getBusinessNumber())) {
                    activeAgreementId = null;
                    activeProductId = null;
                    currentSupplierId = newVal.getBusinessNumber();
                }
                if (!isRebuilding) populateDetailsPane(newVal);
            }
        });
        detailsPane = new VBox(15);
        detailsPane.setPadding(new Insets(20));
        mainTabPane = new TabPane();
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        VBox.setVgrow(mainTabPane, Priority.ALWAYS);
        Label placeholder = new Label("Select a supplier from the list to view details.");
        detailsPane.getChildren().add(placeholder);
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);
        mainLayout.setLeft(supplierListView);
        mainLayout.setCenter(detailsPane);
        this.scene = new Scene(mainLayout, 1000, 750);
        refreshSupplierList();
    }

    private void refreshSupplierList() {
        try {
            List<SupplierPL> suppliers = supplierController.getAllSuppliers();
            supplierListView.getItems().setAll(suppliers);
        } catch (Exception e) {
            showAlert("Error loading suppliers", e.getMessage());
        }
    }

    private void refreshUI(String businessNumber) {
        isRebuilding = true;
        try {
            int currentTabIndex = 0;
            if (!mainTabPane.getTabs().isEmpty()) currentTabIndex = mainTabPane.getSelectionModel().getSelectedIndex();
            refreshSupplierList();
            for (SupplierPL s : supplierListView.getItems())
                if (s.getBusinessNumber().equals(businessNumber)) {
                    supplierListView.getSelectionModel().select(s);
                    populateDetailsPane(s);
                    mainTabPane.getSelectionModel().select(currentTabIndex);
                    return;
                }
            detailsPane.getChildren().clear();
            mainTabPane.getTabs().clear();
        } finally {
            isRebuilding = false;
        }
    }

    private void redrawCurrentSupplier(SupplierPL supplier) {
        int currentTabIndex = 0;
        if (!mainTabPane.getTabs().isEmpty()) currentTabIndex = mainTabPane.getSelectionModel().getSelectedIndex();
        populateDetailsPane(supplier);
        mainTabPane.getSelectionModel().select(currentTabIndex);
    }

    private void populateDetailsPane(SupplierPL supplier) {
        detailsPane.getChildren().clear();
        mainTabPane.getTabs().clear();
        String bn = supplier.getBusinessNumber();
        Label nameTitle = new Label(supplier.getName() + " Dashboard");
        nameTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label bnLabel = new Label("Business Number: " + bn);
        Button deleteSupplierBtn = new Button("Delete Entire Supplier");
        deleteSupplierBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteSupplierBtn.setOnAction(e -> {
            if (showConfirmDialog("Delete Supplier", "Are you sure you want to delete this entire supplier?")) {
                try {
                    supplierController.deleteSupplier(bn);
                    supplierListView.getItems().remove(supplier);
                    detailsPane.getChildren().clear();
                    mainTabPane.getTabs().clear();
                } catch (Exception ex) {
                    showAlert("Error", ex.getMessage());
                }
            }
        });
        HBox headerBox = new HBox(20, bnLabel, deleteSupplierBtn);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Tab overviewTab = new Tab("Overview & Settings");
        VBox overviewLayout = new VBox(15);
        overviewLayout.setPadding(new Insets(15));
        Button editAddressBtn = new Button("Update Address");
        editAddressBtn.setOnAction(e -> showSingleFieldEditDialog("Edit Address", "New address:", supplier.getAddress(), val -> {
            try {
                supplierController.updateAddress(bn, val);
                SupplierPL updated = new SupplierPL(supplier.getName(), supplier.getBusinessNumber(), val, supplier.getContactPersonnel(), supplier.getAgreements(), supplier.getManufacturers());
                int idx = supplierListView.getItems().indexOf(supplier);
                supplierListView.getItems().set(idx, updated);
                supplierListView.getSelectionModel().select(updated);
                redrawCurrentSupplier(updated);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }));
        Button editBankBtn = new Button("Update Bank (IBAN)");
        editBankBtn.setOnAction(e -> showSingleFieldEditDialog("Edit Bank", "New IBAN:", "", val -> {
            try {
                supplierController.updateBankAccount(bn, val);
                refreshUI(bn);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }));
        Button editTermsBtn = new Button("Update Payment Terms");
        editTermsBtn.setOnAction(e -> showSingleFieldEditDialog("Edit Terms", "New Terms:", "", val -> {
            try {
                supplierController.updatePaymentTerms(bn, val);
                refreshUI(bn);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }));
        Button addMfgBtn = new Button("+ Add Manufacturer");
        addMfgBtn.setOnAction(e -> showSingleFieldEditDialog("Add Manufacturer", "Manufacturer name:", "", val -> {
            try {
                supplierController.addManufacturer(bn, val);
                supplier.getManufacturers().add(val);
                redrawCurrentSupplier(supplier);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }));
        Button rmvMfgBtn = new Button("- Remove Manufacturer");
        rmvMfgBtn.setOnAction(e -> {
            if (supplier.getManufacturers().isEmpty()) {
                showAlert("Info", "No manufacturers to remove.");
                return;
            }
            ChoiceDialog<String> dialog = new ChoiceDialog<>(supplier.getManufacturers().get(0), supplier.getManufacturers());
            dialog.setTitle("Remove Manufacturer");
            dialog.setHeaderText("Select a manufacturer to remove:");
            dialog.showAndWait().ifPresent(selected -> {
                try {
                    supplierController.removeManufacturer(bn, selected);
                    supplier.getManufacturers().remove(selected);
                    redrawCurrentSupplier(supplier);
                } catch (Exception ex) {
                    showAlert("Error", ex.getMessage());
                }
            });
        });
        overviewLayout.getChildren().addAll(new Label("Address: " + supplier.getAddress()), new Label("Manufacturers: " + String.join(", ", supplier.getManufacturers())), new Separator(), new HBox(10, editAddressBtn, editBankBtn, editTermsBtn), new HBox(10, addMfgBtn, rmvMfgBtn));
        overviewTab.setContent(overviewLayout);
        Tab contactsTab = new Tab("Contact Personnel");
        VBox contactsLayout = new VBox(10);
        contactsLayout.setPadding(new Insets(15));
        TableView<ContactPersonPL> contactsTable = new TableView<>();
        TableColumn<ContactPersonPL, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<ContactPersonPL, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        TableColumn<ContactPersonPL, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        contactsTable.getColumns().addAll(nameCol, phoneCol, emailCol);
        contactsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        contactsTable.getItems().setAll(supplier.getContactPersonnel());
        Button addContactBtn = new Button("+ Add Contact");
        addContactBtn.setOnAction(e -> showAddContactDialog(bn, newContact -> {
            supplier.getContactPersonnel().add(newContact);
            redrawCurrentSupplier(supplier);
        }));
        Button editContactBtn = new Button("Edit Selected Contact");
        editContactBtn.setOnAction(e -> {
            ContactPersonPL selected = contactsTable.getSelectionModel().getSelectedItem();
            if (selected != null) showEditContactDialog(bn, supplier, selected);
            else showAlert("Warning", "Select a contact first.");
        });
        Button delContactBtn = new Button("Delete Selected");
        delContactBtn.setOnAction(e -> {
            ContactPersonPL selected = contactsTable.getSelectionModel().getSelectedItem();
            if (selected != null && showConfirmDialog("Delete", "Remove this contact?")) {
                try {
                    supplierController.removeContactPerson(bn, selected.getPhone());
                    supplier.getContactPersonnel().remove(selected);
                    redrawCurrentSupplier(supplier);
                } catch (Exception ex) {
                    showAlert("Error", ex.getMessage());
                }
            }
        });
        contactsLayout.getChildren().addAll(contactsTable, new HBox(10, addContactBtn, editContactBtn, delContactBtn));
        contactsTab.setContent(contactsLayout);
        Tab agreementsTab = new Tab("Agreements & Products");
        VBox agreementsLayout = new VBox(10);
        agreementsLayout.setPadding(new Insets(15));
        HBox agreeHeader = new HBox(15);
        agreeHeader.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label agreeLabel = new Label("Agreements:");
        agreeLabel.setStyle("-fx-font-weight: bold;");
        TableView<AgreementPL> agreeTable = new TableView<>();
        TableColumn<AgreementPL, Integer> idCol = new TableColumn<>("Agreement ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("agreementId"));
        TableColumn<AgreementPL, String> dateCol = new TableColumn<>("Start Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        agreeTable.getColumns().addAll(idCol, dateCol);
        agreeTable.getItems().setAll(supplier.getAgreements());
        agreeTable.setPrefHeight(120);
        Button addAgreeBtn = new Button("+ Add Agreement");
        addAgreeBtn.setOnAction(e -> showAddAgreementDialog(bn, newAgree -> {
            supplier.getAgreements().add(newAgree);
            activeAgreementId = newAgree.getAgreementId();
            redrawCurrentSupplier(supplier);
        }));
        Button editAgreeBtn = new Button("Edit Selected Agreement");
        editAgreeBtn.setOnAction(e -> {
            AgreementPL selected = agreeTable.getSelectionModel().getSelectedItem();
            if (selected != null) showEditAgreementDialog(bn, supplier, selected);
            else showAlert("Warning", "Select an Agreement first.");
        });
        Button rmvAgreeBtn = new Button("- Remove Selected Agreement");
        rmvAgreeBtn.setOnAction(e -> {
            AgreementPL selected = agreeTable.getSelectionModel().getSelectedItem();
            if (selected != null && showConfirmDialog("Delete", "Delete this agreement?")) {
                try {
                    supplierController.removeAgreement(bn, selected.getAgreementId());
                    supplier.getAgreements().remove(selected);
                    if (activeAgreementId != null && activeAgreementId == selected.getAgreementId())
                        activeAgreementId = null;
                    redrawCurrentSupplier(supplier);
                } catch (Exception ex) {
                    showAlert("Error", ex.getMessage());
                }
            }
        });
        agreeHeader.getChildren().addAll(agreeLabel, addAgreeBtn, editAgreeBtn, rmvAgreeBtn);
        HBox prodHeader = new HBox(15);
        prodHeader.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label prodLabel = new Label("Products:");
        prodLabel.setStyle("-fx-font-weight: bold;");
        Button addProdBtn = new Button("+ Add Product");
        Button editProdBtn = new Button("Modify Product");
        Button rmvProdBtn = new Button("- Remove Product");
        prodHeader.getChildren().addAll(prodLabel, addProdBtn, editProdBtn, rmvProdBtn);
        TableView<ProductLinePL> prodTable = new TableView<>();
        TableColumn<ProductLinePL, Integer> catCol = new TableColumn<>("Catalog ID");
        catCol.setCellValueFactory(new PropertyValueFactory<>("supplierCatalogId"));
        TableColumn<ProductLinePL, String> pNameCol = new TableColumn<>("Name");
        pNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<ProductLinePL, Double> priceCol = new TableColumn<>("Base Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
        TableColumn<ProductLinePL, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        prodTable.getColumns().addAll(catCol, pNameCol, priceCol, qtyCol);
        prodTable.setPrefHeight(120);
        addProdBtn.setOnAction(e -> {
            AgreementPL selAgree = agreeTable.getSelectionModel().getSelectedItem();
            if (selAgree != null) showAddProductDialog(bn, selAgree.getAgreementId(), newProd -> {
                selAgree.getProductLines().add(newProd);
                activeProductId = newProd.getSupplierCatalogId();
                redrawCurrentSupplier(supplier);
            });
            else showAlert("Warning", "Select an Agreement first.");
        });
        editProdBtn.setOnAction(e -> {
            AgreementPL selAgree = agreeTable.getSelectionModel().getSelectedItem();
            ProductLinePL selProd = prodTable.getSelectionModel().getSelectedItem();
            if (selAgree != null && selProd != null) showEditProductDialog(bn, supplier, selAgree, selProd);
            else showAlert("Warning", "Select a Product first.");
        });
        rmvProdBtn.setOnAction(e -> {
            AgreementPL selAgree = agreeTable.getSelectionModel().getSelectedItem();
            ProductLinePL selProd = prodTable.getSelectionModel().getSelectedItem();
            if (selAgree != null && selProd != null && showConfirmDialog("Delete", "Remove product?")) {
                try {
                    supplierController.removeProductLine(bn, selAgree.getAgreementId(), selProd.getSupplierCatalogId());
                    selAgree.getProductLines().remove(selProd);
                    if (activeProductId != null && activeProductId == selProd.getSupplierCatalogId())
                        activeProductId = null;
                    redrawCurrentSupplier(supplier);
                } catch (Exception ex) {
                    showAlert("Error", ex.getMessage());
                }
            }
        });
        HBox discHeader = new HBox(15);
        discHeader.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label discLabel = new Label("Discounts for Product:");
        discLabel.setStyle("-fx-font-weight: bold;");
        Button addDiscBtn = new Button("+ Add Discount");
        Button editDiscBtn = new Button("Modify Discount");
        Button rmvDiscBtn = new Button("- Remove Discount");
        discHeader.getChildren().addAll(discLabel, addDiscBtn, editDiscBtn, rmvDiscBtn);
        TableView<DiscountBracketPL> discTable = new TableView<>();
        TableColumn<DiscountBracketPL, Integer> minQtyCol = new TableColumn<>("Min Quantity");
        minQtyCol.setCellValueFactory(new PropertyValueFactory<>("minQuantity"));
        TableColumn<DiscountBracketPL, Double> pctCol = new TableColumn<>("Discount %");
        pctCol.setCellValueFactory(new PropertyValueFactory<>("discountPercentage"));
        discTable.getColumns().addAll(minQtyCol, pctCol);
        discTable.setPrefHeight(100);
        addDiscBtn.setOnAction(e -> {
            AgreementPL selAgree = agreeTable.getSelectionModel().getSelectedItem();
            ProductLinePL selProd = prodTable.getSelectionModel().getSelectedItem();
            if (selAgree != null && selProd != null)
                showAddDiscountDialog(bn, supplier, selAgree, selProd.getSupplierCatalogId());
            else showAlert("Warning", "Select a Product first.");
        });
        editDiscBtn.setOnAction(e -> {
            AgreementPL selAgree = agreeTable.getSelectionModel().getSelectedItem();
            ProductLinePL selProd = prodTable.getSelectionModel().getSelectedItem();
            DiscountBracketPL selDisc = discTable.getSelectionModel().getSelectedItem();
            if (selAgree != null && selProd != null && selDisc != null) {
                showSingleFieldEditDialog("Edit Discount", "New % for Min Qty " + selDisc.getMinQuantity() + ":", String.valueOf(selDisc.getDiscountPercentage()), val -> {
                    try {
                        supplierController.updateDiscount(bn, selAgree.getAgreementId(), selProd.getSupplierCatalogId(), selDisc.getMinQuantity(), Double.parseDouble(val));
                        selAgree.getDiscountPolicy().get(selProd.getSupplierCatalogId()).remove(selDisc);
                        selAgree.getDiscountPolicy().get(selProd.getSupplierCatalogId()).add(new DiscountBracketPL(selDisc.getMinQuantity(), Double.parseDouble(val)));
                        redrawCurrentSupplier(supplier);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        });
        rmvDiscBtn.setOnAction(e -> {
            AgreementPL selAgree = agreeTable.getSelectionModel().getSelectedItem();
            ProductLinePL selProd = prodTable.getSelectionModel().getSelectedItem();
            DiscountBracketPL selDisc = discTable.getSelectionModel().getSelectedItem();
            if (selAgree != null && selProd != null && selDisc != null && showConfirmDialog("Delete", "Remove this discount?")) {
                try {
                    supplierController.removeDiscount(bn, selAgree.getAgreementId(), selProd.getSupplierCatalogId(), selDisc.getMinQuantity());
                    selAgree.getDiscountPolicy().get(selProd.getSupplierCatalogId()).remove(selDisc);
                    redrawCurrentSupplier(supplier);
                } catch (Exception ex) {
                    showAlert("Error", ex.getMessage());
                }
            }
        });
        agreeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            discTable.getItems().clear();
            if (newVal != null) {
                activeAgreementId = newVal.getAgreementId();
                prodTable.getItems().setAll(newVal.getProductLines());
                if (activeProductId != null)
                    for (ProductLinePL p : prodTable.getItems())
                        if (p.getSupplierCatalogId() == activeProductId) {
                            prodTable.getSelectionModel().select(p);
                            break;
                        }
            } else {
                if (!isRebuilding) activeAgreementId = null;
                prodTable.getItems().clear();
            }
        });
        prodTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                activeProductId = newVal.getSupplierCatalogId();
                AgreementPL selAgree = agreeTable.getSelectionModel().getSelectedItem();
                if (selAgree != null && selAgree.getDiscountPolicy().containsKey(newVal.getSupplierCatalogId()))
                    discTable.getItems().setAll(selAgree.getDiscountPolicy().get(newVal.getSupplierCatalogId()));
                else discTable.getItems().clear();
            } else {
                if (!isRebuilding) activeProductId = null;
                discTable.getItems().clear();
            }
        });
        agreementsLayout.getChildren().addAll(agreeHeader, agreeTable, new Separator(), prodHeader, prodTable, new Separator(), discHeader, discTable);
        agreementsTab.setContent(agreementsLayout);
        mainTabPane.getTabs().addAll(overviewTab, contactsTab, agreementsTab);
        detailsPane.getChildren().addAll(nameTitle, headerBox, new Separator(), mainTabPane);
        if (!agreeTable.getItems().isEmpty() && activeAgreementId != null)
            for (AgreementPL a : agreeTable.getItems())
                if (a.getAgreementId() == activeAgreementId) {
                    agreeTable.getSelectionModel().select(a);
                    break;
                }
    }

    private void showAddContactDialog(String businessNumber, java.util.function.Consumer<ContactPersonPL> onSuccess) {
        Dialog<ContactPersonPL> dialog = new Dialog<>();
        dialog.setTitle("Add Contact Person");
        dialog.setHeaderText("Enter details:");
        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        TextField nameField = new TextField();
        TextField phoneField = new TextField();
        TextField emailField = new TextField();
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> btn == saveBtn ? new ContactPersonPL(nameField.getText(), phoneField.getText(), emailField.getText()) : null);
        dialog.showAndWait().ifPresent(contact -> {
            try {
                ContactPersonPL newContact = supplierController.addContactPerson(businessNumber, contact.getName(), contact.getPhone(), contact.getEmail());
                onSuccess.accept(newContact);
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        });
    }

    private void showEditContactDialog(String bn, SupplierPL supplier, ContactPersonPL oldContact) {
        Dialog<ContactPersonPL> dialog = new Dialog<>();
        dialog.setTitle("Edit Contact");
        dialog.setHeaderText("Modify details:");
        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        TextField nameField = new TextField(oldContact.getName());
        TextField phoneField = new TextField(oldContact.getPhone());
        TextField emailField = new TextField(oldContact.getEmail());
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> btn == saveBtn ? new ContactPersonPL(nameField.getText(), phoneField.getText(), emailField.getText()) : null);
        dialog.showAndWait().ifPresent(newContact -> {
            try {
                ContactPersonPL latest = oldContact;
                if (!newContact.getName().equals(latest.getName()))
                    latest = supplierController.updateContactName(bn, latest.getPhone(), newContact.getName());
                if (!newContact.getEmail().equals(latest.getEmail()))
                    latest = supplierController.updateContactEmail(bn, latest.getPhone(), newContact.getEmail());
                if (!newContact.getPhone().equals(latest.getPhone()))
                    latest = supplierController.updateContactPhone(bn, latest.getPhone(), newContact.getPhone());
                supplier.getContactPersonnel().remove(oldContact);
                supplier.getContactPersonnel().add(latest);
                redrawCurrentSupplier(supplier);
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        });
    }

    private void showAddAgreementDialog(String bn, java.util.function.Consumer<AgreementPL> onSuccess) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Add Agreement");
        dialog.setHeaderText("Select Delivery Days:");
        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        CheckBox[] days = new CheckBox[7];
        DayOfWeek[] dow = DayOfWeek.values();
        for (int i = 0; i < 7; i++) {
            days[i] = new CheckBox(dow[i].toString());
            box.getChildren().add(days[i]);
        }
        CheckBox transportBox = new CheckBox("Supplier Transports?");
        box.getChildren().add(new Separator());
        box.getChildren().add(transportBox);
        dialog.getDialogPane().setContent(box);
        dialog.setResultConverter(btn -> btn == saveBtn);
        dialog.showAndWait().ifPresent(saved -> {
            if (saved) {
                List<DayOfWeek> fixedDays = new ArrayList<>();
                for (int i = 0; i < 7; i++) if (days[i].isSelected()) fixedDays.add(dow[i]);
                try {
                    AgreementPL newAgree = supplierController.addAgreement(bn, fixedDays, transportBox.isSelected());
                    onSuccess.accept(newAgree);
                } catch (Exception e) {
                    showAlert("Error", e.getMessage());
                }
            }
        });
    }

    private void showEditAgreementDialog(String bn, SupplierPL supplier, AgreementPL agree) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Edit Agreement");
        dialog.setHeaderText("Modify Delivery Days and Transport:");
        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        CheckBox[] days = new CheckBox[7];
        DayOfWeek[] dow = DayOfWeek.values();
        List<DayOfWeek> currentDays = agree.getDeliveryTerms().getFixedDeliveryDays();
        for (int i = 0; i < 7; i++) {
            days[i] = new CheckBox(dow[i].toString());
            if (currentDays.contains(dow[i])) days[i].setSelected(true);
            box.getChildren().add(days[i]);
        }
        CheckBox transportBox = new CheckBox("Supplier Transports?");
        transportBox.setSelected(agree.getDeliveryTerms().isSupplierTransports());
        box.getChildren().add(new Separator());
        box.getChildren().add(transportBox);
        dialog.getDialogPane().setContent(box);
        dialog.setResultConverter(btn -> btn == saveBtn);
        dialog.showAndWait().ifPresent(saved -> {
            if (saved) {
                List<DayOfWeek> fixedDays = new ArrayList<>();
                for (int i = 0; i < 7; i++) if (days[i].isSelected()) fixedDays.add(dow[i]);
                try {
                    if (!fixedDays.equals(currentDays)) supplierController.updateFixedDeliveryDays(bn, agree.getAgreementId(), fixedDays);
                    if (transportBox.isSelected() != agree.getDeliveryTerms().isSupplierTransports()) supplierController.updateSupplierTransports(bn, agree.getAgreementId(), transportBox.isSelected());
                    DeliveryTermsPL updatedTerms = new DeliveryTermsPL(fixedDays, transportBox.isSelected());
                    AgreementPL updatedAgree = new AgreementPL(agree.getAgreementId(), agree.getStartDate(), updatedTerms, agree.getProductLines(), agree.getDiscountPolicy());
                    int idx = supplier.getAgreements().indexOf(agree);
                    if (idx >= 0) supplier.getAgreements().set(idx, updatedAgree);
                    redrawCurrentSupplier(supplier);
                } catch (Exception e) {
                    showAlert("Error", e.getMessage());
                }
            }
        });
    }

    private void showAddProductDialog(String bn, int agreeId, java.util.function.Consumer<ProductLinePL> onSuccess) {
        Dialog<ProductLinePL> dialog = new Dialog<>();
        dialog.setTitle("Add Product");
        dialog.setHeaderText("Enter details:");
        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField priceField = new TextField();
        TextField qtyField = new TextField();
        grid.add(new Label("Catalog ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Base Price:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Quantity:"), 0, 3);
        grid.add(qtyField, 1, 3);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                try {
                    return new ProductLinePL(Integer.parseInt(idField.getText()), nameField.getText(), Double.parseDouble(priceField.getText()), Integer.parseInt(qtyField.getText()));
                } catch (Exception e) {
                    showAlert("Format Error", "Price and Quantity must be numbers.");
                    return null;
                }
            }
            return null;
        });
        dialog.showAndWait().ifPresent(p -> {
            try {
                ProductLinePL newProd = supplierController.addProductLine(bn, agreeId, p.getSupplierCatalogId(), p.getName(), p.getBasePrice(), p.getQuantity());
                onSuccess.accept(newProd);
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        });
    }

    private void showEditProductDialog(String bn, SupplierPL supplier, AgreementPL agree, ProductLinePL prod) {
        Dialog<ProductLinePL> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText("Modify Price/Quantity:");
        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        TextField priceField = new TextField(String.valueOf(prod.getBasePrice()));
        TextField qtyField = new TextField(String.valueOf(prod.getQuantity()));
        grid.add(new Label("Base Price:"), 0, 0);
        grid.add(priceField, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(qtyField, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> btn == saveBtn ? new ProductLinePL(prod.getSupplierCatalogId(), prod.getName(), Double.parseDouble(priceField.getText()), Integer.parseInt(qtyField.getText())) : null);
        dialog.showAndWait().ifPresent(p -> {
            try {
                ProductLinePL latest = prod;
                if (p.getBasePrice() != latest.getBasePrice())
                    latest = supplierController.updateProductLineBasePrice(bn, agree.getAgreementId(), latest.getSupplierCatalogId(), p.getBasePrice());
                if (p.getQuantity() != latest.getQuantity())
                    latest = supplierController.updateProductLineQuantity(bn, agree.getAgreementId(), latest.getSupplierCatalogId(), p.getQuantity());
                agree.getProductLines().remove(prod);
                agree.getProductLines().add(latest);
                redrawCurrentSupplier(supplier);
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        });
    }

    private void showAddDiscountDialog(String bn, SupplierPL supplier, AgreementPL selAgree, int catalogId) {
        Dialog<DiscountBracketPL> dialog = new Dialog<>();
        dialog.setTitle("Add Discount");
        dialog.setHeaderText("Enter details:");
        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        TextField qtyField = new TextField();
        TextField pctField = new TextField();
        grid.add(new Label("Min Quantity:"), 0, 0);
        grid.add(qtyField, 1, 0);
        grid.add(new Label("Discount %:"), 0, 1);
        grid.add(pctField, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> btn == saveBtn ? new DiscountBracketPL(Integer.parseInt(qtyField.getText()), Double.parseDouble(pctField.getText())) : null);
        dialog.showAndWait().ifPresent(d -> {
            try {
                supplierController.addDiscount(bn, selAgree.getAgreementId(), catalogId, d.getMinQuantity(), d.getDiscountPercentage());
                selAgree.getDiscountPolicy().computeIfAbsent(catalogId, k -> new ArrayList<>()).add(new DiscountBracketPL(d.getMinQuantity(), d.getDiscountPercentage()));
                redrawCurrentSupplier(supplier);
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        });
    }

    private void showSingleFieldEditDialog(String title, String header, String defaultValue, java.util.function.Consumer<String> action) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.showAndWait().ifPresent(val -> {
            try {
                action.accept(val);
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        });
    }

    private boolean showConfirmDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void showAddSupplierDialog() {
        Dialog<SupplierPL> dialog = new Dialog<>();
        dialog.setTitle("Add New Supplier");
        dialog.setHeaderText("Enter details:");
        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        TextField nameField = new TextField();
        TextField bnField = new TextField();
        TextField addressField = new TextField();
        TextField ibanField = new TextField();
        TextField termsField = new TextField();
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Business No:"), 0, 1);
        grid.add(bnField, 1, 1);
        grid.add(new Label("Address:"), 0, 2);
        grid.add(addressField, 1, 2);
        grid.add(new Label("IBAN:"), 0, 3);
        grid.add(ibanField, 1, 3);
        grid.add(new Label("Pay Terms:"), 0, 4);
        grid.add(termsField, 1, 4);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> btn == saveBtn ? new SupplierPL(nameField.getText(), bnField.getText(), addressField.getText(), null, null, null) : null);
        dialog.showAndWait().ifPresent(s -> {
            try {
                SupplierPL newSup = supplierController.addSupplier(s.getName(), s.getBusinessNumber(), s.getAddress(), ibanField.getText(), termsField.getText());
                supplierListView.getItems().add(newSup);
                supplierListView.getSelectionModel().select(newSup);
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        });
    }

    private void logout() {
        try {
            ControllerFactory.getInstance().getAuthController().logout();
            MainApp.showLoginScreen();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}