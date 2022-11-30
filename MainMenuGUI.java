import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainMenuGUI {
    private JFrame board;
    private Container container;

    private JPanel upperPanel;
    private JPanel bottomPanel;
    private Role role;
    private ArrayList<String> users;
    private JLabel title;

    //2 User Options
    private JButton peopleViewPressed;
    private JButton accountManagerPressed;


    public MainMenuGUI(JFrame frame, ArrayList<String> users, Role role) {
        this.board = frame;
        this.role = role;
        this.users = users;
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
                PeopleView gui = new PeopleView(board, users, role, null);
                gui.show();
            }
        });
        //TODO
        accountManagerPressed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Must be changed to add the account manager GUI --> Not created YET
                PeopleView gui = new PeopleView(board, users, role, null);
                gui.show();
            }
        });

    }

    public void run() {
        JPanel panel = new JPanel();
        this.container = board.getContentPane();
        container.setLayout(new BorderLayout());
        board.setSize(600,400);
        board.setLocationRelativeTo(null);
        board.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        board.setVisible(true);
        title = new JLabel();
        Font f = new Font("Helvetica", Font.TRUETYPE_FONT, 25);
        title.setFont(f);
        if (role == Role.Customer) {
            title.setText("Welcome to the Turkey Shop! Customer Main Menu: Select an option to continue");
        } else if (role == Role.Seller) {
            title.setText("Welcome to the Turkey Shop! Seller Main Menu: Select an option to continue");
        }
        upperPanel = new JPanel();
        bottomPanel = new JPanel();
        upperPanel.add(title);
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
