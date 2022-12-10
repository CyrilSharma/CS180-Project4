import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

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
    private String selectedStore;
    private Role role;
    private HashMap<String, ArrayList<Object>> stats;
    private HashMap<String, String> storeMap;
    private DashboardInterfaceGUI dig;
    private HashMap<String, HashMap<String, ArrayList<Object>>> statistics;
    public DashboardGUI(JFrame board, HashMap<String,String> user) {
        this.board = board;
        board.setSize(600,500);
        this.user = user;
        role = user.get("role").equals("Seller") ? Role.Seller : Role.Customer;
        dig = new DashboardInterfaceGUI();
        storeMap = dig.getStoreMap(role, user.get("email"));
        this.stores = new ArrayList<String>(storeMap.keySet());
        if (role == Role.Seller) {
            statistics = dig.sellerStats(user.get("id"));
        } else {
            statistics = dig.customerStats(user.get("id"));
        }

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
            scrollPane.setBounds(0,70,150,150);
            scrollPane2.setBounds(0,220,150,150);
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
        statisticLabel.setBounds(180,100,200,100);
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
        statisticLabel.setEditable(false);
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
        customerList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (customerList.getSelectedValue() == null) {
                    return;
                }
                if (storeList.getSelectedValue() == null) {
                    //JOptionPane.showMessageDialog(null, "Please select a store!", "Alert", JOptionPane.ERROR_MESSAGE);
                }
                String user = (String) customerList.getSelectedValue();
                String store = selectedStore;
                String id = dig.getID(user);
                String msg = "Statistic:\nMessages Received: %d\nMost Common words: %s";
                msg = String.format(msg, (int) statistics.get(store).get(id).get(0),
                        (String) statistics.get(store).get(id).get(1));
                statisticLabel.setText(msg);
            }
        });
        storeList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (storeList.getSelectedValue() == null) {
                    return;
                }
                if (role == Role.Customer) {
                    String store = (String) storeList.getSelectedValue();
                    statisticLabel.setText(getStats(store));
                } else {
                    String store = (String) storeList.getSelectedValue();
                    if (store != null) {
                        selectedStore = store;
                    }

                    String[] users = statistics.get(store).keySet().toArray(new String[0]);
                    int index = 0;
                    for (String user: users) {
                        users[index] = dig.getEmail(user);
                        index++;
                    }
                    customerList.setListData(users);
                }


            }
        });
        storeList.setListData(statistics.keySet().toArray(new String[0]));
        HashMap<String, ArrayList<Object>> stat = new HashMap<>();
        ArrayList<Object> sample = new ArrayList<>();
        sample.add(4); sample.add(3); sample.add(6); sample.add("Doge Coin");
        stat.put("Store1", sample);
        setStats(stat);

    }

    public void sortAlphabet() {
        String[] strArr = statistics.keySet().toArray(new String[0]);
        ArrayList<String> data = new ArrayList<>(java.util.List.of(strArr));
        Collections.sort(data, String.CASE_INSENSITIVE_ORDER);
        storeList.setListData(data.toArray());
        storeList.updateUI();
        if (role == Role.Seller && customerList.getModel().getSize() != 0 && selectedStore != null) {
            String[] strArr2 = statistics.get(selectedStore).keySet().toArray(new String[0]);
            ArrayList<String> data2 = new ArrayList<>(java.util.List.of(strArr2));
            int index = 0;
            for (String val: data2) {
                data2.set(index, dig.getEmail(val));
                index++;
            }
            Collections.sort(data2, String.CASE_INSENSITIVE_ORDER);
            customerList.setListData(data2.toArray());
            customerList.updateUI();
        }
    }
    public void sortAlphabetBackwards() {
        String[] strArr = statistics.keySet().toArray(new String[0]);
        ArrayList<String> data = new ArrayList<>(java.util.List.of(strArr));
        Collections.sort(data, String.CASE_INSENSITIVE_ORDER);
        Collections.reverse(data);
        storeList.setListData(data.toArray());
        storeList.updateUI();
        if (role == Role.Seller && customerList.getModel().getSize() != 0 && selectedStore != null) {
            String[] strArr2 = statistics.get(selectedStore).keySet().toArray(new String[0]);
            ArrayList<String> data2 = new ArrayList<>(java.util.List.of(strArr2));
            int index = 0;
            for (String val: data2) {
                data2.set(index, dig.getEmail(val));
                index++;
            }
            Collections.sort(data2, String.CASE_INSENSITIVE_ORDER);
            Collections.reverse(data2);
            customerList.setListData(data2.toArray());
            customerList.updateUI();
        }
    }

    public ArrayList<String> getReceivedArray(String store) {
        ArrayList<String> users = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        System.out.println(1);
        for (String user : statistics.get(store).keySet()) {
            System.out.println(2);
            int received = (int) statistics.get(store).get(user).get(0);
            System.out.println(user + " " + received);
            if (map.size() == 0) {
                map.put(user, received);
                users.add(user);
            } else {
                int index = 0;
                int size = map.size();
                for (String userr: map.keySet()) {
                    if (received < map.get(userr)) {
                        map.put(user, received);
                        users.add(index, user);
                        break;
                    }
                    if (index == size - 1) {
                        System.out.println(user + " " + index);
                        map.put(user, received);
                        users.add(user);
                    }
                    index++;
                }
            }
        }
        return users;
    }
    public ArrayList<String> getReceivedArray() {
        ArrayList<String> stores = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        for (String store : statistics.keySet()) {
            String user = "";
            for (String name: statistics.get(store).keySet()) {
                user = name;
            }
            int received = (int) statistics.get(store).get(user).get(0);
            System.out.println(store + " " + received);
            if (map.size() == 0) {
                map.put(store, received);
                stores.add(store);
            } else {
                int index = 0;
                int size = map.size();
                for (String storee: map.keySet()) {
                    if (received > map.get(storee)) {
                        map.put(store, received);
                        stores.add(index, store);
                        break;
                    }
                    if (index == size - 1) {
                        System.out.println(user + " " + index);
                        map.put(store, received);
                        stores.add(store);
                    }
                    index++;
                }
            }
        }
        return stores;
    }

    public void sortHighReceived() {
        if (role == Role.Seller) {
            if (selectedStore == null) {
                return;
            }
            ArrayList<String> data2 = getReceivedArray(selectedStore);
            int index = 0;
            for (String val: data2) {
                System.out.println(val);
                data2.set(index, dig.getEmail(val));
                index++;
            }
            customerList.setListData(data2.toArray());
            customerList.updateUI();
        } else {
            ArrayList<String> data2 = getReceivedArray();
            storeList.setListData(data2.toArray());
            storeList.updateUI();
        }

    }

    public void sortLowReceived() {
        if (role == Role.Seller) {
            if (selectedStore == null) {
                return;
            }
            ArrayList<String> data2 = getReceivedArray(selectedStore);
            int index = 0;
            for (String val: data2) {
                data2.set(index, dig.getEmail(val));
                index++;
            }
            Collections.reverse(data2);
            customerList.setListData(data2.toArray());
            customerList.updateUI();
        } else {
            ArrayList<String> data2 = getReceivedArray();
            Collections.reverse(data2);
            storeList.setListData(data2.toArray());
            storeList.updateUI();
        }

    }

    public void setStats(HashMap<String, ArrayList<Object>> stats) {
        this.stats = stats;
    }

    public String getStats(String store) {
        String msg = "";
        String otherEmail = storeMap.get(store);
        String rec = dig.getID(otherEmail);
        if (role == Role.Seller) {
            msg = "Statistic:\nMessages Received: %d\nMost Common words: %s";
            msg = String.format(msg, (int) statistics.get(store).get(rec).get(0),
                    (String) statistics.get(store).get(rec).get(1));
        } else {
            msg = "Statistic:\nSeller: %s\nMessages Received: %d\nMessages Sent: %d";
            msg = String.format(msg, otherEmail, (int) statistics.get(store).get(rec).get(0),
                    (int) statistics.get(store).get(rec).get(1));
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

                    AccountManagerGUI accountManagerGUI = new AccountManagerGUI(board, user.get("email"));
                    accountManagerGUI.show();

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
