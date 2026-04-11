package Suppliers.Domain;

import java.util.*;

/**
 * We assumed that there is a discount only for each specific product, and not for the total order volume.
 * For example, if I order all products from the supplier, but only one from each product, there will be no discount.
 * We calculate the discount only using the values mapped here, meaning discount tiers are applied
 * individually per product (varying for each product), rather than aggregating the entire order.
 */
public class BracketDiscountPolicy {

    private static class Bracket {
        int minQuantity;
        double discountPercentage;

        Bracket(int minQuantity, double discountPercentage) {
            this.minQuantity = minQuantity;
            this.discountPercentage = discountPercentage;
        }
    }

    private final Map<Integer, List<Bracket>> productDiscounts = new HashMap<>();

    public void addBracket(Integer catalogNumber, int minQuantity, double discountPercentage) {
        productDiscounts.putIfAbsent(catalogNumber, new ArrayList<>());
        List<Bracket> brackets = productDiscounts.get(catalogNumber);
        brackets.add(new Bracket(minQuantity, discountPercentage));
        brackets.sort(Comparator.comparingInt((Bracket b) -> b.minQuantity).reversed());
    }

    public void removeBracket(Integer catalogNumber, int minQuantity) {
        List<Bracket> brackets = productDiscounts.get(catalogNumber);
        if (brackets != null) {
            Iterator<Bracket> it = brackets.iterator();
            while (it.hasNext())
                if (it.next().minQuantity == minQuantity) {
                    it.remove();
                    break;
                }
            if (brackets.isEmpty())
                removeAllBracketsForProduct(catalogNumber);
        }
    }

    public void removeAllBracketsForProduct(Integer catalogNumber) {
        productDiscounts.remove(catalogNumber);
    }

    public double calculateDiscount(Integer catalogNumber, int quantity) {
        List<Bracket> brackets = productDiscounts.get(catalogNumber);
        if (brackets == null)
            return 0.0;
        for (Bracket bracket : brackets)
            if (quantity >= bracket.minQuantity)
                return bracket.discountPercentage;
        return 0.0;
    }

    public boolean hasDiscountForProduct(int catalogNumber) {
        return productDiscounts.containsKey(catalogNumber);
    }
}