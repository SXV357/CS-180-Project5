import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


/**
 * Project 5 - SignUpForm.java
 *
 * The interface associated with user signup.
 *
 * @author Shafer Anthony Hofmann, Qihang Gan, Shreyas Viswanathan, Nathan Pasic
 *         Miller, Oliver Long
 *
 * @version November 29, 2023
 */

public class SignUp extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton confirmButton;

    // Constructor to initialize and set up the GUI components.
    public SignUp() {
        createUI();
    }

    // Creates and arranges all the GUI components in the form.
    private void createUI() {

        InitialClient initialClient = new in

        setTitle("Sign Up");
        setSize(300, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Role:"));
        roleComboBox = new JComboBox<>(new String[]{"Customer", "Seller"});
        add(roleComboBox);

        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(this::confirmAction);
        add(confirmButton);
        getRole();
    }

    private void getRole() {
        String eamil = new String(emailField.getEmail());
        String password = new String(passwordField.getPassword);
        String role = new String(roleComboBox.getRole());

        if (role.equals("Customer")) {
            InitialClient.attemptCreateNewCustomerAccount(eamil, password);
        } else {
            InitialClient.attemptCreateNewSellerAccount(eamil, password);
        }
    }

    // Clears all input fields in the form.
    private void clearFields() {
        emailField.setText("");
        passwordField.setText("");
        confirmField.setText("");
        roleComboBox.setSelectedIndex(0); // Resets to the first role option
    }

    // The entry point of the application. It creates and shows the sign-up form.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUp().setVisible(true));
    }
}