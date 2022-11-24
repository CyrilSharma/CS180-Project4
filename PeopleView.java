import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PeopleView implements Runnable {
    private JFrame board;
    private Container content;
    private JScrollPane convPane;
    private JScrollPane scrollPane;
    private JPanel rightPanel;
    private JPanel upperPanel;
    private JLabel title;
    private JList people;
    private JList statusList;
    private JTextField searchBar;
    private JButton searchButton;
    private JButton blockButton;
    private JButton viewButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton placeholder;
    private JButton backButton;
    private ArrayList<String> users;
    private Role role;
    private ArrayList<String> status;

    //pass a list of emails of users
    public PeopleView(ArrayList<String> emails, Role role) {
        board = new JFrame("Turkey Shop");
        users = emails;
        this.role = role;
        String[] ex = {"Online", "Offline", "Online"};
        status = new ArrayList<>(Arrays.asList(ex));
    }
    public void show() {
        SwingUtilities.invokeLater(this);
    }
    public void createAndAdd() {
        content = board.getContentPane();
        content.setLayout(new BorderLayout());
        String[] user = users.toArray(new String[0]);
        people = new JList(user);
        statusList = new JList(status.toArray(new String[0]));
        convPane = new JScrollPane();
        scrollPane = new JScrollPane(people, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(370,400));
        //people.setBackground(Color.gray);
        rightPanel = new JPanel();
        title = new JLabel();
        blockButton = new JButton("Block");
        viewButton = new JButton("Message");
        editButton = new JButton("Edit Message");
        deleteButton = new JButton("Delete Message");
        backButton = new JButton("Back");
        upperPanel = new JPanel();
        searchBar = new JTextField("Search...");
        placeholder = new JButton();
        searchButton = new JButton("Search");
        if (role == Role.Customer) {
            title.setText("Sellers");
        } else {
            title.setText("Customers");
        }
        //rightPanel.setBackground(Color.RED);
        upperPanel.add(title);
        upperPanel.add(searchBar);
        upperPanel.add(searchButton);
        upperPanel.add(placeholder);
        rightPanel.add(blockButton);
        rightPanel.add(viewButton);
        rightPanel.add(editButton);
        rightPanel.add(deleteButton);
        rightPanel.add(backButton);
        convPane.add(upperPanel);
        convPane.add(rightPanel);
        convPane.add(scrollPane);
        //scrollPane.add(people);
    }
    public void setFrame() {
        convPane.setLayout(null);
        rightPanel.setLayout(null);
        upperPanel.setLayout(null);
        convPane.setSize(500,400);
        //convPane.setBackground(Color.GRAY);
        scrollPane.setBounds(30,70,370,250);

        //scrollPane.setBackground(Color.green);
        upperPanel.setSize(400,70);
        people.setBounds(0,0,330,5000);
        convPane.setPreferredSize(new Dimension(370, 800));
        title.setBounds(10,10, 200, 50);
        Font f = new Font("Helvetica", Font.BOLD, 25);
        title.setFont(f);
        rightPanel.setBounds(400,0, 200, 400);
        blockButton.setBounds(20, 20, 160,30);
        viewButton.setBounds(20, 60, 160,30);
        //editButton.setBounds(20, 100, 160,30);
        //deleteButton.setBounds(20, 140, 160,30);
        placeholder.setBounds(-1,-1,1,1);
        backButton.setBounds(100, 320, 80, 30);
        searchBar.setBounds(150,25,180,20);

        searchButton.setBounds(335,20,70,30);
    }

    public void addActionListeners() {
        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = (String) people.getSelectedValue();
                String msg = "Trying to block " + email;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = (String) people.getSelectedValue();
                String msg = "Trying to view a conversation with " + email;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                MessageGUI gui = new MessageGUI("view", email);
                gui.show();
                board.dispatchEvent(new WindowEvent(board, WindowEvent.WINDOW_CLOSING));

            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = (String) people.getSelectedValue();
                String msg = "Trying to edit a conversation with " + email;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                MessageGUI gui = new MessageGUI("edit", email);
                gui.show();
                board.dispatchEvent(new WindowEvent(board, WindowEvent.WINDOW_CLOSING));
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = (String) people.getSelectedValue();
                String msg = "Trying to delete a conversation with " + email;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                MessageGUI gui = new MessageGUI("delete", email);
                gui.show();
                board.dispatchEvent(new WindowEvent(board, WindowEvent.WINDOW_CLOSING));
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //will change to mainGUI if it is uploaded
                MainMenuGUI gui = new MainMenuGUI(users, role);
                gui.show();
                board.dispatchEvent(new WindowEvent(board, WindowEvent.WINDOW_CLOSING));
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchUser();
            }
        });
    }
    public void run() {
        board.setSize(600,400);
        board.setLocationRelativeTo(null);
        board.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        board.setVisible(true);
        createAndAdd();
        setFrame();
        addActionListeners();
        searchBar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField field = (JTextField)e.getComponent();
                if (field.getText().equals("Search...")) {
                    field.setText("");
                }
                field.setForeground(Color.BLACK);
                //field.removeFocusListener(this);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setForeground(Color.GRAY);
                    searchBar.setText("Search...");
                }
            }

        });
        //people.setBackground(Color.GREEN);
        content.add(convPane, BorderLayout.CENTER);
    }

    public void searchUser() {
        if (searchBar.getText().equals("Search...") || searchBar.getText().isEmpty()) {
            //JOptionPane.showMessageDialog(null, "Please enter an email!", "Alert", JOptionPane.ERROR_MESSAGE);
            people.setListData(users.toArray());
            people.updateUI();
            return;
        }
        String text = searchBar.getText();
        ArrayList<String> updated = new ArrayList<>();
        for (String user: users) {
            if (user.contains(text)) {
                updated.add(user);
            }
        }
        people.setListData(updated.toArray());
        people.updateUI();
    }
}
