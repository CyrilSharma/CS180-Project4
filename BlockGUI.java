import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Project 5 -> BlockGUI
 *
 * Allows user to block other users from sending messages to them and edit their visibility to others
 * User can view list of users they have blocked or are invisible to.
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class BlockGUI implements Runnable {
    private JFrame board;
    private Container content;
    private JScrollPane convPane;
    private JScrollPane scrollPane;
    private JPanel rightPanel;
    private JPanel upperPanel;
    private JButton blockButton;
    private JButton invisibleButton;
    private JButton unblockButton;
    private JButton undoInvisibilityButton;
    private JButton viewUnblockedListButton;
    private JButton viewBlockedListButton;
    private JButton viewInvisibilityList;
    private JButton viewAllUsersButton;
    private JButton backButton;
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
    /**
     * Creates BlockGUI frame
     * @param frame -> JFrame
     * @throws Exception
     */
    public BlockGUI(JFrame frame) throws Exception {
        //this.email = email;
        this.board = frame;
        blockGUIInterface = new BlockGUIInterface();
    }
    /**
     * Creates the buttons and scrollbar (BorderLayout) used in setFrame()
     */
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
        undoInvisibilityButton = new JButton("Go visible");
        viewUnblockedListButton = new JButton("View Unblocked List");
        viewBlockedListButton = new JButton("View Blocked List");
        viewInvisibilityList = new JButton("View Invisibility List");
        viewAllUsersButton = new JButton("View All Users");
        backButton = new JButton("Back");
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
        rightPanel.add(backButton);
        convPane.add(upperPanel);
        convPane.add(rightPanel);
        convPane.add(scrollPane);
        //scrollPane.add(people);
    }
    /**
     * Creates the actual frame layout (buttons, users, etc.) with locations and fonts
     */
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
        backButton.setBounds(20, 340, 160, 30);
    }
    /**
     * show the BlockGUI (JFrame) when called
     */
    public void show() {
        board.setContentPane(new Container());
        run();
        board.revalidate();
        board.repaint();
    }
    /**
     * initialize the userList and button actionListeners
     */
    @Override
    public void run() {
        //pull all users from interface
        try {
            userList.addAll(blockGUIInterface.getAllUsers());
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        board.setSize(600,400);
        users = new JList(userList);
        createAndAdd();
        setFrame();
        content.add(convPane, BorderLayout.CENTER);
        //block a user from sending message to current user
        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userEmail = (String) users.getSelectedValue();
                String msg = "Trying to block " + userEmail;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                //check if user is blocked already, not needed if we stick with unblockedList
                try {
                    //call blockUser, invisible set to false
                    blockGUIInterface.blockUser(userEmail, false);
                    JOptionPane.showMessageDialog(null, userEmail + " has been blocked", "Success", JOptionPane.INFORMATION_MESSAGE);
                    ArrayList<String> allUsers = blockGUIInterface.getAllUsers();
                    //reset list to show all users and update UI
                    userList.clear();
                    userList.addAll(allUsers);
                    users.updateUI();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //become invisible, no longer appear to the chosen user
        invisibleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userEmail = (String) users.getSelectedValue();
                String msg = "Trying to become invisible from " + userEmail;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                try {
                    //call blockUser, set invisible to tru
                    blockGUIInterface.blockUser(userEmail, true);
                    JOptionPane.showMessageDialog(null, "You are invisible to " + userEmail, "Success", JOptionPane.INFORMATION_MESSAGE);
                    ArrayList<String> allUsers = blockGUIInterface.getAllUsers();
                    //reset list to show all users and update UI
                    userList.clear();
                    userList.addAll(allUsers);
                    users.updateUI();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //unblock a previously blocked user
        unblockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userEmail = (String) users.getSelectedValue();
                String msg = "Trying to unblock " + userEmail;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                try {
                    //unblock the user
                    blockGUIInterface.unblockUser(userEmail, false);
                    BlockGUI blockGUI = new BlockGUI(board);
                    blockGUI.show();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //undo invisibility
        undoInvisibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userEmail = (String) users.getSelectedValue();
                String msg = "Trying to undo the invisibility of " + userEmail;
                JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                try {
                    blockGUIInterface.unblockUser(userEmail, true);
                    BlockGUI blockGUI = new BlockGUI(board);
                    blockGUI.show();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //View list of all users the current user has blocked
        viewBlockedListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<String> blocked = blockGUIInterface.getUsers("blocked");
                    //clear then set list to show blockedUsers and update UI
                    userList.clear();
                    userList.addAll(blocked);
                    users.updateUI();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //view list of every user, blocked or not
        viewAllUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<String> allUsers = blockGUIInterface.getAllUsers();
                    //set list to show allUsers and update UI
                    userList.clear();
                    userList.addAll(allUsers);
                    users.updateUI();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //view list of users that the current user is invisible to
        viewInvisibilityList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<String> invisible = blockGUIInterface.getUsers("invisible");
                    //set list to show users set to invisible and update UI
                    userList.clear();
                    userList.addAll(invisible);
                    users.updateUI();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //view list of users that are currently not blocked
        viewUnblockedListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<String> unblockedUsers = blockGUIInterface.getUnblockedUsers();
                    //set list to show unblockedUsers and update UI
                    userList.clear();
                    userList.addAll(unblockedUsers);
                    users.updateUI();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                //This shows the unblockedUsers list so that if a user wants to block
                //a user from the unblocked user list, he/she can do so
            }
        });
        //go back to previous frame (accountManagerGUI)

        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AccountManagerGUI accountManagerGUI = new AccountManagerGUI(board, (String) blockGUIInterface.geTranslator().query(new Query("User", "getEmail")));
                    accountManagerGUI.show();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        });
    }
}

