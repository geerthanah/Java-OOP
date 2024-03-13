import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingAppGUI extends JFrame {

    private static ShoppingAppGUI instance;
    private final JPanel topPanel = new JPanel();
    private final JPanel middlePanel = new JPanel();
    private final JPanel bottomPanel = new JPanel();
    private JTable productTable = new JTable();
    private JComboBox<String> dropDown = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
    private List<Product> productList = WestminsterShoppingManager.getProductList();

    private JLabel productIdLabel, categoryLabel, nameLabel, sizeLabel, colourLabel, itemsAvailableLabel, brandLabel, warrantyLabel;

    // Constructor for ShoppingGUI
    public ShoppingAppGUI() {
        setupGUI();
    }

    // Singleton pattern to ensure only one instance of ShoppingGUI
    public static ShoppingAppGUI getInstance() {
        if (instance == null) {
            instance = new ShoppingAppGUI();
        }
        return instance;
    }

    // Method to setup the GUI components
    private void setupGUI() {
        setTitle("Westminster Shopping Centre");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        add(getTopPanel(), BorderLayout.NORTH);
        add(getMiddlePanel(), BorderLayout.CENTER);
        add(getBottomPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // Method to create and return the top panel
    private JPanel getTopPanel() {
        JButton shoppingCartButton = new JButton("Shopping Cart");
        shoppingCartButton.addActionListener(e -> openShoppingCartWindow());

        JPanel shoppingCartPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        shoppingCartPanel.add(shoppingCartButton);

        JPanel categoryPanel = new JPanel(new FlowLayout());
        categoryPanel.add(new JLabel("Select Product Category: "));
        categoryPanel.add(dropDown);

        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(shoppingCartPanel);
        topPanel.add(categoryPanel);

        return topPanel;
    }

    // Method to handle opening of the shopping cart window
    private void openShoppingCartWindow() {
        ShoppingCartGUI shoppingCartGUI = ShoppingCartGUI.getInstance();
        shoppingCartGUI.populateCartTable();
        ShoppingCartGUI.updateTotals();
        shoppingCartGUI.setVisible(true); // Open the shopping cart window
    }

    // Method to create and return the middle panel
    private JPanel getMiddlePanel() {
        middlePanel.setLayout(new BorderLayout());
        middlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding for layout

        // Initialize product table
        initializeProductTable("All");
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        dropDown.addActionListener(e -> updateProductTableBasedOnSelection());
        JScrollPane scrollPane = new JScrollPane(productTable);
        middlePanel.add(scrollPane);

        productTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                updateBottomPanelDetails();
            }
        });

        return middlePanel;
    }

    // Method to update the product table based on category selection
    private void updateProductTableBasedOnSelection() {
        initializeProductTable((String) dropDown.getSelectedItem());
        SwingUtilities.invokeLater(() -> {
            middlePanel.revalidate();
            middlePanel.repaint();
        });
    }

    // Method to initialize the product table with data
    private void initializeProductTable(String type) {
        DefaultTableModel tableModel = new DefaultTableModel(prepareTableData(type), new String[]{"Product ID", "Name", "Category", "Price(£)", "Info"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        productTable.setModel(tableModel);
        applyCellRenderer();
    }

    // Method to prepare data for the table based on the selected type
    private Object[][] prepareTableData(String type) {
        List<Product> filteredProducts = filterProductsByType(type);
        Object[][] data = new Object[filteredProducts.size()][5];

        int i = 0;
        for (Product product : filteredProducts) {
            data[i] = new Object[]{product.getProductID(), product.getProductName(), getProductCategory(product), product.getPrice(), getProductInfo(product)};
            i++;
        }
        return data;
    }

    // Method to filter products based on their type
    private List<Product> filterProductsByType(String type) {
        List<Product> filteredProducts = new ArrayList<>();
        if ("All".equals(type)) {
            filteredProducts.addAll(productList);
        } else {
            for (Product product : productList) {
                if ((type.equals("Clothing") && product instanceof Clothing) || (type.equals("Electronics") && product instanceof Electronics)) {
                    filteredProducts.add(product);
                }
            }
        }
        return filteredProducts;
    }

    // Method to apply a custom renderer to the product table
    private void applyCellRenderer() {
        AvailabilityCellRenderer renderer = new AvailabilityCellRenderer(productList);
        for (int i = 0; i < productTable.getColumnCount(); i++) {
            productTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    // Method to create and return the bottom panel
    private JPanel getBottomPanel() {
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding for layout

        JPanel detailsPanel = createDetailsPanel();
        bottomPanel.add(detailsPanel, BorderLayout.CENTER);

        JButton addToCartButton = new JButton("Add to Shopping Cart");
        addToCartButton.addActionListener(e -> addProductToCart());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addToCartButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        return bottomPanel;
    }

    // Method to create the details panel
    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Selected Product - Details"));

        // Initialize labels for displaying product details
        productIdLabel = new JLabel();
        categoryLabel = new JLabel();
        nameLabel = new JLabel();
        sizeLabel = new JLabel();
        colourLabel = new JLabel();
        itemsAvailableLabel = new JLabel();
        brandLabel = new JLabel();
        warrantyLabel = new JLabel();

        // Adding labels and fields to the details panel
        detailsPanel.add(new JLabel("Product ID:"));
        detailsPanel.add(productIdLabel);
        detailsPanel.add(new JLabel("Category:"));
        detailsPanel.add(categoryLabel);
        detailsPanel.add(new JLabel("Name:"));
        detailsPanel.add(nameLabel);
        detailsPanel.add(new JLabel("Items Available:"));
        detailsPanel.add(itemsAvailableLabel);
        detailsPanel.add(new JLabel("Size:"));
        detailsPanel.add(sizeLabel);
        detailsPanel.add(new JLabel("Colour:"));
        detailsPanel.add(colourLabel);
        detailsPanel.add(new JLabel("Brand:"));
        detailsPanel.add(brandLabel);
        detailsPanel.add(new JLabel("Warranty:"));
        detailsPanel.add(warrantyLabel);

        return detailsPanel;
    }

    // Method to handle adding product to the cart
    private void addProductToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            String selectedProductId = productTable.getValueAt(selectedRow, 0).toString();
            Product selectedProduct = getProductById(selectedProductId);
            if (selectedProduct != null) {
                ShoppingCart.getInstance().addProduct(selectedProduct);
                ShoppingCartGUI shoppingCartGUI = ShoppingCartGUI.getInstance();
                shoppingCartGUI.addProductToCart(selectedProduct);
            }
        }
    }

    // Method to update the bottom panel with product details
    private void updateBottomPanelDetails() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            String selectedProductId = productTable.getValueAt(selectedRow, 0).toString();
            Product selectedProduct = getProductById(selectedProductId);
            if (selectedProduct != null) {
                updateProductDetails(selectedProduct);
            }
        }
    }

    // Method to get a product by its ID
    private Product getProductById(String productId) {
        for (Product product : productList) {
            if (product.getProductID().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    // Method to update product details in the bottom panel
    private void updateProductDetails(Product product) {
        productIdLabel.setText(product.getProductID());
        nameLabel.setText(product.getProductName());
        itemsAvailableLabel.setText(String.valueOf(product.getNumAvailableItems()));
        categoryLabel.setText(getProductCategory(product));

        if (product instanceof Electronics) {
            Electronics electronics = (Electronics) product;
            brandLabel.setText(electronics.getBrand());
            warrantyLabel.setText(String.valueOf(electronics.getWarrantyPeriod()));
            sizeLabel.setText("N/A");
            colourLabel.setText("N/A");
        } else if (product instanceof Clothing) {
            Clothing clothing = (Clothing) product;
            sizeLabel.setText(String.valueOf(clothing.getSize()));
            colourLabel.setText(clothing.getColour());
            brandLabel.setText("N/A");
            warrantyLabel.setText("N/A");
        }
    }

    // Method to get the category of a product
    private String getProductCategory(Product product) {
        return product.getClass().getSimpleName();
    }

    // Method to get additional information of a product
    private String getProductInfo(Product product) {
        if (product instanceof Electronics electronicsProduct) {
            return electronicsProduct.getInfo();
        } else if (product instanceof Clothing clothingProduct) {
            return clothingProduct.getInfo();
        } else {
            return "No info";
        }
    }

    static class AvailabilityCellRenderer extends DefaultTableCellRenderer {
        private final List<Product> productList;

        public AvailabilityCellRenderer(List<Product> productList) {
            this.productList = productList;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Product product = productList.get(row);

            if (product != null && product.getNumAvailableItems() < 3) {
                c.setBackground(new Color(255, 102, 102)); // Light red background
            } else {
                c.setBackground(Color.WHITE); // Default background
            }
            return c;
        }
    }
}
