package Suppliers.Domain;

public record Category(int id, String name, Category parent) {

    public Category(int id, String name) {
        this(id, name, null);
    }

    public boolean isTopLevel() {
        return parent == null;
    }

    @Override
    public String toString() {
        return parent == null ? name : parent + " > " + name;
    }
}