package Suppliers.Presentation.Controller;

import Suppliers.DataAccess.AuthDAO;
import Suppliers.DataAccess.FileAuthDAO;
import Suppliers.DataAccess.FileSupplierDAO;
import Suppliers.DataAccess.SupplierDAO;
import Suppliers.Domain.AuthFacade;
import Suppliers.Domain.SupplierFacade;
import Suppliers.Service.AuthService;
import Suppliers.Service.OrderService;
import Suppliers.Service.SupplierService;

public class ControllerFactory {

    private static ControllerFactory instance;

    private final SupplierController supplierController;
    private final OrderController orderController;
    private final AuthController authController;

    private ControllerFactory() {
        SupplierDAO supplierDAO = new FileSupplierDAO("dev/src/Suppliers/Table/suppliers.dat");
        AuthDAO authDAO = new FileAuthDAO("dev/src/Suppliers/Table/auth.dat");
        SupplierFacade supplierFacade = new SupplierFacade(supplierDAO);
        AuthFacade authFacade = new AuthFacade(authDAO);
        SupplierService supplierService = new SupplierService(supplierFacade);
        OrderService orderService = new OrderService(supplierFacade);
        AuthService authService = new AuthService(authFacade);
        supplierController = new SupplierController(supplierService);
        orderController = new OrderController(orderService);
        authController = new AuthController(authService);
    }

    public static synchronized ControllerFactory getInstance() {
        if (instance == null)
            instance = new ControllerFactory();
        return instance;
    }

    public SupplierController getSupplierController() { return supplierController; }
    public OrderController getOrderController() { return orderController; }
    public AuthController getAuthController() { return authController; }
}