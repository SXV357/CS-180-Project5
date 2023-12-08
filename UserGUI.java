import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Project 5 - UserGUI.java
 * 
 * Class that represents the characteristics associated with all users in the
 * application.
 *
 * @author Shafer Anthony Hofmann, Qihang Gan, Shreyas Viswanathan, Nathan Pasic
 * Miller, Oliver Long
 * 
 * @version December 7, 2023
 */
public class UserGUI {
    private final JFrame frame = new JFrame("Boilermaker Market");
    private final JButton logIn = new JButton("Log In");
    private final JButton signUp = new JButton("Sign Up");
    private InitialClient initialClient;

    /**
     * Creates a new UserGUI instance utilizing the initial client for making requests to the server
     * 
     * @param initialClient The client for handling creating user account creation and logging in
     */
    public UserGUI(InitialClient initialClient) {
        this.initialClient = initialClient;
        mainMenuDisplay();
    }

    /**
     * Add event listeners for all buttons
     */
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == logIn) {
                frame.dispose();
                new LoginGUI(initialClient);
            }
            if (e.getSource() == signUp) {
                frame.dispose();
                new SignUpGUI(initialClient);
            }
        }
    };

    /**
     * Constructs the main menu GUI and displays it
     */
    public void mainMenuDisplay() {
        //Set up frame for the display
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setSize(320, 150);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                //Set up panel to hold buttons in frame
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(logIn);
                buttonPanel.add(signUp);
                logIn.addActionListener(actionListener);
                signUp.addActionListener(actionListener);

                //Set up textArea to hold welcome message
                JTextArea welcomeMessage = new JTextArea("\nWelcome to the Boilermaker Market!", 4, 5);
                welcomeMessage.setFont(new Font("Georgia", Font.PLAIN, 18));

                //Set up container to hold the button panel and textArea
                Container content = frame.getContentPane();
                content.setLayout(new BorderLayout());
                content.add(buttonPanel, BorderLayout.SOUTH);
                content.add(welcomeMessage, BorderLayout.CENTER);

                //Set the frame to visible
                frame.setVisible(true);
            }
        });
    }
}