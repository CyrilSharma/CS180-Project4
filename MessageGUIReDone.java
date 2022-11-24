import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

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
    private JPanel rightPanel; //houses the message history
    private JLabel recipientText;

    private String emailSelected;
    private String messageChoice;
    private String id;
    private DefaultListModel messageList = new DefaultListModel();
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
        messageBoard.setSize(600,450);
        messageBoard.setLocationRelativeTo(null);
        messageBoard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        /**Test
         * conversationHistory.add("hello");
         * conversationHistory.add(, "meme");
         * conversationHistory.add("insert message here");
         */
        conversationHistory.add("hello");
        conversationHistory.add("meme");
        conversationHistory.add("insert message here");
        String[] msg = conversationHistory.toArray(new String[0]);
        messageList.addAll(List.of(msg));
        messages = new JList(messageList);
        scrollPane = new JScrollPane(messages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(370,300));
        this.container = messageBoard.getContentPane();
        container.setLayout(new BorderLayout());
        recipientText = new JLabel();
        Font f = new Font("Helvetica", Font.TRUETYPE_FONT, 20);
        recipientText.setFont(f);
        recipientText.setText("Sending message to " + this.emailSelected);
        messageText = new JTextField(12);
        messageText.setForeground(Color.GRAY);
        messageText.setText("Insert Message...");
        upperPanel = new JPanel();
        rightPanel = new JPanel();
        leftPanel = new JPanel();

        upperPanel.add(recipientText);
        editMessage = new JButton("Edit Message");
        deleteMessage = new JButton("Delete Message");
        sendMessage = new JButton("Send Message");
        leftPanel.add(messageText);
        leftPanel.add(sendMessage);
        leftPanel.add(editMessage);
        leftPanel.add(deleteMessage);

        panel.add(upperPanel);
        panel.add(leftPanel);
        rightPanel.add(scrollPane);
        panel.add(rightPanel);

        if (messageChoice.equals("edit")) {
            JOptionPane.showMessageDialog(null, "Select a message to edit", "Message", JOptionPane.INFORMATION_MESSAGE);
        } else if (messageChoice.equals("view")) {
            String tempMsg = "You are viewing chats with " + this.emailSelected;
            JOptionPane.showMessageDialog(null, tempMsg, "Message", JOptionPane.INFORMATION_MESSAGE);
        } else if (messageChoice.equals("delete")) {
            JOptionPane.showMessageDialog(null, "Select a message to delete", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        messageBoard.setVisible(true);
        addActionListeners();
        container.add(panel, BorderLayout.CENTER);


    }

    private void addActionListeners() {
        editMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = (String) messages.getSelectedValue();
                String confirm = "Do you want to edit " + message + " ?";
                int index = conversationHistory.indexOf(message);
                int ans = JOptionPane.showConfirmDialog(null, confirm, "Edit Message", JOptionPane.INFORMATION_MESSAGE);
                if (ans == JOptionPane.YES_OPTION) {
                    String editedMessage = (String) (JOptionPane.showInputDialog(null, "Please enter your edited message: ", "Message", JOptionPane.QUESTION_MESSAGE));
                    //TODO: edit the message in database
                    //TODO: edit the message shown on screen --> maybe done just have to test
                    //Test below
                    messageList.set(index, editedMessage);
                    //messages.add(messageText);
                    messages.updateUI();
                    JOptionPane.showMessageDialog(null, "Message Edited");
                    conversationHistory.set(index, editedMessage);
                }
                //else if no don't do anything

            }
        });
        deleteMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = (String) messages.getSelectedValue();
                int index = conversationHistory.indexOf(message);
                String confirm = "Do you want to delete " + message + " ?";
                int ans = JOptionPane.showConfirmDialog(null, confirm, "Delete Message", JOptionPane.INFORMATION_MESSAGE);
                if (ans == JOptionPane.YES_OPTION) {
                    //Test below
                    messageList.removeElementAt(index);
                    //messages.add(messageText);
                    messages.updateUI();
                    JOptionPane.showMessageDialog(null, "Message Deleted");
                    conversationHistory.remove(index);
                    //messages.remove(index);
                    //removes the message from the conversationHistory
                    //when the run method is called again it should update the Jlist entirely
                    //TODO: delete in the database
                }
                //else if no don't do anything
            }
        });
        sendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: do smth
                String message = messageText.getText();
                //Test below
                messageList.addElement(message);
                //messages.add(messageText);
                messages.updateUI();
                JOptionPane.showMessageDialog(null, "Message Sent");
                //TODO: store back in the databse of the new message --> do thsis functionality
                //TODO: add the message on the screen --> should be done automatically if adding the message to the arraylist

            }
        });
    }

    public void show() {
        SwingUtilities.invokeLater(this);
    }
}

