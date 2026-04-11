package Suppliers.Domain;

public record Product(int catalogNumber, String name, Manufacturer manufacturer, Category category) {
}