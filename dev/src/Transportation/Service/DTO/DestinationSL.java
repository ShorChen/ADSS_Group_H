package Transportation.Service.DTO;
import java.util.List;
public record DestinationSL(String destinationSite, List<CargoItemSL> items) {}