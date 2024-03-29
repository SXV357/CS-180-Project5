import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Project 5 - CustomerGUI.java
 * 
 * The interface associated with a customer in the application.
 *
 * @author Shafer Anthony Hofmann, Qihang Gan, Shreyas Viswanathan, Nathan Pasic
 * Miller, Oliver Long
 * 
 * @version December 8, 2023
 */
public class CustomerGUI extends JComponent {

    private DisplayDashboardGUI displayDashboard = new DisplayDashboardGUI();
    private JFrame customerFrame;
    private CustomerClient customerClient;
    private JLabel welcomeUserLabel;
    private JButton editEmailButton;
    private JButton editPasswordButton;
    private JButton deleteAccountButton;
    private JButton signOutButton;
    private final String[] sortOrderChoices = {"Ascending", "Descending"};

    // Associated with customer permissions
    private JButton viewAllProductsButton;
    private JButton searchForProductButton;
    private JButton sortProductsButton;
    private JButton exportPurchaseHistoryButton;
    private JButton viewStoreDashboardButton;
    private JButton viewPurchaseDashboardButton;
    private JButton checkoutItemsButton;
    private JButton returnItemButton;
    private JButton leaveReviewButton;
    private JButton modifyReviewButton;
    private JButton deleteReviewButton;
    private JButton removeItemFromShoppingCartButton;
    private JButton viewPurchaseHistoryButton;
    private JButton viewShoppingCartButton;

    /**
     * Invokes the error message dialog to display a custom error message when an action taken by this customer fails.
     * 
     * @param errorMessage The custom error message retrieved through the client
     */
    public void displayErrorDialog(String errorMessage) {
        new ErrorMessageGUI(errorMessage);
    }

    /**
     * Invokes the dashboard GUI to display the two dashboards that this customer is capable of viewing
     * 
     * @param dashboardType The type of the dashboard the customer elects to view
     * @param scrollPane The pane containing the data to be displayed on the dashboard
     */
    public void displayDashboard(String dashboardType, JScrollPane scrollPane) {
        displayDashboard.showDashboard(dashboardType, scrollPane);
    }

