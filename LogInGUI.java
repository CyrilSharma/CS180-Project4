import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LogInGUI {
    private JFrame board;
    private Container container;
    private JButton logInButton;
    private JButton createAccountButton;
    private JTextField emailField;
    private JButton placeholder;
    private JPasswordField passwordField;
    private ArrayList<String> users;
    private Translator translator;

    public LogInGUI(JFrame frame) {
        this(frame, new ArrayList<>());
    }

    public LogInGUI(JFrame frame, ArrayList<String> exUsers) {
        board = frame;
        users = exUsers;
        translator = new Translator();
    }

    public void show() {
        board.setContentPane(new Container());
        run();
        board.revalidate();
        board.repaint();
    }

    public void setFrame() {
        emailField.setSize(150,30);
        emailField.setBounds(board.getWidth()/2 - emailField.getWidth()/2, 150,
                emailField.getWidth(), emailField.getHeight());
        emailField.setForeground(Color.GRAY);
        emailField.setText("Email...");
        passwordField.setSize(150,30);
        passwordField.setBounds(board.getWidth()/2 - passwordField.getWidth()/2, 185,
                passwordField.getWidth(), passwordField.getHeight());
        passwordField.setForeground(Color.GRAY);
        passwordField.setText("Password...");
        logInButton.setSize(150,30);
        logInButton.setBounds(board.getWidth()/2 - logInButton.getWidth()/2, 220,
                logInButton.getWidth(), logInButton.getHeight());
        createAccountButton.setSize(150,30);
        createAccountButton.setBounds(board.getWidth()/2 - createAccountButton.getWidth()/2, 255,
                createAccountButton.getWidth(), createAccountButton.getHeight());
    }

    public void createAndAdd(JPanel panel) {
        logInButton = new JButton("Log In");
        emailField = new JTextField(10);
        passwordField = new JPasswordField(10);
        createAccountButton = new JButton("Create Account");
        placeholder = new JButton();
        panel.add(placeholder);
        placeholder.setBounds(-1,-1,1,1);
        panel.setLayout(null);
        panel.add(emailField);
        panel.add(passwordField);
        panel.add(logInButton);
        panel.add(createAccountButton);
    }
    public void run() {
        JPanel panel = new JPanel();
        container = board.getContentPane();
        container.setLayout(new BorderLayout());
        createAndAdd(panel);
        setFrame();
        passwordField.setEchoChar((char)0);
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
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //THIS DOES NOT CHECK CREDENTIALS YET
                String email = emailField.getText();
                String password = passwordField.getText();
                if ((email.isEmpty() || email.equals("Email...")) ||
                        (password.isEmpty() || password.equals("Password..."))) {
                    JOptionPane.showMessageDialog(null, "Please fill out the text fields!", "Error", JOptionPane.WARNING_MESSAGE, null);
                } else {
//                    String f = "Trying to log in with credential {email: %s, pw: %s}\n";
//                    f = String.format(f, email, password);
//                    JOptionPane.showMessageDialog(null, f, "Alert", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        if (!((Boolean) translator.query(new Query("Database", "verify", new String[]{email, password})))) {
                            JOptionPane.showMessageDialog(null, "That is either the wrong email or password. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            HashMap<String, String> loggedIn = translator.get("email", email);
                            Role role = Role.valueOf(loggedIn.get("role"));
                            ArrayList<String> users = (ArrayList<String>) translator.query(new Query("User", "getUsers"));
                            MainMenuGUI menu = new MainMenuGUI(board, users, role);
                            menu.show();
                        }
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateAccountGUI gui = new CreateAccountGUI(board, users);
                gui.show();
            }
        });
        container.add(panel, BorderLayout.CENTER);
        board.setContentPane(container);
    }
}