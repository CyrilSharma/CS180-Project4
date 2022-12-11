import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.awt.*;
import java.awt.datatransfer.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.awt.event.*;

/**
 * Project 5 -> MessageGUI
 *
 * Allows user to send, edit, and delete messages, see conversation
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
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
    private JButton sendFile;
    private JLabel online;
    private String emailSelected;
    //allows for GUI to update after each send/edit/delete
    private DefaultListModel messageList = new DefaultListModel();
    private ArrayList<Message> conversationHistory;
    private String selectedStore;
    private String otherID;
    private MessageInterfaceClient mic;
    private HashMap<String, String> user;
    private boolean running;

    /**
     * Initialize MessageGUI (constructor)
     * @param board -> JFrame
     * @param messageChoice -> 'view'
     * @param email -> email
     * @param username ->  obtained from query
     * @param selectedStore -> chosen store in PeopleView
     * @param parent -> previous PeopleView
     */
    public MessageGUI(JFrame board, String messageChoice, String email, String username, String selectedStore,
        PeopleView parent) {
        board.setSize(700,550);
        messageBoard = board;
        this.conversationHistory = new ArrayList<>();
        this.emailSelected = email; //selected user
        this.selectedStore = selectedStore;
        this.mic = new MessageInterfaceClient();
        //stored in a ArrayListString
        try {
            otherID = mic.getTranslator().get("email", emailSelected).get("id");
            user = mic.getTranslator().get("email", username);
            //conversationHistory = mic.getConversation(otherID, selectedStore);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //create button layout and font format, setup content panel, run actionListeners
    public void run() {
        JPanel panel = new JPanel();
        /*
        String[] msg;
        try {
            msg = mic.messagesToArray(conversationHistory);
            messageList.addAll(Arrays.asList(msg));
        } catch (Exception e) {
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
        online = new JLabel("<html><div style=\"color: green\">Connected</div></html>");
        upperPanel.add(online);
        sendMessage = new JButton("Send Message");
        backButton = new JButton("Back");
        sendFile = new JButton("Send File");
        messagePane.setSize(scrollPane.getWidth() - sendMessage.getWidth(), sendMessage.getHeight());
        messageText.setSize(messagePane.getWidth(), messagePane.getHeight());
        leftPanel.add(messagePane);
        leftPanel.add(sendMessage);
        leftPanel.add(sendFile);
        leftPanel.add(backButton);

        panel.add(upperPanel);
        panel.add(leftPanel);
        rightPanel.add(scrollPane);
        panel.add(rightPanel);
        addActionListeners();
        if (!messageList.isEmpty()) {
            messages.ensureIndexIsVisible(messageList.lastIndexOf(messageList.lastElement()));
        }
        container.add(panel, BorderLayout.CENTER);
        sendMessage.addKeyListener(this);
        messageText.addKeyListener(this);
        sendMessage.setFocusable(true);
        messageText.setFocusable(true);
        //create message text field
        messageText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextArea field = (JTextArea)e.getComponent();
                if (field.getText().equals("Insert Message...")) {
                    field.setText("");
                }
                field.setForeground(Color.BLACK);
                //field.removeFocusListener(this);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (messageText.getText().isEmpty()) {
                    messageText.setForeground(Color.GRAY);
                    messageText.setText("Insert Message...");
                }
            }

        });
        messages.addMouseListener(this);
    }
    //add actionListeners for send and back
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
                    messageText.setText("Insert Message...");
                    messageText.setForeground(Color.GRAY);
                    JOptionPane.showMessageDialog(null, "Message Sent");
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //go back to previous frame (PeopleView)
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PeopleView peopleView = new PeopleView(messageBoard, user);
                    running = false;
                    peopleView.show();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //send text file
        sendFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return f.getName().endsWith(".txt") || f.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return "Text files only";
                    }
                    
                });
                int result = fileChooser.showOpenDialog(messageBoard);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fileChooser.getSelectedFile();
                        BufferedReader bfr = new BufferedReader(new FileReader(file));
                        String line;
                        String message = "";
                        while ((line = bfr.readLine()) != null) {
                            message += line + "\n";
                        }
                        emailSelected = emailSelected.split(" ")[0];
                        mic.message(message, mic.getID(), otherID, selectedStore);
                        bfr.close();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            
        });
    }
    //make frame visible (show MessageGUI)
    public void show() {
        messageBoard.setContentPane(new Container());
        run();
        messageBoard.revalidate();
        messageBoard.repaint();
    }

    //Source: https://stackoverflow.com/questions/27077508/infinite-loop-in-swing
    //autoupdate
    private class UpdateMessages implements Runnable {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        public void addPropertyChangeListeners(PropertyChangeListener pcl) {
            pcs.addPropertyChangeListener(pcl);
        }
        //run update
        @Override
        public void run() {
            int i = 0;
            while (running) {
                try {
                    ArrayList<Message> conversationHistory2 = mic.getConversation(otherID, selectedStore);
                    i++;
                    System.out.println(i);
                    if (FilterInterfaceGUI.status()) {
                        int index = 0;
                        for (Message msg: conversationHistory2) {
                            String message = msg.getMessage();
                            String ID = msg.getMessageID();
                            String sender = msg.getSender();
                            String rec = msg.getRecipient();
                            Instant inst = msg.getTimeStamp();
                            String store = msg.getStore();
                            Message newMsg = new Message(FilterInterfaceGUI.filterMsg(message), ID,
                                    sender, rec, inst, store);
                            conversationHistory2.set(index, msg);
                            index++;
                        }
                    }
                    if (!conversationHistory2.equals(conversationHistory)) {
                        pcs.firePropertyChange("changeUI", conversationHistory, conversationHistory2);
                    }
                    pcs.firePropertyChange("setStatus", false, true);
                } catch (Exception e) {
                    pcs.firePropertyChange("setStatus", true, false);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            }
        }

    }
    //update message command
    private void updateMessages() {
        UpdateMessages um = new UpdateMessages(); 
        um.addPropertyChangeListeners(this);
        running = true;
        new Thread(um).start();
    }
    //update messageList visible on messageGUI
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("changeUI")) {
            conversationHistory = (ArrayList<Message>) evt.getNewValue();
            String[] msg;
            try {
                msg = mic.messagesToArray(conversationHistory);
                int index = 0;
                if (FilterInterfaceGUI.status()) {
                    for (String mes: msg) {
                        msg[index] = FilterInterfaceGUI.filterMsg(mes);
                        index++;
                    }
                }
                messageList.removeAllElements();
                messageList.addAll(Arrays.asList(msg));
                messages.setModel(messageList);
                if (!messageList.isEmpty() && messages.getLastVisibleIndex() == messageList.getSize() - 2) {
                    messages.ensureIndexIsVisible(messageList.lastIndexOf(messageList.lastElement()));
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        SwingUtilities.updateComponentTreeUI(messages);
                    }
                });
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (evt.getPropertyName().equals("setStatus")) {
            boolean result = (boolean) evt.getNewValue();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (result) {
                        online.setText("<html><div style=\"color: green\">Connected</div></html>");
                    } else {
                        online.setText("<html><div style=\"color: red\">Disconnected</div></html>");
                    }
                    SwingUtilities.updateComponentTreeUI(online);
                }
                
            });
        }
    }

    //My source: https://docs.oracle.com/javase/8/docs/api/javax/swing/ListCellRenderer.html
    //Timestamping for notis
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
            String[] valueArray = value.split("\n");
            String newValue = "";
            for (String valueString : valueArray) {
                String[] anotherValueArray = valueString.split(" ");
                for (String anotherValueString : anotherValueArray) {
                    if (anotherValueString.matches("^((((https)|(http))://)|([w0-9])+\\.)[\\.a-zA-Z0-9]+([a-zA-Z0-9/-?=;,\"'+])+$")) {
                        newValue += "<a href=\"" + anotherValueString + "\">" + anotherValueString + "</a> ";
                    } else {
                        newValue += anotherValueString + " ";
                    }
                }
                newValue = newValue.strip() + "\n";
            }
            value = newValue.strip();
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
    //key pressed
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
            messageText.append("\n");
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMessage.doClick();
            e.consume();
        }
    }
    //key typed (undefined)
    @Override
    public void keyTyped(KeyEvent e) {

    }
    //key released (undefined)
    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
    //mouseClick recognition
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.getButton() == 3) {
            messages.setSelectedIndex(messages.locationToIndex(e.getPoint()));
            showContextMenu(e);
        } else if (e.getButton() == 1 && e.isControlDown()) {
            messages.setSelectedIndex(messages.locationToIndex(e.getPoint()));
            String messageSelected = (String) messages.getSelectedValue();
            String[] stuffInMessages = messageSelected.split("\n| ");
            ArrayList<String> links = new ArrayList<>();
            for (String stuff : stuffInMessages) {
                if (stuff.matches("^((((https)|(http))://)|([w0-9])+\\.)[\\.a-zA-Z0-9]+([a-zA-Z0-9/-?=;,\"'+])+$")) {
                    links.add(stuff);
                }
            }
            if (!links.isEmpty()) {
                URI uri = URI.create(links.get(paneOfPane(((int) (messages.getWidth())), links.size(), e.getX())));
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Couldn't open link", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            e.consume();
        }
    }
    //set pane
    private int paneOfPane(int totalWidth, int numberOfElements, int location) {
        int sizeOfPane = totalWidth / numberOfElements;
        int i = 0;
        int count = 0;
        while (i < location) {
            i += sizeOfPane;
            count++;
        }
        return count > numberOfElements ? numberOfElements - 1 : count - 1;
    }

    /**
     * show menu when right-clicking message (edit, delete, copy)
     */
    public void showContextMenu(MouseEvent e) {
        JPopupMenu popupMenu = new JPopupMenu("Actions");
        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
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
                    JTextArea textArea = new JTextArea(originalMessage);
                    JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    textArea.addKeyListener(new KeyListener() {

                        @Override
                        public void keyTyped(KeyEvent e) {

                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                                textArea.append("\n");
                            }
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                        
                    });
                    int result = JOptionPane.showConfirmDialog(null, scrollPane, "Edit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null);
                    String editedMessage = textArea.getText();
                    System.out.println(editedMessage);
                    if (result == JOptionPane.OK_OPTION && !editedMessage.equals("")) {
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
        //delete in right-click menu
        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
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
        //copy
        copy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection = new StringSelection((String) messages.getSelectedValue());
                clipboard.setContents(stringSelection, null);
            }
            
        });
        if (messages.getSelectedIndex() != -1) {
            popupMenu.add(copy);
            String messageSelected = (String) messages.getSelectedValue();
            String[] stuffInMessages = messageSelected.split("\n| ");
            int i = 0;
            for (String stuff : stuffInMessages) {
                if (stuff.matches("^((((https)|(http))://)|([w0-9])+\\.)[\\.a-zA-Z0-9]+([a-zA-Z0-9/-?=;,\"'+])+$")) {
                    i++;
                    JMenuItem link = new JMenuItem("Link " + i);
                    link.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            URI uri = URI.create(stuff);
                            try {
                                Desktop.getDesktop().browse(uri);
                            } catch (IOException e1) {
                                JOptionPane.showMessageDialog(null, "Couldn't open link", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        
                    });
                    popupMenu.add(link);
                }
            }
            popupMenu.add(editItem);
            popupMenu.add(deleteItem);
            popupMenu.show(messages, e.getX(), e.getY());
        }
    }
}