    /*
     * Add event listeners for all buttons in the GUI
    */
    @SuppressWarnings("unchecked")
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == viewAllProductsButton) {
                Object[] viewAllProductsResult = customerClient.getAllProducts();
                if (viewAllProductsResult[0].equals("ERROR")) {
                    displayErrorDialog((String) viewAllProductsResult[1]);
                    return;
                } else {
                    String[] originalProducts = ((String) viewAllProductsResult[1]).split("\n");
                    String[] modifiedProducts = Arrays.copyOfRange(originalProducts, 1, originalProducts.length);
                    String productChoice = (String) JOptionPane.showInputDialog(null,
                            "Which product\'s details would you like to view?", "Products",
                            JOptionPane.QUESTION_MESSAGE, null, modifiedProducts, modifiedProducts[0]);
                    if (productChoice == null) {
                        return;
                    }
                    int productSelection = -1;
                    for (int i = 0; i < modifiedProducts.length; i++) {
                        if (modifiedProducts[i].equals(productChoice)) {
                            productSelection = i;
                            break;
                        }
                    }
                    Object[] incoming = customerClient.getProductInfo(productSelection);

                    if (incoming[0].equals("ERROR")) {
                        displayErrorDialog((String) incoming[1]);
                        return;
                    } else {
                        String[] productInfo = ((String) incoming[1]).split(",");
                        String storeName = productInfo[0];
                        String productName = productInfo[1];
                        String availableQuantity = productInfo[2];
                        String price = productInfo[3];
                        String description = productInfo[4];
                        String orderLimit = productInfo[5];
                        String reviews = productInfo[6];
                        String salePrice = productInfo[7];
                        String reviewDisplay = "";
                        if (reviews.length() == 10) {
                            reviewDisplay = reviews;
                        } else {
                            String info = "";
                            info += "\n";
                            String[] feedback = reviews.split(";");
                            for (String review: feedback) {
                                info += "\t" + review + "\n";
                            }
                            reviewDisplay = info;
                        }
                        String info = String.format("Store Name: %s%nProduct Name: %s%nAvailable Quantity:" +
                                " %s%nOriginal Price: %s%nDescription: %s%nOrder Limit: %s%nReviews: %s%nSale Price: %s", storeName, productName, availableQuantity, price, description, orderLimit, reviewDisplay, salePrice);
                        String[] options = {"Yes", "No"};
                        String addToCart = (String) JOptionPane.showInputDialog(null,
                                "Would you like to add this item to your cart?\n\n" + info, "Add Item",
                                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                        if (addToCart == null || addToCart.equals("No")) {
                            return;
                        } else {
                            String desiredQuantity = JOptionPane.showInputDialog(null,
                                    "How many would you like?", "Quantity", JOptionPane.QUESTION_MESSAGE);
                            Object[] productAddedResult = customerClient.addToCart(productSelection, desiredQuantity);
                            if (productAddedResult[0].equals("SUCCESS")) {
                                JOptionPane.showMessageDialog(null, productAddedResult[1],
                                        "Add To Cart", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                displayErrorDialog((String) productAddedResult[1]);
                            }
                        }
                    }
                }

            } else if (e.getSource() == searchForProductButton) {
                Object[] viewAllProductsResult = customerClient.getAllProducts();
                if (viewAllProductsResult[0].equals("ERROR")) {
                    displayErrorDialog((String) viewAllProductsResult[1]);
                    return;
                } else {
                    String query = JOptionPane.showInputDialog(null, "What would you like to search for?",
                            "Search for Product", JOptionPane.QUESTION_MESSAGE);
                    if (query == null) {
                        return;
                    }
                    Object[] searchProductsResult = customerClient.searchProducts(query);
                    if (searchProductsResult[0].equals("ERROR")) {
                        displayErrorDialog((String) searchProductsResult[1]);
                        return;
                    } else {
                        String[] originalProducts = ((String) viewAllProductsResult[1]).split("\n");
                        String[] modifiedOriginalProducts = Arrays.copyOfRange(originalProducts, 1,
                                originalProducts.length);

                        for (int i = 0; i < modifiedOriginalProducts.length; i++) {
                            modifiedOriginalProducts[i] = modifiedOriginalProducts[i].substring(3).trim();
                        }

                        String[] searchedProducts = ((String) searchProductsResult[1]).split("\n");
                        String[] modifiedSearchedProducts = Arrays.copyOfRange(searchedProducts, 1,
                                searchedProducts.length);   
                        
                        String productChoice = (String) JOptionPane.showInputDialog(null,
                                "Which product\'s details would you like to view?", "Products",
                                JOptionPane.QUESTION_MESSAGE, null, modifiedSearchedProducts, modifiedSearchedProducts[0]);
                        if (productChoice == null) {
                            return;
                        }
                        int productSelection = -1;
                        for (int j = 0; j < modifiedOriginalProducts.length; j++) {
                            if (modifiedOriginalProducts[j].equals(productChoice.substring(3).trim())) {
                                productSelection = j;
                                break;
                            }
                        }
                        // if there's only one search result, the index sent will always be 0
                        Object[] incoming = customerClient.getProductInfo(productSelection);
                        if (incoming[0].equals("ERROR")) {
                            displayErrorDialog((String) incoming[1]);
                            return;
                        } else {
                            String[] productInfo = ((String) incoming[1]).split(",");
                            String storeName = productInfo[0];
                            String productName = productInfo[1];
                            String availableQuantity = productInfo[2];
                            String price = productInfo[3];
                            String description = productInfo[4];
                            String orderLimit = productInfo[5];
                            String reviews = productInfo[6];
                            String salePrice = productInfo[7];
                            String reviewDisplay = "";
                            if (reviews.length() == 10) {
                                reviewDisplay = reviews;
                            } else {
                                String info = "";
                                info += "\n";
                                String[] feedback = reviews.split(";");
                                for (String review: feedback) {
                                    info += "\t" + review + "\n";
                                }
                                reviewDisplay = info;
                            }
                            String info = String.format("Store Name: %s%nProduct Name: %s%nAvailable Quantity:" +
                                    " %s%nOriginal Price: %s%nDescription: %s%nOrder Limit: %s%nReviews: %s%nSale Price: %s", storeName, productName, availableQuantity, price, description, orderLimit, reviewDisplay, salePrice);
                            String[] options = {"Yes", "No"};
                            String addToCart = (String) JOptionPane.showInputDialog(null,
                                    "Would you like to add this item to your cart?\n\n" + info, "Add Item",
                                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                            if (addToCart == null || addToCart.equals("No")) {
                                return;
                            } else {
                                String desiredQuantity = JOptionPane.showInputDialog(null,
                                        "How many would you like?", "Quantity", JOptionPane.QUESTION_MESSAGE);
                                Object[] productAddedResult = customerClient.addToCart(productSelection, desiredQuantity);
                                if (productAddedResult[0].equals("SUCCESS")) {
                                    JOptionPane.showMessageDialog(null, productAddedResult[1],
                                            "Add To Cart", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    displayErrorDialog((String) productAddedResult[1]);
                                }
                            }
                        }
                    }
                }

            } else if (e.getSource() == sortProductsButton) {
                Object[] viewAllProductsResult = customerClient.getAllProducts();
                if (viewAllProductsResult[0].equals("ERROR")) {
                    displayErrorDialog((String) viewAllProductsResult[1]);
                    return;
                } else {
                    String[] originalProducts = ((String) viewAllProductsResult[1]).split("\n");
                    new SortProductsGUI(originalProducts, customerClient);
                }

            } else if (e.getSource() == exportPurchaseHistoryButton) {
                Object[] exportPurchaseHistoryResult = customerClient.exportPurchaseHistory();

                if (exportPurchaseHistoryResult[0].equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(null, exportPurchaseHistoryResult[1],
                            "Export Purchase History", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    displayErrorDialog((String) exportPurchaseHistoryResult[1]);
                }

            } else if (e.getSource() == viewPurchaseHistoryButton) {
                Object[] purchaseHistoryResult = customerClient.getShoppingHistory();
                if (purchaseHistoryResult[0].equals("ERROR")) {
                    displayErrorDialog((String) purchaseHistoryResult[1]);
                } else {
                    String purchaseHistoryEntries = (String) purchaseHistoryResult[1];
                    JOptionPane.showMessageDialog(null, purchaseHistoryEntries, "Purchase History",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } else if (e.getSource() == viewShoppingCartButton) {
                Object[] getShoppingCartResult = customerClient.getCart();
                if (getShoppingCartResult[0].equals("ERROR")) {
                    displayErrorDialog((String) getShoppingCartResult[1]);
                    return;
                } else {
                    String shoppingCartItems = (String) getShoppingCartResult[1];
                    JOptionPane.showMessageDialog(null, shoppingCartItems, "View Shopping Cart",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (e.getSource() == viewStoreDashboardButton) {
                Object[] getAllStoresResult = customerClient.fetchAllStores();
                if (getAllStoresResult[0].equals("ERROR")) {
                    displayErrorDialog((String) getAllStoresResult[1]);
                    return;
                } else {
                    String[] sortChoices = {"Store name", "Number of product sales", "Total revenue"};
                    String sortChoice = (String) JOptionPane.showInputDialog(null,
                            "How would you like to sort the dashboard?", "Dashboard Sort Choice",
                            JOptionPane.QUESTION_MESSAGE, null, sortChoices, sortChoices[0]);
                    if (sortChoice == null) {
                        return;
                    }
                    String orderChoice = (String) JOptionPane.showInputDialog(null,
                            "In what order would you like to sort the dashboard?", "Sorting Order",
                            JOptionPane.QUESTION_MESSAGE, null, sortOrderChoices, sortOrderChoices[0]);
                    if (orderChoice == null) {
                        return;
                    }
                    boolean ascending = orderChoice.equals("Ascending") ? true : false;
                    int sortSelection = -1;
                    for (int i = 0; i < sortChoices.length; i++) {
                        if (sortChoices[i].equals(sortChoice)) {
                            sortSelection = i;
                            break;
                        }
                    }
                    Object[] storeDashboardResult = customerClient.customerGetStoresDashboard(sortSelection, ascending);

                    if (storeDashboardResult[0].equals("ERROR")) {
                        String errorMessage = (String) storeDashboardResult[1];
                        displayErrorDialog(errorMessage);
                        return;
                    } else if (storeDashboardResult[0].equals("SUCCESS")) {
                        ArrayList<String> storeDashboard = (ArrayList<String>) storeDashboardResult[1];
                        Object[][] data = new Object[storeDashboard.size()][3];
                        for (int i = 0; i < storeDashboard.size(); i++) {
                            String[] elems = storeDashboard.get(i).split(",");
                            System.arraycopy(elems, 0, data[i], 0, elems.length);
                        }

                        JTable table = new JTable(data, sortChoices);
                        JScrollPane scrollPane = new JScrollPane(table);
                        displayDashboard("Stores", scrollPane);
                    }
                }

            } else if (e.getSource() == viewPurchaseDashboardButton) {
                Object[] getAllStoresResult = customerClient.fetchAllStores();
                if (getAllStoresResult[0].equals("ERROR")) {
                    displayErrorDialog((String) getAllStoresResult[1]);
                    return;
                } else {
                    String[] sortChoices = {"Store name", "Number of products purchased", "Total spent"};
                    String sortChoice = (String) JOptionPane.showInputDialog(null,
                            "How would you like to sort the dashboard?", "Dashboard Sort Choice",
                            JOptionPane.QUESTION_MESSAGE, null, sortChoices, sortChoices[0]);
                    if (sortChoice == null) {
                        return;
                    }
                    String orderChoice = (String) JOptionPane.showInputDialog(null,
                            "In what order would you like to sort the dashboard?", "Sorting Order",
                            JOptionPane.QUESTION_MESSAGE, null, sortOrderChoices, sortOrderChoices[0]);
                    if (orderChoice == null) {
                        return;
                    }
                    boolean ascending = orderChoice.equals("Ascending") ? true : false;
                    int sortSelection = -1;
                    for (int i = 0; i < sortChoices.length; i++) {
                        if (sortChoices[i].equals(sortChoice)) {
                            sortSelection = i;
                            break;
                        }
                    }
                    Object[] purchasesDashboardResult =
                            customerClient.customerGetPersonalPurchasesDashboard(sortSelection, ascending);

                    if (purchasesDashboardResult[0].equals("ERROR")) {
                        String errorMessage = (String) purchasesDashboardResult[1];
                        displayErrorDialog(errorMessage);
                        return;
                    } else if (purchasesDashboardResult[0].equals("SUCCESS")) {
                        ArrayList<String> purchaseDashboard = (ArrayList<String>) purchasesDashboardResult[1];
                        Object[][] data = new Object[purchaseDashboard.size()][3];
                        for (int i = 0; i < purchaseDashboard.size(); i++) {
                            String[] elems = purchaseDashboard.get(i).split(",");
                            System.arraycopy(elems, 0, data[i], 0, elems.length);
                        }

                        JTable table = new JTable(data, sortChoices);
                        JScrollPane scrollPane = new JScrollPane(table);
                        displayDashboard("Purchases", scrollPane);
                    }
                }

            } else if (e.getSource() == checkoutItemsButton) {
                Object[] purchaseItemsResult = customerClient.purchaseItems();

                if (purchaseItemsResult[0].equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(null, purchaseItemsResult[1], "Purchase Items",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    displayErrorDialog((String) purchaseItemsResult[1]);
                }

            } else if (e.getSource() == returnItemButton) {
                Object[] getPurchaseHistoryResult = customerClient.getShoppingHistory();
                if (getPurchaseHistoryResult[0].equals("ERROR")) {
                    displayErrorDialog((String) getPurchaseHistoryResult[1]);
                    return;
                } else {
                    String[] purchaseHistoryItems = ((String) getPurchaseHistoryResult[1]).split("\n");
                    String[] modifiedHistory = Arrays.copyOfRange(purchaseHistoryItems, 1, purchaseHistoryItems.length);
                    String itemChoice = (String) JOptionPane.showInputDialog(null,
                            "Which item would you like to return?", "Return Item",
                            JOptionPane.QUESTION_MESSAGE, null, modifiedHistory, modifiedHistory[0]);
                    if (itemChoice == null) {
                        return;
                    }
                    int itemSelection = -1;
                    for (int i = 0; i < modifiedHistory.length; i++) {
                        if (modifiedHistory[i].equals(itemChoice)) {
                            itemSelection = i;
                            break;
                        }
                    }
                    Object[] returnItemResult = customerClient.returnItem(itemSelection);
                    if (returnItemResult[0].equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(null, returnItemResult[1],
                                "Remove from cart", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        displayErrorDialog((String) returnItemResult[1]);
                    }
                }
            } else if (e.getSource() == leaveReviewButton) {
                Object[] getPurchaseHistoryResult = customerClient.getShoppingHistory();
                if (getPurchaseHistoryResult[0].equals("ERROR")) {
                    displayErrorDialog((String) getPurchaseHistoryResult[1]);
                    return;
                } else {
                    String[] purchaseHistoryItems = ((String) getPurchaseHistoryResult[1]).split("\n");
                    String[] modifiedHistory = Arrays.copyOfRange(purchaseHistoryItems, 1, purchaseHistoryItems.length);
                    String itemChoice = (String) JOptionPane.showInputDialog(null,
                            "Which item would you like to leave a review for?", "Leave Review",
                            JOptionPane.QUESTION_MESSAGE, null, modifiedHistory, modifiedHistory[0]);
                    if (itemChoice == null) {
                        return;
                    }
                    String review = JOptionPane.showInputDialog(null, "What is the review?", "Review", JOptionPane.QUESTION_MESSAGE);
                    if (review == null) {
                        return;
                    }
                    int itemSelection = -1;
                    for (int i = 0; i < modifiedHistory.length; i++) {
                        if (modifiedHistory[i].equals(itemChoice)) {
                            itemSelection = i;
                            break;
                        }
                    }
                    Object[] leaveReviewResult = customerClient.leaveReview(itemSelection, review);
                    if (leaveReviewResult[0].equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(null, leaveReviewResult[1],
                                "Remove from cart", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        displayErrorDialog((String) leaveReviewResult[1]);
                    }
                }

            } else if (e.getSource() == modifyReviewButton) {
                Object[] getPurchaseHistoryResult = customerClient.getShoppingHistory();
                if (getPurchaseHistoryResult[0].equals("ERROR")) {
                    displayErrorDialog((String) getPurchaseHistoryResult[1]);
                    return;
                } else {
                    Object[] getReviewsResult = customerClient.fetchReviews();
                    if (getReviewsResult[0].equals("SUCCESS")) {
                        ArrayList<String> reviews = (ArrayList<String>) getReviewsResult[1];
                        String itemChoice = (String) JOptionPane.showInputDialog(null,
                            "Which review would you like to edit", "Edit Review",
                            JOptionPane.QUESTION_MESSAGE, null, reviews.toArray(), reviews.get(0));
                        if (itemChoice == null) {
                            return;
                        }
                        int itemSelection = -1;
                        for (int i = 0; i < reviews.size(); i++) {
                            if (itemChoice.equals(reviews.get(i))) {
                                itemSelection = i;
                                break;
                            }
                        }
                        System.out.println(itemSelection);
                        if (itemSelection == 0) {
                            displayErrorDialog("Invalid selection. Please try again");
                            return;
                        } else {
                            String modifiedReview = JOptionPane.showInputDialog(null,
                            "What is the new review?", "Modified Review",
                            JOptionPane.QUESTION_MESSAGE);
                            if (modifiedReview == null) {
                                return;
                            }
                            Object[] editReviewResult = customerClient.editReview(itemSelection, modifiedReview);
                            if (editReviewResult[0].equals("SUCCESS")) {
                                JOptionPane.showMessageDialog(null, editReviewResult[1], "Edit Review", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                displayErrorDialog((String) editReviewResult[1]);
                            }
                        }
                    } else {
                        displayErrorDialog((String) getReviewsResult[1]);
                    }
                }


            } else if (e.getSource() == deleteReviewButton) {
                Object[] getPurchaseHistoryResult = customerClient.getShoppingHistory();
                if (getPurchaseHistoryResult[0].equals("ERROR")) {
                    displayErrorDialog((String) getPurchaseHistoryResult[1]);
                    return;
                } else {
                    Object[] getReviewsResult = customerClient.fetchReviews();
                    if (getReviewsResult[0].equals("SUCCESS")) {
                        ArrayList<String> reviews = (ArrayList<String>) getReviewsResult[1];
                        String itemChoice = (String) JOptionPane.showInputDialog(null,
                            "Which review would you like to delete", "Delete Review",
                            JOptionPane.QUESTION_MESSAGE, null, reviews.toArray(), reviews.get(0));
                        if (itemChoice == null) {
                            return;
                        }
                        int itemSelection = -1;
                        for (int i = 0; i < reviews.size(); i++) {
                            if (itemChoice.equals(reviews.get(i))) {
                                itemSelection = i;
                                break;
                            }
                        }
                        if (itemSelection == 0) {
                            displayErrorDialog("Invalid selection. Please try again");
                            return;
                        } else {
                            Object[] deleteReviewResult = customerClient.deleteReview(itemSelection);
                            if (deleteReviewResult[0].equals("SUCCESS")) {
                                JOptionPane.showMessageDialog(null, deleteReviewResult[1], "Delete Review", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                displayErrorDialog((String) deleteReviewResult[1]);
                            }
                        }
                        
                    } else {
                        displayErrorDialog((String) getReviewsResult[1]);
                    }
                }


            } else if (e.getSource() == removeItemFromShoppingCartButton) {
                Object[] getShoppingCartResult = customerClient.getCart();
                if (getShoppingCartResult[0].equals("ERROR")) {
                    displayErrorDialog((String) getShoppingCartResult[1]);
                    return;
                } else {
                    String[] shoppingCartItems = ((String) getShoppingCartResult[1]).split("\n");
                    String[] modifiedCart = Arrays.copyOfRange(shoppingCartItems, 1, shoppingCartItems.length);
                    String itemChoice = (String) JOptionPane.showInputDialog(null,
                            "Which item would you like to remove?", "Shopping Cart",
                            JOptionPane.QUESTION_MESSAGE, null, modifiedCart, modifiedCart[0]);
                    if (itemChoice == null) {
                        return;
                    }
                    String removeQuantity = JOptionPane.showInputDialog(null, "How many of this item would you like to remove?", "Shopping Cart", JOptionPane.QUESTION_MESSAGE);
                    if (removeQuantity == null) {
                        return;
                    }
                    int itemSelection = -1;
                    for (int i = 0; i < modifiedCart.length; i++) {
                        if (modifiedCart[i].equals(itemChoice)) {
                            itemSelection = i;
                            break;
                        }
                    }
                    Object[] removeFromCartResult = customerClient.removeFromCart(itemSelection, removeQuantity);
                    if (removeFromCartResult[0].equals("SUCCESS")) {
                        JOptionPane.showMessageDialog(null, removeFromCartResult[1],
                                "Remove from cart", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        displayErrorDialog((String) removeFromCartResult[1]);
                    }
                }

            } else if (e.getSource() == editEmailButton) {
                String newEmail = JOptionPane.showInputDialog(null, "What is the new email?", "Edit Email",
                        JOptionPane.QUESTION_MESSAGE);
                if (newEmail == null) {
                    return;
                }
                Object[] editEmailResult = customerClient.editEmail(newEmail);
                if (editEmailResult[0].equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(null, "Email edited successfully!", "Edit Email",
                            JOptionPane.INFORMATION_MESSAGE);
                    customerFrame.dispose();
                    customerClient.homepage((String) editEmailResult[1]);
                } else {
                    displayErrorDialog((String) editEmailResult[1]);
                }

            } else if (e.getSource() == editPasswordButton) {
                String newPassword = JOptionPane.showInputDialog(null, "What is the new password?", "Edit Password",
                        JOptionPane.QUESTION_MESSAGE);
                if (newPassword == null) {
                    return;
                }
                Object[] editPasswordResult = customerClient.editPassword(newPassword);
                if (editPasswordResult[0].equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(null, editPasswordResult[1], "Edit Password",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    displayErrorDialog((String) editPasswordResult[1]);
                }

            } else if (e.getSource() == deleteAccountButton) {
                int deleteAccount = JOptionPane.showOptionDialog(null,
                        "Are you sure you want to delete your account?", "Delete Account",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "No"},
                        null);
                if (deleteAccount == JOptionPane.YES_OPTION) {
                    Object[] deleteAccountResult = customerClient.deleteAccount();
                    if (deleteAccountResult[0].equals("SUCCESS")) {
                        try {
                            customerFrame.dispose();
                            customerClient.handleAccountState();
                        } catch (IOException ex) {
                            return;
                        }
                    } else {
                        displayErrorDialog((String) deleteAccountResult[1]);
                    }
                } 

            } else if (e.getSource() == signOutButton) {
                int signOut = JOptionPane.showOptionDialog(null,
                        "Are you sure you want to sign out?", "Sign out", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "No"}, null);
                if (signOut == JOptionPane.YES_OPTION) {
                    Object[] signOutResult = customerClient.signOut();
                    if (signOutResult[0].equals("SUCCESS")) {
                        try {
                            customerFrame.dispose();
                            customerClient.handleAccountState();
                        } catch (IOException ex) {
                            return;
                        }
                    } else {
                        displayErrorDialog((String) signOutResult[1]);
                    }
                } 
            }
        }
    };

    /**
     * Constructs the GUI for a Customer and displays it
     * 
     * @param customerClient The client to handle this customer GUI's communication with the server
     * @param email The current customer's email
     */
    public CustomerGUI(CustomerClient customerClient, String email) {
        this.customerClient = customerClient;
        customerFrame = new JFrame(email + "\'s" + " Home Page");
        JPanel buttonPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        customerFrame.setSize(800, 500);
        customerFrame.setLocationRelativeTo(null);
        customerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Welcome message label initialization
        welcomeUserLabel = new JLabel("Welcome " + email + "!", SwingConstants.CENTER);
        welcomeUserLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        welcomeUserLabel.setFont(new Font("Serif", Font.BOLD, 20));

        viewAllProductsButton = new JButton("View All Products");
        viewAllProductsButton.addActionListener(actionListener);

        searchForProductButton = new JButton("Search For Product");
        searchForProductButton.addActionListener(actionListener);

        sortProductsButton = new JButton("Sort Products");
        sortProductsButton.addActionListener(actionListener);

        exportPurchaseHistoryButton = new JButton("Export Purchase History");
        exportPurchaseHistoryButton.addActionListener(actionListener);

        viewPurchaseHistoryButton = new JButton("View Purchase History");
        viewPurchaseHistoryButton.addActionListener(actionListener);

        viewShoppingCartButton = new JButton("View Shopping Cart");
        viewShoppingCartButton.addActionListener(actionListener);

        viewStoreDashboardButton = new JButton("View Stores Dashboard");
        viewStoreDashboardButton.addActionListener(actionListener);

        viewPurchaseDashboardButton = new JButton("View Purchases Dashboard");
        viewPurchaseDashboardButton.addActionListener(actionListener);

        checkoutItemsButton = new JButton("Checkout Items");
        checkoutItemsButton.addActionListener(actionListener);

        returnItemButton = new JButton("Return An Item");
        returnItemButton.addActionListener(actionListener);

        leaveReviewButton = new JButton("Add Review");
        leaveReviewButton.addActionListener(actionListener);

        modifyReviewButton = new JButton("Modify Review");
        modifyReviewButton.addActionListener(actionListener);

        deleteReviewButton = new JButton("Delete Review");
        deleteReviewButton.addActionListener(actionListener);

        removeItemFromShoppingCartButton = new JButton("Remove Item From Shopping Cart");
        removeItemFromShoppingCartButton.addActionListener(actionListener);

        editEmailButton = new JButton("Edit Email");
        editEmailButton.addActionListener(actionListener);

        editPasswordButton = new JButton("Edit Password");
        editPasswordButton.addActionListener(actionListener);

        deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.addActionListener(actionListener);

        signOutButton = new JButton("Sign Out");
        signOutButton.addActionListener(actionListener);

        buttonPanel.add(viewAllProductsButton);
        buttonPanel.add(searchForProductButton);
        buttonPanel.add(sortProductsButton);
        buttonPanel.add(exportPurchaseHistoryButton);
        buttonPanel.add(viewPurchaseHistoryButton);
        buttonPanel.add(viewShoppingCartButton);
        buttonPanel.add(viewStoreDashboardButton);
        buttonPanel.add(viewPurchaseDashboardButton);
        buttonPanel.add(checkoutItemsButton);
        buttonPanel.add(returnItemButton);
        buttonPanel.add(leaveReviewButton);
        buttonPanel.add(modifyReviewButton);
        buttonPanel.add(deleteReviewButton);
        buttonPanel.add(removeItemFromShoppingCartButton);
        buttonPanel.add(editEmailButton);
        buttonPanel.add(editPasswordButton);
        buttonPanel.add(deleteAccountButton);
        buttonPanel.add(signOutButton);

        customerFrame.add(welcomeUserLabel, BorderLayout.NORTH);
        customerFrame.add(buttonPanel, BorderLayout.CENTER);

        customerFrame.setVisible(true);
    }
}