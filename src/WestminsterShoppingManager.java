import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class WestminsterShoppingManager implements ShoppingManager {

    private static ArrayList<Product> productList = new ArrayList<Product>();

    final static private int maxProduct = 50;             //maximum products that can add
    private int spaceAvailable = maxProduct;
    Product product;
    public WestminsterShoppingManager() {
        this.productList = new ArrayList<Product>();
    }


    public static ArrayList<Product> getProductList() {
        return productList;
    }

    /**
     * Getting the inputs to add product
     */
    @Override
    public void addNewProduct() {
        boolean x = true;
        while (x) {
            try {
                Product product;
                product = null;

                Scanner input = new Scanner(System.in);
                System.out.println("Please Enter the Product Type:");
                System.out.println("Select 1 for Electronics");
                System.out.println("Select 2 for Clothing");
                System.out.print("Enter your number: ");                                                //getting input after the menu displayed
                int choice = input.nextInt();

                if (choice<1 ||choice>2){
                    System.out.println("Invalid number. Please try again");                             //valid input for choice
                    continue;
                }
                System.out.print("Enter the Product ID (E-XXXX/C-XXXX): ");                             //productID input
                String productID = input.next();
                System.out.print("Enter the Product name: ");                                           //product name input
                String productName = input.next();
                System.out.print("Enter the Number of Available Items: ");                              //available items input
                int numAvailableItems = input.nextInt();
                System.out.print("Enter the Price: ");                                                  //price of the product input
                double price = input.nextDouble();
                switch (choice) {
                    case 1:
                        System.out.print("Enter the warranty period in years: ");                       //warranty period input
                        int warrantyPeriod = input.nextInt();
                        System.out.print("Enter the Brand: ");                                          //brand input
                        String brand = input.next();
                        product = new Electronics(productID, productName, numAvailableItems, price, brand, warrantyPeriod);
                        break;

                    case 2:
                        System.out.print("Enter the Size: ");                                           //size input
                        String size = input.next();
                        System.out.print("Enter the Colour: ");                                         //colour input
                        String colour = input.next();
                        product = new Clothing(productID, productName, numAvailableItems, price, size, colour);
                        break;

                    default:
                        System.out.println("Please Enter a Valid Option");
                        break;
                }
                if (productList.contains(product)) {
                    System.out.println("Product is already exist");
                }
                if (productList.size() >= maxProduct) {
                    System.out.println("You have reached your limit to add products");
                } else {
                    productList.add(product);
                    spaceAvailable -= 1;
                    System.out.println("Product is added");
                    System.out.println(spaceAvailable > 0 ? "You can add " + spaceAvailable + " more products." : "You can't add more products.");       //Ternary operator is used to print message if there is a space or not

                    System.out.print("Do you want to add another product (yes/no): ");                      //get an input to continue or back to menu
                    String ask1 = input.next();
                    String lower_case1 = ask1.toLowerCase();                                                //converting to lower case letters

                    if (lower_case1.equals("yes")) {
                        continue;
                    } else if (lower_case1.equals("no")) {
                        break;
                    } else {
                        System.out.println("Invalid input. Try again");
                        break;
                    }
                }
            } catch(Exception e){
                System.out.println("Invalid Input. Please try again");                                      //proper message display for invalid input
                break;
            }

        }
    }

    /**
     * Getting the input to remove product
     */
    @Override
    public void deleteProduct() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the ID of the Product that You Want to Remove: ");              //product iD input
        String productID = input.next();
        boolean found = false;
        for(Product product: productList){                                                      //Enhanced for loop
            if(product.getProductID().equals(productID)){
                found = true;
                productList.remove(product);                                                    //remove product from product list
                spaceAvailable +=1;                                                             //increase the number of space available to add
                System.out.println(product + " product deleted successfully.");                 //display product information that removed
                System.out.println("Available space to add: " + spaceAvailable);                //display available space to add
                break;
            }
        }
        if(!found){
            System.out.println("Invalid product ID. Please check and try again.");              //Proper message for invalid product ID
        }
    }

    /**
     * Display the products in product list
     */
    @Override
    public void displayProduct() {
        Collections.sort(productList, Comparator.comparing(Product::getProductID));             //sort the list
        if(productList.isEmpty()){
            System.out.println("Product list is empty.");
        }else{
            System.out.println("List of all products");
            for(Product product: productList){
                System.out.println(product);                                                    //display product details
            }
        }
    }

    /**
     * Save the products in to the file
     */
    @Override
    public void saveToFile() {
        try (PrintWriter out = new PrintWriter("products.txt")) {                       //using try catch block. specify the filename.
            for (Product product : productList) {
                out.println(product.toCSVString());
            }
            System.out.println("Product list saved successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the product list.");             //Proper message if an error occurred
        }
    }

    /**
     * Load from the file
     */
    @Override
    public void loadFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("products.txt"))) {              //Reading each line from the file
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Product product = null;

                if (parts.length > 0) {                                                                         //Identifying the product type based on the first part of the line
                    switch (parts[0]) {
                        case "Electronics":
                            product = Electronics.fromCSVString(line);                                          //Creating an Electronics object by parsing the CSV string
                            break;
                        case "Clothing":                                                                        //Creating a Clothing object by parsing the CSV string
                            product = Clothing.fromCSVString(line);
                            break;
                        default:
                            System.out.println("Unknown product type: " + parts[0]);                            //Handling unknown product types
                            break;
                    }

                    if (product != null) {                                                                      //Adding the product to the productList if it is not null
                        productList.add(product);
                    }
                }
            }
            System.out.println("Product list loaded successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while loading the product list: " + e.getMessage());
        }
    }

    @Override
    public boolean runMenu(){
        loadFile();                                                                                             //Load products from file

        Scanner input = new Scanner(System.in);
        boolean run = true;
        while(run){
            System.out.println("\nManagement Menu:");                                                           //Displaying management menu options
            System.out.println("1. Add a new product");
            System.out.println("2. Delete a product");
            System.out.println("3. Display products");
            System.out.println("4. Save to file");
            System.out.println("5. Open GUI");
            System.out.println("0. Exit");
            try{
                System.out.print("Enter your choice number: ");                                                  //Getting user input for menu selection
                int selection = input.nextInt();
                switch (selection) {
                    case 1:
                        addNewProduct();                                                                        //method call for addProduct
                        break;
                    case 2:
                        deleteProduct();                                                                        //method call for removeProduct
                        break;
                    case 3:
                        displayProduct();                                                                       //method call for displayProduct
                        break;
                    case 4:
                        saveToFile();                                                                           //method call for saveToFile
                        break;
                    case 5:
                        new ShoppingAppGUI();                                                                   //Opening the GUI by creating a new ShoppingAppGUI object
                        break;
                    case 0:
                        System.out.println("Thank you for using Westminster shopping manager");                 //Exiting the program and ending the loop
                        run = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid input.\n");
                input.nextLine();
            }
        }
        return run;
    }
    public static void main(String[] args) {
        WestminsterShoppingManager manager = new WestminsterShoppingManager();                      //Creating an instance of WestminsterShoppingManager
        manager.runMenu();                                                                          //method call for run menu

    }
}