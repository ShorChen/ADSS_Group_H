package Suppliers.Domain;

import java.util.Date;

public class DeliveryTask {
    private final Date collectionTime;
    private final Location pickupLocation;

    public DeliveryTask(Date collectionTime, Location pickupLocation, DeliveryStatus deliveryStatus) {
        this.collectionTime = collectionTime;
        this.pickupLocation = pickupLocation;
    }
}
