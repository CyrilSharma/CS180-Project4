import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CreateAccountGUI implements Runnable {
    private JFrame board;
    private Container container;
    private JButton createButton;
    private JButton backButton;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private ArrayList<String> users;
    private JButton placeholder;

    private Role role;
    private JButton customerButton;
    private JButton sellerButton;
    private JLabel userSelectionLabel;
    private CreateAccountInterface createAccountInterface;

    public CreateAccountGUI() {
        this(new ArrayList<>());
    }
    public CreateAccountGUI(ArrayList<String> users) {
        board = new JFrame("Turkey Store");
        this.users = users;
        createAccountInterface = new CreateAccountInterface();
    }

    public void show() {
        SwingUtilities.invokeLater(this);
    }
    public void setFrame() {
        placeholder = new JButton();

        emailField.setBounds(board.getWidth()/2 - emailField.getWidth()/2, 100,
                emailField.getWidth(), emailField.getHeight());
        emailField.setForeground(Color.GRAY);
        emailField.setText("Email...");
        passwordField.setSize(150,30);
        passwordField.setBounds(board.getWidth()/2 - passwordField.getWidth()/2, 135,
                passwordField.getWidth(), passwordField.getHeight());
        passwordField.setForeground(Color.GRAY);
        passwordField.setText("Password...");
        confirmPasswordField.setSize(150,30);
        confirmPasswordField.setBounds(board.getWidth()/2 - confirmPasswordField.getWidth()/2, 170,
                confirmPasswordField.getWidth(), confirmPasswordField.getHeight());
        confirmPasswordField.setText("Confirm Password...");
        confirmPasswordField.setForeground(Color.GRAY);
        userSelectionLabel.setSize(150,30);
        userSelectionLabel.setLocation(board.getWidth()/2 - userSelectionLabel.getWidth()/2, 205);
        customerButton.setSize(150,30);
        customerButton.setLocation(board.getWidth()/2 - customerButton.getWidth()/2, 240);
        sellerButton.setSize(150,30);
        sellerButton.setLocation(board.getWidth()/2 - sellerButton.getWidth()/2, 275);
        createButton.setSize(150,30);
        createButton.setBounds(board.getWidth()/2 - createButton.getWidth()/2, 325,
                createButton.getWidth(), createButton.getHeight());

        backButton.setSize(90, 30);
        backButton.setBounds(20, 380,
                backButton.getWidth(), backButton.getHeight());
    }

    public void createAndAdd(JPanel panel) {
        createButton = new JButton("Create Account");
        emailField = new JTextField(10);
        passwordField = new JPasswordField(10);
        confirmPasswordField = new JPasswordField(10);
        backButton = new JButton("Back");
        placeholder = new JButton();
        customerButton = new JButton("Customer");
        sellerButton = new JButton("Seller");
        userSelectionLabel = new JLabel("select a role...");
        panel.setLayout(null);
        panel.add(emailField);
        panel.add(passwordField);
        panel.add(createButton);
        panel.add(confirmPasswordField);
        panel.add(backButton);
        panel.add(placeholder);
        panel.add(customerButton);
        panel.add(sellerButton);
        panel.add(userSelectionLabel);
        placeholder.setBounds(-1,-1,1,1);
        emailField.setSize(150,30);
    }

    public void addActionListeners() {
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = passwordField.getText();
                String cp = confirmPasswordField.getText();
                if (role == null) {
                    JOptionPane.showMessageDialog(null, "Please select a role!", "Alert", JOptionPane.WARNING_MESSAGE, null);
                    return;
                }
                if ((email.isEmpty() || email.equals("Email...")) ||
                        (password.isEmpty() || password.equals("Password..."))) {
                    JOptionPane.showMessageDialog(null, "Please fill out the text fields!", "Error", JOptionPane.WARNING_MESSAGE, null);
                } else if (!password.equals(cp)) {
                    JOptionPane.showMessageDialog(null, "Password does not match!", "Error", JOptionPane.WARNING_MESSAGE, null);
                } else {
                    //THIS DOES NOT CHECK CREDENTIALS YET
//                    String f = "Trying to create an account with credential {email: %s, pw: %s}\n";
//                    f = String.format(f, email, password);
//                    JOptionPane.showMessageDialog(null, f, "Message", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        createAccountInterface.add(email, password, cp);
                        MainMenuGUI gui = new MainMenuGUI(users, Role.Seller);
                        gui.show();
                        board.dispatchEvent(new WindowEvent(board, WindowEvent.WINDOW_CLOSING));
                    } catch (InvalidUserException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        CreateAccountGUI createAccountGUI = new CreateAccountGUI();
                        createAccountGUI.show();
                        board.dispatchEvent(new WindowEvent(board, WindowEvent.WINDOW_CLOSING));
                    }
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogInGUI gui = new LogInGUI();
                board.dispatchEvent(new WindowEvent(board, WindowEvent.WINDOW_CLOSING));
                gui.show();
            }
        });
        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                role = Role.Customer;
                userSelectionLabel.setText("role: customer");
            }
        });
        sellerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                role = Role.Seller;
                userSelectionLabel.setText("role: seller");
            }
        });
    }

    public void run() {
        JPanel panel = new JPanel();
        container = board.getContentPane();
        container.setLayout(new BorderLayout());
        board.setSize(450,450);
        board.setLocationRelativeTo(null);
        board.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        board.setVisible(true);
        createAndAdd(panel);
        setFrame();
        passwordField.setEchoChar((char)0);
        confirmPasswordField.setEchoChar((char)0);
        emailField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField field = (JTextField)e.getComponent();
                if (field.getText().equals("Email...")) {
                    field.setText("");
                }
                field.setForeground(Color.BLACK);
                //field.removeFocusListener(this);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setForeground(Color.GRAY);
                    emailField.setText("Email...");
                }
            }

        });
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField field = (JTextField)e.getComponent();
                if (field.getText().equals("Password...")) {
                    field.setText("");
                    passwordField.setEchoChar('*');
                }
                field.setForeground(Color.BLACK);
                //field.removeFocusListener(this);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getText().isEmpty()) {
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setEchoChar((char)0);
                    passwordField.setText("Password...");
                }
            }
        });
        confirmPasswordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField field = (JTextField)e.getComponent();
                if (field.getText().equals("Confirm Password...")) {
                    field.setText("");
                    confirmPasswordField.setEchoChar('*');
                }
                field.setForeground(Color.BLACK);
                //field.removeFocusListener(this);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (confirmPasswordField.getText().isEmpty()) {
                    confirmPasswordField.setForeground(Color.GRAY);
                    confirmPasswordField.setEchoChar((char)0);
                    confirmPasswordField.setText("Confirm Password...");
                }
            }
        });
        addActionListeners();
        container.add(panel, BorderLayout.CENTER);
    }

}
