import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

//TODO: implement invisibility, show in PeopleView, database integration, clean up formatting, autoupdate blocked list & all users,
//remove blocked users from being visible, find name of user
public class BlockGUI implements Runnable {
    private JFrame board;
    private Container content;
    private JScrollPane convPane;
    private JScrollPane scrollPane;
    private JPanel rightPanel;
    private JPanel upperPanel;
    private JButton unblockButton;
    private JButton blockButton;
    private JButton invisibleButton;
    private JButton undoInvisibilityButton;
    private JButton viewUnblockedListButton;
    private JButton viewBlockedListButton;
    private JButton viewInvisibilityList;
    private JButton viewAllUsersButton;
    private JLabel title;
    private JList users;
    private DefaultListModel userList = new DefaultListModel();
    private String action;
    private ArrayList<String> unblockedUsers;
    private ArrayList<String> blockedUsers;
    private ArrayList<String> allUsers;
    private ArrayList<String> invisibleUsers;
    private String userToBlock;


    /** Not sure if action parameter is needed
     * public BlockGUI(String email, String action, ArrayList<String> blockedUsers) {
        //are action and
        this.email = email;
        this.action = action;
        this.board = new JFrame("Turkey Shop");
        this.blockedUsers = blockedUsers;
        this.title.setText("Blocked Users");
    }
     */
    //Test constructor
    public BlockGUI(String userToBlock, ArrayList<String> allUsers, ArrayList<String> blockedUsers, ArrayList<String> invisibleUsers) {
        //this.email = email;
        this.userToBlock = userToBlock;
        this.allUsers = allUsers;
        this.blockedUsers = blockedUsers;
        this.invisibleUsers = invisibleUsers;
        this.board = new JFrame("Turkey Shop");
    }
    //Other constructor
    public BlockGUI(ArrayList<String> allUsers, ArrayList<String> blockedUsers) {
        this.allUsers = allUsers;
        this.blockedUsers = blockedUsers;
        this.board = new JFrame("Turkey Shop");
    }
    public void createAndAdd() {
        content = board.getContentPane();
        content.setLayout(new BorderLayout());
        //statusList = new JList(status.toArray(new String[0]));
        convPane = new JScrollPane();
        scrollPane = new JScrollPane(users, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(370,400));
        //people.setBackground(Color.gray);
        rightPanel = new JPanel();
        title = new JLabel();
        blockButton = new JButton("Block User");
        unblockButton = new JButton("Unblock User");
        invisibleButton = new JButton("Go Invisible");
        undoInvisibilityButton = new JButton("Undo Invisibility");
        viewUnblockedListButton = new JButton("View Unblocked List");
        viewBlockedListButton = new JButton("View Blocked List");
        viewInvisibilityList = new JButton("View Invisibility List");
        viewAllUsersButton = new JButton("View All Users");
        upperPanel = new JPanel();
        upperPanel.add(title);
        rightPanel.add(blockButton);
        rightPanel.add(unblockButton);
        rightPanel.add(invisibleButton);
        rightPanel.add(undoInvisibilityButton);
        rightPanel.add(viewUnblockedListButton);
        rightPanel.add(viewBlockedListButton);
        rightPanel.add(viewInvisibilityList);
        rightPanel.add(viewAllUsersButton);

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
        unblockButton.setBounds(20, 60, 160,30);
        invisibleButton.setBounds(20, 100, 160, 30);
        undoInvisibilityButton.setBounds(20, 140, 160, 30);
        viewUnblockedListButton.setBounds(20, 180, 160,30);
        viewBlockedListButton.setBounds(20, 220, 160,30);
        viewInvisibilityList.setBounds(20, 260, 160, 30);
        viewAllUsersButton.setBounds(20, 300, 160,30);
    }
    public void show() {
        SwingUtilities.invokeLater(this);
    }

