import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        frame = new JFrame("Turkey Shop");
        frame.setSize(600,400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        LogInGUI loginGUI = new LogInGUI(frame);
        loginGUI.show();
    }
}