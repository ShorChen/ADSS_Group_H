package Core.Controller;

import Inventory.Presentation.Controller.InventoryController;
import Suppliers.Presentation.Controller.OrderController;
import Suppliers.Presentation.Controller.SupplierController;
import Transportation.DataAccess.SqlImpl.SqlTransportationDAO;
import Transportation.DataAccess.TransportationDAO;
import Transportation.Domain.Facades.TransportationFacade;
import Transportation.Presentation.Controller.TransportationController;
import Core.Service.AuthService;
import Suppliers.Service.Core.OrderService;
import Suppliers.Service.Core.SupplierService;
import Inventory.Service.Core.InventoryService;
import Transportation.Service.Core.TransportationService;
import Core.Domain.AuthFacade;
import Suppliers.Domain.Facades.OrderFacade;
import Suppliers.Domain.Facades.SupplierFacade;
import Inventory.Domain.Facades.InventoryFacade;
import Core.DataAccess.SqlImpl.SqlAuthDAO;
import Suppliers.DataAccess.SqlImpl.SqlOrderDAO;
import Suppliers.DataAccess.SqlImpl.SqlSupplierDAO;
import Inventory.DataAccess.SqlImpl.SqlInventoryDAO;

// --- NEW IMPORTS FOR EMPLOYEES MODULE ---
import Employees.Domain.Facades.EmployeesFacade;
import Employees.Service.Core.HRService;
import Employees.Service.Core.BranchManagerService;
import Employees.Service.Core.EmployeeService;
import Employees.Presentation.Controller.HRController;
import Employees.Presentation.Controller.BranchManagerController;
import Employees.Presentation.Controller.EmployeeController;

public class ControllerFactory {
    private static ControllerFactory instance;
    private AuthController authController;
    private SupplierController supplierController;
    private OrderController orderController;
    private InventoryController inventoryController;
    private TransportationController transportationController;

    private SupplierFacade supplierFacade;
    private SupplierService supplierService;
    private OrderService orderService;
    private InventoryService inventoryService;
    private TransportationService transportationService;

    // --- NEW INSTANCE VARIABLES ---
    private EmployeesFacade employeesFacade;
    private HRService hrService;
    private BranchManagerService branchManagerService;
    private EmployeeService employeeService;

    private HRController hrController;
    private BranchManagerController branchManagerController;
    private EmployeeController employeeController;

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
        if (inventoryService == null) {
            inventoryService = new InventoryService(new InventoryFacade(new SqlInventoryDAO()));
            inventoryService.setOrderService(getOrderService());
        }
        return inventoryService;
    }

    public InventoryController getInventoryController() {
        if (inventoryController == null) inventoryController = new InventoryController(getInventoryService());
        return inventoryController;
    }

    // --- NEW EMPLOYEE FACTORY METHODS ---
    private EmployeesFacade getEmployeesFacade() {
        if (employeesFacade == null) employeesFacade = new EmployeesFacade();
        return employeesFacade;
    }

    public HRService getHRService() {
        if (hrService == null) hrService = new HRService(getEmployeesFacade());
        return hrService;
    }

    public HRController getHRController() {
        if (hrController == null) hrController = new HRController(getHRService());
        return hrController;
    }

    public BranchManagerService getBranchManagerService() {
        if (branchManagerService == null) branchManagerService = new BranchManagerService(getEmployeesFacade());
        return branchManagerService;
    }

    public BranchManagerController getBranchManagerController() {
        if (branchManagerController == null) branchManagerController = new BranchManagerController(getBranchManagerService());
        return branchManagerController;
    }

    public EmployeeService getEmployeeService() {
        if (employeeService == null) employeeService = new EmployeeService(getEmployeesFacade());
        return employeeService;
    }

    public EmployeeController getEmployeeController() {
        if (employeeController == null) employeeController = new EmployeeController(getEmployeeService());
        return employeeController;
    }

    private TransportationService getTransportService() {
        if (transportationService == null) {
            TransportationDAO transportationDAO = new SqlTransportationDAO();
            TransportationFacade transportationFacade = new TransportationFacade(transportationDAO);
            transportationService = new TransportationService(transportationFacade, transportationDAO, getBranchManagerService());
        }
        return transportationService;
    }

    public TransportationController getTransportController() {
        if (transportationController == null) transportationController = new TransportationController(getTransportService());
        return transportationController;
    }
}