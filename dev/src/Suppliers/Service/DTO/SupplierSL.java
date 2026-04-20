package Suppliers.Service.DTO;

import Suppliers.Domain.Entities.SupplierDL;

import java.util.List;
import java.util.stream.Collectors;

public class SupplierSL {
    private final String name;
    private final String businessNumber;
    private final String address;
    private final List<ContactPersonSL> contactPersonnel;
    private final List<AgreementSL> agreements;
    private final List<String> manufacturers;

    public SupplierSL(SupplierDL supplierDL) {
        name = supplierDL.getName();
        businessNumber = supplierDL.getBusinessNumber();
        address = supplierDL.getAddress();
        contactPersonnel = supplierDL.getContactPersonnel().stream()
                .map(ContactPersonSL::new)
                .collect(Collectors.toList());
        agreements = supplierDL.getAgreements().stream()
                .map(AgreementSL::new)
                .collect(Collectors.toList());
        manufacturers = supplierDL.getManufacturers();
    }

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

    public String getName() {
        return name;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public String getAddress() {
        return address;
    }

    public List<ContactPersonSL> getContactPersonnel() {
        return contactPersonnel;
    }

    public List<AgreementSL> getAgreements() {
        return agreements;
    }

    public List<String> getManufacturers() {
        return manufacturers;
    }
}