public class Clothing extends Product{
    private String size;                        //Attributes of Clothing class
    private String colour;

    //Constructor
    public Clothing(String productID, String productName, int numAvailableItem, double price, String size, String colour){
        super(productID, productName, numAvailableItem, price);
        this.size = size;
        this.colour = colour;
    }

    // Getters and setters
    public String getSize(){
        return size;
    }
    public String getColour(){
        return colour;
    }

    public void setSize(String size){
        this.size = size;
    }
    public void setColour(String colour){
        this.colour = colour;
    }

    //toString
    @Override
    public String toString() {
        return "Clothing{" + super.toString() +
                "Size = " + size + "\n" +
                "Colour = " + colour + "\n" +
                '}';
    }

    public String getInfo(){
        return "size: " + size +
                ", colour: '" + colour;
    }

    public String toCSVString() {
        return "Clothing," + getProductID() + "," + getProductName() + "," + getNumAvailableItems() +
                "," + getPrice() + "," + size + "," + getNumAvailableItems();                                           //Generating a CSV string representation of the Clothing object
    }

    public static Product fromCSVString(String csvString) {
        String[] parts = csvString.split(",");                                                                    //Splitting the CSV string into parts
        if (parts.length != 7 || !parts[0].equals("Clothing")) {                                                        //Validating the CSV string
            throw new IllegalArgumentException("Invalid Clothing CSV string");
        }

        String productID = parts[1];
        String productName = parts[2];
        int noOfAvailableItems = Integer.parseInt(parts[3]);
        double productPrice = Double.parseDouble(parts[4]);
        String size = parts[5];
        String colour = parts[6];

        return new Clothing(productID, productName, noOfAvailableItems, productPrice, size, colour);
    }
}
