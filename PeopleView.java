import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PeopleView implements Runnable {
    //TODO 1: remove html after user checks that message
    //TODO 2: remove html tag when transferring data
    private JFrame board;
    private Container content;
    private JScrollPane convPane;
    private JScrollPane scrollPane;
    private JPanel rightPanel;
    private JPanel upperPanel;
    private JLabel title;
    private JList people;
    private JList statusList;
    private JTextField searchBar;
    private JTextField storeSearchBar;
    private JList storeList;
    private JButton blockButton;
    private JButton viewButton;
    private JButton placeholder;
    private JButton backButton;
    private JButton invisibleButton;
    private ArrayList<String> users;
    private Role role;
    private ArrayList<String> status;

    private JScrollPane scroll2;

    private Translator translator;
    private HashMap<String, String> user;
    private HashMap<String, String> map;

    private ArrayList<String> newUserNotif;
    private ArrayList<String> newStoreNotif;
    private HashMap<String, ArrayList<String>> userNotifications;

    //pass a list of emails of users
    //HashMap of {key: store name, value: owner id} must be passed in order to show list of stores
    public PeopleView(JFrame frame, HashMap<String,String> user) throws Exception {
        translator = new Translator();
        frame.setSize(600,540);
        board = frame;
        users = (ArrayList<String>) translator.query(new Query("User", "getUsers"));
        this.user = user;
        this.role = Role.valueOf(user.get("role"));
        String[] ex = {"Online", "Offline", "Online"};
        status = new ArrayList<>(Arrays.asList(ex));
        HashMap<String,String> stores;
        if (role.equals(Role.Customer)) {
            stores = (HashMap<String, String>) translator.query(new Query("User", "viewStores"));
        } else {
            stores = (HashMap<String, String>) translator.query(new Query("User", "getStores"));
        }
        map = stores;
        userNotifications = new HashMap<>();
    }
    public void show() {
        board.setContentPane(new Container());
        run();
        board.revalidate();
        board.repaint();
    }
    public void createAndAdd() {
        content = board.getContentPane();
        content.setLayout(new BorderLayout());
        String[] user = users.toArray(new String[0]);
        people = new JList(user);
        statusList = new JList(status.toArray(new String[0]));
        convPane = new JScrollPane();
        scrollPane = new JScrollPane(people, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(370,400));
        //people.setBackground(Color.gray);
        rightPanel = new JPanel();
        title = new JLabel();
        blockButton = new JButton("Block");
        viewButton = new JButton("Message");
        backButton = new JButton("Back");
        invisibleButton = new JButton("Invisible");
        upperPanel = new JPanel();
        searchBar = new JTextField("Search...");
        storeSearchBar = new JTextField("Search stores...");
        //storeList = new JList(map.keySet().toArray(new String[0]));
        storeList = new JList();
        placeholder = new JButton();
        scroll2 = new JScrollPane(storeList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        if (role == Role.Customer) {
            title.setText("Stores");
        } else {
            title.setText("Customers");
        }
        //rightPanel.setBackground(Color.RED);
        upperPanel.add(title);
        upperPanel.add(searchBar);
        upperPanel.add(placeholder);
        rightPanel.add(blockButton);
        rightPanel.add(viewButton);
        rightPanel.add(backButton);
        rightPanel.add(invisibleButton);
        convPane.add(upperPanel);
        convPane.add(rightPanel);
        convPane.add(scrollPane);
        if (role == Role.Customer) {
            upperPanel.add(storeSearchBar);
        } else {
            convPane.add(storeSearchBar);
        }

        convPane.add(scroll2);
        //scrollPane.add(people);
    }


    public void updateNotif(HashMap<String, ArrayList<String>> notif) {
        userNotifications = notif;
    }

    public void removeStoreNotif(String store) {
        if (userNotifications.containsKey(store)) {
            userNotifications.remove(store);
        }
    }

    public String storeHTMLRemover(String str) {
        str = str.substring(str.indexOf(">") + 1);
        str = str.substring(str.indexOf(">") + 1);
        str = str.substring(str.indexOf(">") + 1);
        str = str.substring(0, str.indexOf("<"));
        return str;
    }
    public void updateUserUI(String store) {
        String[] newArray = new String[users.size()];
        int index = 0;
        if (userNotifications == null || userNotifications.get(store) == null) {
            return;
        }
        for (String user: users) {
            if (userNotifications.get(store).contains(user)) {
                String newData = "<html><b><font color=blue>";
                newData += user + "</font></b></html>";
                newArray[index] = newData;
            } else {
                newArray[index] = user;
            }
            index++;
        }
        people.setListData(newArray);
    }
    public void updateStoreUI() {
        String[] newArray = new String[map.keySet().size()];
        int index = 0;
        for (String store: map.keySet()) {
            if (userNotifications.keySet().contains(store)) {
                String newData = "<html><b><font color=blue>";
                newData += store + "</font></b></html>";
                newArray[index] = newData;
            } else {
                newArray[index] = store;
            }
            index++;
        }
        storeList.setListData(newArray);
    }

    public void setFrame() {
        convPane.setLayout(null);
        rightPanel.setLayout(null);
        upperPanel.setLayout(null);
        convPane.setSize(500,400);
        //convPane.setBackground(Color.GRAY);
        storeSearchBar.setForeground(Color.GRAY);
        upperPanel.setSize(400,70);
        title.setBounds(10,10, 200, 50);
        Font f = new Font("Helvetica", Font.BOLD, 25);
        title.setFont(f);
        rightPanel.setBounds(400,0, 200, 530);
        blockButton.setBounds(20, 20, 160,30);
        invisibleButton.setBounds(20, 100, 160,30);
        viewButton.setBounds(20, 60, 160,30);
        backButton.setBounds(100, 450, 80, 30);
        if (role == Role.Customer) {
            storeSearchBar.setBounds(200,25,200,20);
            scroll2.setBounds(30,70,370,400);
            storeList.setListData(map.keySet().toArray());
            storeList.updateUI();
        } else {
            scrollPane.setBounds(30,70,370,250);
            //scrollPane.setBackground(Color.green);
            people.setBounds(0,0,330,5000);
            convPane.setPreferredSize(new Dimension(370, 800));
            storeSearchBar.setBounds(30, 340, 300, 20);
            //editButton.setBounds(20, 100, 160,30);
            //deleteButton.setBounds(20, 140, 160,30);
            placeholder.setBounds(-1,-1,1,1);
            //backButton.setBounds(100, 450, 80, 30);
            searchBar.setBounds(200,25,200,20);
            scroll2.setBounds(30, 370, 370, 100);
            searchBar.setForeground(Color.GRAY);
            ArrayList<String> stores = getMyStores(user.get("id"));
            storeList.setListData(stores.toArray());
            storeList.updateUI();
        }


    }

    public ArrayList<String> getMyStores(String myid) {
        ArrayList<String> stores = new ArrayList<>();
        for (String store: map.keySet()) {
            if (myid.equals(map.get(store))) {
                stores.add(store);
            }
        }
        return stores;
    }

    public ArrayList<String> getUserStores(String email) throws Exception {
        //Need a translator here
        HashMap<String, String> data = translator.get("email", email.split(" ")[0]);
        String id = data.get("id");
        ArrayList<String> stores = new ArrayList<>();
        for (String store: map.keySet()) {
            if (id.equals(map.get(store))) {
                stores.add(store);
            }
        }
        return stores;
    }
    public void addActionListeners() {
        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String email;
                    if (user.get("role").equals(Role.Customer.toString())) {
                        String store = (String) storeList.getSelectedValue();
                        email = (String) translator.query(new Query("User", "getEmailFromStore", store));
                    } else {
                        email = (String) people.getSelectedValue();
                    }
                    String msg = "Trying to block " + email;
                    JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                    BlockGUIInterface blockGUIInterface = new BlockGUIInterface();
                    blockGUIInterface.blockUser(email, false);
                    PeopleView peopleView = new PeopleView(board, user);
                    peopleView.show();
                    JOptionPane.showMessageDialog(null, "Successful!", "Message", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String email;
                    //email = email.split(" ")[0];
                    String store = (String) storeList.getSelectedValue();
                    if (role == Role.Customer) {
                        //ID OR EMAIL
                        email = map.get(store);
                        if (store.indexOf("<") != -1) {
                            store = storeHTMLRemover(store);
                            removeStoreNotif(store);
                        } else {
                            removeStoreNotif(store);
                        }
                    } else {
                        email = (String) people.getSelectedValue();
                    }
                    if (email != null && store != null) {
                        String msg = "Trying to view a conversation with " + email + " with " + store;
                        JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                        try {
                            MessageGUI gui = new MessageGUI(board, "view", email, (String) translator.query(new Query("User", "getEmail")), store, PeopleView.this);
                            gui.show();
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(null, e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        invisibleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String email;
                    if (user.get("role").equals(Role.Customer.toString())) {
                        String store = (String) storeList.getSelectedValue();
                        email = (String) translator.query(new Query("User", "getEmailFromStore", store));
                    } else {
                        email = (String) people.getSelectedValue();
                    }
                    String msg = "Trying to become invisible from " + email;
                    JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                    BlockGUIInterface blockGUIInterface = new BlockGUIInterface();
                    blockGUIInterface.blockUser(email, true);
                    PeopleView peopleView = new PeopleView(board, user);
                    peopleView.show();
                    JOptionPane.showMessageDialog(null, "Successful!", "Message", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //will change to mainGUI if it is uploaded
                MainMenuGUI gui = new MainMenuGUI(board, user);
                gui.show();
            }
        });
        searchBar.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                searchUser();
            }
            public void removeUpdate(DocumentEvent e) {
                searchUser();
            }
            public void insertUpdate(DocumentEvent e) {
                searchUser();
            }

        });
        storeSearchBar.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                searchStore();
            }
            public void removeUpdate(DocumentEvent e) {
                searchStore();
            }
            public void insertUpdate(DocumentEvent e) {
                searchStore();
            }

        });
    }
    public void run() {
        createAndAdd();
        setFrame();
        addActionListeners();
        storeList.setListData(map.keySet().toArray(new String[0]));
        //testAdd();
        searchBar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField field = (JTextField)e.getComponent();
                if (field.getText().equals("Search...")) {
                    field.setText("");
                }
                field.setForeground(Color.BLACK);
                //field.removeFocusListener(this);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setForeground(Color.GRAY);
                    searchBar.setText("Search...");
                }
            }

        });
        storeSearchBar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField field = (JTextField)e.getComponent();
                if (field.getText().equals("Search stores...")) {
                    field.setText("");
                }
                field.setForeground(Color.BLACK);
                //field.removeFocusListener(this);
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (storeSearchBar.getText().isEmpty()) {
                    storeSearchBar.setForeground(Color.GRAY);
                    storeSearchBar.setText("Search stores...");
                }
            }

        });
        people.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (role == Role.Customer) {
                    String user = (String) people.getSelectedValue();
                    try {
                        ArrayList<String> stores = getUserStores(user);
                        storeList.setListData(stores.toArray());
                        storeList.updateUI();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        storeList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //STORE NAME MUST NOT HAVE A SPECIAL CHARACTER
                String store = (String) storeList.getSelectedValue();
                if (store != null && store.indexOf("<") != -1) {
                    store = storeHTMLRemover(store);
                }
                if (role == Role.Seller) {
                    updateUserUI(store);
                }

            }
        });
        //people.setBackground(Color.GREEN);
        content.add(convPane, BorderLayout.CENTER);
    }

    public void searchUser() {
        if (searchBar.getText().equals("Search...") || searchBar.getText().isEmpty()) {
            //JOptionPane.showMessageDialog(null, "Please enter an email!", "Alert", JOptionPane.ERROR_MESSAGE);
            people.setListData(users.toArray());
            people.updateUI();
            return;
        }
        String text = searchBar.getText().toLowerCase();
        ArrayList<String> updated = new ArrayList<>();
        for (String user: users) {
            if (user.toLowerCase().contains(text)) {
                updated.add(user);
            }
        }
        people.setListData(updated.toArray());
        people.updateUI();
    }

    public void searchStore() {
        if (storeSearchBar.getText().equals("Search stores...") || storeSearchBar.getText().isEmpty()) {
            //JOptionPane.showMessageDialog(null, "Please enter an email!", "Alert", JOptionPane.ERROR_MESSAGE);
            storeList.setListData(map.keySet().toArray());
            storeList.updateUI();
            return;
        }
        String text = storeSearchBar.getText().toLowerCase();
        ArrayList<String> updated = new ArrayList<>();
        for (String store: map.keySet()) {
            if (store.toLowerCase().contains(text)) {
                updated.add(store);
            }
        }
        storeList.setListData(updated.toArray());
        storeList.updateUI();
    }
}