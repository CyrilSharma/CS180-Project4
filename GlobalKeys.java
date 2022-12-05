import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GlobalKeys implements KeyListener {
    
    private JFrame frame;
    
    public GlobalKeys(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Reload frame
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_R) {
            System.out.println("Got here");
            frame.revalidate();
            frame.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

}
