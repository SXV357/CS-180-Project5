import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Project 5 - SignUpGUI.java
 *
 * The interface associated with user signup.
 *
 * @author Shafer Anthony Hofmann, Qihang Gan, Shreyas Viswanathan, Nathan Pasic
 *         Miller, Oliver Long
 *
 * @version November 30, 2023
 */
public class SignUpGUI extends JFrame {
    private JFrame signupFrame;
    private JLabel emailLabel;
    private JTextField emailField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JLabel roleLabel;
    private JComboBox<String> roleComboBox;
    private JButton signupButton;
    private InitialClient initialClient;

    public static void main(String[] args) { // for temporary testing purposes
        new SignUpGUI(null);
    }

    // Constructor to initialize and set up the GUI components.
    public SignUpGUI(InitialClient initialClient) {
        this.initialClient = initialClient;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                signupFrame = new JFrame("Create Account");
                signupFrame.setSize(275, 175);
                signupFrame.setLocationRelativeTo(null);
                signupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                //Set up panel to hold buttons in frame
                signupButton = new JButton("Sign Up");

                signupButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String email = emailField.getText();
                    char[] passwordChars = passwordField.getPassword();
                    String password = new String(passwordChars);
                    String role = (String) roleComboBox.getSelectedItem();
                    if (role.equals("Customer")) {
                        initialClient.attemptCreateNewCustomerAccount(email, password);
                    } else {
                        initialClient.attemptCreateNewSellerAccount(email, password);
                    }
                } 
                });

                emailLabel = new JLabel("Email:");
                emailField = new JTextField(15);
                passwordLabel = new JLabel("Password:");
                passwordField = new JPasswordField(15); 
                roleLabel = new JLabel("Role");
                roleComboBox = new JComboBox<>(new String[]{"Customer", "Seller"});
                
                // Reset the contents of all fields when the GUI is opened
                emailField.setText("");
                passwordField.setText(null);
                roleComboBox.setSelectedItem("Customer");

                //Add panel for text fields and labels
                JPanel textPanel = new JPanel();
                textPanel.setLayout(new FlowLayout());
                textPanel.add(emailLabel);
                textPanel.add(emailField);
                textPanel.add(passwordLabel);
                textPanel.add(passwordField);
                textPanel.add(roleLabel);
                textPanel.add(roleComboBox);

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(signupButton);

                signupFrame.add(textPanel, BorderLayout.CENTER);
                signupFrame.add(buttonPanel, BorderLayout.SOUTH);

                signupFrame.setVisible(true);
            }
        });
    }
}