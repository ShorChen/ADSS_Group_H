package Suppliers.Domain;

public class ContactPerson {
    private final String name;
    private String phone;
    private String email;

    public ContactPerson(String name, String phone, String email) {
        this.name  = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName()  { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
}