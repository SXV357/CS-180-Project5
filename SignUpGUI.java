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
 * Miller, Oliver Long
 * 
 * @version December 7, 2023
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
    private JButton loginButton;
    private InitialClient initialClient;

    /**
     * Creates a new SignUpGUI instance utilizing the initial client for making requests to the server
     * 
     * @param initialClient The client for handling creating a new account
     */
    public SignUpGUI(InitialClient initialClient) {
        this.initialClient = initialClient;
        signUpDisplay();
    }

    /**
     * Add event listeners for all buttons
     */
    public ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == signupButton) {
                String email = emailField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);
                String role = (String) roleComboBox.getSelectedItem();
                if (role == null) {
                    new ErrorMessageGUI("You need to select a role in order to access the application");
                    return;
                } else {
                    if (role.equals("Customer")) {
                        signupFrame.dispose();
                        initialClient.attemptCreateNewCustomerAccount(email, password);
                    } else {
                        signupFrame.dispose();
                        initialClient.attemptCreateNewSellerAccount(email, password);
                    }
                }
            } else if (e.getSource() == loginButton) {
                signupFrame.dispose();
                new LoginGUI(initialClient);
            }
        }
    };

     /**
     * Constructs the signup GUI and displays it
     */
    public void signUpDisplay() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                signupFrame = new JFrame("Create Account");
                signupFrame.setSize(275, 175);
                signupFrame.setLocationRelativeTo(null);
                signupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                signupButton = new JButton("Sign Up");
                signupButton.addActionListener(actionListener);
                loginButton = new JButton("Log In");
                loginButton.addActionListener(actionListener);

                emailLabel = new JLabel("Email:");
                emailField = new JTextField(15);
                passwordLabel = new JLabel("Password:");
                passwordField = new JPasswordField(15);
                roleLabel = new JLabel("Role");
                roleComboBox = new JComboBox<>(new String[]{"Customer", "Seller"});

                // Reset the contents of all fields when the GUI is opened
                emailField.setText("");
                passwordField.setText(null);
                roleComboBox.setSelectedItem(null);

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
                buttonPanel.add(loginButton);

                signupFrame.add(textPanel, BorderLayout.CENTER);
                signupFrame.add(buttonPanel, BorderLayout.SOUTH);

                signupFrame.setVisible(true);
            }
        });
    }
}