import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Project 4 - Dashboard.java
 * 
 * Class that encompasses functionality for customers to view store and seller
 * information, and for sellers to view statistics for each of their stores
 * Uses four methods, two for sellers and two for customers.
 * To create a full dashboard, call the two customer/seller methods, and display
 * the resulting data.
 * Each Dashboard will return an arraylist with three collumns: Name, Total
 * quantity, Total revenue
 * Sort index is which collumn to sort by.
 * 
 * @author Shafer Anthony Hofmann, Qihang Gan, Shreyas Viswanathan, Nathan Pasic
 *         Miller, Oliver Long
 * 
 * @version November 2, 2023
 */
public class Dashboard {

    // To display a dashboard to the terminal with a relevant header.
    // Input the type of dashbard to display and the dashboard array returned by one
    // of the other functions
    public static void printDashboardData(String dashboardType, ArrayList<String> dashboardData) {
        // Print the relevant header for the data
        switch (dashboardType) {
            case "Customers":
                System.out.println("Email, Quantity Purchased, Money Spent");
                break;
            case "Products":
                System.out.println("Product, Quantity Sold,Total Revenue");
                break;
            case "Stores":
                System.out.println("Store, Product Sales,Total Revenue");

                break;
            case "PersonalPurchases":
                System.out.println("Product, Amount bought, Amount spent:");
                break;
        }

        for (String line : dashboardData) {
            System.out.println(line);
        }
    }

    // Seller's method to view all customers
    // Return line format: email,totalQuantity,totalSpent
    public static ArrayList<String> sellerGetCustomersDashboard(int sortIndex, boolean sortAscending) {
        Database database = new Database();

        // Get all customer Users
        ArrayList<String> allCustomers = database.getMatchedEntries("users.csv", 3, "Customer");

        // return array
        ArrayList<String> customerStatisticsStrings = new ArrayList<>();

        // for each customer, find their purchases
        for (String customer : allCustomers) {
            String[] customerDataList = customer.split(",");
            String customerID = customerDataList[0];

            // get all purchases this customer has made
            ArrayList<String> customerPurchases = database.getMatchedEntries("purchaseHistories.csv", 0, customerID);

            // Accumulate all this customer's purchase data into total purchases and total
            // spending
            double totalQuantity = 0;
            double totalSpent = 0;
            for (String purchase : customerPurchases) {
                String[] splitPurchase = purchase.split(",");

                double purchaseQuantity = Double.parseDouble(splitPurchase[6]);
                totalQuantity += purchaseQuantity;
                // price times quantity
                double purchaseCost = Double.parseDouble(splitPurchase[7]) * purchaseQuantity;
                totalSpent += purchaseCost;
            }

            String customerEmail = customerDataList[1];
            // Add customer's stats to the list
            // Format: email, totalQuantity, totalSpent
            String StatsString = String.format("%s,%.2f,%.2f", customerEmail, totalQuantity, totalSpent);
            customerStatisticsStrings.add(StatsString);
        }

        // Sorts the processed data
        // See here for allowed values of sortType
        return sortResults(sortIndex, sortAscending, customerStatisticsStrings);
    }

    // Seller's method to view all products
    // Return line format: productName,TotalSales,Total Revenue
    public static ArrayList<String> sellerGetProductsDashboard(int sortIndex, boolean sortAscending) {
        Database database = new Database();
        // Get all products:Seller ID,Store ID,Product ID,Store Name,Product
        // Name,Available Quantity,Price,Description
        ArrayList<String> allProducts = database.getDatabaseContents("products.csv");

        // return array
        ArrayList<String> productStatisticsStrings = new ArrayList<>();

        // for each product, accumulate the total number of sales
        for (String product : allProducts) {
            String[] productDataList = product.split(",");
            // product id is the 3rd collumn of the products csv
            String productID = productDataList[2];

            // get all purchases this customer has made, productID on 3rd index of
            // purchasHistories
            // Customer ID,Seller ID,Store ID,Product ID,Store Name,Product Name,Purchase
            // Quantity,Price
            ArrayList<String> productPurchases = database.getMatchedEntries("purchaseHistories.csv", 3, productID);

            // Accumulate all this customer's purchase data into total purchases and total
            // spending

            double totalSold = 0;
            double totalRevenue = 0;
            for (String purchase : productPurchases) {
                String[] splitPurchase = purchase.split(",");

                double purchaseQuantity = Double.parseDouble(splitPurchase[6]);
                totalSold += purchaseQuantity;
                // price times quantity
                double saleRevenue = Double.parseDouble(splitPurchase[7]) * purchaseQuantity;
                totalRevenue += saleRevenue;
            }
            // Product's name is stored in the 4th collumn
            String productName = productDataList[4];
            // Add customer's stats to the list
            // Format: email, totalQuantity, totalSpent
            String StatsString = String.format("%s,%.2f,%.2f", productName, totalSold, totalRevenue);
            productStatisticsStrings.add(StatsString);
        }

        return sortResults(sortIndex, sortAscending, productStatisticsStrings);
    }

