import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageGUI implements Runnable {
    JFrame messageBoard;
    Container container;
    JButton createMessage;
    JButton editMessage;
    JButton deleteMessage;
    JTextField messageText;
    JButton exit;

    public MessageGUI() {
        this.messageBoard = new JFrame("Turkey Shop");
    }

    public void show() {
        SwingUtilities.invokeLater(this);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };
    @Override
    public void run() {
        JPanel panel = new JPanel();
        this.container = messageBoard.getContentPane();
        container.setLayout(new BorderLayout());
        messageBoard.setSize(600,400);
        messageBoard.setLocationRelativeTo(null);
        messageBoard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        messageBoard.setVisible(true);
        createMessage = new JButton("Create Message");
        editMessage = new JButton("Edit Message");
        deleteMessage = new JButton("Delete Message");
        exit = new JButton("Exit");
        panel.add(createMessage);
        panel.add(editMessage);
        panel.add(deleteMessage);
        panel.add(exit);


        // add action listeners and update method actions

        container.add(panel, BorderLayout.CENTER);
    }

}
