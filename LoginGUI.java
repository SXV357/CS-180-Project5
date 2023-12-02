import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Project 5 - LoginGUI.java
 * 
 * Class that constructs the GUI for a user that is logging in
 *
 * @author Shafer Anthony Hofmann, Qihang Gan, Shreyas Viswanathan, Nathan Pasic
 * Miller, Oliver Long
 * 
 * @version December 1, 2023
 */
public class LoginGUI {
    private final JFrame FRAME = new JFrame("Log-In");
    private final JLabel EMAIL_LABEL = new JLabel("Email:");
    private JTextField email = new JTextField(15);
    private final JLabel PASSWORD_LABEL = new JLabel("Password:");
    private JTextField password = new JTextField(15);
    private final JButton LOG_IN_BUTTON = new JButton("Log In");
    private final JButton EXIT_BUTTON = new JButton("Main Menu");
    private InitialClient initialClient;

    public LoginGUI(InitialClient initialClient) {
        this.initialClient = initialClient;
        logInDisplay();
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == LOG_IN_BUTTON) {
                // central method for login common to both customers and sellers
            }
            if (e.getSource() == EXIT_BUTTON) {
                FRAME.dispose();
                new UserGUI(initialClient);
            }
        }
    };

    private void logInDisplay() {
        //Set up frame for the display
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FRAME.setSize(275, 125);
                FRAME.setLocationRelativeTo(null);
                FRAME.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                //Set up panel to hold buttons in frame
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(LOG_IN_BUTTON);
                LOG_IN_BUTTON.addActionListener(actionListener);
                buttonPanel.add(EXIT_BUTTON);
                EXIT_BUTTON.addActionListener(actionListener);
                FRAME.add(buttonPanel, BorderLayout.SOUTH);


                //Add panel for text fields and labels
                JPanel textPanel = new JPanel();
                textPanel.add(EMAIL_LABEL);
                textPanel.add(email);
                textPanel.add(PASSWORD_LABEL);
                textPanel.add(password);
                FRAME.add(textPanel);

                FRAME.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginGUI(null);
            }
        });
    }
}