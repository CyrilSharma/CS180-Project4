import java.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;

public class MainInterfaceGUI implements Runnable {
    
    JFrame frame;
    Client client;
    Boolean loggedIn;

    public MainInterfaceGUI() {
        client = new Client();
        loggedIn = true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainInterfaceGUI());
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        frame = new JFrame();
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        if (loggedIn) {
            MessageGUI messageGUI = new MessageGUI("edit", "avadakedavera@gmail.com");
            messageGUI.run();
        } else {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.run();
        }
    }
}
