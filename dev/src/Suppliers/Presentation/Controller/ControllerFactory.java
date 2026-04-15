package Suppliers.Presentation.Controller;

import Suppliers.Domain.Business.SupplierFacade;
import Suppliers.Domain.Service.SupplierService;

public class ControllerFactory {

    private static ControllerFactory instance;
    private final SupplierController supplierController;

    private ControllerFactory() {
        SupplierFacade facade = new SupplierFacade();
        SupplierService service = new SupplierService(facade);
        supplierController = new SupplierController(service);
    }

    public static synchronized ControllerFactory getInstance() {
        if (instance == null)
            instance = new ControllerFactory();
        return instance;
    }

    public SupplierController getSupplierController() {
        return supplierController;
    }
}