import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Project 4 - Runner.java
 * <p>
 * Driver class for the application
 *
 * @author Shafer Anthony Hofmann, Qihang Gan, Shreyas Viswanathan, Nathan Pasic Miller, Oliver Long
 * @version November 8, 2023
 */


//TODO MAKE ALL PROMPTS UNIFORM (like capitalization)

public class Runner {
    private static final Database db = new Database();  //Makes a Database object
    private static boolean userLoggedIn = false;  //This boolean is used to check if the user has logged in successfully
    private static User curUser = new User();  //This field stores the user object for the current User of the program

    //Welcomes the user and asks them whether they would like to: Log-in, Create Account, or Quit
    //Takes only the Scanner as a Parameter
    public static int welcomeUser(Scanner scan) {
        System.out.println("What would you like to do?");
        do {
            System.out.println("1) Log-in\n2) Create Account\n3) Quit");
            try {
                int response = Integer.parseInt(scan.nextLine());
                if (response > 0 && response < 4) {
                    return response;
                } else {
                    System.out.println("Please enter your choice's corresponding Integer");
                }
            } catch (Exception e) {
                System.out.println("Please enter your choice's corresponding Integer");
            }
        } while (true);
    }

    //Prompts the user to enter email and then password, if they match with an existing user, they log in. If they don't
    //match the user is told that either the email or password was incorrect, and they are prompted again.
    //Takes scanner as a parameter
    public static void loginUser(Scanner scan) {
        System.out.println("Enter your E-mail: ");
        String uEmail = scan.nextLine();
        System.out.println("Enter your password: ");
        String uPassword = scan.nextLine();
        if (db.retrieveUserMatchForLogin(uEmail, uPassword) != null) {
            System.out.println("Login successful!");
            String userAsString = db.retrieveUserMatchForLogin(uEmail, uPassword);
            String[] userFromDB = userAsString.split(",");
            String uID = userFromDB[0];
            UserRole uRole = UserRole.UNDECIDED;
            if (userFromDB[3].equals("SELLER")) {
                uRole = UserRole.SELLER;
            } else if (userFromDB[3].equals("CUSTOMER")) {
                uRole = UserRole.CUSTOMER;
            }
            curUser = new User(uID, uEmail, uPassword, uRole);
            userLoggedIn = true;

        } else {
            System.out.println("Username or password is incorrect");
        }
    }

    //This method creates a new user object,
    public static void createUser(Scanner scan) {
        System.out.println("Enter your E-mail: ");
        String uEmail = scan.nextLine();
        System.out.println("Enter your password: ");
        String uPassword = scan.nextLine();
        if (db.retrieveUserMatchForSignUp(uEmail) != null) {  //Check to see if there is a user with the email already
            System.out.println("User already exists");
        } else {
            UserRole uRole = UserRole.UNDECIDED;  //New user is defaulted to undecided until they set it
            while (uRole.equals(UserRole.UNDECIDED)) {
                System.out.println("Which would you like to be? \n1) Seller\n2) Customer");
                try {
                    int input = Integer.parseInt(scan.nextLine());
                    if (input == 1) {
                        uRole = UserRole.SELLER;
                    } else if (input == 2) {
                        uRole = UserRole.CUSTOMER;
                    } else {
                        System.out.println("Please enter your choice's corresponding Integer");
                    }
                } catch (Exception e) {
                    System.out.println("Please enter your choice's corresponding Integer");
                }
            }
            curUser = new User(uEmail, uPassword, uRole);
            db.addToDatabase("users.csv", curUser.toString()); // based on latest modification of database
            userLoggedIn = true;
        }
    }


