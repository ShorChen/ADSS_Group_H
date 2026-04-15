package Suppliers.Domain.Service;

import Suppliers.Domain.Business.SupplierBL;

import java.util.List;
import java.util.stream.Collectors;

public class SupplierSL {
    private final String name;
    private final String businessNumber;
    private final String address;
    private final List<ContactPersonSL> contactPersonnel;
    private final List<AgreementSL> agreements;
    private final List<String> manufacturers;

    SupplierSL(SupplierBL supplierBL) {
        this.name = supplierBL.getName();
        this.businessNumber = supplierBL.getBusinessNumber();
        this.address = supplierBL.getAddress();
        this.contactPersonnel = supplierBL.getContactPersonnel().stream()
                .map(ContactPersonSL::new)
                .collect(Collectors.toList());
        this.agreements = supplierBL.getAgreements().stream()
                .map(AgreementSL::new)
                .collect(Collectors.toList());
        this.manufacturers = supplierBL.getManufacturers();
    }

    public String getName() { return name; }
    public String getBusinessNumber() { return businessNumber; }
    public String getAddress() { return address; }
    public List<ContactPersonSL> getContactPersonnel() { return contactPersonnel; }
    public List<AgreementSL> getAgreements() { return agreements; }
    public List<String> getManufacturers() { return manufacturers; }
}