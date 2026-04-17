package Suppliers.Presentation.GUI;

import Suppliers.Presentation.Controller.ControllerFactory;
import Suppliers.Presentation.Controller.OrderController;
import Suppliers.Presentation.SupplierPL;
import Suppliers.Presentation.AgreementPL;
import Suppliers.Presentation.ProductLinePL;
import Suppliers.Presentation.DiscountBracketPL;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;
import java.util.stream.Collectors;

public class OrderDashboard {
    private final Scene scene;
    private final OrderController orderController;
    private final BorderPane mainLayout;
    private final VBox centerPane;
    private final HBox topBar;
    private final Button abortBtn;
    private List<SupplierPL> onDemandSuppliers;
    private Set<String> availableProducts;
    private List<OrderItem> currentOrder;
    private String selectedProductName;

    public OrderDashboard() {
        this.orderController = ControllerFactory.getInstance().getOrderController();
        this.currentOrder = new ArrayList<>();
        this.availableProducts = new HashSet<>();
        this.onDemandSuppliers = new ArrayList<>();
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> logout());
        this.abortBtn = new Button("Abort Order");
        this.abortBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; font-weight: bold;");
        this.abortBtn.setVisible(false);
        this.abortBtn.setOnAction(e -> resetToStart());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        this.topBar = new HBox(15, abortBtn, spacer, logoutBtn);
        this.topBar.setPadding(new Insets(10));
        this.topBar.setStyle("-fx-background-color: #333333;");
        this.centerPane = new VBox(20);
        this.centerPane.setAlignment(Pos.CENTER);
        this.centerPane.setPadding(new Insets(40));
        this.mainLayout = new BorderPane();
        this.mainLayout.setTop(topBar);
        this.mainLayout.setCenter(centerPane);
        this.scene = new Scene(mainLayout, 900, 600);
        resetToStart();
    }

    private void loadOnDemandCatalog() {
        this.availableProducts.clear();
        try {
            this.onDemandSuppliers = orderController.getOnDemandSuppliers();
            for (SupplierPL sup : this.onDemandSuppliers) {
                for (AgreementPL agr : sup.getAgreements()) {
                    for (ProductLinePL prod : agr.getProductLines()) {
                        availableProducts.add(prod.getName());
                    }
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Could not load catalog: " + e.getMessage());
        }
    }

    private void resetToStart() {
        this.currentOrder.clear();
        this.selectedProductName = null;
        this.abortBtn.setVisible(false);
        this.centerPane.getChildren().clear();
        Label welcome = new Label("Order Management System");
        welcome.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        Button startBtn = new Button("Start Order");
        startBtn.setStyle("-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 30;");
        startBtn.setOnAction(e -> {
            loadOnDemandCatalog();
            showSearchPhase();
        });
        this.centerPane.getChildren().addAll(welcome, startBtn);
    }

    private void showSearchPhase() {
        this.abortBtn.setVisible(true);
        this.centerPane.getChildren().clear();
        Label prompt = new Label("What item do you seek to buy?");
        prompt.setStyle("-fx-font-size: 20px;");
        TextField searchBar = new TextField();
        searchBar.setPromptText("Type product name...");
        searchBar.setMaxWidth(400);
        searchBar.setStyle("-fx-font-size: 16px;");
        ListView<String> resultsList = new ListView<>();
        resultsList.setMaxWidth(400);
        resultsList.setPrefHeight(200);
        Label errorLabel = new Label("No matching products found in on-demand agreements.");
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        errorLabel.setVisible(false);
        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                resultsList.getItems().clear();
                errorLabel.setVisible(false);
            } else {
                List<String> matches = availableProducts.stream().filter(p -> p.toLowerCase().contains(newVal.toLowerCase())).collect(Collectors.toList());
                resultsList.getItems().setAll(matches);
                errorLabel.setVisible(matches.isEmpty());
            }
        });
        resultsList.setOnMouseClicked(e -> {
            String selected = resultsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                this.selectedProductName = selected;
                showQuantityPhase();
            }
        });
        this.centerPane.getChildren().addAll(prompt, searchBar, errorLabel, resultsList);
    }

    private void showQuantityPhase() {
        this.centerPane.getChildren().clear();
        Label prompt = new Label("How many units of '" + selectedProductName + "' do you need?");
        prompt.setStyle("-fx-font-size: 20px;");
        TextField qtyField = new TextField();
        qtyField.setPromptText("Enter quantity...");
        qtyField.setMaxWidth(200);
        qtyField.setStyle("-fx-font-size: 16px;");
        Button calcBtn = new Button("Find Cheapest Supplier");
        calcBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        calcBtn.setOnAction(e -> {
            try {
                int qty = Integer.parseInt(qtyField.getText());
                if (qty <= 0) throw new NumberFormatException();
                calculateCheapestAndShowResult(qty);
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid positive number.");
            }
        });
        this.centerPane.getChildren().addAll(prompt, qtyField, calcBtn);
    }

    private OrderItem findCheapestItem(String productName, int quantity) {
        OrderItem bestItem = null;
        double minPrice = Double.MAX_VALUE;
        for (SupplierPL sup : onDemandSuppliers) {
            for (AgreementPL agr : sup.getAgreements()) {
                for (ProductLinePL prod : agr.getProductLines()) {
                    if (prod.getName().equals(productName)) {
                        double priceBefore = prod.getBasePrice() * quantity;
                        double bestDiscountPct = 0.0;
                        List<DiscountBracketPL> discounts = agr.getDiscountPolicy().get(prod.getSupplierCatalogId());
                        if (discounts != null) {
                            for (DiscountBracketPL d : discounts) {
                                if (quantity >= d.getMinQuantity() && d.getDiscountPercentage() > bestDiscountPct) {
                                    bestDiscountPct = d.getDiscountPercentage();
                                }
                            }
                        }
                        double currentPrice = priceBefore * (1.0 - (bestDiscountPct / 100.0));
                        if (currentPrice < minPrice) {
                            minPrice = currentPrice;
                            bestItem = new OrderItem(productName, sup.getName(), prod.getSupplierCatalogId(), agr.getAgreementId(), quantity, priceBefore, currentPrice, agr.getDeliveryTerms().isSupplierTransports(), sup.getAddress());
                        }
                    }
                }
            }
        }
        return bestItem;
    }

    private void calculateCheapestAndShowResult(int quantity) {
        OrderItem bestItem = findCheapestItem(selectedProductName, quantity);
        if (bestItem == null) {
            showAlert("System Error", "Could not calculate price for this product.");
            showSearchPhase();
            return;
        }
        this.currentOrder.add(bestItem);
        this.centerPane.getChildren().clear();
        Label success = new Label("Item Added Successfully!");
        success.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: green;");
        VBox detailsBox = new VBox(10);
        detailsBox.setAlignment(Pos.CENTER);
        detailsBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 2; -fx-padding: 20; -fx-background-color: #f9f9f9;");
        detailsBox.setMaxWidth(500);
        detailsBox.getChildren().addAll(
                new Label("Product: " + bestItem.getProductName()),
                new Label("Cheapest Supplier: " + bestItem.getSupplierName()),
                new Label("Agreement ID: " + bestItem.getAgreementId()),
                new Label("Supplier Catalog ID: " + bestItem.getCatalogId()),
                new Label("Quantity: " + bestItem.getQuantity()),
                new Label(String.format("Price Before Discount: NIS %.2f", bestItem.getPriceBeforeDiscount())),
                new Label(String.format("Final Price: NIS %.2f", bestItem.getTotalPrice()))
        );
        for (javafx.scene.Node node : detailsBox.getChildren()) {
            node.setStyle("-fx-font-size: 16px;");
        }
        HBox actionBox = new HBox(20);
        actionBox.setAlignment(Pos.CENTER);
        Button addAnotherBtn = new Button("Add Another Product");
        addAnotherBtn.setStyle("-fx-font-size: 16px;");
        addAnotherBtn.setOnAction(e -> showSearchPhase());
        Button finishBtn = new Button("Finish Order");
        finishBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #FF9800; -fx-text-fill: white;");
        finishBtn.setOnAction(e -> showSummaryPhase());
        actionBox.getChildren().addAll(addAnotherBtn, finishBtn);
        this.centerPane.getChildren().addAll(success, detailsBox, actionBox);
    }

    private void showSummaryPhase() {
        this.centerPane.getChildren().clear();
        Label title = new Label("Order Summary");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        TableView<OrderItem> table = new TableView<>();
        table.setMaxWidth(800);
        table.setPrefHeight(250);
        TableColumn<OrderItem, String> prodCol = new TableColumn<>("Product");
        prodCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductName()));
        TableColumn<OrderItem, String> supCol = new TableColumn<>("Supplier");
        supCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSupplierName()));
        TableColumn<OrderItem, Integer> catCol = new TableColumn<>("Catalog ID");
        catCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCatalogId()));
        TableColumn<OrderItem, Integer> agrCol = new TableColumn<>("Agreement ID");
        agrCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAgreementId()));
        TableColumn<OrderItem, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQuantity()));
        TableColumn<OrderItem, String> beforeCol = new TableColumn<>("Before Disc.");
        beforeCol.setCellValueFactory(data -> new SimpleStringProperty(String.format("NIS %.2f", data.getValue().getPriceBeforeDiscount())));
        TableColumn<OrderItem, String> priceCol = new TableColumn<>("Final Price");
        priceCol.setCellValueFactory(data -> new SimpleStringProperty(String.format("NIS %.2f", data.getValue().getTotalPrice())));
        table.getColumns().addAll(prodCol, supCol, catCol, agrCol, qtyCol, beforeCol, priceCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getItems().setAll(currentOrder);
        HBox editBox = new HBox(15);
        editBox.setAlignment(Pos.CENTER);
        Button modifyBtn = new Button("Modify Quantity");
        modifyBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        modifyBtn.setOnAction(e -> {
            OrderItem selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getQuantity()));
                dialog.setTitle("Modify Quantity");
                dialog.setHeaderText("New quantity for " + selected.getProductName() + ":");
                dialog.showAndWait().ifPresent(val -> {
                    try {
                        int newQty = Integer.parseInt(val);
                        if (newQty <= 0) throw new NumberFormatException();
                        currentOrder.remove(selected);
                        OrderItem newItem = findCheapestItem(selected.getProductName(), newQty);
                        if (newItem != null) currentOrder.add(newItem);
                        else showAlert("Error", "Could not recalculate. Item removed.");
                        showSummaryPhase();
                    } catch (NumberFormatException ex) {
                        showAlert("Invalid Input", "Please enter a valid positive number.");
                    }
                });
            } else {
                showAlert("Warning", "Select an item to modify.");
            }
        });
        Button deleteBtn = new Button("Delete Item");
        deleteBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #f44336; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> {
            OrderItem selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                currentOrder.remove(selected);
                showSummaryPhase();
            } else {
                showAlert("Warning", "Select an item to delete.");
            }
        });
        editBox.getChildren().addAll(modifyBtn, deleteBtn);
        double grandTotal = currentOrder.stream().mapToDouble(OrderItem::getTotalPrice).sum();
        Label totalLabel = new Label(String.format("Grand Total: NIS %.2f", grandTotal));
        totalLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox actionBox = new HBox(20);
        actionBox.setAlignment(Pos.CENTER);
        Button returnBtn = new Button("Return / Add More");
        returnBtn.setStyle("-fx-font-size: 16px;");
        returnBtn.setOnAction(e -> showSearchPhase());
        Button finalizeBtn = new Button("Make Order Final");
        finalizeBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        finalizeBtn.setDisable(currentOrder.isEmpty());
        finalizeBtn.setOnAction(e -> showConfirmationPhase());
        actionBox.getChildren().addAll(returnBtn, finalizeBtn);
        this.centerPane.getChildren().addAll(title, table, editBox, totalLabel, actionBox);
    }

    private void showConfirmationPhase() {
        this.abortBtn.setVisible(false);
        this.centerPane.getChildren().clear();
        Label confirm = new Label("Order sent successfully!");
        confirm.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: green;");
        VBox messagesBox = new VBox(10);
        messagesBox.setAlignment(Pos.CENTER);
        Set<String> processedSuppliers = new HashSet<>();
        for (OrderItem item : currentOrder) {
            if (!processedSuppliers.contains(item.getSupplierName())) {
                processedSuppliers.add(item.getSupplierName());
                String msg = "Supplier '" + item.getSupplierName() + "': A manager will call shortly before the order is ready. ";
                if (item.isSupplierTransports()) msg += "They will deliver it to us.";
                else msg += "We need to send a delivery guy to their address: " + item.getSupplierAddress() + ".";
                Label lbl = new Label(msg);
                lbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                messagesBox.getChildren().add(lbl);
            }
        }
        Button newOrderBtn = new Button("Start New Order");
        newOrderBtn.setStyle("-fx-font-size: 18px; -fx-padding: 10 30;");
        newOrderBtn.setOnAction(e -> resetToStart());
        this.centerPane.getChildren().addAll(confirm, messagesBox, newOrderBtn);
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

    public static class OrderItem {
        private final String productName;
        private final String supplierName;
        private final int catalogId;
        private final int agreementId;
        private final int quantity;
        private final double priceBeforeDiscount;
        private final double totalPrice;
        private final boolean supplierTransports;
        private final String supplierAddress;

        public OrderItem(String productName, String supplierName, int catalogId, int agreementId, int quantity, double priceBeforeDiscount, double totalPrice, boolean supplierTransports, String supplierAddress) {
            this.productName = productName;
            this.supplierName = supplierName;
            this.catalogId = catalogId;
            this.agreementId = agreementId;
            this.quantity = quantity;
            this.priceBeforeDiscount = priceBeforeDiscount;
            this.totalPrice = totalPrice;
            this.supplierTransports = supplierTransports;
            this.supplierAddress = supplierAddress;
        }

        public String getProductName() {
            return productName;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public int getCatalogId() {
            return catalogId;
        }

        public int getAgreementId() {
            return agreementId;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPriceBeforeDiscount() {
            return priceBeforeDiscount;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public boolean isSupplierTransports() {
            return supplierTransports;
        }

        public String getSupplierAddress() {
            return supplierAddress;
        }
    }
}