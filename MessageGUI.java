import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MessageGUI implements Runnable {
    private JFrame messageBoard;
    private Container container;
    private JButton editMessage;
    private JScrollPane scrollPane;
    private JButton deleteMessage;
    private JButton sendMessage;
    //list of messages
    private JList messages;
    //where user enters the message to send
    private JTextField messageText;
    private JPanel leftPanel; //houses the buttons
    private JPanel upperPanel; //title text (recipient name)
    private JPanel rightPanel; //houses the message history
    private JLabel recipientText;

    private String currentUser;

    private String emailSelected;
    private String messageChoice;
    //allows for GUI to update after each send/edit/delete
    private DefaultListModel messageList = new DefaultListModel();
    private ArrayList<Message> conversationHistory;
    private String selectedStore;
    private MessageInterfaceClient mic;
    public MessageGUI(JFrame board, String messageChoice, String email, String username, String selectedStore) {
        board.setSize(600,500);
        messageBoard = board;
        this.currentUser = username; //logged in user
        this.conversationHistory = new ArrayList<>();
        this.emailSelected = email; //selected user
        this.selectedStore = selectedStore;
        this.messageChoice = messageChoice;
        this.mic = new MessageInterfaceClient();
        //TODO: get the conversationHistory from the translator module
        //stored in a ArrayListString
        try {
            conversationHistory = mic.getConversation(mic.getTranslator().get("email", email).get("id"), selectedStore);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    public void run() {
        JPanel panel = new JPanel();
        //TODO: pull conversationHistory from database for two people
        /**Test conversation, should usually pull conversation from database
         * conversationHistory.add("hello");
         * conversationHistory.add(, "meme");
         * conversationHistory.add("insert message here");
         */
        //TODO: Change to only take in the message text parameters
        String[] msg = mic.messagesToArray(conversationHistory);
        messageList.addAll(List.of(msg));
        messages = new JList(messageList);
        scrollPane = new JScrollPane(messages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(370,300));
        this.container = messageBoard.getContentPane();
        container.setLayout(new BorderLayout());
        recipientText = new JLabel();
        Font f = new Font("Helvetica", Font.TRUETYPE_FONT, 15);
        recipientText.setFont(f);
        recipientText.setText(this.selectedStore + " Sending message to " + this.emailSelected);
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
    /** Not needed unless these options exist from peopleview
        if (messageChoice.equals("edit")) {
            JOptionPane.showMessageDialog(null, "Select a message to edit", "Message", JOptionPane.INFORMATION_MESSAGE);
        } else if (messageChoice.equals("view")) {
            String tempMsg = "You are viewing chats with " + this.emailSelected;
            JOptionPane.showMessageDialog(null, tempMsg, "Message", JOptionPane.INFORMATION_MESSAGE);
        } else if (messageChoice.equals("delete")) {
            JOptionPane.showMessageDialog(null, "Select a message to delete", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
     */
        addActionListeners();
        container.add(panel, BorderLayout.CENTER);


    }

    private void addActionListeners() {
        //Select message and edit
        editMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String originalMessage = (String) messages.getSelectedValue();
                String confirm = "Do you want to edit " + originalMessage + " ?";
                //int index = conversationHistory.indexOf(originalMessage);
                int index = messages.getSelectedIndex();
                int ans = JOptionPane.showConfirmDialog(null, confirm, "Edit Message", JOptionPane.INFORMATION_MESSAGE);
                if (ans == JOptionPane.YES_OPTION) {
                    String editedMessage = (String) (JOptionPane.showInputDialog(null, "Please enter your edited message: ", "Message", JOptionPane.QUESTION_MESSAGE));
                    if (editedMessage != null) {
                        editedMessage = currentUser + ": " + editedMessage;
                        //TODO: edit the message in database
                        //edit the message shown on screen --> maybe done just have to test
                        //Test below
                        messageList.set(index, editedMessage + " (Edited Message)");
                        //messages.add(messageText);
                        messages.updateUI();
                        JOptionPane.showMessageDialog(null, "Message Edited");
                        emailSelected = emailSelected.split(" ")[0];
                        try {
                            mic.editMessage(mic.getID(), emailSelected, editedMessage, conversationHistory.get(index).getMessageID());
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                //else if no don't do anything

            }
        });
        //Select message and delete
        deleteMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Having issue where sometimes index is -1 with below command (specifically if an edit or delete is
                // cancelled and then a delete/edit is attemped after (Resolved)
                String deletedMessage = "";
                deletedMessage = (String) messages.getSelectedValue();
                //int index = conversationHistory.indexOf(deletedMessage);
                int index = messages.getSelectedIndex();
                String confirm = "Do you want to delete " + deletedMessage + " ?";
                int ans = JOptionPane.showConfirmDialog(null, confirm, "Delete Message", JOptionPane.INFORMATION_MESSAGE);
                if (ans == JOptionPane.YES_OPTION) {
                    //Test below
                    messageList.removeElementAt(index);
                    //messages.add(messageText);
                    messages.updateUI();
                    JOptionPane.showMessageDialog(null, "Message Deleted");
                    //removes the message from the conversationHistory
                    //when the run method is called again it should update the Jlist entirely
                    //TODO: delete in the database
                    try {
                        emailSelected = emailSelected.split(" ")[0];
                        mic.deleteMessage(mic.getID(), mic.getTranslator().get("email", emailSelected).get("id"), conversationHistory.get(index).getMessageID());
                        conversationHistory.remove(index);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                //else if no don't do anything
            }
        });
        //Send a new message
        sendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = currentUser + ": " + messageText.getText();
                //Test below
                //TODO: store back in the database of the new message --> do this functionality
                //add the message on the screen --> should be done automatically if adding the message to the arraylist
                try {
                    emailSelected = emailSelected.split(" ")[0];
                    mic.message(message, mic.getID(), emailSelected, selectedStore);
                    conversationHistory = mic.getConversation(emailSelected, selectedStore);
                    messageList.addElement(message);
                    //Update GUI to show changes
                    messages.updateUI();
                    JOptionPane.showMessageDialog(null, "Message Sent");
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void show() {
        messageBoard.setContentPane(new Container());
        run();
        messageBoard.revalidate();
        messageBoard.repaint();
    }
}

