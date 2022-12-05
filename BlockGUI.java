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
    private BlockGUIInterface blockGUIInterface;


    /** Not sure if action parameter is needed
     * public BlockGUI(String email, String action, ArrayList<String> blockedUsers) {
        //are action and
        this.email = email;
        this.action = action;
        this.board = new JFrame("Turkey Shop");
        this.blockedUsers = blockedUsers;
        this.title.setText("Blocked Users");
    }
     * @throws Exception
     */
    //Test constructor
    public BlockGUI(JFrame frame) throws Exception {
        //this.email = email;
        this.board = frame;
        blockGUIInterface = new BlockGUIInterface();
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
        board.setContentPane(new Container());
        run();
        board.revalidate();
        board.repaint();
    }

    @Override
    public void run() {
        try {
            userList.addAll(blockGUIInterface.getAllUsers());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        board.setSize(600,400);
        users = new JList(userList);
        createAndAdd();
        setFrame();
        content.add(convPane, BorderLayout.CENTER);
        unblockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userEmail = (String) users.getSelectedValue();
                String msg = "Trying to unblock " + userEmail;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                try {
                    blockGUIInterface.unblockUser(userEmail, false);
                    userList.clear();
                    userList.addAll(blockGUIInterface.getAllUsers());
                    users.updateUI();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        undoInvisibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userEmail = (String) users.getSelectedValue();
                String msg = "Trying to undo the invisibility of " + userEmail;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                try {
                    blockGUIInterface.unblockUser(userEmail, true);
                    userList.clear();
                    userList.addAll(blockGUIInterface.getAllUsers());
                    users.updateUI();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        viewBlockedListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    userList.addAll(blockGUIInterface.getUsers("blocked"));
                    userList.clear();
                    users.updateUI();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        viewAllUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    userList.addAll(blockGUIInterface.getAllUsers());
                    userList.clear();
                    users.updateUI();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        viewInvisibilityList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    userList.addAll(blockGUIInterface.getUsers("invisible"));
                    userList.clear();
                    users.updateUI();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

