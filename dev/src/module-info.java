module Suppliers {
    exports Suppliers.Presentation;
    exports Suppliers.Presentation.Controller;
    requires javafx.controls;
    exports Suppliers.Presentation.GUI to javafx.graphics;
}