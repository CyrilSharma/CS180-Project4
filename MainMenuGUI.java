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
    private Role role;
    private ArrayList<String> users;
    private JLabel title;
    private JLabel contentText;

    //2 User Options
    private JButton peopleViewPressed;
    private JButton accountManagerPressed;
    
    private Translator translator;


    public MainMenuGUI(JFrame frame, ArrayList<String> users, Role role) {
        frame.setSize(600,400);
        this.board = frame;
        this.role = role;
        this.users = users;
        translator = new Translator();
    }
    public void show() {
        board.setContentPane(new Container());
        run();
        board.revalidate();
        board.repaint();
    }

    public void addActionListeners() {
        peopleViewPressed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PeopleView gui;
                try {
                    if (role.equals(Role.Customer)) {
                        gui = new PeopleView(board, users, role, (HashMap<String, String>) translator.query(new Query("User", "viewStores")));
                    } else {
                        gui = new PeopleView(board, users, role, (HashMap<String, String>) translator.query(new Query("User", "getStores")));
                    }
                    gui.show();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //TODO
        accountManagerPressed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AccountManagerGUI accountGUI = new AccountManagerGUI(board, "testUserName", users);
                //TODO: Must be changed to add the account manager GUI --> Not created YET
                PeopleView gui;
                try {
                    accountGUI = new AccountManagerGUI(board, (String) translator.query(new Query("User", "getEmail")), (ArrayList<String>) translator.query(new Query("User", "getUsers")));
                    accountGUI.show();
                } catch (Exception e2) {
                    // TODO Auto-generated catch block
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
