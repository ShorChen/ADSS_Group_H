package Core.Controller;

import Core.Service.SystemIntegrationService;
import Inventory.Presentation.Controller.InventoryController;
import Suppliers.Presentation.Controller.OrderController;
import Suppliers.Presentation.Controller.SupplierController;
import Transportation.Presentation.Controller.TransportController;
import Core.Service.AuthService;
import Suppliers.Service.Core.OrderService;
import Suppliers.Service.Core.SupplierService;
import Inventory.Service.Core.InventoryService;
import Transportation.Service.Core.TransportService;
import Core.Domain.AuthFacade;
import Suppliers.Domain.Facades.OrderFacade;
import Suppliers.Domain.Facades.SupplierFacade;
import Inventory.Domain.Facades.InventoryFacade;
import Transportation.Domain.Facades.TransportFacade;
import Core.DataAccess.SqlImpl.SqlAuthDAO;
import Suppliers.DataAccess.SqlImpl.SqlOrderDAO;
import Suppliers.DataAccess.SqlImpl.SqlSupplierDAO;
import Inventory.DataAccess.SqlImpl.SqlInventoryDAO;
import Transportation.DataAccess.SqlImpl.SqlTransportDAO;
import Workers.Domain.Service.ShiftService;

public class ControllerFactory {
    private static ControllerFactory instance;
    private AuthController authController;
    private SupplierController supplierController;
    private OrderController orderController;
    private InventoryController inventoryController;
    private TransportController transportController;
    private SystemIntegrationController systemIntegrationController;
    private SupplierFacade supplierFacade;
    private SupplierService supplierService;
    private OrderService orderService;
    private InventoryService inventoryService;
    private TransportService transportService;

    private ControllerFactory() {}

    public static ControllerFactory getInstance() {
        if (instance == null) instance = new ControllerFactory();
        return instance;
    }

    public AuthController getAuthController() {
        if (authController == null) authController = new AuthController(new AuthService(new AuthFacade(new SqlAuthDAO())));
        return authController;
    }

    private SupplierFacade getSupplierFacade() {
        if (supplierFacade == null) supplierFacade = new SupplierFacade(new SqlSupplierDAO());
        return supplierFacade;
    }

    private SupplierService getSupplierService() {
        if (supplierService == null) supplierService = new SupplierService(getSupplierFacade());
        return supplierService;
    }

    public SupplierController getSupplierController() {
        if (supplierController == null) supplierController = new SupplierController(getSupplierService());
        return supplierController;
    }

    private OrderService getOrderService() {
        if (orderService == null) orderService = new OrderService(getSupplierFacade(), new OrderFacade(new SqlOrderDAO()));
        return orderService;
    }

    public OrderController getOrderController() {
        if (orderController == null) orderController = new OrderController(getOrderService());
        return orderController;
    }

    private InventoryService getInventoryService() {
        if (inventoryService == null) inventoryService = new InventoryService(new InventoryFacade(new SqlInventoryDAO()));
        return inventoryService;
    }

    public InventoryController getInventoryController() {
        if (inventoryController == null) inventoryController = new InventoryController(getInventoryService());
        return inventoryController;
    }

    private TransportService getTransportService() {
        if (transportService == null) {
            Transportation.DataAccess.TransportDAO transportDAO = new Transportation.DataAccess.SqlImpl.SqlTransportDAO();
            Transportation.Domain.Facades.TransportFacade transportFacade = new Transportation.Domain.Facades.TransportFacade(transportDAO);
            ShiftService shiftService = new ShiftService();
            transportService = new TransportService(transportFacade, transportDAO, shiftService);
        }
        return transportService;
    }

    public TransportController getTransportController() {
        if (transportController == null) transportController = new TransportController(getTransportService());
        return transportController;
    }

    public SystemIntegrationController getSystemIntegrationController() {
        if (systemIntegrationController == null) systemIntegrationController = new SystemIntegrationController(new SystemIntegrationService(getInventoryService(), getOrderService(), getTransportService()));
        return systemIntegrationController;
    }
}