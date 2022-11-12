import java.lang.reflect.Array;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

//dashboard class
public class Dashboard {
    private final String DATABASE_SECTION_STRING = ":";
    private final String USER_SPLIT_STRING = "-";
    private final String ARRAY_SPLITTER = ":::";
    private ArrayList<ArrayList<String[]>> allConversations;
    private ArrayList<ArrayList<String[]>> myConversations;
    private Role role;
    private String id;
    private String email;
    private HashMap<String, String> userdata;

    private File textDatabase;

    public Dashboard(String email, String msgDatabaseLocation) {
        this.email = email;
        Database database = new Database("UserDatabase.txt");
        loadUserFromDatabase(email, database);
        allConversations = new ArrayList<>();
        myConversations = new ArrayList<>();
    }

    //sets up role from the userDatabase
    private void loadUserFromDatabase(String email, Database database) {
        HashMap<String, String> map = database.get("email", email);
        if (map.get("role").equals("Seller")) {
            //userSeller = new Seller(email);
            role = Role.Seller;
        } else if (map.get("role").equals("Customer")){
            //userCustomer = new Customer(email);
            role = Role.Customer;
        } else {
            System.out.println("DATABASE ERROR");
        }
        id = map.get("id");
        textDatabase = new File("history/"+ id + "-messageHistory.txt");
        userdata = map;
        System.out.println("Successfully loaded!");
    }

