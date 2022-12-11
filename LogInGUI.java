import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
/**
 * Project 5 -> LogInGUI
 *
 * Allows user to log in to their account using username and password, or access createAccountGUI
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
public class LogInGUI {
    private JFrame board;
    private Container container;
    private JButton logInButton;
    private JButton createAccountButton;
    private JLabel turkeyImage;
    private JTextField emailField;
    private JButton placeholder;
    private JLabel title;
    private JPasswordField passwordField;
    private Translator translator;
    private BufferedImage image;
    private ImageIcon ii;
    private URL url;


    /**
     * initialize LogInGUI frame (constructor)
     * @param frame -> JFrame
     */
    public LogInGUI(JFrame frame) {
        frame.setSize(600,540);
        board = frame;
        translator = new Translator();
        try {
            //turkey image :)
            url = new URL("https://i.imgur.com/il2xdrq.jpg");
            image = ImageIO.read(url);
            Image newimg = image.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH);
            ii = new ImageIcon(newimg);
        } catch (Exception e){

        }

    }

    /**
     * Show LogInGUI (Visible)
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
        emailField.setSize(150,30);
        emailField.setBounds(board.getWidth()/2 - emailField.getWidth()/2, 240,
                emailField.getWidth(), emailField.getHeight());
        emailField.setForeground(Color.GRAY);
        emailField.setText("Email...");
        passwordField.setSize(150,30);
        passwordField.setBounds(board.getWidth()/2 - passwordField.getWidth()/2, 275,
                passwordField.getWidth(), passwordField.getHeight());
        passwordField.setForeground(Color.GRAY);
        passwordField.setText("Password...");
        logInButton.setSize(150,30);
        title.setBounds(board.getWidth()/2 - 200, 140, 400, 35);

        turkeyImage.setBounds(board.getWidth()/2 - 45,30,90,90);
        logInButton.setBounds(board.getWidth()/2 - logInButton.getWidth()/2, 310,
                logInButton.getWidth(), logInButton.getHeight());
        createAccountButton.setSize(150,30);
        createAccountButton.setBounds(board.getWidth()/2 - createAccountButton.getWidth()/2, 345,
                createAccountButton.getWidth(), createAccountButton.getHeight());
    }
    /**
     * Creates the buttons and scrollbar (BorderLayout) used in setFrame()
     */
    public void createAndAdd(JPanel panel) {
        logInButton = new JButton("Log In");
        emailField = new JTextField(10);
        passwordField = new JPasswordField(10);
        createAccountButton = new JButton("Create Account");
        turkeyImage = new JLabel(ii);
        placeholder = new JButton();
        title = new JLabel("Turkey Marketplace", SwingConstants.CENTER);
        Font f = new Font("Baumans", Font.BOLD, 30);
        title.setFont(f);
        title.setForeground(new Color(102, 52 , 0));
        panel.add(placeholder);
        placeholder.setBounds(-1,-1,1,1);
        panel.setBackground(Color.white);
        panel.setLayout(null);
        panel.add(emailField);
        panel.add(passwordField);
        panel.add(logInButton);
        panel.add(createAccountButton);
        panel.add(turkeyImage);
        panel.add(title);
    }
    /**
     * initialize the userList and button actionListeners, run GUI
     */
    public void run() {
        JPanel panel = new JPanel();
        container = board.getContentPane();
        container.setLayout(new BorderLayout());
        createAndAdd(panel);
        setFrame();
        passwordField.setEchoChar((char)0);
        //field to enter email
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
        //field to enter password
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
        //login button to attempt to log in with entered credentials
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = String.valueOf(passwordField.getPassword());
                if ((email.isEmpty() || email.equals("Email...")) ||
                        (password.isEmpty() || password.equals("Password..."))) {
                    JOptionPane.showMessageDialog(null, "Please fill out the text fields!", "Error", JOptionPane.WARNING_MESSAGE, null);
                } else {
                    try {
                        if (!((Boolean) translator.query(new Query("Database", "verify", new String[]{email, password})))) {
                            JOptionPane.showMessageDialog(null, "That is either the wrong email or password. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            HashMap<String, String> user = translator.get("email", email);
                            MainMenuGUI menu = new MainMenuGUI(board, user);
                            menu.show();
                        }
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        //Open CreateAccountGUI
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateAccountGUI gui = new CreateAccountGUI(board);
                gui.show();
            }
        });
        container.add(panel, BorderLayout.CENTER);
        board.setContentPane(container);
    }
}