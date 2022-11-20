import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LogInGUI implements Runnable {
    JFrame board;
    Container container;
    JButton logInButton;
    JButton createAccountButton;
    JTextField emailField;
    JPasswordField passwordField;

    public LogInGUI() {
        board = new JFrame("Turkey Shop");
    }

    public void show() {
        SwingUtilities.invokeLater(this);
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

        board.setSize(600,400);
        board.setLocationRelativeTo(null);
        board.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        board.setVisible(true);
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
                String email = emailField.getText();
                String password = passwordField.getText();
                if ((email.isEmpty() || email.equals("Email...")) ||
                        (password.isEmpty() || password.equals("Password..."))) {
                    JOptionPane.showMessageDialog(null, "Please fill out the text fields!", "Error", JOptionPane.WARNING_MESSAGE, null);
                } else {
                    String f = "Trying to log in with credential {email: %s, pw: %s}\n";
                    f = String.format(f, email, password);
                    JOptionPane.showMessageDialog(null, f, "Alert", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateAccountGUI gui = new CreateAccountGUI();
                gui.show();
            }
        });
        container.add(panel, BorderLayout.CENTER);
    }
}