    @Override
    public void run() {
        String[] listOfUsers = allUsers.toArray(new String[0]);
        userList.addAll(List.of(listOfUsers));
        boolean blocked = false;
        if (userToBlock!=null) {
            for (String user: blockedUsers) {
                if (user.equals(userToBlock)) {
                    JOptionPane.showMessageDialog(null, "This user has already been blocked.", "Cannot Block", JOptionPane.INFORMATION_MESSAGE);
                    blocked = true;
                }
            }
            if (!blocked) {
                blockedUsers.add(userToBlock);
                userList.removeElement(userToBlock);
            }
        }
        for (String blockedUser: allUsers) {
            if (blockedUsers.contains(blockedUser)) {
                userList.removeElement(blockedUser);
            }
        }
        if (unblockedUsers == null) {
            unblockedUsers = allUsers;
            if (blockedUsers != null) {
                unblockedUsers.removeAll(blockedUsers);
            }
        }
        board.setSize(600,400);
        board.setLocationRelativeTo(null);
        board.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        board.setVisible(true);
        users = new JList(userList);
        createAndAdd();
        setFrame();
        content.add(convPane, BorderLayout.CENTER);
        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Do an action for blocking a user/pull and send back to backend
                String userEmail = (String) users.getSelectedValue();
                String msg = "Trying to block " + userEmail;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                //check if user is blocked already, not needed if we stick with unblockedList
                boolean blocked = false;
                for (String user: blockedUsers) {
                    if (user.equals(userEmail)) {
                        JOptionPane.showMessageDialog(null, "This user has already been blocked.", "Cannot Block", JOptionPane.INFORMATION_MESSAGE);
                        blocked = true;
                    }
                }
                if (!blocked) {
                    blockedUsers.add(userEmail);
                    userList.removeElement(userEmail);
                    users.updateUI();
                }
                /**
                String confirm = "Do you want to be invisible to " + userEmail + " ?";
                int ans = JOptionPane.showConfirmDialog(null, confirm, "Invisible?", JOptionPane.INFORMATION_MESSAGE);
                if (ans == JOptionPane.YES_OPTION) {
                    //delete from userlist for userEmail -> that user
                } else if (ans == JOptionPane.NO_OPTION){
                    //nothing, just block the userEmail -> user from sending messages to the current user

                }
                 */
            }
        });
        invisibleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (invisibleUsers == null) {
                    invisibleUsers = new ArrayList<>();
                }
                String userEmail = (String) users.getSelectedValue();
                String msg = "Trying to become invisible from " + userEmail;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                //TODO: action call to backend to be invisible from userEmail
                invisibleUsers.add(userEmail);
            }
        });
        unblockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (unblockedUsers == null) {
                    unblockedUsers = allUsers;
                    if (blockedUsers != null) {
                        unblockedUsers.removeAll(blockedUsers);
                    }
                }
                String userEmail = (String) users.getSelectedValue();
                String msg = "Trying to unblock " + userEmail;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                boolean blocked = false;
                for (String user: unblockedUsers) {
                    if (user.equals(userEmail)) {
                        JOptionPane.showMessageDialog(null, "This user has already been unblocked.", "Cannot Block", JOptionPane.INFORMATION_MESSAGE);
                        blocked = true;
                    }
                }
                if (!blocked) {
                    blockedUsers.remove(userEmail);
                    allUsers.add(userEmail);
                    userList.clear();
                    userList.addAll(unblockedUsers);
                    users.updateUI();
                }
            }
        });
        undoInvisibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userEmail = (String) users.getSelectedValue();
                String msg = "Trying to undo the invisibility of " + userEmail;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                boolean invisible = false;
                for (String user: invisibleUsers) {
                    if (user.equals(userEmail)) {
                        invisible = true;
                    }
                }
                if (invisible) {
                    invisibleUsers.remove(userEmail);
                    userList.clear();
                    userList.addAll(unblockedUsers);
                    users.updateUI();
                } else {
                    JOptionPane.showMessageDialog(null, "This user is already visible.", "Cannot Undo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        //Is this needed, instead maybe a viewBlockedList
        viewUnblockedListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userList.removeAllElements();
                userList.addAll(allUsers);
                for (String blocked: allUsers) {
                    if (blockedUsers.contains(blocked)) {
                        userList.removeElement(blocked);
                    }
                }
                users.updateUI();
                //This shows the unblockedUsers list so that if a user wants to block
                //a user from the unblocked user list, he/she can do so
            }
        });
        viewBlockedListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userList.clear();
                userList.addAll(blockedUsers);
                users.updateUI();
            }
        });
        viewAllUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userList.clear();
                userList.addAll(allUsers);
                users.updateUI();
            }
        });
        viewInvisibilityList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userList.clear();
                userList.addAll(invisibleUsers);
                users.updateUI();
            }
        });
    }
}

