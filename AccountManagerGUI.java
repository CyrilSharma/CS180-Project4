import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private String currentUserName;
    private AccountInterfaceClient aic;

    public AccountManagerGUI(JFrame board, String currentUserName) {
        board.setSize(600,500);
        accountBoard = board;
        this.currentUserName = currentUserName;
        aic = new AccountInterfaceClient();
    }

    public void addActionListeners() {
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
                        } catch (Exception error) {
                            JOptionPane.showMessageDialog(null, 
                                "We're having trouble communicating with the server.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        editPasswordButton.addActionListener(new ActionListener() {
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
            @Override
            public void actionPerformed(ActionEvent e) {
                int ans = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account? ", "Edit Username", JOptionPane.INFORMATION_MESSAGE);
                if (ans == JOptionPane.YES_OPTION) {
                    try {
                        aic.deleteAccount(currentUserName);
                    } catch (Exception error) {
                        JOptionPane.showMessageDialog(null, 
                        "We're having trouble communicating with the server.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        dashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //DashBoardGui gui = new DashBoardGUI(board, "view", email, "test", store);
                //gui.show();
            }
        });
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //FilterGUI gui = new FilterGUI(board, "view", email, "test", store);
                //gui.show();
            }
        });
        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogInGUI gui = new LogInGUI(accountBoard);
                gui.show();
            }
        });
        blockUserButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                try {
                    BlockGUI blockGUI = new BlockGUI(accountBoard);
                    blockGUI.show();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        exportCSV.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                ExportCSV exportCSV;
                try {
                    exportCSV = new ExportCSV(accountBoard, currentUserName);
                    exportCSV.show();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        });
    }

    public void run() {
        this.container = accountBoard.getContentPane();
        container.setLayout(new BorderLayout());
        Font f = new Font("Helvetica", Font.TRUETYPE_FONT, 24);
        loginName = new JLabel();
        loginName.setFont(f);
        loginName.setText(currentUserName);
        JPanel rightPanel = new JPanel();
        editUsernameButton = new JButton("Edit Username");
        editPasswordButton = new JButton("Edit Password");
        dashboardButton = new JButton("Access Dashboard");
        filterButton = new JButton("Access Filter");
        signOutButton = new JButton("Sign out");
        deleteAccountButton = new JButton("Delete Account");
        blockUserButton = new JButton("Unblock/Visibility");
        exportCSV = new JButton("Export Conversations");
        rightPanel.add(loginName);
        rightPanel.add(editUsernameButton);
        rightPanel.add(editPasswordButton);
        //rightPanel.setBounds(400,0,200,530);
        loginName.setBounds(0, 40, 160,30);
        editUsernameButton.setBounds(300, 100, 160,30);
        editPasswordButton.setBounds(300, 60, 160,30);
        //blockUserButton.setBounds(650, 100, 160, 30);
        //exportCSV.setBounds(650, 340, 160, 30);
        JPanel contents = new JPanel();
        JPanel contents2 = new JPanel();
        contents.add(dashboardButton);
        //contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents.add(filterButton);
        //contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents.add(signOutButton);
        //contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents2.add(deleteAccountButton);
        //contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents2.add(blockUserButton);
       // contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents2.add(exportCSV);
        System.out.println("WHERE THIS AT");
        accountBoard.setVisible(true);
        addActionListeners();

        container.add(contents, BorderLayout.CENTER);
        container.add(contents2, BorderLayout.NORTH);
        container.add(rightPanel, BorderLayout.SOUTH);
    }
    public void show() {
        accountBoard.setContentPane(new Container());
        run();
        accountBoard.revalidate();
        accountBoard.repaint();
    }
}