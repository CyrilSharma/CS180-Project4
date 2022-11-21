import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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
    private JButton blockButton;
    private JButton viewButton;
    private JButton editButton;
    private JButton deleteButton;
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
        blockButton = new JButton("block");
        viewButton = new JButton("view");
        editButton = new JButton("Edit");
        deleteButton = new JButton("delete");
        backButton = new JButton("Log in");
        upperPanel = new JPanel();
        if (role == Role.Customer) {
            title.setText("Sellers");
        } else {
            title.setText("Customers");
        }
        //rightPanel.setBackground(Color.RED);
        upperPanel.add(title);
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
        convPane.setSize(400,400);
        //convPane.setBackground(Color.GRAY);
        scrollPane.setBounds(30,70,370,250);

        //scrollPane.setBackground(Color.green);
        upperPanel.setSize(400,70);
        people.setBounds(0,0,330,5000);
        convPane.setPreferredSize(new Dimension(370, 800));
        title.setBounds(10,10, 200, 50);
        Font f = new Font("Helvetica", Font.BOLD, 25);
        title.setFont(f);
        rightPanel.setBounds(400,0, 100, 400);
        blockButton.setBounds(10, 20, 80,30);
        viewButton.setBounds(10, 60, 80,30);
        editButton.setBounds(10, 100, 80,30);
        deleteButton.setBounds(10, 140, 80,30);
        backButton.setBounds(10, 320, 80, 30);
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
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = (String) people.getSelectedValue();
                String msg = "Trying to edit a conversation with " + email;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = (String) people.getSelectedValue();
                String msg = "Trying to delete a conversation with " + email;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //will change to mainGUI if it is uploaded
                LogInGUI gui = new LogInGUI();
                gui.show();
                board.dispatchEvent(new WindowEvent(board, WindowEvent.WINDOW_CLOSING));
            }
        });
    }
    public void run() {
        board.setSize(500,400);
        board.setLocationRelativeTo(null);
        board.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        board.setVisible(true);
        createAndAdd();
        setFrame();
        addActionListeners();
        //people.setBackground(Color.GREEN);
        content.add(convPane, BorderLayout.CENTER);
    }
}
