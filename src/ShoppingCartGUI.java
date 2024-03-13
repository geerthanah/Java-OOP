import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ShoppingCartGUI extends JFrame {

    private static ShoppingCartGUI instance;
    static JTable cartTable = new JTable();
    JPanel topPanel = new JPanel();
    JPanel bottomPanel = new JPanel();
    static JLabel subtotalLabel = new JLabel();
    static JLabel firstPurchaseDiscountLabel = new JLabel();
    static JLabel categoryDiscountLabel = new JLabel();
    static JLabel finalTotalLabel = new JLabel();

    // Constructor for CartGUI - sets up the GUI layout and properties
    private ShoppingCartGUI() {
        setTitle("Shopping Cart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);

        add(getTopPanel(), BorderLayout.CENTER);
        add(getBottomPanel(), BorderLayout.SOUTH);
        setVisible(false);
    }

    // Singleton pattern to ensure only one instance of CartGUI
    public static ShoppingCartGUI getInstance() {
        if (instance == null) {
            instance = new ShoppingCartGUI();
        }
        return instance;
    }

    // Creates the top panel with the cart table
    private JPanel getTopPanel() {
        String[] columnNames = {"Product", "Quantity", "Price"};
        DefaultTableModel cartModel = new DefaultTableModel(columnNames, 0);
        cartTable.setModel(cartModel);

        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JScrollPane scrollPane = new JScrollPane(cartTable);
        topPanel.add(scrollPane);

        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return topPanel;
    }

    // Creates the bottom panel with pricing information
    private JPanel getBottomPanel() {
        bottomPanel.setLayout(new GridLayout(0, 2, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        alignLabelsRight(); // Aligns all labels to the right
        addPricingLabelsToPanel(); // Adds pricing labels to the panel

        return bottomPanel;
    }

    // Aligns all labels in the bottom panel to the right
    private void alignLabelsRight() {
        subtotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        firstPurchaseDiscountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        categoryDiscountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        finalTotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    }

    // Adds pricing labels to the bottom panel
    private void addPricingLabelsToPanel() {
        bottomPanel.add(new JLabel("Total: "));
        bottomPanel.add(subtotalLabel);

        bottomPanel.add(new JLabel("First Purchase Discount (10%): "));
        bottomPanel.add(firstPurchaseDiscountLabel);

        bottomPanel.add(new JLabel("Three Items in same Category Discount (20%): "));
        bottomPanel.add(categoryDiscountLabel);

        bottomPanel.add(new JLabel("Final Total: "));
        bottomPanel.add(finalTotalLabel);
    }

    // Adds a product to the cart table and updates the totals
    public void addProductToCart(Product product) {
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
            updateCartModelWithProduct(model, product);
            updateTotals();
        });
    }

    // Updates the cart model with the given product
    private void updateCartModelWithProduct(DefaultTableModel model, Product product) {
        // Check if the product is already in the cart and update the quantity
        for (int i = 0; i < model.getRowCount(); i++) {
            String productName = (String) model.getValueAt(i, 0);
            if (productName.equals(product.getProductName())) {
                int currentQuantity = (int) model.getValueAt(i, 1);
                model.setValueAt(currentQuantity + 1, i, 1); // Update quantity
                return;
            }
        }
        // If the product is not in the cart, add it as a new row
        model.addRow(new Object[]{product.getProductName(), 1, String.format("%.2f £", product.getPrice())});
    }

    // Updates the pricing labels in the bottom panel
    public static void updateTotals() {
        ShoppingCart cart = ShoppingCart.getInstance();

        double subtotal = cart.calculateSubtotal();
        double firstPurchaseDiscount = cart.calculateFirstPurchaseDiscount();
        double categoryDiscount = cart.calculateCategoryDiscount();
        double finalTotal = cart.calculateTotalCost();

        subtotalLabel.setText(String.format("%.2f £", subtotal));
        firstPurchaseDiscountLabel.setText(String.format("-%.2f £", firstPurchaseDiscount));
        categoryDiscountLabel.setText(String.format("-%.2f £", categoryDiscount));
        finalTotalLabel.setText(String.format("%.2f £", finalTotal));
    }

    // Populates the cart table with the current items in the ShoppingCart
    public void populateCartTable() {
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
        model.setRowCount(0); // Clear existing rows

        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        shoppingCart.getCartItems().forEach(cartItem -> {
            Product product = cartItem.getProduct();
            model.addRow(new Object[]{product.getProductName(), cartItem.getQuantity(), String.format("%.2f £", product.getPrice())});
        });

    }
}
