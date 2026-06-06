package Transportation.Domain.Entities;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class DestinationDL {
    private final String destinationSite;
    private final List<CargoItemDL> items;

    public DestinationDL(String destinationSite, List<CargoItemDL> items) {
        this.destinationSite = destinationSite;
        this.items = items;
    }

    public String getDestinationSite() { return destinationSite; }
    public List<CargoItemDL> getItems() { return items; }
}