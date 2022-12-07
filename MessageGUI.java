import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.awt.event.*;

//TODO: Add the ability to import files, create multiple line messages
@SuppressWarnings({"unchecked", "rawtypes"})
public class MessageGUI extends MouseAdapter implements PropertyChangeListener, KeyListener {
    private JFrame messageBoard;
    private Container container;
    private JScrollPane scrollPane;
    private JButton sendMessage;
    //list of messages
    private JList messages = new JList();
    //where user enters the message to send
    private JTextArea messageText;
    private JScrollPane messagePane;
    private JPanel leftPanel; //houses the buttons
    private JPanel upperPanel; //title text (recipient name)
    private JPanel rightPanel; //houses the message history
    private JLabel recipientText;
    private JButton backButton;

    private String currentUser;

    private String emailSelected;
    private String messageChoice;
    //allows for GUI to update after each send/edit/delete
    private DefaultListModel messageList = new DefaultListModel();
    private ArrayList<Message> conversationHistory;
    private String selectedStore;
    private String otherID;
    private PeopleView parent;
    private MessageInterfaceClient mic;
    
    public MessageGUI(JFrame board, String messageChoice, String email, String username, String selectedStore,
        PeopleView parent) {
        board.setSize(600,550);
        messageBoard = board;
        this.currentUser = username; //logged in user
        this.conversationHistory = new ArrayList<>();
        this.emailSelected = email; //selected user
        this.selectedStore = selectedStore;
        this.messageChoice = messageChoice;
        this.mic = new MessageInterfaceClient();
        this.parent = parent;
        //TODO: get the conversationHistory from the translator module
        //stored in a ArrayListString
        try {
            otherID = mic.getTranslator().get("email", emailSelected).get("id");
            //conversationHistory = mic.getConversation(otherID, selectedStore);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void run() {
        JPanel panel = new JPanel();
        /*
        String[] msg;
        try {
            msg = mic.messagesToArray(conversationHistory);
            messageList.addAll(Arrays.asList(msg));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            msg = new String[0];
        } */
        messages.setModel(messageList);
        messages.setCellRenderer(new CellRenderer());
        updateMessages();
        scrollPane = new JScrollPane(messages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(450,400));
        this.container = messageBoard.getContentPane();
        container.setLayout(new BorderLayout());
        recipientText = new JLabel();
        Font f = new Font("Helvetica", Font.TRUETYPE_FONT, 15);
        recipientText.setFont(f);
        recipientText.setText(this.emailSelected + " via " + this.selectedStore);
        messageText = new JTextArea(1, 26);
        messageText.setLineWrap(true);
        messageText.setForeground(Color.GRAY);
        messageText.setText("Insert Message...");
        messageText.setWrapStyleWord(true);
        messageText.setMaximumSize(messageText.getPreferredSize());
        messagePane = new JScrollPane(messageText, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        upperPanel = new JPanel();
        rightPanel = new JPanel();
        leftPanel = new JPanel();
        upperPanel.add(recipientText);
        sendMessage = new JButton("Send Message");
        backButton = new JButton("Back");
        messagePane.setSize(scrollPane.getWidth() - sendMessage.getWidth(), sendMessage.getHeight());
        messageText.setSize(messagePane.getWidth(), messagePane.getHeight());
        leftPanel.add(messagePane);
        leftPanel.add(sendMessage);
        leftPanel.add(backButton);

        panel.add(upperPanel);
        panel.add(leftPanel);
        rightPanel.add(scrollPane);
        panel.add(rightPanel);
        addActionListeners();
        container.add(panel, BorderLayout.CENTER);
        sendMessage.addKeyListener(this);
        messageText.addKeyListener(this);
        sendMessage.setFocusable(true);
        messageText.setFocusable(true);
        messages.addMouseListener(this);
    }

    private void addActionListeners() {
        //Send a new message
        sendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // String message = currentUser + ": " + messageText.getText();
                String message = messageText.getText();
                try {
                    emailSelected = emailSelected.split(" ")[0];
                    mic.message(message, mic.getID(), otherID, selectedStore);
                    JOptionPane.showMessageDialog(null, "Message Sent");
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.show();
            }
        });
    }

    public void show() {
        messageBoard.setContentPane(new Container());
        run();
        messageBoard.revalidate();
        messageBoard.repaint();
    }

    //TODO: My source: https://stackoverflow.com/questions/27077508/infinite-loop-in-swing
    private class UpdateMessages implements Runnable {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        public void addPropertyChangeListeners(PropertyChangeListener pcl) {
            pcs.addPropertyChangeListener(pcl);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    ArrayList<Message> conversationHistory2 = mic.getConversation(otherID, selectedStore);
                    if (!conversationHistory2.equals(conversationHistory)) {
                        pcs.firePropertyChange("changeUI", conversationHistory, conversationHistory2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            }
        }

    }

