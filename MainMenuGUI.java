import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuGUI implements Runnable {
    JFrame board;
    Container container;

    JPanel bottomPanel;

    //14 user options
    JButton messageUser;
    JButton viewMessage;
    JButton editMessage;
    JButton deleteMessage;
    JButton exportConvo;
    JButton blockUser;
    JButton unblockUser;
    JButton viewStores;
    JButton viewCustomers;
    JButton addStores;
    JButton openDashboard;
    JButton openFilter;
    JButton deleteAccount;
    JButton exit;

    public MainMenuGUI() {
        this.board = new JFrame("Turkey Shop");
    }
    public void show() {
        SwingUtilities.invokeLater(this);
    }

    public void addActionListeners() {

    }

    @Override
    public void run() {
        JPanel panel = new JPanel();
        this.container = board.getContentPane();
        container.setLayout(new BorderLayout());
        board.setSize(600,400);
        board.setLocationRelativeTo(null);
        board.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        board.setVisible(true);

        bottomPanel = new JPanel();
        //figure out how to switch the bottom panel view when button is clicked
        JScrollPane scroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messageUser = new JButton("Message User");
        viewMessage = new JButton("View a Message");
        editMessage = new JButton("Edit Message");
        deleteMessage = new JButton("Delete Message");
        exportConvo = new JButton("Export Conversations");
        blockUser = new JButton("Block User");
        unblockUser = new JButton("Unblock User");
        viewStores = new JButton("View Stores");
        viewCustomers = new JButton("View Customers");
        addStores = new JButton("Add Store");
        openDashboard = new JButton("Open Dashboard");
        openFilter = new JButton("Open Filter");
        deleteAccount = new JButton("Delete Account");
        exit = new JButton("Exit");
        panel.add(messageUser);
        panel.add(viewMessage);
        panel.add(editMessage);
        panel.add(deleteMessage);
        panel.add(exportConvo);
        panel.add(blockUser);
        panel.add(unblockUser);
        panel.add(viewStores);
        panel.add(viewCustomers);
        panel.add(addStores);
        panel.add(openDashboard);
        panel.add(openFilter);
        panel.add(deleteAccount);
        panel.add(exit);
        //add method actions
        addActionListeners();

        container.add(scroll, BorderLayout.NORTH); //setting it to the top part of the GUI
    }
}
