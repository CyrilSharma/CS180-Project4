import java.lang.reflect.Array;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

//dashboard class
public class Dashboard {
    private final String DATABASE_SECTION_STRING = ":";
    private final String USER_SPLIT_STRING = "-";
    private final String ARRAY_SPLITTER = ":::";
    private ArrayList<Seller> sellers;
    private ArrayList<Customer> customers;
    private ArrayList<ArrayList<String[]>> allConversations;
    private ArrayList<ArrayList<String[]>> myConversations;
    private Seller userSeller;
    private Customer userCustomer;
    private Role role;
    private String username;
    private HashMap<String, String> userdata;

    private File textDatabase;

    public Dashboard(String username, String msgDatabaseLocation) {
        this.username = username;
        Database database = new Database("TestDatabase.txt");
        loadUserFromDatabase(username, database);
        textDatabase = new File(msgDatabaseLocation);
        allConversations = new ArrayList<>();
        myConversations = new ArrayList<>();
    }

    private void loadUserFromDatabase(String username, Database database) {
        HashMap<String, String> map = database.get("username", username);
        if (map.get("role").equals("Seller")) {
            //userSeller = new Seller(username);
            role = Role.Seller;
        } else if (map.get("role").equals("Customer")){
            //userCustomer = new Customer(username);
            role = Role.Customer;
        } else {
            System.out.println("DATABASE ERROR");
        }
        userdata = map;
        System.out.println("Successfully loaded!");
    }

    public int[] getMessageData(ArrayList<String[]> conversation) {
        //messageData[0] = # customer sent && messageData[1] = # seller sent
        int[] messageData = new int[2];
        int customerSent = 0;
        int sellerSent = 0;
        for (String[] msg: conversation) {
            if (msg[0].equals(username)) {
                if (role == Role.Customer) {
                    customerSent++;
                } else {
                    sellerSent++;
                }
            } else {
                if (role == Role.Customer) {
                    sellerSent++;
                } else {
                    customerSent++;
                }
            }
        }
        messageData[0] = customerSent;
        messageData[1] = sellerSent;
        return messageData;
    }

    public String getOtherName(ArrayList<String[]> conversation) {
        //TODO: fix this with msg id in case other person doesn't send any msg
        String name = "";
        for (String[] msg: conversation) {
            if (!msg[0].equals(username)) {
                name = msg[0];
            }
        }
        return name;
    }

    public String findMostCommonWord(ArrayList<String[]> conversation) {
        //TODO: replace special characters with ""
        HashMap<String, Integer> map = new HashMap<>();
        for (String[] msg: conversation) {
            String[] words = msg[1].split(" ");
            for (String word: words) {
                if (map.containsKey(word)) {
                    map.put(word, map.get(word) + 1);
                } else {
                    map.put(word, 1);
                }
            }
        }
        String word = "";
        int num = 0;
        for (String key: map.keySet()) {
            if (map.get(key) > num) {
                word = key;
                num = map.get(key);
            }
        }
        return word;
    }

    public void printMyStatistic() {
        if (role == Role.Customer) {
            for (ArrayList<String[]> conv : myConversations) {
                int[] data = getMessageData(conv);
                System.out.printf("Seller name: %s\n", getOtherName(conv));
                System.out.printf("Message Sent: %d\n", data[0]);
                System.out.printf("Message Received: %d\n", data[1]);
            }
        } else {
            for (ArrayList<String[]> conv : myConversations) {
                int[] data = getMessageData(conv);
                System.out.printf("Customer name: %s\n", getOtherName(conv));
                System.out.printf("Message Received: %d\n", data[0]);
                System.out.printf("Most Common Word: %s\n", findMostCommonWord(conv));
            }
        }

    }

    public void readDatabase() {
        FileReader fr;
        BufferedReader bfr;
        try {
            fr = new FileReader(textDatabase);
            bfr = new BufferedReader(fr);
            while(true) {
                String line = bfr.readLine();
                if (line == null) {
                    break;
                }
                String[] users = line.split(USER_SPLIT_STRING);
                line = bfr.readLine();
                ArrayList<String[]> conversation = new ArrayList<>();
                if (line == null) {
                    break;
                }
                while (!line.equals("-")) {
                    String[] chart = line.split(DATABASE_SECTION_STRING);
                    //System.out.println(line);
                    String[] message = new String[2];
                    String name = chart[0].substring(chart[0].indexOf(">") + 1);
                    String msg = chart[1].substring(1);
                    message[0] = name;
                    message[1] = msg;
                    conversation.add(message);
                    line = bfr.readLine();
                    if (line == null) {
                        break;
                    }
                }
                allConversations.add(conversation);
                if (users[0].equals(username) || users[1].equals(username)) {
                    myConversations.add(conversation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database Error!");
        }
    }

    public void presentDashboard() {
        String MENU_MESSAGE = "what do you want to do? " +
                              "\n1. sort stores";

        if (role == Role.Seller) {
            MENU_MESSAGE += "\n2. sort customers" +
                            "\n3. quit";
        } else {
            MENU_MESSAGE += "\n2. sort sellers" +
                            "\n3. quit";
        }

        String SORT_MESSAGE = "How do you want to sort? " +
                "\n1. sort by alphabetical order " +
                "\n2. sort by alphabetical backwards" +
                "\n3. sort by lowest message sent" +
                "\n4. sort by highest message sent" +
                "\n5.quit";
        String ERROR_MSG = "Please enter a valid number";
        boolean ongoing = true;
        while (ongoing) {
            System.out.println(MENU_MESSAGE);
            Scanner sc = new Scanner(System.in);
            String option = sc.nextLine();
            switch (option) {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    ongoing = false;
                    break;
                default:
                    System.out.println(ERROR_MSG);
                    break;
            }
        }
    }

    public void printConversation() {
        for (ArrayList<String[]> conversation: allConversations) {
            for (String[] message: conversation) {
                System.out.println(message[0] + ": " + message[1]);
            }
            System.out.println("-----divider-----");
        }
    }




}