    private void updateMessages() {
        UpdateMessages um = new UpdateMessages(); 
        um.addPropertyChangeListeners(this);
        new Thread(um).start();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("changeUI")) {
            conversationHistory = (ArrayList<Message>) evt.getNewValue();
            String[] msg;
            try {
                msg = mic.messagesToArray(conversationHistory);
                messageList.removeAllElements();
                messageList.addAll(Arrays.asList(msg));
                messages.setModel(messageList);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        SwingUtilities.updateComponentTreeUI(messages);
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //TODO: my source: https://docs.oracle.com/javase/8/docs/api/javax/swing/ListCellRenderer.html
    private class CellRenderer implements ListCellRenderer<String> {

        DefaultListCellRenderer renderer = new DefaultListCellRenderer();

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
                boolean isSelected, boolean cellHasFocus) {
            // TODO Auto-generated method stub
            JLabel label = (JLabel) renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            int maxWidth = (int) (scrollPane.getWidth() * .7);
            int r;
            int g;
            int b;
            if (conversationHistory.size() > index && !conversationHistory.get(index).getSender().equals(otherID)) {
                label.setHorizontalAlignment(JLabel.RIGHT);
                //label.setForeground(new Color(7, 252, 3));
                r = 7;
                g = 252;
                b = 3;
            } else {
                label.setHorizontalAlignment(JLabel.LEFT);
                //label.setForeground(new Color(179, 186, 184));
                r = 179;
                g = 186;
                b = 184;
            }
            Instant time = conversationHistory.get(index).getTimeStamp();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("eee, MMM d, YYYY 'at' h:mm a");
            LocalDateTime localTime = time.atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDateTime();
            String timeString =  localTime.format(dateTimeFormatter);
            value = value.replaceAll("\n", "<br>");
            if (label.getPreferredSize().getWidth() >= maxWidth - 10) {
                value = String.format("<html><div style=\"color: rgb(211, 211, 211);\">%s</div><div WIDTH=%d style=\"background-color: rgb(%d, %d, %d); padding: 5px; white-space: pre-line;\">%s</div></html>", timeString, maxWidth, r, g, b, value);
            } else {
                value = String.format("<html><div style=\"color: rgb(211, 211, 211);\">%s</div><div WIDTH=%d style=\"background-color: rgb(%d, %d, %d); padding: 5px; white-space: pre-line;\">%s</div></html>", timeString, (int) label.getPreferredSize().getWidth() + 10, r, g, b, value);
            }
            label.setText(value);
            return label;
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
            messageText.append("\n");
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMessage.doClick();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println(e.getButton());
        if (e.getButton() == 3) {
            messages.setSelectedIndex(messages.locationToIndex(e.getPoint()));
            showContextMenu(e);
        } else if (e.getButton() == 1) {
            System.out.println((String) messages.getSelectedValue());
            e.consume();
        }
    }

    public void showContextMenu(MouseEvent e) {
        JPopupMenu popupMenu = new JPopupMenu("Actions");
        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String originalMessage = (String) messages.getSelectedValue();
                if (originalMessage == null) {
                    JOptionPane.showMessageDialog(null, "You must select a message!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String confirm = "Do you want to edit " + originalMessage + " ?";
                //int index = conversationHistory.indexOf(originalMessage);
                int index = messages.getSelectedIndex();
                int ans = JOptionPane.showConfirmDialog(null, confirm, "Edit Message", JOptionPane.INFORMATION_MESSAGE);
                if (ans == JOptionPane.YES_OPTION) {
                    String editedMessage = (String) (JOptionPane.showInputDialog(null, "Please enter your edited message: ", "Message", JOptionPane.QUESTION_MESSAGE));
                    if (editedMessage != null) {
                        emailSelected = emailSelected.split(" ")[0];
                        try {
                            mic.editMessage(mic.getID(), otherID, editedMessage, conversationHistory.get(index).getMessageID());
                            JOptionPane.showMessageDialog(null, "Message Edited");
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                //Having issue where sometimes index is -1 with below command (specifically if an edit or delete is
                // cancelled and then a delete/edit is attemped after (Resolved)
                String deletedMessage = "";
                deletedMessage = (String) messages.getSelectedValue();
                if (deletedMessage == null) {
                    JOptionPane.showMessageDialog(null, "You must select a message!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //int index = conversationHistory.indexOf(deletedMessage);
                int index = messages.getSelectedIndex();
                String confirm = "Do you want to delete " + deletedMessage + " ?";
                int ans = JOptionPane.showConfirmDialog(null, confirm, "Delete Message", JOptionPane.INFORMATION_MESSAGE);
                if (ans == JOptionPane.YES_OPTION) {
                    try {
                        emailSelected = emailSelected.split(" ")[0];
                        mic.deleteMessage(mic.getID(), otherID, conversationHistory.get(index).getMessageID());
                        JOptionPane.showMessageDialog(null, "Message Deleted");
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection = new StringSelection((String) messages.getSelectedValue());
                clipboard.setContents(stringSelection, null);
            }
            
        });
        popupMenu.add(copy);
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        popupMenu.show(messages, e.getX(), e.getY());
    }
}
