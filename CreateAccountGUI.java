import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Project 5 -> CreateAccountGUI
 *
 * Allows user to create a new account w/ unique username and password
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
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

    /**
     * initialize createAccountGUI (constructor) w frame
     * @param frame -> JFrame
     */
    public CreateAccountGUI(JFrame frame) {
        board = frame;
        translator = new Translator();
    }

    /**
     * Make CreateAccountGUI visible, (show GUI)
     */
    public void show() {
        board.setContentPane(new Container());
        run();
        board.revalidate();
        board.repaint();
    }

    /**
     * Creates the actual frame layout (buttons, users, etc.) with locations and fonts
     */
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
    /**
     * Creates the buttons and scrollbar (BorderLayout) used in setFrame()
     */
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

    /**
     * add actionListeners
     */
    public void addActionListeners() {
        //create account with given credentials (check unique username)
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = String.valueOf(passwordField.getPassword());
                String cp = String.valueOf(confirmPasswordField.getPassword());
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
                        if (stores.size() == 0 && Role.Seller == role) {
                            JOptionPane.showMessageDialog(null, 
                                "As a seller, you must have at least 1 store.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Object o = translator.query(new Query("Database", "add", new Object[]{email, password, role}));
                        // log in user.
                        translator.query(new Query("Database", "verify", new String[]{email, password}));
                        if (Role.Seller == role) {
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
                            HashMap<String, String> user = translator.get("email", email);
                            MainMenuGUI gui = new MainMenuGUI(board, user);
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
        //go back to previous frame (LogInGUI)
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogInGUI gui = new LogInGUI(board);
                gui.show();
            }
        });
        //set role to customer
        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                role = Role.Customer;
                userSelectionLabel.setText("role: customer");
                addStoreButton.setVisible(false);
                storeField.setVisible(false);
            }
        });
        //set role to seller
        sellerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                role = Role.Seller;
                userSelectionLabel.setText("role: seller");
                addStoreButton.setVisible(true);
                storeField.setVisible(true);
            }
        });
        //add store if new user is seller
        addStoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String store = storeField.getText();
                stores.add(store);
            }
        });
    }
    //run actionListeners and create panel/frame
    public void run() {
        JPanel panel = new JPanel();
        container = board.getContentPane();
        container.setLayout(new BorderLayout());
        board.setSize(450,450);
        createAndAdd(panel);
        setFrame();
        passwordField.setEchoChar((char)0);
        confirmPasswordField.setEchoChar((char)0);
        //email field
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
        //password field
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
                if (passwordField.getPassword().length == 0) {
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setEchoChar((char)0);
                    passwordField.setText("Password...");
                }
            }
        });
        //confirm password
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
                if (confirmPasswordField.getPassword().length == 0) {
                    confirmPasswordField.setForeground(Color.GRAY);
                    confirmPasswordField.setEchoChar((char)0);
                    confirmPasswordField.setText("Confirm Password...");
                }
            }
        });
        //store name
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
                if (confirmPasswordField.getPassword().length == 0) {
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