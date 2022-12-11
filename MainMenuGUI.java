import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
/**
 * Project 5 -> MainMenuGUI
 *
 * Allows user to either access their conversations or manage their account
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
public class MainMenuGUI {
    private JFrame board;
    private Container container;

    private JPanel upperPanel;
    private JPanel bottomPanel;
    private HashMap<String,String> user;
    private Role role;
    private JLabel title;
    private JLabel contentText;

    //2 User Options
    private JButton peopleViewPressed;
    private JButton accountManagerPressed;

    /**
     * Initialize MainMenuGUI (constructor) with JFrame and the user from HashMap
     * @param frame -> JFrame
     * @param user -> current user
     */
    public MainMenuGUI(JFrame frame, HashMap<String,String> user) {
        frame.setSize(600,400);
        this.board = frame;
        this.user = user;
        // used frequently enough to justify a separate variable.
        this.role = Role.valueOf(user.get("role"));
    }

    /**
     * show MainMenuGUI (become visible)
     */
    public void show() {
        board.setContentPane(new Container());
        run();
        board.revalidate();
        board.repaint();
    }

    /**
     * add actionlisteners for two buttons, manage account and access conversations
     */
    public void addActionListeners() {
        //open peopleview gui
        peopleViewPressed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PeopleView gui;
                try {
                    gui = new PeopleView(board, user);
                    gui.show();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //open AccountManagerGUI
        accountManagerPressed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try { 
                    AccountManagerGUI accountGUI = new AccountManagerGUI(board, user.get("email"));
                    accountGUI.show();
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(null, e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }
    //create button layout and font format, setup content panel, run actionListeners
    public void run() {
        JPanel panel = new JPanel();
        this.container = board.getContentPane();
        container.setLayout(new BorderLayout());
        title = new JLabel();
        Font f = new Font("Helvetica", Font.TRUETYPE_FONT, 20);
        title.setFont(f);
        contentText = new JLabel();
        contentText.setFont(f);
        if (role == Role.Customer) {
            title.setText("Welcome to the Turkey Shop!");
            contentText.setText("Customer Main Menu: Select an option to continue");
        } else if (role == Role.Seller) {
            title.setText("Welcome to the Turkey Shop!");
            contentText.setText("Seller Main Menu: Select an option to continue");
        }
        title.setBounds(10,10, 200, 50);
        contentText.setBounds(60,40,200,50);
        upperPanel = new JPanel();
        bottomPanel = new JPanel();
        panel.add(title);
        panel.add(contentText);
       // upperPanel.add(title);
        //upperPanel.add(contentText);
        panel.add(upperPanel);

        peopleViewPressed = new JButton("Access Conversations");
        accountManagerPressed = new JButton("Manage Account");
        bottomPanel.add(peopleViewPressed);
        bottomPanel.add(accountManagerPressed);
        panel.add(bottomPanel);
        addActionListeners();
        container.add(panel, BorderLayout.CENTER);
    }
}