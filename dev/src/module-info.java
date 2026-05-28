module Suppliers {
    requires javafx.controls;
    requires java.sql;
    exports Suppliers.Presentation.GUI to javafx.graphics;
    opens Suppliers.Presentation.GUI to javafx.graphics;
    exports Suppliers.Presentation.Controller;
    exports Suppliers.Presentation.DTO;
}