    // Customer's method to view all stores and their products sold
    // Return line format: storeName, Products Sold, Total Revenue
    public static ArrayList<String> customerGetStoresDashboard(int sortIndex, boolean sortAscending) {
        Database database = new Database();
        // Get all stores:Store ID,Seller ID,Store Name,Number of Products
        ArrayList<String> allStores = database.getDatabaseContents("stores.csv");

        // return array
        ArrayList<String> storeStatisticsStrings = new ArrayList<>();

        // for each product, accumulate the total number of sales
        for (String store : allStores) {
            String[] storeDataList = store.split(",");
            // store id is the 1st collumn of the stores csv
            String storeID = storeDataList[0];

            // get all purchases made at this store
            // Customer ID,Seller ID,Store ID,Product ID,Store Name,Product Name,Purchase
            // Quantity,Price
            ArrayList<String> storeSales = database.getMatchedEntries("purchaseHistories.csv", 2, storeID);

            // Accumulate all this customer's purchase data into total purchases and total
            // spending

            double totalProductsSold = 0;
            double totalRevenue = 0;
            for (String purchase : storeSales) {
                String[] splitPurchase = purchase.split(",");

                double purchaseQuantity = Double.parseDouble(splitPurchase[6]);
                totalProductsSold += purchaseQuantity;
                // price times quantity
                double saleRevenue = Double.parseDouble(splitPurchase[7]) * purchaseQuantity;
                totalRevenue += saleRevenue;
            }
            // Store name is stored in the 3rd collumn
            String storeName = storeDataList[2];
            // Add customer's stats to the list
            // Format: email, totalQuantity, totalSpent
            String StatsString = String.format("%s,%.2f,%.2f", storeName, totalProductsSold, totalRevenue);
            storeStatisticsStrings.add(StatsString);
        }

        return sortResults(sortIndex, sortAscending, storeStatisticsStrings);
    }

    // Personal Dashboard requires CustomerID
    // Returns all products purchased by the customer with customer ID
    // Return line format: storeName, Products Bought, Total Spent
    public static ArrayList<String> customerGetPersonalPurchasesDashboard(int sortIndex, boolean sortAscending,
            String customerID) {
        Database database = new Database();
        // Get all stores:Store ID,Seller ID,Store Name,Number of Products
        ArrayList<String> allStores = database.getDatabaseContents("stores.csv");

        // return array
        ArrayList<String> storeStatisticsStrings = new ArrayList<>();

        // for each product, accumulate the total number of sales
        for (String store : allStores) {
            String[] storeDataList = store.split(",");
            // store id is the 1st collumn of the stores csv
            String storeID = storeDataList[0];

            // get all purchases made at this store
            // Customer ID,Seller ID,Store ID,Product ID,Store Name,Product Name,Purchase
            // Quantity,Price
            ArrayList<String> storeSales = database.getMatchedEntries("purchaseHistories.csv", 2, storeID);

            // Accumulate all this customer's purchase data into total purchases and total
            // spending

            double totalProductsBought = 0;
            double totalSpent = 0;
            for (String purchase : storeSales) {
                String[] splitPurchase = purchase.split(",");

                // If purchase wasn't made by this customer, continue
                if (!splitPurchase[0].equals(customerID)) {
                    continue;
                }

                double purchaseQuantity = Double.parseDouble(splitPurchase[6]);
                totalProductsBought += purchaseQuantity;
                // price times quantity
                double saleRevenue = Double.parseDouble(splitPurchase[7]) * purchaseQuantity;
                totalSpent += saleRevenue;
            }
            // Store name is stored in the 3th collumn
            String storeName = storeDataList[2];
            // Add customer's stats to the list
            // Format: email, totalQuantity, totalSpent
            String StatsString = String.format("%s,%.2f,%.2f", storeName, totalProductsBought, totalSpent);
            storeStatisticsStrings.add(StatsString);
        }
        return sortResults(sortIndex, sortAscending, storeStatisticsStrings);
    }

    // Assuming strings are in format: String, Double, Double
    private static ArrayList<String> sortResults(int sortIndex, boolean sortAscending, ArrayList<String> arrayList) {
        switch (sortIndex) {
            case 0:
                Collections.sort(arrayList,
                        (a, b) -> a.split(",")[0].compareTo(b.split(",")[0]));

                        //Alphabetically sorting works reverse to numerical sorting
                        sortAscending = !sortAscending;
                break;
            case 1, 2:
                // Using a lambda function to compare only the price collumn after casting to
                // double
                Collections.sort(arrayList,
                        (a, b) -> Double.compare(Double.parseDouble(a.split(",")[sortIndex]),
                                Double.parseDouble(b.split(",")[sortIndex])));
                    
                break;

        }

        if (!sortAscending)
            Collections.reverse(arrayList);

        return arrayList;
    }

}
