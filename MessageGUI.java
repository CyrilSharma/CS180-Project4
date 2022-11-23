import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class MessageGUI implements Runnable {
    private JFrame messageBoard;
    private Container container;
    private JTextField messageText;
    private JScrollBar scroll;
    private JButton exit;
    private JButton sendMessage;
    private JButton deleteMessage;
    private JButton editMessage;
    private JButton button;
    private JLabel title;
    private String messageChoice;
    private String email;
    private String messageID;
    private String message;
    private String id;
    private JTextField messages;
    private JLabel label;

    public MessageGUI(String messageChoice, String email) {
        this.messageBoard = new JFrame("Turkey Shop");
        Database db = new Database();
        HashMap<String, String> acct = db.get("email", email);
        String id = acct.get("id");
        this.id = id;
        this.email = email;
        this.messageChoice = messageChoice;
    }

    public void show() {
        SwingUtilities.invokeLater(this);
    }

    public void addActionListeners() {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MessageInterfaceClient messageClient = new MessageInterfaceClient();
                if (button == deleteMessage) {
                    message = messageText.getText();
                    messageClient.deleteMessage(id, messageID);
                } else if (button == editMessage) {
                    message = messageText.getText();
                    messageClient.editMessage(id, messageID);
                    //should be some parameter to take new message text?
                } else if (button == sendMessage) {
                    message = messageText.getText();
                    messageClient.message(message, id);
                }
            }
        });
        };
    @Override
    public void run() {
        this.container = messageBoard.getContentPane();
        MessageInterfaceClient client = new MessageInterfaceClient();
        messages = new JTextField();
        messages.setText(client.getMessageHistory(id).toString());
        title = new JLabel();
        Font f = new Font("Helvetica", Font.TRUETYPE_FONT, 25);
        title.setFont(f);
        JPanel panel = new JPanel();
        JPanel panelExit = new JPanel();
        //JPanel panel3 = new JPanel();
        if (messageChoice.equals("edit")) {
            title.setText("Edit Message");
            messageID = JOptionPane.showInputDialog(null, "Please enter the message # that you would like to edit", "Choice?", JOptionPane.QUESTION_MESSAGE);
            //TODO: check edit function and add String message as parameter?
            editMessage = new JButton();
            label = new JLabel("Edit: ");
            button = editMessage;
            //panel.add(editMessage);
            //client.editMessage(id, messageID);
        } else if (messageChoice.equals("view")) {
            title.setText("View/Send Message");
            client.message(message, id);
            sendMessage = new JButton();
            button = sendMessage;
            //panel.add(sendMessage);
        } else if (messageChoice.equals("delete")) {
            title.setText("Delete Message");
            deleteMessage = new JButton();
            messageID = JOptionPane.showInputDialog(null, "Please enter the message # that you would like to delete", "Choice?", JOptionPane.QUESTION_MESSAGE);
            button = deleteMessage;
            //panel.add(deleteMessage);
            // add action listener
            //client.deleteMessage(id, messageID);
        }
        container.setLayout(new BorderLayout());
        messageBoard.setSize(600,400);
        messageBoard.setLocationRelativeTo(null);
        messageBoard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        messageBoard.setVisible(true);
        exit = new JButton("Exit");
        messageText = new JTextField();
        panel.add(label);
        panel.add(messageText);
        panel.add(button);
        panelExit.add(exit);


        // add action listeners and update method actions

        container.add(panel, BorderLayout.SOUTH);
        container.add(panelExit, BorderLayout.NORTH);
    }
}
