package Transportation.Presentation.DTO;
import java.util.List;
public record DestinationPL(String destinationSite, List<CargoItemPL> items) {}