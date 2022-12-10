import javax.swing.*;
/**
 * Project 5 -> Launch
 *
 * Concurrency and Networking starts here
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
public class Launch implements Runnable {
    
    JFrame frame;

    public Launch() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Launch());
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