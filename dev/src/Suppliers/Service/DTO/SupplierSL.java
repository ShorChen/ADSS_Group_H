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
}