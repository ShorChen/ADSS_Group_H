module Suppliers {
    requires javafx.controls;
    exports Suppliers.Presentation.GUI to javafx.graphics;
    opens Suppliers.Presentation.GUI to javafx.graphics;
    exports Suppliers.Presentation.Controller;
    exports Suppliers.Presentation.DTO;
}