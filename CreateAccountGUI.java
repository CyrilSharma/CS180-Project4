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
    private JButton placeholder;
    private JTextField storeField;
    private JButton addStoreButton;
    private Role role;
    private JButton customerButton;
    private JButton sellerButton;
    private JLabel userSelectionLabel;
    private ArrayList<String> stores = new ArrayList<String>();
    private Translator translator;

    public CreateAccountGUI(JFrame frame) {
        board = frame;
        translator = new Translator();
    }

    public void show() {
        board.setContentPane(new Container());
        run();
        board.revalidate();
        board.repaint();
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
        createButton.setBounds(board.getWidth()/2 - createButton.getWidth()/2, 350,
                createButton.getWidth(), createButton.getHeight());

        backButton.setSize(90, 30);
        backButton.setBounds(20, 380, backButton.getWidth(), backButton.getHeight());

        storeField.setSize(150,30);
        storeField.setLocation(board.getWidth()/2 - storeField.getWidth()/2, 300);
        storeField.setText("Store name...");
        storeField.setForeground(Color.GRAY);
        storeField.setVisible(false);
        addStoreButton.setSize(150,30);
        addStoreButton.setLocation(board.getWidth()/2 - addStoreButton.getWidth()/2, 325);
        addStoreButton.setVisible(false);
    }

    public void createAndAdd(JPanel panel) {
        createButton = new JButton("Create Account");
        emailField = new JTextField(10);
        passwordField = new JPasswordField(10);
        confirmPasswordField = new JPasswordField(10);
        storeField = new JTextField(10);

        backButton = new JButton("Back");
        placeholder = new JButton();
        customerButton = new JButton("Customer");
        sellerButton = new JButton("Seller");
        userSelectionLabel = new JLabel("select a role...");
        addStoreButton = new JButton("Add Store");
        panel.setLayout(null);
        panel.add(emailField);
        panel.add(passwordField);
        panel.add(createButton);
        panel.add(confirmPasswordField);
        panel.add(backButton);
        panel.add(placeholder);
        panel.add(customerButton);
        panel.add(sellerButton);
        panel.add(storeField);
        panel.add(addStoreButton);
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
                    try {
                        Object o = translator.query(new Query("Database", "add", new String[]{email, password, role.toString()}));
                        // log in user.
                        translator.query(new Query("Database", "verify", new String[]{email, password}));
                        if (role.toString().equals("Seller")) {
                            for (String store: stores) {
                                try {
                                    translator.query(new Query("User", "addStores", new Object[]{store}));
                                } catch (Exception error) {
                                    JOptionPane.showMessageDialog(null, 
                                        "We're having trouble communicating with the server.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                    break;
                                }
                            }
                        }
                        if (!(o instanceof Exception)) {
                            MainMenuGUI gui = new MainMenuGUI(board, users, translator.get("email", email));
                            gui.show();
                        } else {
                            throw new InvalidUserException(((Exception) o).getMessage());
                        }
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogInGUI gui = new LogInGUI(board);
                gui.show();
            }
        });
        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                role = Role.Customer;
                userSelectionLabel.setText("role: customer");
                addStoreButton.setVisible(false);
                storeField.setVisible(false);
            }
        });
        sellerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                role = Role.Seller;
                userSelectionLabel.setText("role: seller");
                addStoreButton.setVisible(true);
                storeField.setVisible(true);
            }
        });

        addStoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String store = storeField.getText();
                stores.add(store);
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
        storeField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField field = (JTextField)e.getComponent();
                if (field.getText().equals("Store name...")) {
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
                    confirmPasswordField.setText("Store name...");
                }
            }
        });
        addActionListeners();
        container.add(panel, BorderLayout.CENTER);
    }

}