package Suppliers.Domain;

import Suppliers.Domain.Facades.OrderFacade;
import Suppliers.Domain.Facades.SupplierFacade;
import Suppliers.Domain.Entities.AgreementDL;
import Suppliers.Domain.ValueObjects.OrderItemDL;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DataSeeder {

    public static void loadExampleSuppliers(SupplierFacade supplierFacade) {
        System.out.println("Seeding example suppliers...");
        supplierFacade.addSupplier("Tnuva Dairy", "511111111", "Tel Aviv", "IL1234567891234567891234567", "Net 30");
        supplierFacade.addContactPerson("511111111", "Yossi Cohen", "054-1234567", "yossi@tnuva.co.il");
        List<DayOfWeek> days1 = new ArrayList<>(); days1.add(DayOfWeek.SUNDAY);
        AgreementDL tnuvaAgreement = supplierFacade.addAgreement("511111111", days1, true);
        supplierFacade.addProductLine("511111111", tnuvaAgreement.getAgreementId(), 101, "Milk 3%", 5.90, 1000);
        supplierFacade.addDiscount("511111111", tnuvaAgreement.getAgreementId(), 101, 100, 5.0);
        supplierFacade.addSupplier("Osem", "522222222", "Petah Tikva", "IL1234567891234567891234567", "Net + 60");
        supplierFacade.addContactPerson("522222222", "Avi Israeli", "050-1112233", "avi@osem.co.il");
        List<DayOfWeek> days2 = new ArrayList<>(); days2.add(DayOfWeek.MONDAY);
        AgreementDL osemAgreement = supplierFacade.addAgreement("522222222", days2, false);
        supplierFacade.addProductLine("522222222", osemAgreement.getAgreementId(), 201, "Bamba", 4.50, 2000);
    }

    public static void loadExampleOrders(OrderFacade orderFacade) {
        System.out.println("Seeding example orders...");
        List<OrderItemDL> tnuvaItems = new ArrayList<>();
        tnuvaItems.add(new OrderItemDL(101, "Milk 3%", 150, 5.90, 5.0, 5.605));
        orderFacade.createOrder("511111111", "Tnuva Dairy", "Tel Aviv", "054-1234567", tnuvaItems);
    }
}