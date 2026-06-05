module SuperLee {
    requires javafx.controls;
    requires java.sql;

    // Core Module GUI
    exports Core.App to javafx.graphics;
    opens Core.App to javafx.graphics;

    // Suppliers Module GUI
    exports Suppliers.Presentation.GUI to javafx.graphics;
    opens Suppliers.Presentation.GUI to javafx.graphics;

    // Inventory Module GUI
    exports Inventory.Presentation.GUI to javafx.graphics;
    opens Inventory.Presentation.GUI to javafx.graphics;
}