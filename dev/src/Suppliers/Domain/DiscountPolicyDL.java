package Suppliers.Domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class DiscountPolicyDL implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<Integer, List<DiscountBracketDL>> productDiscounts = new HashMap<>();

    public void addBracket(int supplierCatalogId, int minQuantity, double discountPercentage) {
        if (minQuantity <= 0) throw new IllegalArgumentException("Minimum quantity must be greater than zero");
        if (discountPercentage <= 0 || discountPercentage >= 100) throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        productDiscounts.putIfAbsent(supplierCatalogId, new ArrayList<>());
        List<DiscountBracketDL> brackets = productDiscounts.get(supplierCatalogId);
        for (DiscountBracketDL b : brackets)
            if (b.getMinQuantity() == minQuantity) throw new IllegalArgumentException("Discount bracket for this quantity already exists");
        brackets.add(new DiscountBracketDL(minQuantity, discountPercentage));
        brackets.sort(Comparator.comparingInt(DiscountBracketDL::getMinQuantity).reversed());
    }

    public void removeBracket(int supplierCatalogId, int minQuantity) {
        List<DiscountBracketDL> brackets = productDiscounts.get(supplierCatalogId);
        if (brackets == null) throw new NoSuchElementException("No discounts found for this product");
        boolean removed = brackets.removeIf(b -> b.getMinQuantity() == minQuantity);
        if (!removed) throw new NoSuchElementException("Discount bracket not found");
        if (brackets.isEmpty()) productDiscounts.remove(supplierCatalogId);
    }

    public void updateBracket(int supplierCatalogId, int minQuantity, double newDiscountPercentage) {
        if (newDiscountPercentage <= 0 || newDiscountPercentage >= 100) throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        List<DiscountBracketDL> brackets = productDiscounts.get(supplierCatalogId);
        if (brackets == null) throw new NoSuchElementException("No discounts found for this product");
        for (DiscountBracketDL b : brackets)
            if (b.getMinQuantity() == minQuantity) {
                b.setDiscountPercentage(newDiscountPercentage);
                return;
            }
        throw new NoSuchElementException("Discount bracket not found");
    }

    public double calculateDiscount(int supplierCatalogId, int quantity) {
        List<DiscountBracketDL> brackets = productDiscounts.get(supplierCatalogId);
        if (brackets == null) return 0.0;
        for (DiscountBracketDL bracket : brackets)
            if (quantity >= bracket.getMinQuantity()) return bracket.getDiscountPercentage();
        return 0.0;
    }

    public Map<Integer, List<DiscountBracketDL>> getProductDiscounts() { return Collections.unmodifiableMap(productDiscounts); }
}