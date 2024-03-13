public class Electronics extends Product {
    private String brand;                       //Attributes of Electronics class
    private int warrantyPeriod;

    //Constructor
    public Electronics(String productID, String productName, int numAvailableItems, double price, String brand, int warrantyPeriod) {
        super(productID, productName, numAvailableItems, price);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    // Getters and setters
    public String getBrand() {
        return brand;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    //toString
    @Override
    public String toString() {
        return "Electronics{" + super.toString() +
                "Brand = " + brand + "\n" +
                "Warranty Period = " + warrantyPeriod + "\n" +
                '}';
    }

    public String getInfo(){
        return "Brand: '" + brand + '\'' +
                ", Warranty Period:" + warrantyPeriod;
    }

    public String toCSVString() {
        return "Electronics," + getProductID() + "," + getProductName() + "," + getNumAvailableItems() +
                "," + getPrice() + "," + brand + "," + warrantyPeriod;                                                  //Generating a CSV string representation of the Electronics object
    }

    public static Product fromCSVString(String csvString) {
        String[] parts = csvString.split(",");                                                                    //Splitting the CSV string into parts
        if (parts.length != 7 || !parts[0].equals("Electronics")) {                                                     //Validating the CSV string
            throw new IllegalArgumentException("Invalid Electronics CSV string");
        }

        String productID = parts[1];
        String productName = parts[2];
        int noOfAvailableItems = Integer.parseInt(parts[3]);
        double productPrice = Double.parseDouble(parts[4]);
        String brand = parts[5];
        int warrantyPeriod = Integer.parseInt(parts[6]);

        return new Electronics(productID, productName, noOfAvailableItems, productPrice, brand, warrantyPeriod);
    }
}