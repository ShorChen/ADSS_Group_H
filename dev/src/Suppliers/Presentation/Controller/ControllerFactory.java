package Suppliers.Presentation.Controller;

import Suppliers.DataAccess.*;
import Suppliers.DataAccess.FileImpl.FileAuthDAO;
import Suppliers.DataAccess.FileImpl.FileOrderDAO;
import Suppliers.DataAccess.FileImpl.FileSupplierDAO;
import Suppliers.Domain.Facades.AuthFacade;
import Suppliers.Domain.Facades.OrderFacade;
import Suppliers.Domain.Facades.SupplierFacade;
import Suppliers.Service.Core.AuthService;
import Suppliers.Service.Core.OrderService;
import Suppliers.Service.Core.SupplierService;

public class ControllerFactory {
    private static ControllerFactory instance;
    private final SupplierController supplierController;
    private final OrderController orderController;
    private final AuthController authController;

    private ControllerFactory() {
        SupplierDAO supplierDAO = new FileSupplierDAO("dev/src/Suppliers/Table/suppliers.dat");
        AuthDAO authDAO = new FileAuthDAO("dev/src/Suppliers/Table/auth.dat");
        OrderDAO orderDAO = new FileOrderDAO("dev/src/Suppliers/Table/orders.dat");
        SupplierFacade supplierFacade = new SupplierFacade(supplierDAO);
        AuthFacade authFacade = new AuthFacade(authDAO);
        OrderFacade orderFacade = new OrderFacade(orderDAO);
        SupplierService supplierService = new SupplierService(supplierFacade);
        OrderService orderService = new OrderService(supplierFacade, orderFacade);
        AuthService authService = new AuthService(authFacade);
        supplierController = new SupplierController(supplierService);
        orderController = new OrderController(orderService);
        authController = new AuthController(authService);
    }

    public static synchronized ControllerFactory getInstance() {
        if (instance == null) instance = new ControllerFactory();
        return instance;
    }

    public SupplierController getSupplierController() {
        return supplierController;
    }

    public OrderController getOrderController() {
        return orderController;
    }

    public AuthController getAuthController() {
        return authController;
    }
}