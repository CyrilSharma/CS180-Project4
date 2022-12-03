import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
public class AccountManagerGUI implements Runnable {
    private JFrame accountBoard;
    private Container container;
    private JButton editUsernameButton;
    private JButton editPasswordButton;
    private JLabel loginName;
    private JButton dashboardButton;
    private JButton filterButton;
    private JButton signOutButton;
    private JButton deleteAccountButton;
    private String currentUserName;
    private ArrayList<String> users;
    private AccountInterfaceClient aic;

    public AccountManagerGUI(JFrame board, String currentUserName, ArrayList<String> users) {
        board.setSize(600,500);
        accountBoard = board;
        this.currentUserName = currentUserName;
        this.users = users;
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
                            int index = users.indexOf(currentUserName);
                            currentUserName = editedUsername;
                            users.set(index, editedUsername);
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
    }

    @Override
    public void run() {
        JPanel panel = new JPanel();
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
        deleteAccountButton = new JButton("Delete Account");
        rightPanel.add(loginName);
        rightPanel.add(editUsernameButton);
        rightPanel.add(editPasswordButton);
        rightPanel.setBounds(400,0,200,530);
        loginName.setBounds(20, 20, 160,30);
        editUsernameButton.setBounds(20, 100, 160,30);
        editPasswordButton.setBounds(20, 60, 160,30);
        JPanel contents = new JPanel();
        contents.add(dashboardButton);
        contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents.add(filterButton);
        contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents.add(signOutButton);
        contents.add(new JSeparator(SwingConstants.HORIZONTAL));
        contents.add(deleteAccountButton);
        accountBoard.setVisible(true);
        addActionListeners();
        container.add(panel, BorderLayout.CENTER);
    }
    public void show() {
        SwingUtilities.invokeLater(this);
    }
}
