import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

public class MainInterfaceGUI implements Runnable {
    
    JFrame frame;
    JButton login;
    JButton createAccount;
    JButton exit;

    public MainInterfaceGUI() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainInterfaceGUI());
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        frame = new JFrame("Turkey Shop");
        homePage();
        frame.setSize(600,400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public void homePage() {
        Container content = frame.getContentPane();
        login = new JButton("Login");
        createAccount = new JButton("Create Account");
        exit = new JButton("Exit");
        JPanel stuff = new JPanel(new BorderLayout());
        stuff.add(login, BorderLayout.NORTH);
        stuff.add(createAccount, BorderLayout.CENTER);
        stuff.add(exit, BorderLayout.SOUTH);
        addListeners();
        content.add(stuff, BorderLayout.CENTER);
        frame.setContentPane(content);
    }

    private void addListeners() {
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                LogInGUI loginGUI = new LogInGUI(frame);
                loginGUI.show();
            }
        });
        createAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                CreateAccountGUI createAccountGUI = new CreateAccountGUI(frame);
                createAccountGUI.show();
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                frame.dispose();
            }
        });
    }
}
