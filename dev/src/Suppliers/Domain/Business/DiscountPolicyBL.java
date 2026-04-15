package Suppliers.Domain.Business;

import java.util.*;

public class DiscountPolicyBL {
    private final Map<Integer, List<DiscountBracketBL>> productDiscounts = new HashMap<>();

    public void addBracket(int supplierCatalogId, int minQuantity, double discountPercentage) {
        if (minQuantity <= 0) throw new IllegalArgumentException("Minimum quantity must be greater than zero");
        if (discountPercentage <= 0 || discountPercentage >= 100) throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        productDiscounts.putIfAbsent(supplierCatalogId, new ArrayList<>());
        List<DiscountBracketBL> brackets = productDiscounts.get(supplierCatalogId);
        for (DiscountBracketBL b : brackets) {
            if (b.getMinQuantity() == minQuantity) throw new IllegalArgumentException("Discount bracket for this quantity already exists");
        }
        brackets.add(new DiscountBracketBL(minQuantity, discountPercentage));
        brackets.sort(Comparator.comparingInt(DiscountBracketBL::getMinQuantity).reversed());
    }

    public void removeBracket(int supplierCatalogId, int minQuantity) {
        List<DiscountBracketBL> brackets = productDiscounts.get(supplierCatalogId);
        if (brackets == null) throw new NoSuchElementException("No discounts found for this product");
        boolean removed = brackets.removeIf(b -> b.getMinQuantity() == minQuantity);
        if (!removed) throw new NoSuchElementException("Discount bracket not found");
        if (brackets.isEmpty()) productDiscounts.remove(supplierCatalogId);
    }

    public void updateBracket(int supplierCatalogId, int minQuantity, double newDiscountPercentage) {
        if (newDiscountPercentage <= 0 || newDiscountPercentage >= 100) throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        List<DiscountBracketBL> brackets = productDiscounts.get(supplierCatalogId);
        if (brackets == null) throw new NoSuchElementException("No discounts found for this product");
        for (DiscountBracketBL b : brackets) {
            if (b.getMinQuantity() == minQuantity) {
                b.setDiscountPercentage(newDiscountPercentage);
                return;
            }
        }
        throw new NoSuchElementException("Discount bracket not found");
    }

    public double calculateDiscount(int supplierCatalogId, int quantity) {
        List<DiscountBracketBL> brackets = productDiscounts.get(supplierCatalogId);
        if (brackets == null) return 0.0;
        for (DiscountBracketBL bracket : brackets) {
            if (quantity >= bracket.getMinQuantity()) return bracket.getDiscountPercentage();
        }
        return 0.0;
    }

    public Map<Integer, List<DiscountBracketBL>> getProductDiscounts() { return Collections.unmodifiableMap(productDiscounts); }
}