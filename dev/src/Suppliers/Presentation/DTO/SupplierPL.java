package Suppliers.Presentation.DTO;

import java.util.List;

public record SupplierPL(String name, String businessNumber, String address, List<ContactPersonPL> contactPersonnel,
                         List<AgreementPL> agreements, List<String> manufacturers) {
}