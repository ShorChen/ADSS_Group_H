module SuperLee {
    requires javafx.controls;
    requires java.sql;
    requires org.jetbrains.annotations;
    requires jdk.jshell;
    exports Core.App to javafx.graphics;
    opens Core.App to javafx.graphics;
    exports Suppliers.Presentation.GUI to javafx.graphics;
    opens Suppliers.Presentation.GUI to javafx.graphics;
    exports Inventory.Presentation.GUI to javafx.graphics;
    opens Inventory.Presentation.GUI to javafx.graphics;
    exports Transportation.Presentation.GUI to javafx.graphics;
    opens Transportation.Presentation.GUI to javafx.graphics;
    exports Workers.Presentation.GUI to javafx.graphics;
    opens Workers.Presentation.GUI to javafx.graphics;
    exports Core.Navigation;
}