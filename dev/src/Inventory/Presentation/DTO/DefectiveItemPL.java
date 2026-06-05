package Inventory.Presentation.DTO;
import java.time.LocalDate;
public record DefectiveItemPL(int defectId, String barcode, int quantity, String location, String reason, LocalDate reportDate) {}