    //return int[2] data where data[0] = message customer sent and data[1] = message seller sent from history txt file
    public int[] getMessageData(ArrayList<String[]> conversation) {
        //messageData[0] = # customer sent && messageData[1] = # seller sent
        int[] messageData = new int[2];
        int customerSent = 0;
        int sellerSent = 0;
        for (int i = 0 ; i < conversation.size(); i++) {
            String[] msg = conversation.get(i);
            if (i == 0) {
                continue;
            }
            if (msg[0].equals(id)) {
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

    //get other user's name with conversation list
    public String getOtherName(ArrayList<String[]> conversation) {
        String name = "";
        if (conversation.get(0)[0].equals(id)) {
            name = conversation.get(0)[1];
        } else {
            name = conversation.get(0)[0];
        }
        return name;
    }

    //remove special characters from word
    public String removeSpecialChar(String word) {
        String result = "";
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) > 64 && word.charAt(i) <= 122) {
                result = result + word.charAt(i);
            }
        }
        return result;
    }
    //finds the most common word from conversation(excluding special characters)
    public String findMostCommonWord(ArrayList<String[]> conversation) {
        HashMap<String, Integer> map = new HashMap<>();
        for (String[] msg: conversation) {
            String[] words = msg[1].split(" ");
            for (String word: words) {
                word = removeSpecialChar(word);
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

    //print the statistics of the current user
    public void printMyStatistic() {
        //System.out.println(myConversations.size());
        if (role == Role.Customer) {
            for (ArrayList<String[]> conv : myConversations) {
                int[] data = getMessageData(conv);
                System.out.printf("Store name: %s\n", "placeholder");
                System.out.printf("Seller name: %s\n", getOtherName(conv));
                System.out.printf("Message Sent: %d\n", data[0]);
                System.out.printf("Message Received: %d\n\n", data[1]);
            }
        } else {
            for (ArrayList<String[]> conv : myConversations) {
                int[] data = getMessageData(conv);
                System.out.printf("Store name: %s\n", "placeholder");
                System.out.printf("Customer name: %s\n", getOtherName(conv));
                System.out.printf("Message Received: %d\n", data[0]);
                System.out.printf("Most Common Word: %s\n\n", findMostCommonWord(conv));
            }
        }

    }

    // read database and store conversation data
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
                ArrayList<String[]> conversation = new ArrayList<>();
                String path = "history/"+ line + "-messageHistory.txt";
                String[] users = new String[2];
                if (role == Role.Seller) {
                    users[0] = line;
                    users[1] = id;
                } else {
                    users[0] = id;
                    users[1] = line;
                }
                conversation.add(users);
                line = bfr.readLine();
                if (line == null) {
                    break;
                }
                while (!line.equals("#####")) {
                    String[] chart = line.split("\\|\\|\\|\\|\\|");
                    //System.out.println(line);
                    String[] message = new String[2];
                    String name = chart[1];
                    String msg = chart[0];
                    message[0] = name;
                    message[1] = msg;
                    conversation.add(message);
                    line = bfr.readLine();
                    if (line == null) {
                        break;
                    }
                }
                ArrayList<String> messages = readDatabaseOther(path);
                String recipient = "";
                for (String msg: messages) {
                    String[] message = new String[2];
                    message[0] = recipient;
                    message[1] = msg;
                    conversation.add(message);
                }
                myConversations.add(conversation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database Error!");
        }
    }
    public ArrayList<String> readDatabaseOther(String path) {
        File f = new File(path);
        FileReader fr;
        BufferedReader bfr;
        try {
            fr = new FileReader(f);
            bfr = new BufferedReader(fr);
            ArrayList<String> messages = new ArrayList<>();
            while(true) {
                String line = bfr.readLine();
                if (line == null) {
                    break;
                }
                String[] users = new String[2];
                if (line.equals(id)) {
                    line = bfr.readLine();
                    while (!line.equals("#####")) {
                        String[] chart = line.split("\\|\\|\\|\\|\\|");
                        messages.add(chart[0]);
                        line = bfr.readLine();
                        if (line == null) {
                            break;
                        }
                    }
                }
                line = bfr.readLine();
                if (line == null) {
                    break;
                }
            }
            return messages;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database Error!");
        }
        return null;
    }

    //dashboard menu

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
                "\n3. sort by lowest message received" +
                "\n4. sort by highest message received" +
                "\n5. go back to previous menu";
        String SORT_MESSAGE1 = "How do you want to sort? " +
                "\n1. sort by alphabetical order " +
                "\n2. sort by alphabetical backwards" +
                "\n3. go back to previous menu";
        String ERROR_MSG = "Please enter a valid number";
        boolean ongoing = true;
        while (ongoing) {
            System.out.println(MENU_MESSAGE);
            Scanner sc = new Scanner(System.in);
            String option = sc.nextLine();
            switch (option) {
                case "1":
                    boolean ongoing1 = true;
                    while (ongoing1) {
                        System.out.println(SORT_MESSAGE1);
                        String option1 = sc.nextLine();
                        switch (option1) {
                            case "1":
                                sortByAlphabet(1);
                                printMyStatistic();
                                break;
                            case "2":
                                sortByAlphabetInverse(1);
                                printMyStatistic();
                                break;
                            case "3":
                                ongoing1 = false;
                                break;
                            default:
                                System.out.println(ERROR_MSG);
                                break;
                        }
                    }
                    break;
                case "2":
                    boolean ongoing2 = true;
                    while (ongoing2) {
                        System.out.println(SORT_MESSAGE);
                        String option1 = sc.nextLine();
                        switch (option1) {
                            case "1":
                                sortByAlphabet(2);
                                printMyStatistic();
                                break;
                            case "2":
                                sortByAlphabetInverse(2);
                                printMyStatistic();
                                break;
                            case "3":
                                sortByLowestReceived();
                                printMyStatistic();
                                break;
                            case "4":
                                sortByHighestReceived();
                                printMyStatistic();
                                break;
                            case "5":
                                ongoing2 = false;
                                break;
                            default:
                                System.out.println(ERROR_MSG);
                                break;
                        }
                    }
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
            for (int i = 0; i < conversation.size(); i++) {
                if (i == 0) {
                    System.out.println(conversation.get(i)[0] + "-" + conversation.get(i)[1]);
                } else {
                    System.out.println(conversation.get(i)[0] + ": " + conversation.get(i)[1]);
                }
            }
            System.out.println("-----divider-----");
        }
    }
    //option 1 = sort store, 2 = sort other users
    //set myConversation list that sorted store/customers in alphabetical order
    public void sortByAlphabet(int option) {
        ArrayList<String> sortedList = new ArrayList<>();
        if (option == 1) {
            for (ArrayList<String[]> conversation: myConversations) {
                sortedList.add(conversation.get(0)[2]);
            }
        } else {
            if (role == Role.Seller) {
                for (ArrayList<String[]> conversation: myConversations) {
                    sortedList.add(conversation.get(0)[0]);
                }
            } else {
                for (ArrayList<String[]> conversation: myConversations) {
                    sortedList.add(conversation.get(0)[1]);
                }
            }
        }
        Collections.sort(sortedList);
        ArrayList<ArrayList<String[]>> temp = new ArrayList<>();
        for (String user: sortedList) {
            for (ArrayList<String[]> conversation: myConversations) {
                if (option == 1) {
                    if (conversation.get(0)[2].equals(user)) {
                        temp.add(conversation);
                        break;
                    }
                } else {
                    if (role == Role.Seller) {
                        if (conversation.get(0)[0].equals(user)) {
                            temp.add(conversation);
                            break;
                        }
                    } else {
                        if (conversation.get(0)[1].equals(user)) {
                            temp.add(conversation);
                        }
                    }
                }
            }
        }
        myConversations = temp;
    }
    //reverse sortByAlphabet();
    public void sortByAlphabetInverse(int option) {
        sortByAlphabet(option);
        Collections.reverse(myConversations);
    }
    //get number of message sent by users from history txt file
    public int getMessageSent(ArrayList<String[]> conversation) {
        int count = 0;
        for (int i = 1; i < conversation.size(); i++) {
            if (!conversation.get(i)[0].equals(email)) {
                count++;
            }
        }
        return count;
    }
    //sort myConversation list in a way from # highest message received to lowest.
    public void sortByHighestReceived() {
        ArrayList<String> users = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        for (ArrayList<String[]> conversation: myConversations) {
            int sent = getMessageSent(conversation);
            String otherEmail = getOtherName(conversation);
            if (map.size() == 0) {
                map.put(otherEmail, sent);
                users.add(otherEmail);
            } else {
                int index = 0;
                int size = map.size();
                for (String email: map.keySet()) {
                    if (sent > map.get(email)) {
                        map.put(otherEmail, sent);
                        users.add(index, otherEmail);
                        break;
                    }
                    if (index == size - 1) {
                        map.put(otherEmail,sent);
                        users.add(otherEmail);
                    }
                    index++;
                }
            }
        }
        ArrayList<ArrayList<String[]>> temp = new ArrayList<>();
        for (String user: users) {
            for (ArrayList<String[]> conversation: myConversations) {
                if (role == Role.Seller) {
                    if (conversation.get(0)[0].equals(user)) {
                        temp.add(conversation);
                        break;
                    }
                } else {
                    if (conversation.get(0)[1].equals(user)) {
                        temp.add(conversation);
                        break;
                    }
                }
            }
        }
        myConversations = temp;
    }
    //reverse previous method
    public void sortByLowestReceived() {
        sortByHighestReceived();
        Collections.reverse(myConversations);
    }
}
