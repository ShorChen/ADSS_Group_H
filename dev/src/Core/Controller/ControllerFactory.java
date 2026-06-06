package Core.Controller;

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

public class ControllerFactory {
    private static ControllerFactory instance;
    private final AuthController authController;
    private final SupplierController supplierController;
    private final OrderController orderController;
    private final InventoryController inventoryController;
    private final TransportController transportController;

    private ControllerFactory() {
        SqlAuthDAO authDAO = new SqlAuthDAO();
        SqlSupplierDAO supplierDAO = new SqlSupplierDAO();
        SqlOrderDAO orderDAO = new SqlOrderDAO();
        SqlInventoryDAO inventoryDAO = new SqlInventoryDAO();
        SqlTransportDAO transportDAO = new SqlTransportDAO();
        AuthFacade authFacade = new AuthFacade(authDAO);
        SupplierFacade supplierFacade = new SupplierFacade(supplierDAO);
        OrderFacade orderFacade = new OrderFacade(orderDAO);
        InventoryFacade inventoryFacade = new InventoryFacade(inventoryDAO);
        TransportFacade transportFacade = new TransportFacade(transportDAO);
        AuthService authService = new AuthService(authFacade);
        SupplierService supplierService = new SupplierService(supplierFacade);
        OrderService orderService = new OrderService(supplierFacade, orderFacade);
        InventoryService inventoryService = new InventoryService(inventoryFacade);
        TransportService transportService = new TransportService(transportFacade);
        this.authController = new AuthController(authService);
        this.supplierController = new SupplierController(supplierService);
        this.orderController = new OrderController(orderService);
        this.inventoryController = new InventoryController(inventoryService);
        this.transportController = new TransportController(transportService);
    }

    public static ControllerFactory getInstance() {
        if (instance == null) instance = new ControllerFactory();
        return instance;
    }

    public AuthController getAuthController() { return authController; }
    public SupplierController getSupplierController() { return supplierController; }
    public OrderController getOrderController() { return orderController; }
    public InventoryController getInventoryController() { return inventoryController; }
    public TransportController getTransportController() { return transportController; }
}