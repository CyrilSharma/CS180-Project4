import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MainMenuGUI {
    private JFrame board;
    private Container container;

    private JPanel upperPanel;
    private JPanel bottomPanel;
    private HashMap<String,String> user;
    private Role role;
    private ArrayList<String> users;
    private JLabel title;
    private JLabel contentText;

    //2 User Options
    private JButton peopleViewPressed;
    private JButton accountManagerPressed;
    
    private Translator translator;


    public MainMenuGUI(JFrame frame, ArrayList<String> users, HashMap<String,String> user) {
        frame.setSize(600,400);
        this.board = frame;
        this.user = user;
        // used frequently enough to justify a seperate variable.
        this.role = Role.valueOf(user.get("role"));
        this.users = users;
        translator = new Translator();
    }
    public void show() {
        board.setContentPane(new Container());
        run();
        board.revalidate();
        board.repaint();
    }

    @SuppressWarnings("unchecked")
    public void addActionListeners() {
        peopleViewPressed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PeopleView gui;
                try {
                    HashMap<String,String> stores;
                    if (role.equals(Role.Customer)) {
                            stores = (HashMap<String, String>) translator.query(
                            new Query("User", "viewStores"));
                    } else {
                        stores = (HashMap<String, String>) translator.query(
                            new Query("User", "getStores"));
                    }
                    if (stores == null) {
                        JOptionPane.showMessageDialog(null, "No store available!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    gui = new PeopleView(board, users, user, stores);
                    gui.show();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        accountManagerPressed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try { 
                    AccountManagerGUI accountGUI = new AccountManagerGUI(board, user.get("email"), users);
                    accountGUI.show();
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(null, e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

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