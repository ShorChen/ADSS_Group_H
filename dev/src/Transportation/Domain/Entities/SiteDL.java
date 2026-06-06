package Transportation.Domain.Entities;

@SuppressWarnings("ClassCanBeRecord")
public class SiteDL {
    private final String siteName;
    private final String address;
    private final String contactPerson;
    private final String phoneNumber;

    public SiteDL(String siteName, String address, String contactPerson, String phoneNumber) {
        this.siteName = siteName;
        this.address = address;
        this.contactPerson = contactPerson;
        this.phoneNumber = phoneNumber;
    }

    public String getSiteName() { return siteName; }
    public String getAddress() { return address; }
    public String getContactPerson() { return contactPerson; }
    public String getPhoneNumber() { return phoneNumber; }
}