    public static int sellerPrompt(Scanner scan) {
        int sellerChoice;
        do {
            System.out.println("1) Stores\n2) Dashboard\n3) Customer Shopping Carts\n4) Edit Account\n" +
                    "6) Sign Out");
            try {
                sellerChoice = Integer.parseInt(scan.nextLine());
                if (sellerChoice > 0 && sellerChoice < 6) {
                    return sellerChoice;
                } else {
                    System.out.println("Please enter your choice's corresponding Integer");
                }
            } catch (Exception e) {
                System.out.println("Please enter your choice's corresponding Integer");
            }
        } while (true);
    }

    public static int customerPrompt(Scanner scan) {
        int customerChoice;
        do {
            System.out.println("1) Market\n2) Purchase History\n3) Dashboard\n4) Shopping Cart\n5) Edit Account\n" +
                    "6) Sign Out");
            try {
                customerChoice = Integer.parseInt(scan.nextLine());
                if (customerChoice > 0 && customerChoice < 7) {
                    return customerChoice;
                } else {
                    System.out.println("Please enter your choice's corresponding Integer");
                }
            } catch (Exception e) {
                System.out.println("Please enter your choice's corresponding Integer");
            }
        } while (true);
    }

    public static void storeUI(Scanner scan, Seller curSeller) {
        while (true) {  //loops until seller returns
            System.out.println("What would you like to do?");
            System.out.println("1) Select Store\n2) Create Store\n3) Go Back");
            ArrayList<Store> curSellerStores = curSeller.getStores();
            try {
                int storeUIChoice = Integer.parseInt(scan.nextLine());
                switch (storeUIChoice) {
                    case 1:  //Select a Store
                        if (curSellerStores != null) {
                            while (true) {  //loops until seller selects "Go Back"
                                System.out.println("Select a store or go back:");
                                for (int i = 0; i <= curSellerStores.size(); i++) {
                                    if (i < curSellerStores.size()) {
                                        System.out.println((i + 1) + ") " + curSellerStores.get(i).getStoreName());
                                    } else {
                                        System.out.println((i + 1) + ") " + "Go Back");
                                    }
                                }
                                try {
                                    int storeChoice = Integer.parseInt(scan.nextLine());
                                    if (storeChoice == curSellerStores.size() + 1) {
                                        break;
                                    } else {
                                        Store curStore = curSellerStores.get(storeChoice - 1);
                                        manageStore(scan, curSeller, curStore);
                                    }
                                } catch (Exception e) {
                                    System.out.println("Please enter your choice's corresponding Integer");
                                }
                            }
                            break;
                        } else {
                            System.out.println("You do not currently have any stores!");
                            break;
                        }
                    case 2:  //Create Store
                        System.out.println("What would you like to name the store?");
                        String newStoreName = scan.nextLine();
                        curSeller.createNewStore(newStoreName);
                        System.out.println(newStoreName + " has been created.");
                        break;
                    case 3:  //sends seller back to initial SellerUI
                        return;
                    default:
                        System.out.println("Please enter your choice's corresponding Integer");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Please enter your choice's corresponding Integer");
            }
        }
    }

    public static void manageStore(Scanner scan, Seller curSeller, Store curStore) {
        while (true) {  //loops until seller goes back
            System.out.println(curStore.getStoreName() + " Options:\n1) Create Product\n2) Edit Product\n" +
                    "3) Delete Product\n4) Import Products\n5) Export Products\n6) Go Back");
            try {
                switch (Integer.parseInt(scan.nextLine())) {
                    case 1:  //Create Product
                        System.out.println("What would you like to name the product?");
                        String productName = scan.nextLine();
                        int availableQuantity;
                        while (true) {
                            System.out.println("How many are for sale?");
                            try {
                                availableQuantity = Integer.parseInt(scan.nextLine());
                                break;
                            } catch (Exception e) {
                                System.out.println("Error: Please enter an integer");
                            }
                        }
                        double price;
                        while (true) {
                            System.out.println("What is the price per product in dollars?");
                            try {
                                price = Double.parseDouble(scan.nextLine());
                                break;
                            } catch (Exception e) {
                                System.out.println("Error: Please enter a double");
                            }
                        }
                        System.out.println("Describe the product please");
                        String description = scan.nextLine();
                        boolean worked = curSeller.createNewProduct(curStore.getStoreName(), productName,
                                availableQuantity, price, description);
                        System.out.println(worked);
                        break;
                    case 2:  //Edit Product
                        Product curProduct = pickProduct(scan, curSeller, curStore);
                        if (curProduct == null) {
                            break;
                        }
                        String editParam;
                        while (true) {
                            System.out.println("Would you like to change the product's name, quantity, " +
                                    "price, or description?");
                            editParam = scan.nextLine();
                            if (editParam.equals("name") || editParam.equals("quantity") || editParam.equals("price")
                                    || editParam.equals("description")) {
                                break;
                            } else {
                                System.out.println("Please enter one of the three options.");
                            }
                        }
                        String newValue;
                        while (true) {  //loops until newValue is not null
                            System.out.println("What would you like the new value to be?");
                            newValue = scan.nextLine();
                            if (newValue != null) {
                                break;
                            }
                        }
                        if (curSeller.editProduct(curStore.getStoreName(), curProduct.getName(), editParam, newValue)) {
                            System.out.println("Product successfully edited");
                        } else {
                            System.out.println("Error editing product");  //fix to be more specific
                        }
                        break;
                    case 3:  //Delete Product
                        curProduct = pickProduct(scan, curSeller, curStore);
                        curSeller.deleteProduct(curStore.getStoreName(),
                                curProduct.getName());
                        System.out.println("Product has been deleted");
                        break;
                    case 4:  //Import Products
                        while (true) {  //loop until import is successful
                            System.out.println("Please enter the file path for the import\n Enter \"Quit\" to exit");
                            String filePath = scan.nextLine();
                            if (filePath.equals("Quit")) {
                                break;
                            }
                            boolean successfulImport = curSeller.importProducts(filePath,
                                    curStore.getStoreName());
                            if (successfulImport) {
                                break;
                            } else {
                                System.out.println("file path is incorrect");
                            }
                        }
                    case 5:  //Export Products
                        curSeller.exportProducts(curStore.getStoreIdentificationNumber());
                        System.out.println("Products have been exported to " + "exportedProducts/"
                                + curStore.getStoreName() + ".csv");
                        break;
                    case 6:  //Go Back
                        return;
                    default:  //integer is out of range
                        System.out.println("Please enter your choice's corresponding Integer");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Please enter your choice's corresponding Integer");
            }

        }
    }

    public static Product pickProduct(Scanner scan, Seller curSeller, Store curStore) {
        ArrayList<Product> productList = curStore.getProducts();
        System.out.println("Which product would you like to adjust?");
        for (int i = 0; i <= productList.size(); i++) {
            if (i < productList.size()) {
                System.out.println((i + 1) + ") " + productList.get(i).getName());
            } else {
                System.out.println((i + 1) + ") " + "Go Back");
            }
        }
        int editProductChoice;
        Product chosenProduct = new Product("", 0, 0, "");  //Where is the empty constructor
        try {
            editProductChoice = Integer.parseInt(scan.nextLine());
            if (editProductChoice == productList.size() + 1) {
                return null;
            } else {
                chosenProduct = productList.get(editProductChoice - 1);
            }
        } catch (Exception e) {
            System.out.println("Please enter your choice's corresponding Integer");
        }
        return chosenProduct;
    }

    public static void marketUI(Scanner scan, Customer curCustomer) {
        while (true) {  //loops until user selects to go back
            System.out.println("What would you like to do?\n1) View All Products\n2) Search for a Product\n" +
                    "3) Return");
            int marketChoice = Integer.parseInt(scan.nextLine());
            switch (marketChoice) {
                case 1:  //View Products
                    //TODO  View all products
                    showAllProducts(scan, curCustomer);
                    break;
                case 2:  //Search for product
                    //TODO  search for a product
                    System.out.println("What would you like to search for?");
                    String query = scan.nextLine();
                    System.out.println(curCustomer.searchProducts(query));
                    break;
                case 3:  //Go Back
                    return;
                default: //marketChoice was out of range
            }
        }
    }

    public static void showAllProducts(Scanner scan, Customer curCustomer) {
        //TODO  Select a product
        System.out.println(curCustomer.getAllProducts());
        try {
            int productSelection = Integer.parseInt(scan.nextLine());
            System.out.println(curCustomer.getProductInfo(productSelection));
            while (true) {
                System.out.println("Would you like to add this item to cart?\n1) Yes\n2) No");
                switch (Integer.parseInt(scan.nextLine())) {
                    case 1:  //yes
                        System.out.println("How many would you like?");
                        int availableQuantity = Integer.parseInt(
                                curCustomer.getProductInfo(productSelection).substring(curCustomer.getProductInfo(productSelection).indexOf("Quantity: ") + 10, curCustomer.getProductInfo(productSelection).indexOf("\nPrice:") - 1));
                        int quantity = Integer.parseInt(scan.nextLine());
                        if (quantity < 1) {
                            System.out.println("Please enter a positive integer for the number you wish to purchase");
                        } else if (quantity > availableQuantity) {
                            System.out.println("There are only " + availableQuantity + " available");
                        } else {
                            curCustomer.addToCart(productSelection, quantity);
                            System.out.println("Product has been added to cart");
                            return;
                        }
                        break;
                    case 2: //No
                        return;
                    default:  //error
                        System.out.println("Please enter your choice's corresponding Integer");
                }
            }
        } catch (Exception e) {
            System.out.println("Please enter your choice's corresponding Integer");
        }
        //TODO  Sort Market (price or quantity)
    }

    private static void customerShoppingCart(Scanner scan, Customer curCustomer) {
        while (true) {
            System.out.println(curCustomer.getCart());
            System.out.println("What would you like to do?\n1) Checkout\n2) Remove a Product\n3) Return");
            try {
                switch (Integer.parseInt(scan.nextLine())) {
                    case 1:  //checkout
                        curCustomer.purchaseItems();
                        break;
                    case 2:  //remove an item
                        System.out.println("Which item would you like to remove?");
                        System.out.println(curCustomer.getCart());
                        int removeChoice = Integer.parseInt(scan.nextLine());
                        curCustomer.removeFromCart(removeChoice);
                        System.out.println("Item has been removed from cart");
                        break;
                    case 3:  //return
                        return;
                    default:  //error
                        System.out.println("Please enter your choice's corresponding Integer");
                }

            } catch (Exception e) {
                System.out.println("Please enter your choice's corresponding Integer");
            }
        }
    }

    private static void userIsSeller(Scanner scan, Seller curSeller) {
        while (true) {  //loops until Seller signs out
            int sellerChoice = sellerPrompt(scan);
            switch (sellerChoice) {
                case 1:  //Stores
                    storeUI(scan, curSeller);
                    break;
                case 2:  //Dashboard
                    //TODO fix Dashboard UI
                    System.out.println("What would you like to view?\n1) Sales to Customers\n2) Product Sales");
                    try {
                        switch (Integer.parseInt(scan.nextLine())) {
                            case 1:  //sales to customers
                                System.out.println(Dashboard.sellerGetCustomersDashboard(0, false));
                                break;
                            case 2:  //product sales
                                System.out.println(Dashboard.sellerGetProductsDashboard(0, false));
                                break;
                            default:  //error
                                System.out.println("Please enter your choice's corresponding Integer");
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Please enter your choice's corresponding Integer");
                    }
                    break;
                case 3:  //Customer Shopping Carts
                    System.out.println(curSeller.viewCustomerShoppingCarts());;
                    break;
                case 4:  //Edit user
                    editUser(scan);
                    if (!userLoggedIn) {
                        return;
                    }
                    break;
                case 5:  //Sign Out
                    userLoggedIn = false;
                    System.out.println("Thank you!");
                    return;  //ends the program
                default: //error
                    System.out.println("Please enter your choice's corresponding Integer");
                    break;
            }
        }
    }

    private static void userIsCustomer(Scanner scan, Customer curCustomer) {
        while (true) {  //loops until the user signs out
            int customerChoice = customerPrompt(scan);
            switch (customerChoice) {
                case 1:  //Market
                    marketUI(scan, curCustomer);
                    break;
                case 2:  //Purchase History
                    curCustomer.exportPurchaseHistory();
                    break;
                case 3:  //Dashboard
                    //TODO add Dashboard UI
                    System.out.println(Dashboard.customerGetStoresDashboard(0, false));
                    break;
                case 4:  //Shopping Cart
                    customerShoppingCart(scan, curCustomer);
                    break;
                case 5:  //Edit Account
                    editUser(scan);
                    if (!userLoggedIn) {
                        return;
                    }
                    break;
                case 6:  //Sign Out
                    userLoggedIn = false;
                    System.out.println("Thank you!");
                    return;  //ends the program
                default:  //error
                    System.out.println("Please enter your choice's corresponding Integer");
                    break;
            }
        }
    }

    private static void editUser (Scanner scan) {
        while (true) {
            String newUserString = "";
            String prevUserString = "";
            System.out.println("What would you like to do?\n1) Change Email\n2) Change Password\n3) Delete Account\n" +
                    "4) Exit");
            try {
                switch (Integer.parseInt(scan.nextLine())) {
                    case 1:  //Change Email
                        prevUserString = curUser.toString();
                        System.out.println("What would you like your new Email to be?");
                        String newEmail = scan.nextLine();
                        curUser.setEmail(newEmail);
                        newUserString = curUser.toString();
                        db.modifyDatabase("users.csv", prevUserString, newUserString);
                        break;
                    case 2:  //Change Password
                        prevUserString = curUser.toString();
                        System.out.println("What would you like your new Password to be?");
                        String newPassword = scan.nextLine();
                        curUser.setPassword(newPassword);
                        newUserString = curUser.toString();
                        db.modifyDatabase("users.csv", prevUserString, newUserString);
                        break;
                    case 3:  //Delete Account
                        curUser.deleteAccount();
                        userLoggedIn = false;
                        return;
                    case 4:  //Return
                        return;
                    default:  //error
                        System.out.println("Please enter your choice's corresponding Integer");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Please enter your choice's corresponding Integer");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome!");
        Scanner scan = new Scanner(System.in);
        while (true) {
            do {
                int welcomeResponse = welcomeUser(scan);
                switch (welcomeResponse) {
                    case 1:  //Log user in
                        loginUser(scan);
                        break;
                    case 2:  //Create new user
                        createUser(scan);
                        break;
                    case 3:  //Quit Program
                        System.out.println("Goodbye");
                        scan.close();
                        return;  //ends the program
                }
            } while (!userLoggedIn);
            if (curUser.getRole().equals(UserRole.SELLER)) {  //code for if the current user is a seller
                Seller curSeller = new Seller(curUser.getUserID(), curUser.getEmail(),
                        curUser.getPassword(), curUser.getRole()); //makes a seller object with same ID as user
                System.out.println("Welcome Seller");
                userIsSeller(scan, curSeller);
            } else if (curUser.getRole().equals(UserRole.CUSTOMER)) {  //code for if the current user is a customer
                Customer curCustomer = new Customer(curUser.getUserID(), curUser.getEmail(),
                        curUser.getPassword(), UserRole.CUSTOMER);
                System.out.println("Welcome Customer");
                userIsCustomer(scan, curCustomer);
            }
        }
    }
}