import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private static ShoppingCart instance;
    private final List<CartItem> cartItems;

    private ShoppingCart() {
        cartItems = new ArrayList<>();
    }

    public static ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    public void addProduct(Product product) {
        CartItem cartItem = findCartItemByProduct(product);
        if (cartItem == null) {
            cartItem = new CartItem(product, 1);
            cartItems.add(cartItem);
        } else {
            cartItem.incrementQuantity();
        }
    }

    public void removeProduct(Product product) {
        CartItem cartItem = findCartItemByProduct(product);
        if (cartItem != null) {
            cartItem.decrementQuantity();
            if (cartItem.getQuantity() <= 0) {
                cartItems.remove(cartItem);
            }
        }
    }


    // Count categories of products in the cart
    private Map<String, Integer> countCategories(List<CartItem> cartItems) {
        Map<String, Integer> categoryCount = new HashMap<>();
        for (CartItem cartItem : cartItems) {
            String category = cartItem.getProduct().getClass().getSimpleName();
            categoryCount.merge(category, cartItem.getQuantity(), Integer::sum);
        }
        return categoryCount;
    }

    public double calculateTotalCost() {
        double totalCost = 0.0;
        boolean firstPurchaseDiscount = isFirstPurchase();
        Map<String, Integer> categoryCount = countCategories(cartItems);

        for (CartItem cartItem : cartItems) {
            totalCost += cartItem.getTotalPrice();
        }

        double discount = 0.0;
        if (firstPurchaseDiscount) {
            discount += totalCost * 0.10; // Apply 10% discount for the first purchase
        }

        for (Integer count : categoryCount.values()) {
            if (count >= 3) {
                discount += totalCost * 0.20; // Apply 20% discount for three or more items in the same category
                break; // Assuming the discount is applied only once regardless of how many categories qualify
            }
        }

        return totalCost - discount;
    }


    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems); // Return a copy of the list to prevent external modification
    }

    private boolean isFirstPurchase() {
        // Implement logic to determine if this is the first purchase
        // This could check a database, file, or other data structure that tracks purchase history
        // TODO
        return false;
    }

    private CartItem findCartItemByProduct(Product product) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getProductID().equals(product.getProductID())) {
                return cartItem;
            }
        }
        return null;
    }

    public double calculateFirstPurchaseDiscount() {
        if (isFirstPurchase()) {
            return calculateSubtotal() * 0.10; // 10% discount for the first purchase
        }
        return 0.0;
    }

    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (CartItem cartItem : cartItems) {
            subtotal += cartItem.getTotalPrice();
        }
        return subtotal;
    }

    public double calculateCategoryDiscount() {
        double categoryDiscount = 0.0;
        Map<String, Integer> categoryCount = countCategories(cartItems);

        // Apply discount if at least three items of the same category are found
        for (Integer count : categoryCount.values()) {
            if (count >= 3) {
                categoryDiscount = calculateSubtotal() * 0.20; // 20% discount for three or more items in the same category
                break;
            }
        }

        return categoryDiscount;
    }


    public static class CartItem {
        private final Product product;
        private int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public void incrementQuantity() {
            this.quantity++;
        }

        public void decrementQuantity() {
            this.quantity--;
        }

        public int getQuantity() {
            return this.quantity;
        }

        public Product getProduct() {
            return product;
        }

        public double getTotalPrice() {
            return this.quantity * product.getPrice();
        }
    }
}
