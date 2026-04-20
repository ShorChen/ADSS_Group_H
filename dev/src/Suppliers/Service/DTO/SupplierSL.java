package Suppliers.Service.DTO;

import Suppliers.Domain.Entities.SupplierDL;

import java.util.List;
import java.util.stream.Collectors;

public record SupplierSL(
        String name,
        String businessNumber,
        String address,
        List<ContactPersonSL> contactPersonnel,
        List<AgreementSL> agreements,
        List<String> manufacturers
) {

    public SupplierSL(SupplierDL supplierDL) {
        this(
                supplierDL.getName(),
                supplierDL.getBusinessNumber(),
                supplierDL.getAddress(),
                supplierDL.getContactPersonnel().stream().map(ContactPersonSL::new).collect(Collectors.toList()),
                supplierDL.getAgreements().stream().map(AgreementSL::new).collect(Collectors.toList()),
                supplierDL.getManufacturers()
        );
    }

    /*
    The constructor that fits the trivial solution for OnDemand orders, but Max changed that.
        SupplierSL(SupplierDL supplierDL, List<AgreementSL> onDemandAgreements) {
        name = supplierDL.getName();
        businessNumber = supplierDL.getBusinessNumber();
        address = supplierDL.getAddress();
        contactPersonnel = supplierDL.getContactPersonnel().stream()
                .map(ContactPersonSL::new)
                .collect(Collectors.toList());
        agreements = onDemandAgreements;
        manufacturers = supplierDL.getManufacturers();
    }
     */
}