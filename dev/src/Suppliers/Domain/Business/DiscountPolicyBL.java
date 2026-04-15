package Suppliers.Domain.Business;

import java.util.*;

/**
 * We assumed that there is a discount only for each specific product, and not for the total order volume.
 * For example, if I order all products from the supplier, but only one from each product, there will be no discount.
 * We calculate the discount only using the values mapped here, meaning discount tiers are applied
 * individually per product (varying for each product), rather than aggregating the entire order.
 */
public class DiscountPolicyBL {
    private final Map<Integer, List<DiscountBracketBL>> productDiscounts = new HashMap<>();

    public void addBracket(int internalCatalogId, int minQuantity, double discountPercentage) {
        if (minQuantity <= 0) throw new IllegalArgumentException("Minimum quantity must be greater than zero");
        if (discountPercentage <= 0 || discountPercentage >= 100) throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        productDiscounts.putIfAbsent(internalCatalogId, new ArrayList<>());
        List<DiscountBracketBL> brackets = productDiscounts.get(internalCatalogId);
        for (DiscountBracketBL b : brackets)
            if (b.getMinQuantity() == minQuantity) throw new IllegalArgumentException("Discount bracket for this quantity already exists");
        brackets.add(new DiscountBracketBL(minQuantity, discountPercentage));
        brackets.sort(Comparator.comparingInt(DiscountBracketBL::getMinQuantity).reversed());
    }

    public void removeBracket(int internalCatalogId, int minQuantity) {
        List<DiscountBracketBL> brackets = productDiscounts.get(internalCatalogId);
        if (brackets == null) throw new NoSuchElementException("No discounts found for this product");
        boolean removed = brackets.removeIf(b -> b.getMinQuantity() == minQuantity);
        if (!removed) throw new NoSuchElementException("Discount bracket not found");
        if (brackets.isEmpty()) productDiscounts.remove(internalCatalogId);
    }

    public void updateBracket(int internalCatalogId, int minQuantity, double newDiscountPercentage) {
        if (newDiscountPercentage <= 0 || newDiscountPercentage >= 100) throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        List<DiscountBracketBL> brackets = productDiscounts.get(internalCatalogId);
        if (brackets == null) throw new NoSuchElementException("No discounts found for this product");
        for (DiscountBracketBL b : brackets) {
            if (b.getMinQuantity() == minQuantity) {
                b.setDiscountPercentage(newDiscountPercentage);
                return;
            }
        }
        throw new NoSuchElementException("Discount bracket not found");
    }

    public double calculateDiscount(int internalCatalogId, int quantity) {
        List<DiscountBracketBL> brackets = productDiscounts.get(internalCatalogId);
        if (brackets == null) return 0.0;
        for (DiscountBracketBL bracket : brackets)
            if (quantity >= bracket.getMinQuantity()) return bracket.getDiscountPercentage();
        return 0.0;
    }

    public Map<Integer, List<DiscountBracketBL>> getProductDiscounts() { return Collections.unmodifiableMap(productDiscounts); }
}