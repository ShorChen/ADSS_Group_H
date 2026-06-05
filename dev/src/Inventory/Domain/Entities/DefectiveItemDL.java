package Inventory.Domain.Entities;

import java.time.LocalDate;

@SuppressWarnings("ClassCanBeRecord")
public class DefectiveItemDL {
    private final int defectId;
    private final String barcode;
    private final int quantity;
    private final String location;
    private final String reason;
    private final LocalDate reportDate;

    public DefectiveItemDL(int defectId, String barcode, int quantity, String location, String reason, LocalDate reportDate) {
        this.defectId = defectId;
        this.barcode = barcode;
        this.quantity = quantity;
        this.location = location;
        this.reason = reason;
        this.reportDate = reportDate;
    }

    public int getDefectId() { return defectId; }
    public String getBarcode() { return barcode; }
    public int getQuantity() { return quantity; }
    public String getLocation() { return location; }
    public String getReason() { return reason; }
    public LocalDate getReportDate() { return reportDate; }
}