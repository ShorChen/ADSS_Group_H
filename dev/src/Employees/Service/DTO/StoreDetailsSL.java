package Employees.Service.DTO;

import java.util.List;

public record StoreDetailsSL(
        boolean firstStartUp,
        List<String> closedDays
) {}