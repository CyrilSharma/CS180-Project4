import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

//TODO: Need to pull role, create database interface
public class DashboardGUI implements Runnable {
    //private ArrayList<ArrayList<String[]>> stores;
    //private ArrayList<ArrayList<String[]>> myConversations;
    private ArrayList<String> stores;
    private HashMap<String,String> user;
    private JList storeList;
    private JList customerList;
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
    private JTextArea statisticLabel;
    private JLabel sortLabel;
    private Container content;
    private JScrollPane convPane;
    private JScrollPane scrollPane;
    private JScrollPane scrollPane2;
    private Role role;
    private HashMap<String, ArrayList<Object>> stats;
    private HashMap<String, String> storeMap;
    private DashboardInterfaceGUI dig;
    public DashboardGUI(JFrame board, HashMap<String, String> stores, HashMap<String,String> user) {
        this.board = board;
        Set<String> keySet = stores.keySet();
        this.stores = new ArrayList<String>(keySet);
        board.setSize(600,500);
        this.user = user;
        storeMap = stores;
        role = user.get("role").equals("Seller") ? Role.Seller : Role.Customer;
        dig = new DashboardInterfaceGUI();
        storeMap = dig.getStoreMap(role, user.get("email"));
        this.stores = new ArrayList<String>(storeMap.keySet());

    }
    public void setFrame() {
        convPane.setLayout(null);
        rightPanel.setLayout(null);
        upperPanel.setLayout(null);
        upperPanel.setSize(430,70);
        convPane.setSize(500,400);
        //convPane.setBackground(Color.GRAY);
        if (role == Role.Customer) {
            scrollPane.setBounds(0,70,150,330);
        } else {
            scrollPane.setBounds(0,70,150,165);
            scrollPane2.setBounds(0,235,150,165);
        }

        convPane.setPreferredSize(new Dimension(370, 800));
        title.setBounds(10,10, 200, 50);
        Font f = new Font("Helvetica", Font.BOLD, 25);
        title.setFont(f);
        title.setText("Dashboard");
        rightPanel.setBounds(430,0, 200, 400);
        //TODO: add a way to choose which buttons are displayed based on seller/customer
        /**
         * if (role == seller) {
         *     mostCommonWords.setBounds(20, 60, 140,30);
         *     } else {
         *     sortAscendingReceivedNum.setBounds(20, 140, 160,30);
         *     sortDescendingReceivedNum.setBounds(20, 180, 160,30);
         */
        sortLabel.setBounds(60,25,140,30);
        sortAscendingSentNum.setBounds(20, 60, 140,30);
        sortDescendingSentNum.setBounds(20, 100, 140,30);
        sortAscendingReceivedNum.setBounds(20, 140, 140,30);
        sortDescendingReceivedNum.setBounds(20, 180, 140,30);
        backButton.setBounds(20, 330, 140, 30);
        statisticLabel.setBounds(180,100,180,100);
        placeUp();
    }

