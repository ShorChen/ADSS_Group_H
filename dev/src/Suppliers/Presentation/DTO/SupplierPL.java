package Suppliers.Presentation.DTO;

import java.util.List;

public class SupplierPL {
    private final String name;
    private final String businessNumber;
    private final String address;
    private final List<ContactPersonPL> contactPersonnel;
    private final List<AgreementPL> agreements;
    private final List<String> manufacturers;

    public SupplierPL(String name, String businessNumber, String address, List<ContactPersonPL> contactPersonnel, List<AgreementPL> agreements, List<String> manufacturers) {
        this.name = name;
        this.businessNumber = businessNumber;
        this.address = address;
        this.contactPersonnel = contactPersonnel;
        this.agreements = agreements;
        this.manufacturers = manufacturers;
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

    public List<ContactPersonPL> getContactPersonnel() {
        return contactPersonnel;
    }

    public List<AgreementPL> getAgreements() {
        return agreements;
    }

    public List<String> getManufacturers() {
        return manufacturers;
    }
}