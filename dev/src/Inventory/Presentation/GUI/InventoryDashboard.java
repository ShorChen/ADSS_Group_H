package Inventory.Presentation.GUI;

import Core.Controller.ControllerFactory;
import Inventory.Presentation.Controller.InventoryController;
import Inventory.Presentation.DTO.LowStockAlertPL;
import Inventory.Presentation.DTO.ProductPL;
import Inventory.Presentation.DTO.InventoryReportPL;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryDashboard {
    private final Scene scene;
    private final InventoryController inventoryController;
    private final TableView<ProductPL> productsTable;

    public InventoryDashboard() {
        inventoryController = ControllerFactory.getInstance().getInventoryController();
        productsTable = new TableView<>();
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> System.out.println("Logout triggered - replace with routing logic"));
        HBox topBar = new HBox(15, new Region(), logoutBtn);
        HBox.setHgrow(topBar.getChildren().get(0), Priority.ALWAYS);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #333333;");
        TabPane tabPane = new TabPane();
        Tab productsTab = new Tab("Products", createProductsView());
        productsTab.setClosable(false);
        Tab categoriesTab = new Tab("Categories", createCategoriesView());
        categoriesTab.setClosable(false);
        Tab defectsTab = new Tab("Defective Items", createDefectsView());
        defectsTab.setClosable(false);
        Tab promotionsTab = new Tab("Promotions", createPromotionsView());
        promotionsTab.setClosable(false);
        Tab reportsTab = new Tab("Reports", createReportsView());
        reportsTab.setClosable(false);
        tabPane.getTabs().addAll(productsTab, categoriesTab, defectsTab, promotionsTab, reportsTab);
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);
        mainLayout.setCenter(tabPane);
        scene = new Scene(mainLayout, 1000, 700);
        refreshProductsTable();
    }

    private VBox createProductsView() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        HBox searchBox = new HBox(10);
        TextField searchIdField = new TextField();
        searchIdField.setPromptText("Search by Barcode");
        Button searchIdBtn = new Button("Search ID");
        searchIdBtn.setOnAction(e -> {
            try {
                List<ProductPL> all = inventoryController.getAllProducts();
                List<ProductPL> filtered = all.stream().filter(p -> p.barcode().equals(searchIdField.getText())).toList();
                productsTable.getItems().setAll(filtered);
            } catch (Exception ex) { showAlert("Error", ex.getMessage()); }
        });
        TextField searchCatField = new TextField();
        searchCatField.setPromptText("Search by Category ID");
        Button searchCatBtn = new Button("Search Category");
        searchCatBtn.setOnAction(e -> {
            try {
                List<ProductPL> all = inventoryController.getAllProducts();
                List<ProductPL> filtered = all.stream().filter(p -> p.categoryId() == Integer.parseInt(searchCatField.getText())).toList();
                productsTable.getItems().setAll(filtered);
            } catch (Exception ex) { showAlert("Error", ex.getMessage()); }
        });
        Button resetSearchBtn = new Button("Show All");
        resetSearchBtn.setOnAction(e -> refreshProductsTable());
        searchBox.getChildren().addAll(searchIdField, searchIdBtn, searchCatField, searchCatBtn, resetSearchBtn);
        HBox formBox = new HBox(10);
        TextField barcodeField = new TextField();
        barcodeField.setPromptText("Barcode");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField mfgField = new TextField();
        mfgField.setPromptText("Manufacturer");
        TextField catField = new TextField();
        catField.setPromptText("Category ID");
        TextField costField = new TextField();
        costField.setPromptText("Cost Price");
        TextField sellField = new TextField();
        sellField.setPromptText("Selling Price");
        TextField minField = new TextField();
        minField.setPromptText("Min Qty");
        TextField aisleField = new TextField();
        aisleField.setPromptText("Aisle");
        TextField posField = new TextField();
        posField.setPromptText("Position");
        Button addBtn = new Button("Add Product");
        addBtn.setOnAction(e -> {
            try {
                inventoryController.addProduct(barcodeField.getText(), nameField.getText(), mfgField.getText(), Integer.parseInt(catField.getText()), Double.parseDouble(costField.getText()), Double.parseDouble(sellField.getText()), Integer.parseInt(minField.getText()), 0, 0, aisleField.getText(), Integer.parseInt(posField.getText()));
                refreshProductsTable();
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        formBox.getChildren().addAll(barcodeField, nameField, mfgField, catField, costField, sellField, minField, aisleField, posField, addBtn);
        TableColumn<ProductPL, String> bCol = new TableColumn<>("Barcode");
        bCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().barcode()));
        TableColumn<ProductPL, String> nCol = new TableColumn<>("Name");
        nCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().name()));
        TableColumn<ProductPL, Double> sCol = new TableColumn<>("Sell Price");
        sCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().sellingPrice()).asObject());
        TableColumn<ProductPL, Integer> shelfCol = new TableColumn<>("Shelf");
        shelfCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().shelfQuantity()).asObject());
        TableColumn<ProductPL, Integer> wareCol = new TableColumn<>("Warehouse");
        wareCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().warehouseQuantity()).asObject());
        TableColumn<ProductPL, String> aCol = new TableColumn<>("Aisle");
        aCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().aisle()));
        TableColumn<ProductPL, Integer> pCol = new TableColumn<>("Position");
        pCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().position()).asObject());
        productsTable.getColumns().addAll(List.of(bCol, nCol, sCol, shelfCol, wareCol, aCol, pCol));
        productsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        HBox qtyBox = new HBox(10);
        TextField qtyBarcode = new TextField();
        qtyBarcode.setPromptText("Barcode");
        TextField newShelf = new TextField();
        newShelf.setPromptText("New Shelf Qty");
        TextField newWare = new TextField();
        newWare.setPromptText("New Warehouse Qty");
        Button updateQtyBtn = new Button("Update Quantities");
        updateQtyBtn.setOnAction(e -> {
            try {
                inventoryController.updateProductQuantities(qtyBarcode.getText(), Integer.parseInt(newShelf.getText()), Integer.parseInt(newWare.getText()));
                refreshProductsTable();
                checkAutomaticAlert(qtyBarcode.getText());
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        qtyBox.getChildren().addAll(qtyBarcode, newShelf, newWare, updateQtyBtn);
        HBox priceBox = new HBox(10);
        TextField priceBarcode = new TextField();
        priceBarcode.setPromptText("Barcode");
        TextField newPrice = new TextField();
        newPrice.setPromptText("New Selling Price");
        TextField newMin = new TextField();
        newMin.setPromptText("New Min Alert Qty");
        Button updatePriceBtn = new Button("Update Price & Alerts");
        updatePriceBtn.setOnAction(e -> {
            try {
                inventoryController.updateProductPricing(priceBarcode.getText(), Double.parseDouble(newPrice.getText()), Integer.parseInt(newMin.getText()));
                refreshProductsTable();
                checkAutomaticAlert(priceBarcode.getText());
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        Button deleteBtn = new Button("Delete Product");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> {
            try {
                inventoryController.deleteProduct(priceBarcode.getText());
                refreshProductsTable();
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        priceBox.getChildren().addAll(priceBarcode, newPrice, newMin, updatePriceBtn, deleteBtn);
        HBox transferBox = new HBox(10);
        TextField transBarcode = new TextField();
        transBarcode.setPromptText("Barcode");
        TextField transAmount = new TextField();
        transAmount.setPromptText("Amount to Transfer");
        Button transBtn = getTransBtn(transBarcode, transAmount);
        transferBox.getChildren().addAll(transBarcode, transAmount, transBtn);
        layout.getChildren().addAll(new Label("Search Products"), searchBox, new Label("Add New Product"), formBox, new Label("Update/Transfer Quantities"), qtyBox, transferBox, new Label("Edit Details"), priceBox, productsTable);
        return layout;
    }

    private Button getTransBtn(TextField transBarcode, TextField transAmount) {
        Button transBtn = new Button("Transfer to Shelf");
        transBtn.setOnAction(e -> {
            try {
                inventoryController.transferWarehouseToShelf(transBarcode.getText(), Integer.parseInt(transAmount.getText()));
                refreshProductsTable();
                showAlert("Success", "Units transferred to shelf.");
                checkAutomaticAlert(transBarcode.getText());
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        return transBtn;
    }

    private void checkAutomaticAlert(String barcode) {
        List<LowStockAlertPL> alerts = inventoryController.generateShortageReport();
        for (LowStockAlertPL alert : alerts) {
            if (alert.barcode().equals(barcode)) {
                Alert popup = new Alert(Alert.AlertType.WARNING);
                popup.setTitle("AUTOMATIC LOW STOCK ALERT");
                popup.setHeaderText("Stock dropped below minimum!");
                popup.setContentText("Product: " + alert.productName() + "\nCurrent Total: " + alert.currentTotal() + "\nMinimum Required: " + alert.minRequired());
                popup.showAndWait();
            }
        }
    }

    private VBox createCategoriesView() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        HBox formBox = new HBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Category Name");
        TextField parentField = new TextField();
        parentField.setPromptText("Parent ID (Optional)");
        Button addBtn = new Button("Add Category");
        ListView<String> catList = new ListView<>();
        addBtn.setOnAction(e -> {
            try {
                Integer parentId = parentField.getText().isEmpty() ? null : Integer.parseInt(parentField.getText());
                inventoryController.addCategory(nameField.getText(), parentId);
                catList.getItems().setAll(inventoryController.getAllCategories().stream().map(c -> "ID: " + c.categoryId() + " | Name: " + c.name() + " | Parent: " + c.parentId()).collect(Collectors.toList()));
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        formBox.getChildren().addAll(nameField, parentField, addBtn);
        HBox delBox = new HBox(10);
        TextField delCatField = new TextField();
        delCatField.setPromptText("Category ID to Delete");
        Button delBtn = getButton(delCatField, catList);
        delBox.getChildren().addAll(delCatField, delBtn);
        Button refreshBtn = new Button("Load Categories");
        refreshBtn.setOnAction(e -> catList.getItems().setAll(inventoryController.getAllCategories().stream().map(c -> "ID: " + c.categoryId() + " | Name: " + c.name() + " | Parent: " + c.parentId()).collect(Collectors.toList())));
        layout.getChildren().addAll(new Label("Manage Hierarchy"), formBox, delBox, refreshBtn, catList);
        return layout;
    }

    private Button getButton(TextField delCatField, ListView<String> catList) {
        Button delBtn = new Button("Delete Category");
        delBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        delBtn.setOnAction(e -> {
            try {
                inventoryController.deleteCategory(Integer.parseInt(delCatField.getText()));
                catList.getItems().setAll(inventoryController.getAllCategories().stream().map(c -> "ID: " + c.categoryId() + " | Name: " + c.name() + " | Parent: " + c.parentId()).collect(Collectors.toList()));
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        return delBtn;
    }

    private VBox createDefectsView() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        HBox formBox = new HBox(10);
        TextField barcodeField = new TextField();
        barcodeField.setPromptText("Barcode");
        TextField qtyField = new TextField();
        qtyField.setPromptText("Quantity");
        ComboBox<String> locationBox = new ComboBox<>();
        locationBox.getItems().addAll("SHELF", "WAREHOUSE");
        locationBox.setPromptText("Location");
        TextField reasonField = new TextField();
        reasonField.setPromptText("Reason (e.g. Expired)");
        Button reportBtn = new Button("Report Defect");
        ListView<String> defectList = new ListView<>();
        reportBtn.setOnAction(e -> {
            try {
                inventoryController.reportDefectiveItem(barcodeField.getText(), Integer.parseInt(qtyField.getText()), locationBox.getValue(), reasonField.getText());
                showAlert("Success", "Defect recorded and quantities adjusted.");
                refreshProductsTable();
                defectList.getItems().setAll(inventoryController.getAllDefectiveItems().stream().map(d -> "ID: " + d.defectId() + " | Barcode: " + d.barcode() + " | Qty: " + d.quantity() + " | Reason: " + d.reason()).collect(Collectors.toList()));
                checkAutomaticAlert(barcodeField.getText());
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        formBox.getChildren().addAll(barcodeField, qtyField, locationBox, reasonField, reportBtn);
        Button loadBtn = getLoadBtn(defectList);
        layout.getChildren().addAll(new Label("Report Defective or Expired Item"), formBox, loadBtn, defectList);
        return layout;
    }

    private Button getLoadBtn(ListView<String> defectList) {
        Button loadBtn = new Button("Load Defect History");
        loadBtn.setOnAction(e -> {
            try {
                defectList.getItems().setAll(inventoryController.getAllDefectiveItems().stream().map(d -> "ID: " + d.defectId() + " | Barcode: " + d.barcode() + " | Qty: " + d.quantity() + " | Reason: " + d.reason() + " | Date: " + d.reportDate().toString()).collect(Collectors.toList()));
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        return loadBtn;
    }

    private VBox createPromotionsView() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        HBox formBox = new HBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Promo Name");
        TextField discField = new TextField();
        discField.setPromptText("Discount %");
        DatePicker startPicker = new DatePicker();
        startPicker.setPromptText("Start Date");
        DatePicker endPicker = new DatePicker();
        endPicker.setPromptText("End Date");
        ComboBox<String> targetType = new ComboBox<>();
        targetType.getItems().addAll("CATEGORY", "PRODUCT");
        targetType.setPromptText("Target Type");
        TextField targetId = new TextField();
        targetId.setPromptText("Target ID/Barcode");
        Button addBtn = new Button("Create Promotion");
        ListView<String> promoList = new ListView<>();
        addBtn.setOnAction(e -> {
            try {
                inventoryController.addPromotion(nameField.getText(), Double.parseDouble(discField.getText()), startPicker.getValue(), endPicker.getValue(), targetType.getValue(), targetId.getText());
                showAlert("Success", "Promotion Created");
                promoList.getItems().setAll(inventoryController.getAllPromotions().stream().map(p -> "ID: " + p.promoId() + " | Name: " + p.name() + " | Target: " + p.targetId() + " | Status: " + (p.isActive() ? "ACTIVE" : "EXPIRED")).collect(Collectors.toList()));
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        formBox.getChildren().addAll(nameField, discField, startPicker, endPicker, targetType, targetId, addBtn);
        HBox delBox = new HBox(10);
        TextField delPromoField = new TextField();
        delPromoField.setPromptText("Promo ID to Delete");
        Button delBtn = getDelBtn(delPromoField, promoList);
        delBox.getChildren().addAll(delPromoField, delBtn);
        Button loadBtn = new Button("Load Promotions");
        loadBtn.setOnAction(e -> {
            try {
                promoList.getItems().setAll(inventoryController.getAllPromotions().stream().map(p -> "ID: " + p.promoId() + " | Name: " + p.name() + " | Target: " + p.targetId() + " | Status: " + (p.isActive() ? "ACTIVE" : "EXPIRED")).collect(Collectors.toList()));
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        layout.getChildren().addAll(new Label("Store Promotions"), formBox, delBox, loadBtn, promoList);
        return layout;
    }

    private Button getDelBtn(TextField delPromoField, ListView<String> promoList) {
        Button delBtn = new Button("Delete Promotion");
        delBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        delBtn.setOnAction(e -> {
            try {
                inventoryController.deletePromotion(Integer.parseInt(delPromoField.getText()));
                promoList.getItems().setAll(inventoryController.getAllPromotions().stream().map(p -> "ID: " + p.promoId() + " | Name: " + p.name() + " | Target: " + p.targetId() + " | Status: " + (p.isActive() ? "ACTIVE" : "EXPIRED")).collect(Collectors.toList()));
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        return delBtn;
    }

    private VBox createReportsView() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        Button shortageBtn = new Button("Run Shortage Report");
        ListView<String> reportList = new ListView<>();
        shortageBtn.setOnAction(e -> {
            try {
                List<LowStockAlertPL> shortages = inventoryController.generateShortageReport();
                if (shortages.isEmpty()) reportList.getItems().setAll("All items are fully stocked.");
                else reportList.getItems().setAll(shortages.stream().map(a -> "ALERT - " + a.productName() + " (Barcode: " + a.barcode() + ") | Current Total: " + a.currentTotal() + " | Min Required: " + a.minRequired()).collect(Collectors.toList()));
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        HBox catBox = new HBox(10);
        TextField catIdField = new TextField();
        catIdField.setPromptText("Enter Category ID");
        Button catReportBtn = getCatReportBtn(catIdField, reportList);
        catBox.getChildren().addAll(catIdField, catReportBtn);
        layout.getChildren().addAll(shortageBtn, catBox, reportList);
        return layout;
    }

    private Button getCatReportBtn(TextField catIdField, ListView<String> reportList) {
        Button catReportBtn = new Button("Run Category Report");
        catReportBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(catIdField.getText());
                InventoryReportPL report = inventoryController.generateCategoryReport(id);
                if (report.items().isEmpty()) reportList.getItems().setAll("No products found in category.");
                else reportList.getItems().setAll(report.items().stream().map(p -> p.name() + " | Total Qty: " + (p.shelfQuantity() + p.warehouseQuantity()) + " | Aisle: " + p.aisle()).collect(Collectors.toList()));
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        return catReportBtn;
    }

    private void refreshProductsTable() {
        try {
            productsTable.getItems().setAll(inventoryController.getAllProducts());
        } catch (Exception e) {
            showAlert("Error loading products", e.getMessage());
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