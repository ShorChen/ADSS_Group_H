package Employees.Presentation.DTO;

import java.util.List;

public record StoreDetailsPL(
        boolean firstStartUp,
        List<String> closedDays
) {}