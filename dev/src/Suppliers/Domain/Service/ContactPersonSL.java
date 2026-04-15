package Suppliers.Domain.Service;

import Suppliers.Domain.Business.ContactPersonBL;

public class ContactPersonSL {
    private final String name;
    private final String phone;
    private final String email;

    ContactPersonSL(ContactPersonBL cp) {
        this.name = cp.getName();
        this.phone = cp.getPhone();
        this.email = cp.getEmail();
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}