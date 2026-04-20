package Suppliers.Presentation.GUI;

import Suppliers.Presentation.Controller.ControllerFactory;
import Suppliers.Presentation.Controller.OrderController;
import Suppliers.Presentation.DTO.SupplierPL;
import Suppliers.Presentation.DTO.AgreementPL;
import Suppliers.Presentation.DTO.ProductLinePL;
import Suppliers.Presentation.DTO.DiscountBracketPL;
import Suppliers.Presentation.DTO.OrderItemPL;
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
    private static int globalOrderCounter = 1;
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
        orderController = ControllerFactory.getInstance().getOrderController();
        currentOrder = new ArrayList<>();
        availableProducts = new HashSet<>();
        onDemandSuppliers = new ArrayList<>();
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> logout());
        abortBtn = new Button("Abort Order");
        abortBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; font-weight: bold;");
        abortBtn.setVisible(false);
        abortBtn.setOnAction(e -> resetToStart());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar = new HBox(15, abortBtn, spacer, logoutBtn);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #333333;");
        centerPane = new VBox(20);
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(40));
        mainLayout = new BorderPane();
        mainLayout.setTop(topBar);
        mainLayout.setCenter(centerPane);
        scene = new Scene(mainLayout, 900, 600);
        resetToStart();
    }

    private void loadOnDemandCatalog() {
        availableProducts.clear();
        try {
            onDemandSuppliers = orderController.getOnDemandSuppliers();
            for (SupplierPL sup : onDemandSuppliers)
                for (AgreementPL agr : sup.getAgreements())
                    for (ProductLinePL prod : agr.getProductLines())
                        availableProducts.add(prod.getName());
        } catch (Exception e) {
            showAlert("Error", "Could not load catalog: " + e.getMessage());
        }
    }

    private void resetToStart() {
        currentOrder.clear();
        selectedProductName = null;
        abortBtn.setVisible(false);
        centerPane.getChildren().clear();
        Label welcome = new Label("Order Management System");
        welcome.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        Button startBtn = new Button("Start Order");
        startBtn.setStyle("-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 30;");
        startBtn.setOnAction(e -> {
            loadOnDemandCatalog();
            showSearchPhase();
        });
        centerPane.getChildren().addAll(welcome, startBtn);
    }

    private void showSearchPhase() {
        abortBtn.setVisible(true);
        centerPane.getChildren().clear();
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
                selectedProductName = selected;
                showQuantityPhase();
            }
        });
        centerPane.getChildren().addAll(prompt, searchBar, errorLabel, resultsList);
        if (!currentOrder.isEmpty()) {
            Button returnSummaryBtn = new Button("Return to Order Summary");
            returnSummaryBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #FF9800; -fx-text-fill: white;");
            returnSummaryBtn.setOnAction(e -> showSummaryPhase());
            centerPane.getChildren().add(returnSummaryBtn);
        }
    }

    private void showQuantityPhase() {
        centerPane.getChildren().clear();
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
        Button backBtn = new Button("Go Back");
        backBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #9e9e9e; -fx-text-fill: white;");
        backBtn.setOnAction(e -> showSearchPhase());
        HBox actionBox = new HBox(15, calcBtn, backBtn);
        actionBox.setAlignment(Pos.CENTER);
        centerPane.getChildren().addAll(prompt, qtyField, actionBox);
    }

    private OrderItem findCheapestItem(String productName, int quantity) throws IllegalArgumentException {
        OrderItem bestItem = null;
        double minPrice = Double.MAX_VALUE;
        int maxAvailable = 0;
        for (SupplierPL sup : onDemandSuppliers)
            for (AgreementPL agr : sup.getAgreements())
                for (ProductLinePL prod : agr.getProductLines())
                    if (prod.getName().equals(productName)) {
                        if (prod.getQuantity() > maxAvailable) maxAvailable = prod.getQuantity();
                        if (quantity <= prod.getQuantity()) {
                            double priceBefore = prod.getBasePrice() * quantity;
                            double bestDiscountPct = 0.0;
                            List<DiscountBracketPL> discounts = agr.getDiscountPolicy().get(prod.getSupplierCatalogId());
                            if (discounts != null) {
                                for (DiscountBracketPL d : discounts)
                                    if (quantity >= d.getMinQuantity() && d.getDiscountPercentage() > bestDiscountPct)
                                        bestDiscountPct = d.getDiscountPercentage();
                            }
                            double currentPrice = priceBefore * (1.0 - (bestDiscountPct / 100.0));
                            if (currentPrice < minPrice) {
                                minPrice = currentPrice;
                                bestItem = new OrderItem(productName, sup.getBusinessNumber(), sup.getName(), prod.getSupplierCatalogId(), agr.getAgreementId(), quantity, priceBefore, currentPrice, agr.getDeliveryTerms().isSupplierTransports());
                            }
                        }
                    }
        if (bestItem == null) {
            if (maxAvailable > 0 && quantity > maxAvailable)
                throw new IllegalArgumentException("The maximum quantity any supplier can provide for this item is " + maxAvailable + ".");
            throw new IllegalArgumentException("Could not calculate price for this product.");
        }
        return bestItem;
    }

    private void calculateCheapestAndShowResult(int quantity) {
        try {
            OrderItem bestItem = findCheapestItem(selectedProductName, quantity);
            currentOrder.add(bestItem);
            centerPane.getChildren().clear();
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
            for (javafx.scene.Node node : detailsBox.getChildren()) node.setStyle("-fx-font-size: 16px;");
            HBox actionBox = new HBox(20);
            actionBox.setAlignment(Pos.CENTER);
            Button addAnotherBtn = new Button("Add Another Product");
            addAnotherBtn.setStyle("-fx-font-size: 16px;");
            addAnotherBtn.setOnAction(e -> showSearchPhase());
            Button finishBtn = new Button("Finish Order");
            finishBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #FF9800; -fx-text-fill: white;");
            finishBtn.setOnAction(e -> showSummaryPhase());
            actionBox.getChildren().addAll(addAnotherBtn, finishBtn);
            centerPane.getChildren().addAll(success, detailsBox, actionBox);
        } catch (IllegalArgumentException ex) {
            showAlert("Error", ex.getMessage());
        }
    }

    private void showSummaryPhase() {
        centerPane.getChildren().clear();
        Label title = new Label("Order Summary");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        if (currentOrder.isEmpty()) {
            Label emptyMsg = new Label("No items in the current order.");
            emptyMsg.setStyle("-fx-font-size: 18px; -fx-text-fill: gray;");
            HBox actionBox = new HBox(20);
            actionBox.setAlignment(Pos.CENTER);
            Button returnBtn = new Button("Return / Add More");
            returnBtn.setStyle("-fx-font-size: 16px;");
            returnBtn.setOnAction(e -> showSearchPhase());
            actionBox.getChildren().add(returnBtn);
            centerPane.getChildren().addAll(title, emptyMsg, actionBox);
            return;
        }
        VBox tablesContainer = new VBox(30);
        tablesContainer.setAlignment(Pos.CENTER);
        tablesContainer.setPadding(new Insets(10));
        List<TableView<OrderItem>> allTables = new ArrayList<>();
        Map<String, List<OrderItem>> groupedOrder = currentOrder.stream().collect(Collectors.groupingBy(OrderItem::getSupplierName));
        for (Map.Entry<String, List<OrderItem>> entry : groupedOrder.entrySet()) {
            String supplierName = entry.getKey();
            List<OrderItem> items = entry.getValue();
            SupplierPL supData = onDemandSuppliers.stream().filter(s -> s.getName().equals(supplierName)).findFirst().orElse(null);
            VBox orderBlock = new VBox();
            orderBlock.setMaxWidth(850);
            GridPane grid = new GridPane();
            grid.setHgap(1);
            grid.setVgap(1);
            grid.setStyle("-fx-background-color: black; -fx-border-color: black; -fx-border-width: 1;");
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(16.66);
            grid.getColumnConstraints().addAll(cc, cc, cc, cc, cc, cc);
            Label titleLbl = new Label("Order Details");
            titleLbl.setMaxWidth(Double.MAX_VALUE);
            titleLbl.setAlignment(Pos.CENTER);
            titleLbl.setStyle("-fx-background-color: #c0c0c0; -fx-padding: 5; -fx-font-weight: bold;");
            grid.add(titleLbl, 0, 0, 6, 1);
            String hStyle = "-fx-background-color: #c0c0c0; -fx-padding: 5; -fx-font-weight: bold;";
            String vStyle = "-fx-background-color: white; -fx-padding: 5;";
            Label snH = new Label("Supplier Name:");
            snH.setMaxWidth(Double.MAX_VALUE);
            snH.setStyle(hStyle);
            Label snV = new Label(supplierName);
            snV.setMaxWidth(Double.MAX_VALUE);
            snV.setStyle(vStyle);
            Label adH = new Label("Address:");
            adH.setMaxWidth(Double.MAX_VALUE);
            adH.setStyle(hStyle);
            Label adV = new Label(supData != null ? supData.getAddress() : "N/A");
            adV.setMaxWidth(Double.MAX_VALUE);
            adV.setStyle(vStyle);
            Label onH = new Label("Order No:");
            onH.setMaxWidth(Double.MAX_VALUE);
            onH.setStyle(hStyle);
            Label onV = new Label(String.valueOf(globalOrderCounter));
            onV.setMaxWidth(Double.MAX_VALUE);
            onV.setStyle(vStyle);
            grid.add(snH, 0, 1);
            grid.add(snV, 1, 1);
            grid.add(adH, 2, 1);
            grid.add(adV, 3, 1);
            grid.add(onH, 4, 1);
            grid.add(onV, 5, 1);
            Label snoH = new Label("Supplier No:");
            snoH.setMaxWidth(Double.MAX_VALUE);
            snoH.setStyle(hStyle);
            Label snoV = new Label(supData != null ? supData.getBusinessNumber() : "N/A");
            snoV.setMaxWidth(Double.MAX_VALUE);
            snoV.setStyle(vStyle);
            Label odH = new Label("Order Date:");
            odH.setMaxWidth(Double.MAX_VALUE);
            odH.setStyle(hStyle);
            Label odV = new Label(java.time.LocalDate.now().toString());
            odV.setMaxWidth(Double.MAX_VALUE);
            odV.setStyle(vStyle);
            Label cpH = new Label("Contact Phone:");
            cpH.setMaxWidth(Double.MAX_VALUE);
            cpH.setStyle(hStyle);
            String phones = "N/A";
            if (supData != null && !supData.getContactPersonnel().isEmpty())
                phones = supData.getContactPersonnel().stream().map(cp -> cp.getName() + " (" + cp.getPhone() + ")").collect(Collectors.joining(", "));
            Label cpV = new Label(phones);
            cpV.setMaxWidth(Double.MAX_VALUE);
            cpV.setStyle(vStyle);
            cpV.setWrapText(true);
            grid.add(snoH, 0, 2);
            grid.add(snoV, 1, 2);
            grid.add(odH, 2, 2);
            grid.add(odV, 3, 2);
            grid.add(cpH, 4, 2);
            grid.add(cpV, 5, 2);
            TableView<OrderItem> table = new TableView<>();
            table.setPrefHeight(items.size() * 25 + 30);
            TableColumn<OrderItem, Integer> catCol = new TableColumn<>("Catalog ID");
            catCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCatalogId()));
            TableColumn<OrderItem, String> prodCol = new TableColumn<>("Product Name");
            prodCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductName()));
            TableColumn<OrderItem, Integer> qtyCol = new TableColumn<>("Quantity");
            qtyCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQuantity()));
            TableColumn<OrderItem, String> listCol = new TableColumn<>("List Price");
            listCol.setCellValueFactory(data -> new SimpleStringProperty(String.format("NIS %.2f", data.getValue().getPriceBeforeDiscount())));
            TableColumn<OrderItem, String> discCol = new TableColumn<>("Discount");
            discCol.setCellValueFactory(data -> new SimpleStringProperty(String.format("NIS %.2f", data.getValue().getPriceBeforeDiscount() - data.getValue().getTotalPrice())));
            TableColumn<OrderItem, String> finalCol = new TableColumn<>("Final Price");
            finalCol.setCellValueFactory(data -> new SimpleStringProperty(String.format("NIS %.2f", data.getValue().getTotalPrice())));
            table.getColumns().addAll(catCol, prodCol, qtyCol, listCol, discCol, finalCol);
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            table.getItems().setAll(items);
            allTables.add(table);
            table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null)
                    for (TableView<OrderItem> t : allTables) if (t != table) t.getSelectionModel().clearSelection();
            });
            orderBlock.getChildren().addAll(grid, table);
            tablesContainer.getChildren().add(orderBlock);
        }
        ScrollPane scrollPane = new ScrollPane(tablesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setPrefViewportHeight(300);
        HBox editBox = new HBox(15);
        editBox.setAlignment(Pos.CENTER);
        Button modifyBtn = new Button("Modify Quantity");
        modifyBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        modifyBtn.setOnAction(e -> {
            OrderItem selected = null;
            for (TableView<OrderItem> t : allTables)
                if (t.getSelectionModel().getSelectedItem() != null) {
                    selected = t.getSelectionModel().getSelectedItem();
                    break;
                }
            if (selected != null) {
                OrderItem finalSelected = selected;
                TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getQuantity()));
                dialog.setTitle("Modify Quantity");
                dialog.setHeaderText("New quantity for " + selected.getProductName() + ":");
                dialog.showAndWait().ifPresent(val -> {
                    try {
                        int newQty = Integer.parseInt(val);
                        if (newQty <= 0) throw new NumberFormatException();
                        OrderItem newItem = findCheapestItem(finalSelected.getProductName(), newQty);
                        currentOrder.remove(finalSelected);
                        currentOrder.add(newItem);
                        showSummaryPhase();
                    } catch (NumberFormatException ex) {
                        showAlert("Invalid Input", "Please enter a valid positive number.");
                    } catch (IllegalArgumentException ex) {
                        showAlert("Error", ex.getMessage());
                    }
                });
            } else showAlert("Warning", "Select an item to modify.");
        });
        Button deleteBtn = new Button("Delete Item");
        deleteBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #f44336; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> {
            OrderItem selected = null;
            for (TableView<OrderItem> t : allTables)
                if (t.getSelectionModel().getSelectedItem() != null) {
                    selected = t.getSelectionModel().getSelectedItem();
                    break;
                }
            if (selected != null) {
                currentOrder.remove(selected);
                showSummaryPhase();
            } else showAlert("Warning", "Select an item to delete.");
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
        centerPane.getChildren().addAll(title, scrollPane, editBox, totalLabel, actionBox);
    }

    private void showConfirmationPhase() {
        try {
            List<OrderItemPL> plItems = currentOrder.stream().map(i -> new OrderItemPL(
                    i.getProductName(), i.getSupplierBusinessNumber(), i.getSupplierName(),
                    i.getCatalogId(), i.getQuantity(), i.getPriceBeforeDiscount(), i.getTotalPrice()
            )).collect(Collectors.toList());
            orderController.placeOnDemandOrders(plItems);
            globalOrderCounter++;
            abortBtn.setVisible(false);
            centerPane.getChildren().clear();
            Label confirm = new Label("Order saved and placed successfully!");
            confirm.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: green;");
            VBox messagesBox = new VBox(10);
            messagesBox.setAlignment(Pos.CENTER);
            Set<String> processedSuppliers = new HashSet<>();
            for (OrderItem item : currentOrder)
                if (!processedSuppliers.contains(item.getSupplierName())) {
                    processedSuppliers.add(item.getSupplierName());
                    SupplierPL supData = onDemandSuppliers.stream().filter(s -> s.getName().equals(item.getSupplierName())).findFirst().orElse(null);
                    String address = supData != null ? supData.getAddress() : "their address";
                    String msg = "Supplier '" + item.getSupplierName() + "': A manager will call shortly before the order is ready. ";
                    if (item.isSupplierTransports()) msg += "They will deliver it to us.";
                    else msg += "We need to send a delivery guy to " + address + ".";
                    Label lbl = new Label(msg);
                    lbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                    messagesBox.getChildren().add(lbl);
                }
            Button newOrderBtn = new Button("Start New Order");
            newOrderBtn.setStyle("-fx-font-size: 18px; -fx-padding: 10 30;");
            newOrderBtn.setOnAction(e -> resetToStart());
            centerPane.getChildren().addAll(confirm, messagesBox, newOrderBtn);
        } catch (Exception ex) {
            showAlert("Error", "Failed to save order: " + ex.getMessage());
        }
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
        private final String supplierBusinessNumber;
        private final String supplierName;
        private final int catalogId;
        private final int agreementId;
        private final int quantity;
        private final double priceBeforeDiscount;
        private final double totalPrice;
        private final boolean supplierTransports;

        public OrderItem(String productName, String supplierBusinessNumber, String supplierName, int catalogId, int agreementId, int quantity, double priceBeforeDiscount, double totalPrice, boolean supplierTransports) {
            this.productName = productName;
            this.supplierBusinessNumber = supplierBusinessNumber;
            this.supplierName = supplierName;
            this.catalogId = catalogId;
            this.agreementId = agreementId;
            this.quantity = quantity;
            this.priceBeforeDiscount = priceBeforeDiscount;
            this.totalPrice = totalPrice;
            this.supplierTransports = supplierTransports;
        }

        public String getProductName() {
            return productName;
        }

        public String getSupplierBusinessNumber() {
            return supplierBusinessNumber;
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
    }
}