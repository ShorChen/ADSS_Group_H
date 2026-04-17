package Suppliers.Service;

import Suppliers.Domain.AgreementDL;
import Suppliers.Domain.SupplierDL;
import Suppliers.Domain.SupplierFacade;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final SupplierFacade supplierFacade;

    public OrderService(SupplierFacade supplierFacade) {
        this.supplierFacade = supplierFacade;
    }

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
}