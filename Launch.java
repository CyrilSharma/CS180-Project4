import javax.swing.*;


public class Launch {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Turkey Shop");
        frame.setSize(600,540);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        try {
            LogInGUI gui = new LogInGUI(frame);
            gui.show();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
