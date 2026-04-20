package Suppliers.Service.DTO;

import Suppliers.Domain.Entities.ContactPersonDL;

public class ContactPersonSL {
    private final String name;
    private final String phone;
    private final String email;

    public ContactPersonSL(ContactPersonDL cp) {
        name = cp.getName();
        phone = cp.getPhone();
        email = cp.getEmail();
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}