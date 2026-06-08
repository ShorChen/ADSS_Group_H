package Inventory.Service.DTO;
import java.time.LocalDate;
public record DefectiveItemSL(int defectId, String barcode, int quantity, String location, String reason, LocalDate reportDate) {}