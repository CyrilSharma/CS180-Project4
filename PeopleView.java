import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
/**
 * Project 5 -> PeopleView
 *
 * Allows user to either access messageGUI, block, or become invisible
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version December 10, 2022
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PeopleView implements PropertyChangeListener {
    //1: remove html after user checks that message
    //2: remove html tag when transferring data
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
    private HashMap<String, Boolean> userNotifications;
    private HashMap<String, Boolean> storeNotifications;
    private HashMap<String, HashMap<String, Boolean>> notifications;
    private boolean running;

    //pass a list of emails of users
    //HashMap of {key: store name, value: owner id} must be passed in order to show list of stores

    /**
     * initialize PeopleView (constructor)
     * @param frame -> JFrame
     * @param user -> current user from HashMap
     * @throws Exception
     */
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
    }

    /**
     * make PeopleView visible (show PeopleView)
     */
    public void show() {
        board.setContentPane(new Container());
        run();
        updateNotifications();
        board.setSize(600,540);
        board.revalidate();
        board.repaint();
    }
    /**
     * Creates the buttons and scrollbar (BorderLayout) used in setFrame()
     */
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

    /**
     * remove HTML from store name
     * @param str -> initial store name
     * @return store name without html
     */
    public String storeHTMLRemover(String str) {
        str = str.substring(str.indexOf(">") + 1);
        str = str.substring(str.indexOf(">") + 1);
        str = str.substring(str.indexOf(">") + 1);
        str = str.substring(0, str.indexOf("<"));
        return str;
    }

    /**
     * update user UI
     */
    public void updateUserUI() {
        String[] newArray = new String[users.size()];
        int index = 0;
        for (String user: users) {
            if (checkNotification(user)) {
                String newData = "<html><b><font color=blue>";
                newData += user + "</font></b></html>";
                newArray[index] = newData;
            } else {
                newArray[index] = user;
            }
            index++;
        }
        people.setListData(newArray);
        people.updateUI();
    }

    /**
     * Check user's notifications
     * @param user -> current user
     * @return true or false, dep if message has come since user last logged in
     */
    public boolean checkNotification(String user) {
        if (notifications.get(user) == null) {
            return false;
        }
        for (Entry<String, Boolean> entry: notifications.get(user).entrySet()) {
            if (!entry.getValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * update store UI
     * @param user -> current seller (store)
     */
    public void updateStoreUI(String user) {
        String[] newArray = new String[map.keySet().size()];
        int index = 0;
        for (String store: map.keySet()) {
            if (checkStoreNotification(user, store)) {
                String newData = "<html><b><font color=blue>";
                newData += store + "</font></b></html>";
                newArray[index] = newData;
            } else {
                newArray[index] = store;
            }
            index++;
        }
        storeList.setListData(newArray);
        storeList.updateUI();
    }

    /**
     * Check store notification
     * @param user -> current user
     * @param store -> specific store
     * @return T/F, if store has received message since last login
     */
    public boolean checkStoreNotification(String user, String store) {
        if (notifications.get(user) == null) {
            return false;
        } else if (notifications.get(user).get(store) == null) {
            return false;
        }
        return !notifications.get(user).get(store);
    }
    //Check store notification
    public void checkStoreNotification() {
        String[] newArray = new String[map.keySet().size()];
        int index = 0;
        for (String store: map.keySet()) {
            if (check(store)) {
                String newData = "<html><b><font color=blue>";
                newData += store + "</font></b></html>";
                newArray[index] = newData;
            } else {
                newArray[index] = store;
            }
            index++;
        }
        storeList.setListData(newArray);
        storeList.updateUI();
    }
    //check notifications
    public boolean check(String store) {
        for (Entry<String, HashMap<String, Boolean>> entry: notifications.entrySet()) {
            if (entry.getValue().get(store) != null) {
                if (!entry.getValue().get(store)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Creates the actual frame layout (buttons, users, etc.) with locations and fonts
     */
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

    /**
     * get stores apparent to user
     * @param myid -> current user id
     * @return list of stores
     */
    public ArrayList<String> getMyStores(String myid) {
        ArrayList<String> stores = new ArrayList<>();
        for (String store: map.keySet()) {
            if (myid.equals(map.get(store))) {
                stores.add(store);
            }
        }
        return stores;
    }

    /**
     * get stores visible to users
     * @param email -> current user
     * @return list of stores
     * @throws Exception -> key
     */
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
    //add actionListeners
    public void addActionListeners() {
        //block a user from sending message to current user
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
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //view a conversation
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String email;
                    //email = email.split(" ")[0];
                    String store = (String) storeList.getSelectedValue();
                    if (store.indexOf("<") != -1) {
                        store = storeHTMLRemover(store);
                    } else {
                    }
                    if (role == Role.Customer) {
                        //ID OR EMAIL
                        email = map.get(store);

                    } else {
                        email = (String) people.getSelectedValue();
                    }

                    if (email == null) {
                        String msg = "Please select a user to message!";
                        JOptionPane.showMessageDialog(null, msg, "Alert", JOptionPane.ERROR_MESSAGE);
                    }
                    if (email.contains("<")) {
                        email = storeHTMLRemover(email);
                    }
                    if (email != null && store != null) {
                        String msg = "Trying to view a conversation with " + email + " with " + store;
                        JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                        try {
                            MessageGUI gui = new MessageGUI(board, "view", email, (String) translator.query(new Query("User", "getEmail")), store, PeopleView.this);
                            running = false;
                            gui.show();
                            String self = user.get("email");
                            String[] param = {self, email, store};
                            translator.query(new Query("MessageManager", "updateReadStatusSelf", param));
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(null, e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //become invisible to a user
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
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        //go back to previous frame (MainMenuGUI)
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //will change to mainGUI if it is uploaded
                running = false;
                MainMenuGUI gui = new MainMenuGUI(board, user);
                gui.show();
            }
        });
        //searchbar for finding users
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
    /**
     * run PeopleView, create frame and run focusListeners, etc.
     */
    public void run() {
        createAndAdd();
        setFrame();
        initializeNotifs();
        addActionListeners();
        if (role == Role.Seller) {
            storeList.setListData(map.keySet().toArray(new String[0]));
        }
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
                String user = (String) people.getSelectedValue();
                if (role == Role.Customer) {
                    try {
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    if (user != null) {
                        if (notifications == null) {
                            return;
                        }
                        if (user.contains("<")) {
                            user = storeHTMLRemover(user);
                        }
                        updateStoreUI(user);
                    }
                }
            }
        });
        //list of stores
        storeList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //STORE NAME MUST NOT HAVE A SPECIAL CHARACTER
            }
        });
        //people.setBackground(Color.GREEN);
        content.add(convPane, BorderLayout.CENTER);

    }
    //update notifications
    private class UpdateNotifications implements Runnable {

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            pcs.addPropertyChangeListener(pcl);
        }

        @Override
        public void run() {
            int i = 0;
            while (running) {
                i++;
                System.out.println(i);
                try {
                    HashMap<String, Boolean> newNotifications = (HashMap<String, Boolean>) translator.query(new Query("MessageManager", "getReadStatus", user.get("email")));
                    if (!notifications.equals(newNotifications)) {
                        pcs.firePropertyChange("updated", notifications, newNotifications);
                    }
                } catch (Exception e) {
                    
                } finally {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        
                    }
                }
            }
        }
    }
    //update notifications
    private void updateNotifications() {
        UpdateNotifications updateNotifications = new UpdateNotifications();
        updateNotifications.addPropertyChangeListener(this);
        running = true;
        new Thread(updateNotifications).start();
    }
    //initialize notifications
    public void initializeNotifs() {
        try {
            notifications = (HashMap<String, HashMap<String, Boolean>>) translator.query(new Query("MessageManager",
                    "getReadStatus", new Object[]{user.get("email")}));
            updateUserUI();
            if (role == Role.Customer) {
                checkStoreNotification();
            }
        } catch (Exception e) {
            ; // idk what to do here yet.
        }
    }

    /**
     * search for a user
     */
    public void searchUser() {
//        for (String content: users) {
//            System.out.println(content);
//        }
        if (searchBar.getText().equals("Search...") || searchBar.getText().isEmpty()) {
            //JOptionPane.showMessageDialog(null, "Please enter an email!", "Alert", JOptionPane.ERROR_MESSAGE);
            people.setListData(users.toArray());
            people.updateUI();
            initializeNotifs();
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

    /**
     * search for a store
     */
    public void searchStore() {
        if (storeSearchBar.getText().equals("Search stores...") || storeSearchBar.getText().isEmpty()) {
            //JOptionPane.showMessageDialog(null, "Please enter an email!", "Alert", JOptionPane.ERROR_MESSAGE);
            storeList.setListData(map.keySet().toArray());
            storeList.updateUI();
            initializeNotifs();
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

    /**
     * change notifications
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("updated")) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    notifications = (HashMap<String, HashMap<String, Boolean>>) evt.getNewValue();
                    updateUserUI();
                    if (role == Role.Customer) {
                        checkStoreNotification();
                    }
                }

            });
        }
    }
}