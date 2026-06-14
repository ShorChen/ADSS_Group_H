package Core.Service;

import Inventory.Service.Core.InventoryService;
import Inventory.Service.DTO.LowStockAlertSL;
import Suppliers.Service.Core.OrderService;
import Suppliers.Service.DTO.OrderItemSL;
import Transportation.Service.Core.TransportService;
import Transportation.Service.DTO.CargoItemSL;
import Transportation.Service.DTO.DestinationSL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemIntegrationService {
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final TransportService transportService;

    public SystemIntegrationService(InventoryService inventoryService, OrderService orderService, TransportService transportService) {
        this.inventoryService = inventoryService;
        this.orderService = orderService;
        this.transportService = transportService;
    }

    public Response<Boolean> executeDailyIntegration() {
        try {
            Response<Integer> periodicRes = orderService.executeAutomaticOrders();
            if (!periodicRes.isSuccess()) return new Response<>(false, periodicRes.getErrorMessage());
            Response<List<LowStockAlertSL>> shortageRes = inventoryService.generateShortageReport();
            if (!shortageRes.isSuccess()) return new Response<>(false, shortageRes.getErrorMessage());
            List<LowStockAlertSL> alerts = shortageRes.getData();
            if (alerts.isEmpty()) return new Response<>(true, null);
            Map<String, List<OrderItemSL>> ordersBySupplier = new HashMap<>();
            for (LowStockAlertSL alert : alerts) {
                int neededAmount = (alert.minRequired() - alert.currentTotal()) + 1;
                Response<OrderItemSL> cheapestRes = orderService.findCheapest(alert.barcode(), neededAmount);
                if (cheapestRes.isSuccess() && cheapestRes.getData() != null) {
                    OrderItemSL item = cheapestRes.getData();
                    ordersBySupplier.computeIfAbsent(item.supplierBusinessNumber(), k -> new ArrayList<>()).add(item);
                }
            }
            for (Map.Entry<String, List<OrderItemSL>> entry : ordersBySupplier.entrySet()) {
                String bizNum = entry.getKey();
                List<OrderItemSL> items = entry.getValue();
                Response<Integer> orderRes = orderService.placeOrder(bizNum, items);
                if (orderRes.isSuccess()) {
                    boolean supplierTransports = orderService.getSupplierTransports(bizNum);
                    if (!supplierTransports) {
                        List<DestinationSL> destinations = new ArrayList<>();
                        List<CargoItemSL> cargoItems = new ArrayList<>();
                        for (OrderItemSL item : items) {
                            cargoItems.add(new CargoItemSL(item.productName(), 5.0, item.quantity()));
                        }
                        destinations.add(new DestinationSL("Main Logistics Center", cargoItems));
                        transportService.createDelivery(LocalDate.now().plusDays(1), "08:00", "11-222-33", "D001", items.get(0).supplierName(), destinations, "SYSTEM_AUTO");
                    }
                }
            }
            return new Response<>(true, null);
        } catch (Exception e) {
            return new Response<>(false, e.getMessage());
        }
    }
}