    public void createAndAdd() {
        content = board.getContentPane();
        content.setLayout(new BorderLayout());
        convPane = new JScrollPane();
        storeList = new JList(stores.toArray());
        customerList = new JList();
        scrollPane = new JScrollPane(storeList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(370,400));
        scrollPane2 = new JScrollPane(customerList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //statusList = new JList(status.toArray(new String[0]));
        //people.setBackground(Color.gray);
        rightPanel = new JPanel();
        title = new JLabel();
        sortLabel = new JLabel("Sort By...");
        sortAscendingSentNum = new JButton("A...Z");
        sortDescendingSentNum = new JButton("Z...A");
        sortAscendingReceivedNum = new JButton("Highest Received");
        sortDescendingReceivedNum = new JButton("Lowest Received");
        mostCommonWords = new JButton("Most Common Words");
        backButton = new JButton("Back");
        statisticLabel = new JTextArea("");
        upperPanel = new JPanel();
        upperPanel.add(title);
        rightPanel.add(sortAscendingSentNum);
        rightPanel.add(sortDescendingSentNum);
        rightPanel.add(sortAscendingReceivedNum);
        rightPanel.add(sortDescendingReceivedNum);
        //TODO: pull role and choose buttons to add
        /**
         * if (role == seller) {
         *     rightPanel.add(mostCommonWords);
         * } else {
         *     rightPanel.add(sortAscendingReceivedNum);
         *     rightPanel.add(sortDescendingReceivedNum);
         */
        rightPanel.add(backButton);
        rightPanel.add(sortLabel);
        convPane.add(upperPanel);
        convPane.add(rightPanel);
        convPane.add(scrollPane);
        convPane.add(scrollPane2);
        convPane.add(statisticLabel);
        addActionListeners();
        storeList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (storeList.getSelectedValue() == null) {
                    return;
                }
                String store = (String) storeList.getSelectedValue();
                if (stats.containsKey(store)) {
                    statisticLabel.setText(getStats(store));
                    placeUp();
                } else {
                    noData(store);
                    placeUp();
                }

            }
        });
        HashMap<String, ArrayList<Object>> stat = new HashMap<>();
        ArrayList<Object> sample = new ArrayList<>();
        sample.add(4); sample.add(3); sample.add(6); sample.add("Doge Coin");
        stat.put("Store1", sample);
        setStats(stat);

    }

    public void sortAlphabet() {
        Collections.sort(stores, String.CASE_INSENSITIVE_ORDER);
        storeList.setListData(stores.toArray());
        storeList.updateUI();
    }
    public void sortAlphabetBackwards() {
        Collections.sort(stores, String.CASE_INSENSITIVE_ORDER);
        Collections.reverse(stores);
        storeList.setListData(stores.toArray());
        storeList.updateUI();
    }

    public ArrayList<String> getReceivedArray() {
        ArrayList<String> stores = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        for (String store : this.stores) {
            if (!stats.containsKey(store)) {
                map.put(store, 0);
                stores.add(store);
                continue;
            }
            int received = (role == Role.Customer) ? (int) stats.get(store).get(0) :(int) stats.get(store).get(2);
            if (map.size() == 0) {
                map.put(store, received);
                stores.add(store);
            } else {
                int index = 0;
                int size = map.size();
                for (String storr: map.keySet()) {
                    if (received < map.get(storr)) {
                        map.put(store, received);
                        stores.add(index, store);
                    }
                    if (index == size - 1) {
                        map.put(store, received);
                        stores.add(index, store);
                    }
                    index++;
                }
            }
        }
        return stores;
    }

    public void sortHighReceived() {
        storeList.setListData(getReceivedArray().toArray());
        storeList.updateUI();
    }

    public void sortLowReceived() {
        ArrayList<String> list = getReceivedArray();
        Collections.reverse(list);
        storeList.setListData(list.toArray());
        storeList.updateUI();
    }

    public void setStats(HashMap<String, ArrayList<Object>> stats) {
        this.stats = stats;
    }

    public String getStats(String store) {
        String msg = "";
        ArrayList<Object> stat = stats.get(store);
        if (role == Role.Seller) {
            msg = "Statistic:\nMessages Received: %d\nMost Common words: %s";
            msg = String.format(msg, (int) stat.get(2), (String) stat.get(3));
        } else {
            msg = "Statistic:\nSeller: %s\nMessages Received: %d\nMessages Sent: %d";
            msg = String.format(msg, storeMap.get(store), (int) stat.get(0), (int) stat.get(1));
        }
        return msg;
    }

    public void addActionListeners() {
        sortDescendingSentNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Insert sort command here
                sortAlphabetBackwards();
                }
        });
        sortAscendingSentNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Insert sort command here
                sortAlphabet();
            }
        });
        sortDescendingReceivedNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Insert sort command here
                sortLowReceived();
            }
        });
        sortAscendingReceivedNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Insert sort command here
                sortHighReceived();
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
        content.add(convPane, BorderLayout.CENTER);

    }
    public void show() {
        board.setContentPane(new Container());
        run();
        board.revalidate();
        board.repaint();
    }

    public void noData(String store) {
        String msg = "";
        ArrayList<Object> stat = stats.get(store);
        if (role == Role.Seller) {
            msg = "Statistic:\nMessages Received: 0\nMost Common words: N/A";
        } else {
            msg = "Statistic:\nSeller: %s\nMessages Received: 0\nMessages Sent: 0";
            msg = String.format(msg, storeMap.get(store));
        }
        statisticLabel.setText(msg);
        statisticLabel.updateUI();
    }
    public void placeUp() {
        statisticLabel.setLocation(160,80);
    }
}
