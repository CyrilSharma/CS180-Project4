import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class BlockGUI implements Runnable {
    private JFrame board;
    private Container content;
    private JScrollPane convPane;
    private JScrollPane scrollPane;
    private JPanel rightPanel;
    private JPanel upperPanel;
    private JButton unblockButton;
    private JButton blockButton;
    private JButton viewUnblockedListButton;
    private JLabel title;
    private JList users;
    private String action;
    private ArrayList<String> unblockedUsers;
    private ArrayList<String> blockedUsers;
    private String email;

    public BlockGUI(String email, String action, ArrayList<String> blockedUsers) {
        this.email = email;
        this.action = action;
        this.board = new JFrame("Turkey Shop");
        this.blockedUsers = blockedUsers;
        this.title.setText("Blocked Users");
    }
    public void createAndAdd(ArrayList<String> list) {
        content = board.getContentPane();
        content.setLayout(new BorderLayout());
        String[] user = list.toArray(new String[0]);
        users = new JList(user);
        //statusList = new JList(status.toArray(new String[0]));
        convPane = new JScrollPane();
        scrollPane = new JScrollPane(users, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(370,400));
        //people.setBackground(Color.gray);
        rightPanel = new JPanel();
        title = new JLabel();
        blockButton = new JButton("Block User");
        unblockButton = new JButton("Unblock User");
        viewUnblockedListButton = new JButton("View Unblock List");
        upperPanel = new JPanel();

        upperPanel.add(title);
        rightPanel.add(blockButton);
        rightPanel.add(unblockButton);
        rightPanel.add(viewUnblockedListButton);

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
        users.setBounds(0,0,330,5000);
        convPane.setPreferredSize(new Dimension(370, 800));
        title.setBounds(10,10, 200, 50);
        Font f = new Font("Helvetica", Font.BOLD, 25);
        title.setFont(f);
        rightPanel.setBounds(400,0, 200, 400);
        blockButton.setBounds(20, 20, 160,30);
        blockButton.setBounds(20, 60, 160,30);
        unblockButton.setBounds(20, 100, 160,30);
        viewUnblockedListButton.setBounds(20, 140, 160,30);
    }
    public void show() {
        SwingUtilities.invokeLater(this);
    }

    @Override
    public void run() {
        board.setSize(600,400);
        board.setLocationRelativeTo(null);
        board.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        board.setVisible(true);
        createAndAdd(blockedUsers);
        setFrame();
        content.add(convPane, BorderLayout.CENTER);
        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Do an action for blocking a user
                String userEmail = (String) users.getSelectedValue();
                String msg = "Trying to unblock " + userEmail;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        unblockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Do an action for unblocking a user
                String userEmail = (String) users.getSelectedValue();
                String msg = "Trying to unblock " + userEmail;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                unblockedUsers.add(userEmail);
            }
        });

        viewUnblockedListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAndAdd(unblockedUsers);
                //This shows the unblockedUsers list so that if a user wants to block
                //a user from the unblocked user list, he/she can do so
            }
        });
    }
}

