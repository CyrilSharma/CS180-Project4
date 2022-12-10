import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Project 5 -> AccountManagerGUI
 *
 * Allows user to handle various account features: block, visibility,
 * edit username/password, access dashboard/filter, export CSVs,
 * delete account, and sign out
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
public class AccountManagerGUI {
    private JFrame accountBoard;
    private Container container;
    private JButton editUsernameButton;
    private JButton editPasswordButton;
    private JLabel loginName;
    private JButton dashboardButton;
    private JButton filterButton;
    private JButton signOutButton;
    private JButton deleteAccountButton;
    private JButton blockUserButton;
    private JButton exportCSV;
    private JButton backButton;
    private String currentUserName;
    private AccountInterfaceClient aic;

    /**
     * Create AccountManagerGUI frame for user (constructor)
     * @param board -> JFrame
     * @param currentUserName -> current user
     */
    public AccountManagerGUI(JFrame board, String currentUserName) {
        board.setSize(600,500);
        accountBoard = board;
        this.currentUserName = currentUserName;
        aic = new AccountInterfaceClient();
    }

    /**
     * add action listeners for all the buttons
     */
    public void addActionListeners() {
        //edit username/email (cannot be one that exists)
        editUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ans = JOptionPane.showConfirmDialog(null, "Do you want to edit your username? ", "Edit Username", JOptionPane.INFORMATION_MESSAGE);
                if (ans == JOptionPane.YES_OPTION) {
                    String editedUsername = (String) (JOptionPane.showInputDialog(null, "Please enter your edited username: ", "Username", JOptionPane.QUESTION_MESSAGE));
                    if (editedUsername != null) {
                        try {
                            aic.modifyUsername(currentUserName, editedUsername);
                            JOptionPane.showMessageDialog(null, "Username Edited");
                            loginName.setText(editedUsername);
                        } catch (Exception error) {
                            JOptionPane.showMessageDialog(null, 
                                error.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        editPasswordButton.addActionListener(new ActionListener() {
            //edit password
            @Override
            public void actionPerformed(ActionEvent e) {
                int ans = JOptionPane.showConfirmDialog(null, "Do you want to edit your password? ", "Edit Password", JOptionPane.INFORMATION_MESSAGE);
                if (ans == JOptionPane.YES_OPTION) {
                    String editedPassword = (String) (JOptionPane.showInputDialog(null, "Please enter your edited password: ", "Username", JOptionPane.QUESTION_MESSAGE));
                    if (editedPassword != null) {
                        try {
                            aic.modifyPassword(currentUserName, editedPassword);
                            JOptionPane.showMessageDialog(null, "Password Edited");
                        } catch (Exception error) {
                            JOptionPane.showMessageDialog(null, 
                                "We're having trouble communicating with the server.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        deleteAccountButton.addActionListener(new ActionListener() {
            //delete account
            @Override
            public void actionPerformed(ActionEvent e) {
                int ans = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account? ", "Edit Username", JOptionPane.INFORMATION_MESSAGE);
                if (ans == JOptionPane.YES_OPTION) {
                    try {
                        aic.deleteAccount(currentUserName);
                        aic.logout();
                        LogInGUI gui = new LogInGUI(accountBoard);
                        gui.show();
                    } catch (Exception error) {
                        JOptionPane.showMessageDialog(null, 
                        "We're having trouble communicating with the server.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        dashboardButton.addActionListener(new ActionListener() {
            //pull up dashboard
            @Override
            public void actionPerformed(ActionEvent e) {
                Translator tr = new Translator();
                try {
                    DashboardGUI gui = new DashboardGUI(accountBoard, tr.get("email", currentUserName));
                    gui.show();
                } catch (Exception er) {
                    er.getMessage();
                }

            }
        });
        filterButton.addActionListener(new ActionListener() {
            //pull up filterGUI where words can be filtered out
            @Override
            public void actionPerformed(ActionEvent e) {
                FilterPanel gui = new FilterPanel(accountBoard, currentUserName);
                gui.show();
            }
        });
        signOutButton.addActionListener(new ActionListener() {
            //sign out of account
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    aic.logout();
                    LogInGUI gui = new LogInGUI(accountBoard);
                    gui.show();
                } catch (Exception e1) {

                    JOptionPane.showMessageDialog(null, "We are having trouble logging you out", "Error", JOptionPane.ERROR_MESSAGE);
                    LogInGUI gui = new LogInGUI(accountBoard);
                    gui.show();
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            //go back to previous frame (MainMenuGUI)
            @Override
            public void actionPerformed(ActionEvent e) {
                Translator tr = new Translator();
                try {
                    MainMenuGUI gui = new MainMenuGUI(accountBoard, tr.get("email", currentUserName));
                    gui.show();
                } catch (Exception er) {

                }

            }
        });
        blockUserButton.addActionListener(new ActionListener() {
            //open BlockGUI
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BlockGUI blockGUI = new BlockGUI(accountBoard);
                    blockGUI.show();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        exportCSV.addActionListener(new ActionListener() {
            //enter exportCSV to export certain conversations
            @Override
            public void actionPerformed(ActionEvent e) {
                ExportCSV exportCSV;
                try {
                    exportCSV = new ExportCSV(accountBoard, currentUserName);
                    exportCSV.show();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        });
    }

    /**
     * Create frame with buttons and formatted layout
     */
    public void run() {
        this.container = accountBoard.getContentPane();
        container.setLayout(new BorderLayout());
        Font f = new Font("Helvetica", Font.TRUETYPE_FONT, 15);
        loginName = new JLabel();
        loginName.setFont(f);
        loginName.setText(currentUserName);
        JPanel rightPanel = new JPanel();
        editUsernameButton = new JButton("Edit Username");
        editPasswordButton = new JButton("Edit Password");
        dashboardButton = new JButton("Access Dashboard");
        filterButton = new JButton("Access Filter");
        signOutButton = new JButton("Sign out");
        backButton = new JButton("back");
        deleteAccountButton = new JButton("Delete Account");
        blockUserButton = new JButton("Unblock/Visibility");
        exportCSV = new JButton("Export Conversations");
        rightPanel.add(loginName);
        rightPanel.add(editUsernameButton);
        rightPanel.add(editPasswordButton);
        rightPanel.setBounds(400,0,200,530);
        loginName.setBounds(20, 20, 160,30);
        editUsernameButton.setBounds(20, 100, 160,30);
        editPasswordButton.setBounds(20, 60, 160,30);
        blockUserButton.setBounds(20, 20, 160, 30);
        exportCSV.setBounds(20, 240, 160, 30);
        backButton.setBounds(20,280,160,30);
        JPanel contents = new JPanel();
        contents.add(dashboardButton);
        contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents.add(filterButton);
        contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents.add(signOutButton);
        contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents.add(deleteAccountButton);
        contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents.add(blockUserButton);
        contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents.add(exportCSV);
        contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents.add(backButton);
        System.out.println("WHERE THIS AT");
        accountBoard.setVisible(true);
        addActionListeners();
        container.add(rightPanel, BorderLayout.EAST);
        container.add(contents, BorderLayout.CENTER);
    }

    /**
     * display AccountManagerGUI JFrame
     */
    public void show() {
        accountBoard.setContentPane(new Container());
        run();
        accountBoard.revalidate();
        accountBoard.repaint();
    }
}