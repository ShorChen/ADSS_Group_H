package Suppliers.Service;

import Suppliers.Domain.SupplierDL;
import Suppliers.Domain.SupplierFacade;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final SupplierFacade supplierFacade;

    public OrderService(SupplierFacade supplierFacade) {
        this.supplierFacade = supplierFacade;
    }

    /*
        public Response<List<SupplierSL>> getOnDemandSuppliers() {
        try {
            List<SupplierSL> onDemandSuppliers = new ArrayList<>();
            for (SupplierDL dl : supplierFacade.getOnDemandSuppliers()) {
                List<AgreementSL> onDemandAgreements = new ArrayList<>();
                for (AgreementDL agr : dl.getOnDemandAgreements()) onDemandAgreements.add(new AgreementSL(agr));
                onDemandSuppliers.add(new SupplierSL(dl, onDemandAgreements));
            }
            return new Response<>(onDemandSuppliers);
        } catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }
     */

    // The trivial solution is the one above, as written in the description of the module. But Max said that for
    // suppliers with fixed days agreements, we should allow to make an order in other days too, if it is urgent
    public Response<List<SupplierSL>> getOnDemandSuppliers() {
        try {
            List<SupplierSL> onDemandSuppliers = new ArrayList<>();
            for (SupplierDL dl : supplierFacade.getAllSuppliers())
                onDemandSuppliers.add(new SupplierSL(dl));
            return new Response<>(onDemandSuppliers);
        }
        catch (Exception ex) {
            return new Response<>(ex.getMessage());
        }
    }
}