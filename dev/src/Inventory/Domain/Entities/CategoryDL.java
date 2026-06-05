package Inventory.Domain.Entities;

@SuppressWarnings("ClassCanBeRecord")
public class CategoryDL {
    private final int categoryId;
    private final String name;
    private final Integer parentId;

    public CategoryDL(int categoryId, String name, Integer parentId) {
        this.categoryId = categoryId;
        this.name = name;
        this.parentId = parentId;
    }

    public int getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public Integer getParentId() { return parentId; }
}