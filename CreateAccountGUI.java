import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CreateAccountGUI implements Runnable {
    private JFrame board;
    private Container container;
    private JButton createButton;
    private JButton backButton;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public CreateAccountGUI() {
        board = new JFrame("Turkey Store");
    }

    public void show() {
        SwingUtilities.invokeLater(this);
    }
    public void setFrame() {
        emailField.setSize(150,30);
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
        createButton.setSize(150,30);
        createButton.setBounds(board.getWidth()/2 - createButton.getWidth()/2, 205,
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
        panel.setLayout(null);
        panel.add(emailField);
        panel.add(passwordField);
        panel.add(createButton);
        panel.add(confirmPasswordField);
        panel.add(backButton);
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
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = passwordField.getText();
                String cp = confirmPasswordField.getText();
                if ((email.isEmpty() || email.equals("Email...")) ||
                        (password.isEmpty() || password.equals("Password..."))) {
                    JOptionPane.showMessageDialog(null, "Please fill out the text fields!", "Error", JOptionPane.WARNING_MESSAGE, null);
                } else if (!password.equals(cp)) {
                    JOptionPane.showMessageDialog(null, "Password does not match!", "Error", JOptionPane.WARNING_MESSAGE, null);
                } else {
                    String f = "Trying to create an account with credential {email: %s, pw: %s}\n";
                    f = String.format(f, email, password);
                    JOptionPane.showMessageDialog(null, f, "Alert", JOptionPane.INFORMATION_MESSAGE);
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
        container.add(panel, BorderLayout.CENTER);
    }

}
