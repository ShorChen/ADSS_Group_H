package Transportation.Domain.Entities;

@SuppressWarnings({"ClassCanBeRecord"})
public class TransportationManager {
    private final String managerId;
    private final String name;

    public TransportationManager(String managerId, String name) {
        this.managerId = managerId;
        this.name = name;
    }

    public String getManagerId() { return managerId; }
    public String getName() { return name; }

    public void authorizeDelivery(DeliveryDL delivery) {
        delivery.setStatus("AUTHORIZED_BY_" + this.managerId);
    }
}