import javax.swing.*;

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