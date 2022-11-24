import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class MessageGUIReDone implements Runnable {
    private JFrame messageBoard;
    private Container container;
    private JButton editMessage;
    private JScrollPane scrollPane;
    private JButton deleteMessage;
    private JButton sendMessage;
    private JList messages;
    private JTextField messageText;
    private JPanel leftPanel; //houses the buttons
    private JPanel upperPanel; //title text (recipient name)
    private JPanel bottomPanel; //has the text field and the send text button
    private JPanel rightPanel; //houses the message history
    private JLabel recipientText;

    private String emailSelected;
    private String messageChoice;
    private String id;
    private ArrayList<String> conversationHistory;

    public MessageGUIReDone(String messageChoice, String email) {
        messageBoard = new JFrame("Turkey Shop");
        //TODO: Figure out how to take the user ID
        //this.id = id;
        this.conversationHistory = new ArrayList<>();
        this.emailSelected = email;
        this.messageChoice = messageChoice;
        //TODO: get the conversationHistory from the translator module
        //stored in a ArrayListString
    }


    @Override
    public void run() {
        JPanel panel = new JPanel();
        messageBoard.setSize(600,400);
        messageBoard.setLocationRelativeTo(null);
        messageBoard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        messageBoard.setVisible(true);
        String[] msg = conversationHistory.toArray(new String[0]);
        messages = new JList(msg);
        scrollPane = new JScrollPane(messages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(370,400));
        this.container = messageBoard.getContentPane();
        container.setLayout(new BorderLayout());
        recipientText = new JLabel();
        Font f = new Font("Helvetica", Font.TRUETYPE_FONT, 25);
        recipientText.setFont(f);
        recipientText.setText("Sending message to " + this.emailSelected);
        messageText = new JTextField(12);
        messageText.setForeground(Color.GRAY);
        messageText.setText("Insert Message...");
        upperPanel = new JPanel();
        rightPanel = new JPanel();
        rightPanel.setBounds(400,0,200,400);
        leftPanel = new JPanel();
        bottomPanel = new JPanel();

        upperPanel.add(recipientText);
        editMessage = new JButton("Edit Message");
        deleteMessage = new JButton("Delete Message");
        sendMessage = new JButton("Send Message");
        leftPanel.add(editMessage);
        leftPanel.add(deleteMessage);
        //leftPanel.add(sendMessage);

        bottomPanel.add(messageText);
        bottomPanel.add(sendMessage);

        panel.add(upperPanel);
        panel.add(bottomPanel);
        panel.add(leftPanel);
        rightPanel.add(scrollPane);
        panel.add(scrollPane);
        if (messageChoice.equals("edit")) {
            JOptionPane.showMessageDialog(null, "Select a message to edit", "Message", JOptionPane.INFORMATION_MESSAGE);
        } else if (messageChoice.equals("view")) {
            String tempMsg = "You are viewing chats with " + this.emailSelected;
            JOptionPane.showMessageDialog(null, tempMsg, "Message", JOptionPane.INFORMATION_MESSAGE);
        } else if (messageChoice.equals("delete")) {
            JOptionPane.showMessageDialog(null, "Select a message to delete", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        addActionListeners();
        container.add(panel, BorderLayout.CENTER);


    }

    private void addActionListeners() {
        editMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: do smth
                String message = (String) messages.getSelectedValue();
                String confirm = "Do you want to edit " + message + " ?";
                int ans = JOptionPane.showConfirmDialog(null, confirm, "Edit Message", JOptionPane.INFORMATION_MESSAGE);
                if (ans == JOptionPane.YES_OPTION) {
                    //TODO: edit the message
                    //ask for the new message and edit the message shown
                    //edit in the databse
                }
                //else if no don't do anything

            }
        });
        deleteMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = (String) messages.getSelectedValue();
                String confirm = "Do you want to delete " + message + " ?";
                int ans = JOptionPane.showConfirmDialog(null, confirm, "Delete Message", JOptionPane.INFORMATION_MESSAGE);
                if (ans == JOptionPane.YES_OPTION) {
                    //TODO: delete the message
                    //delete in the databse
                }
                //else if no don't do anything
            }
        });
        sendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: do smth
                //get value from the text field
                //TODO: store back in the databse of the new message
                //conversationHistory.add(newMessage);
            }
        });
    }

    public void show() {
        SwingUtilities.invokeLater(this);
    }
}

