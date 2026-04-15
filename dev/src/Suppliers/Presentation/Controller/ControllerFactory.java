package Suppliers.Presentation.Controller;

import Suppliers.Domain.Business.AuthFacade;
import Suppliers.Domain.Business.SupplierFacade;
import Suppliers.Domain.Service.AuthService;
import Suppliers.Domain.Service.OrderService;
import Suppliers.Domain.Service.SupplierService;

public class ControllerFactory {
    private static ControllerFactory instance;
    private final SupplierController supplierController;
    private final OrderController orderController;
    private final AuthController authController;

    private ControllerFactory() {
        SupplierFacade supplierFacade = new SupplierFacade();
        AuthFacade authFacade = new AuthFacade();
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