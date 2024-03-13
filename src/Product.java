public abstract class Product {
    private String productID;                       //Attributes of Product class
    private String productName;
    private int numAvailableItems;
    private double price;

    //Constructor
    public Product(String productId, String productName, int numAvailableItems, double price) {
        this.productID = productId;
        this.productName = productName;
        this.numAvailableItems = numAvailableItems;
        this.price = price;
    }

    // Getters and setters
    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public int getNumAvailableItems() {
        return numAvailableItems;
    }

    public double getPrice() {
        return price;
    }

    public void setProductId(String productId) {
        this.productID = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setAvailableItems(int availableItems) {
        this.numAvailableItems = availableItems;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    //toString
    @Override
    public String toString() {
        return "Product{" + "\n"+
                "Product Id = " + productID + "\n" +
                "Product Name = " + productName + "\n" +
                "Number of available items = " + numAvailableItems + "\n"+
                "Price = " + price + "Rs"+ "\n" +
                '}';
    }

    public abstract String toCSVString();
}
