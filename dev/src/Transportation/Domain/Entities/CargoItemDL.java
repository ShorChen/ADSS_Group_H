package Transportation.Domain.Entities;

@SuppressWarnings("ClassCanBeRecord")
public class CargoItemDL {
    private final String itemName;
    private final double weight;
    private final int quantity;

    public CargoItemDL(String itemName, double weight, int quantity) {
        this.itemName = itemName;
        this.weight = weight;
        this.quantity = quantity;
    }

    public String getItemName() { return itemName; }
    public double getWeight() { return weight; }
    public int getQuantity() { return quantity; }
}