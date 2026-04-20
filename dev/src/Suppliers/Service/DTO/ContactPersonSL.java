package Suppliers.Service.DTO;

import Suppliers.Domain.Entities.ContactPersonDL;

public record ContactPersonSL(
        String name,
        String phone,
        String email
) {
    public ContactPersonSL(ContactPersonDL cp) {
        this(
                cp.getName(),
                cp.getPhone(),
                cp.getEmail()
        );
    }
}