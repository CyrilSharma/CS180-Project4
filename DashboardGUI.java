import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

//TODO: Need to pull role, create database interface
public class DashboardGUI implements Runnable {
    //private ArrayList<ArrayList<String[]>> stores;
    //private ArrayList<ArrayList<String[]>> myConversations;
    private ArrayList<String> stores;
    private JList storeList;
    private JList messageSentList;
    private JList messageReceivedList;
    private JList wordList;
    private JList displayList;
    private DefaultListModel List = new DefaultListModel();
    private JButton sortDescendingSentNum;
    private JButton sortAscendingSentNum;
    private JButton sortDescendingReceivedNum;
    private JButton sortAscendingReceivedNum;
    private JButton mostCommonWords;
    private JButton backButton;
    private JPanel rightPanel;
    private JPanel upperPanel;
    private String email;
    private JFrame board;
    private JLabel title;
    private Container content;
    private JScrollPane convPane;
    private JScrollPane scrollPane;
    public DashboardGUI(JFrame board, HashMap<String, String> stores) {
        this.board = board;
        Set<String> keySet = stores.keySet();
        this.stores = new ArrayList<String>(keySet);
        board.setSize(600,500);
    }
    public void setFrame() {
        convPane.setLayout(null);
        rightPanel.setLayout(null);
        upperPanel.setLayout(null);
        upperPanel.setSize(400,70);
        convPane.setSize(500,400);
        //convPane.setBackground(Color.GRAY);
        scrollPane.setBounds(30,70,370,250);
        convPane.setPreferredSize(new Dimension(370, 800));
        title.setBounds(10,10, 200, 50);
        Font f = new Font("Helvetica", Font.BOLD, 25);
        title.setFont(f);
        rightPanel.setBounds(400,0, 200, 400);
        //TODO: add a way to choose which buttons are displayed based on seller/customer
        /**
         * if (role == seller) {
         *     mostCommonWords.setBounds(20, 60, 140,30);
         *     } else {
         *     sortAscendingReceivedNum.setBounds(20, 140, 160,30);
         *     sortDescendingReceivedNum.setBounds(20, 180, 160,30);
         */
        mostCommonWords.setBounds(20, 20, 140,30);
        sortAscendingSentNum.setBounds(20, 60, 160,30);
        sortDescendingSentNum.setBounds(20, 100, 160,30);
        sortAscendingReceivedNum.setBounds(20, 140, 160,30);
        sortDescendingReceivedNum.setBounds(20, 180, 160,30);
        backButton.setBounds(20, 220, 160, 30);
    }
    public void createAndAdd() {
        content = board.getContentPane();
        content.setLayout(new BorderLayout());
        convPane = new JScrollPane();
        scrollPane = new JScrollPane(storeList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(370,400));
        //statusList = new JList(status.toArray(new String[0]));
        //people.setBackground(Color.gray);
        rightPanel = new JPanel();
        title = new JLabel();
        sortAscendingSentNum = new JButton("Sort Ascending by Messages Sent");
        sortDescendingSentNum = new JButton("Sort Descending by Messages Sent");
        sortAscendingReceivedNum = new JButton("Sort Ascending by Messages Received");
        sortDescendingReceivedNum = new JButton("Sort Descending by Messages Received");
        mostCommonWords = new JButton("Most Common Words");
        backButton = new JButton("Back");
        upperPanel = new JPanel();
        upperPanel.add(title);
        rightPanel.add(sortAscendingSentNum);
        rightPanel.add(sortDescendingSentNum);
        //TODO: pull role and choose buttons to add
        /**
         * if (role == seller) {
         *     rightPanel.add(mostCommonWords);
         * } else {
         *     rightPanel.add(sortAscendingReceivedNum);
         *     rightPanel.add(sortDescendingReceivedNum);
         */
        rightPanel.add(backButton);
        convPane.add(upperPanel);
        convPane.add(rightPanel);
        convPane.add(scrollPane);
    }
    public void addActionListeners() {
        sortDescendingSentNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Insert sort command here
                }
        });
        sortAscendingSentNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Insert sort command here
            }
        });
        sortDescendingReceivedNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Insert sort command here
            }
        });
        sortAscendingReceivedNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Insert sort command here
            }
        });
        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                try {
                    /**
                    AccountManagerGUI accountManagerGUI = new AccountManagerGUI(board, (String) blockGUIInterface.geTranslator().query(new Query("User", "getEmail")));
                    accountManagerGUI.show();
                     */
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
    }
    @Override
    public void run() {

        /**
        try {
            List.addAll(blockGUIInterface.getAllUsers());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
         */
        board.setSize(600,400);
        createAndAdd();
        setFrame();
        List.addAll(stores);
        storeList = new JList(List);
        content.add(convPane, BorderLayout.CENTER);

    }
    public void show() {
        board.setContentPane(new Container());
        run();
        board.revalidate();
        board.repaint();
    }
}
