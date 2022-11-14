import java.util.*;
import java.io.*;
/**
 * Project 4 -> Dashboard
 *
 * Allows for user to access statistics related to user's messaging as well as unique statistics for sellers
 * and customers respectively, such as the most common word used.
 *
 * @author Atharva Gupta, Cyril Sharma, Josh George, Nitin Murthy, Jacob Choi, L11
 *
 * @version November 13, 2022
 *
 */
public class Dashboard {
    private ArrayList<ArrayList<String[]>> allConversations;
    private ArrayList<ArrayList<String[]>> myConversations;
    private Role role;
    private String id;
    private String email;
    private File textDatabase;
    private Database database;

    /**
     * initializes the instance field variables
     *
     * @param email email of the user
     */
    public Dashboard(String email, String msgDatabaseLocation, Database database) {
        this.email = email;
        this.database = database;
        loadUserFromDatabase(email);
        allConversations = new ArrayList<>();
        myConversations = new ArrayList<>();
    }

    /**
     * initializes the role field to the Role Enum:
     * Seller or Customer
     *
     * @param emailString email of the user
     */
    private void loadUserFromDatabase(String emailString) {
        HashMap<String, String> map = database.get("email", emailString);
        if (map.get("role").equals("Seller")) {
            role = Role.Seller; //Seller Enum
        } else if (map.get("role").equals("Customer")) {
            role = Role.Customer; //Customer Enum
        } else {
            System.out.println("DATABASE ERROR");
        }
        id = map.get("id");
        textDatabase = new File("history/" + id + "-messageHistory.txt");

    }

    /**
     * creates a messageData integer array that contains the number of messages
     * given from the customer and seller and sotres it
     * messageData[0] is the customer messages and messageData[1]
     * is the seller messages.
     *
     * @param conversation ArrayList of the message conversation
     * @return returns the messageData integer array
     */
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
                    //increasing customer message count
                    customerSent++;
                } else {
                    //increasing seller message count
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

    /**
     * retrieves another user's name that is provided in the conversation list
     *
     * @param conversation ArrayList of the message conversation
     * @return returns name
     */
    public String getOtherName(ArrayList<String[]> conversation) {
        String name = "";

        if (conversation.get(0)[0].equals(id)) {
            if (conversation.get(0)[1].equals("No Customer")) {
                return "No Customer";
            }
            name = conversation.get(0)[1];
            name = getEmail(name);
        } else {
            if (conversation.get(0)[0].equals("No Customer")) {
                return "No Customer";
            }
            name = conversation.get(0)[0];
            name = getEmail(name);
        }
        return name;
    }

    /**
     * removes special characters from a word
     *
     * @param word String
     * @return returns result, word without special characters
     */
    public String removeSpecialChar(String word) {
        String result = "";
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) > 64 && word.charAt(i) <= 122) {
                result = result + word.charAt(i);
            }
        }
        return result;
    }

    /**
     * finds the most common word from conversation(excluding special characters)
     *
     * @param conversation ArrayList String[] containing conversation split by lines in a String array
     * @return returns word that occurs most often
     */
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

    /**
     * returns the 2nd element of the String array of the first String
     * array found in the ArrayList conv.
     *
     * @param conv ArrayList String[] containing conversation
     * @return String
     */
    public String getStoreName(ArrayList<String[]> conv) {
        return conv.get(0)[2];
    }

    /**
     * print the statistics of the current user
     */
    public void printMyStatistic() {
        if (role == Role.Customer) {
            for (ArrayList<String[]> conv : myConversations) {
                int[] data = getMessageData(conv);
                System.out.printf("Store name: %s\n", getStoreName(conv));
                System.out.printf("Seller name: %s\n", getOtherName(conv));
                System.out.printf("Message Sent: %d\n", data[1]);
                System.out.printf("Message Received: %d\n\n", data[0]);
            }
        } else if (role == Role.Seller) {
            for (ArrayList<String[]> conv : myConversations) {
                if (getOtherName(conv).equals("No Customer")) {
                    System.out.printf("Store name: %s\n", getStoreName(conv));
                    System.out.println("No customers\n");
                } else {
                    int[] data = getMessageData(conv);
                    System.out.printf("Store name: %s\n", getStoreName(conv));
                    System.out.printf("Customer name: %s\n", getOtherName(conv));
                    System.out.printf("Message Received: %d\n", data[1]);
                    if (data[0] == 0) {
                        System.out.println();
                    } else {
                        System.out.printf("Most Common Word: %s\n\n", findMostCommonWord(conv));
                    }
                }
            }
        }

    }

    /**
     * gets the store from database and adds the data to the ArrayList
     * conv.
     */
    public void getStoreFromDatabase() {
        File f = new File("Stores.txt");
        FileReader fr;
        BufferedReader bfr;
        try {
            fr = new FileReader(f);
            bfr = new BufferedReader(fr);
            while (true) {
                String line = bfr.readLine();
                if (line == null) {
                    break;
                }
                String[] info = line.split("-");
                if (email.equals(info[1])) {
                    if (!checkStoreExist(info[0])) {
                        String[] data = new String[3];
                        data[1] = id;
                        data[0] = "No Customer";
                        data[2] = info[0];
                        ArrayList<String[]> conv = new ArrayList<>();
                        conv.add(data);
                        myConversations.add(conv);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("An error has occurred trying to read the database");
        }
    }

    /**
     * checks to see if the store exists by seeing if the name of the store is in
     * the arrayList myCOnversations
     *
     * @param name
     * @return true or false
     */
    public boolean checkStoreExist(String name) {
        for (ArrayList<String[]> conv: myConversations) {
            if (conv.get(0)[2].equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks to see if the store exists by seeing if the name of the store is
     * in the arrayList conv
     * @param conv ArrayList String[] containing conversation
     * @param name
     * @return boolean true or false
     */
    public boolean checkStoreExist(ArrayList<ArrayList<String[]>> conv, String name) {
        for (ArrayList<String[]> con: conv) {
            if (con.get(0)[2].equals(name)) {
                return true;
            }
        }
        return false;
    }
    /**
     * checks to see if the user exists in the arrayList conv
     * @param conv ArrayList String[] containing conversation
     * @param name
     * @return boolean true or false
     */
    public boolean checkUserExist(ArrayList<ArrayList<String[]>> conv, String name) {
        for (ArrayList<String[]> con: conv) {
            if ((con.get(0)[1].equals(name) || con.get(0)[0].equals(name)) && !name.equals("No Customer")) {
                return true;
            }
        }
        return false;
    }

    /**
     * read database and store conversation data
     */
    public void readDatabase() {
        FileReader fr;
        BufferedReader bfr;
        try {
            if (!textDatabase.exists()) {
                System.out.println("No data here yet!");
                return;
            }
            fr = new FileReader(textDatabase);
            bfr = new BufferedReader(fr);
            while (true) {
                String line = bfr.readLine();
                if (line == null) {
                    break;
                }
                ArrayList<String[]> conversation = new ArrayList<>();
                String path = "history/" + line + "-messageHistory.txt";
                String[] users = new String[3];
                users[2] = "";
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
                    String[] message = new String[2];
                    String name = chart[1];
                    String msg = chart[0];
                    message[0] = name;
                    message[1] = msg;
                    if (conversation.get(0)[2].equals("")) {
                        String[] conv = conversation.get(0);
                        conv[2] = chart[4].substring(0, chart[4].indexOf("-----"));
                        conversation.set(0, conv);
                    }
                    conversation.add(message);
                    line = bfr.readLine();
                    if (line == null) {
                        break;
                    }
                }
                myConversations.add(conversation);
            }
            getStoreFromDatabase();
        } catch (Exception e) {
            System.out.println("Database Error!");
        }
    }
    /**
     * dashboard menu, user interface to use dashboard
     */
    public void presentDashboard(Scanner sc) {
        if (!textDatabase.exists()) {
            return;
        }
        String menuMessage = "what do you want to do? " +
                "\n1. sort stores";

        if (role == Role.Seller) {
            menuMessage += "\n2. sort customers" +
                    "\n3. quit";
        } else {
            menuMessage += "\n2. sort sellers" +
                    "\n3. quit";
        }

        String sortMessage = "How do you want to sort? " +
                "\n1. sort by alphabetical order " +
                "\n2. sort by alphabetical backwards" +
                "\n3. sort by lowest message received" +
                "\n4. sort by highest message received" +
                "\n5. go back to previous menu";
        String sortMessage1 = "How do you want to sort? " +
                "\n1. sort by alphabetical order " +
                "\n2. sort by alphabetical backwards" +
                "\n3. go back to previous menu";
        String errorMsg = "Please enter a valid number";
        boolean ongoing = true;
        while (ongoing) {
            System.out.println(menuMessage);
            String option = sc.nextLine();
            switch (option) {
                case "1":
                    boolean ongoing1 = true;
                    while (ongoing1) {
                        System.out.println(sortMessage1);
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
                                System.out.println(errorMsg);
                                break;
                        }
                    }
                    break;
                case "2":
                    boolean ongoing2 = true;
                    while (ongoing2) {
                        System.out.println(sortMessage);
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
                                System.out.println(errorMsg);
                                break;
                        }
                    }
                    break;
                case "3":
                    ongoing = false;
                    break;
                default:
                    System.out.println(errorMsg);
                    break;
            }
        }
    }

    /**
     * dashboard menu, user interface to use dashboard
     */
    public String getEmail(String idString) {
        if (idString.equals("No Customer")) {
            return "No Customer";
        }
        HashMap<String, String> map = database.get("id", idString);
        return map.get("email");
    }

    /**
     * get the ID of a user from their email
     * @param emailString
     * @return ID
     */
    public String getID(String emailString) {
        if (emailString.equals("No Customer")) {
            return "No Customer";
        }
        HashMap<String, String> map = database.get("email", emailString);
        return map.get("id");
    }

    /**
     * print out a conversation between the user and someone else
     */
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

    /**
     * Set the myConversation list to store stores/customers in alphabetical order
     * @param option int (1=store, 2=sort other users)
     */
    public void sortByAlphabet(int option) {
        ArrayList<String> sortedList = new ArrayList<>();
        if (option == 1) {
            for (ArrayList<String[]> conversation: myConversations) {
                sortedList.add(conversation.get(0)[2]);
            }
        } else {
            if (role == Role.Seller) {
                for (ArrayList<String[]> conversation: myConversations) {
                    sortedList.add(getEmail(conversation.get(0)[0]));
                }
            } else {
                for (ArrayList<String[]> conversation: myConversations) {
                    sortedList.add(getEmail(conversation.get(0)[1]));
                }
            }
        }
        Collections.sort(sortedList);
        ArrayList<ArrayList<String[]>> temp = new ArrayList<>();
        for (String user: sortedList) {
            for (ArrayList<String[]> conversation: myConversations) {
                if (option == 1) {
                    if (conversation.get(0)[2].equals(user)) {
                        if (role == Role.Customer) {
                            if (!checkUserExist(temp, conversation.get(0)[1])) {
                                temp.add(conversation);
                            } else {
                                continue;
                            }

                        } else {
                            if (!checkUserExist(temp, conversation.get(0)[0])) {
                                temp.add(conversation);
                            } else {
                                continue;
                            }
                        }
                        break;
                    }
                } else {
                    if (role == Role.Seller) {
                        if (conversation.get(0)[0].equals("No Customer")) {
                            if (!checkStoreExist(temp, conversation.get(0)[2])) {
                                temp.add(0, conversation);
                            } else {
                                continue;
                            }
                        } else {
                            if (conversation.get(0)[0].equals(getID(user))) {
                                temp.add(conversation);
                                break;
                            }
                        }
                    } else {
                        if (conversation.get(0)[1].equals(getID(user))) {
                            temp.add(conversation);
                        }
                    }
                }
            }
        }
        myConversations = temp;
    }

    /* Set the myConversation list to store stores/customers in reverse alphabetical order
     * @param option int (1=store, 2=sort other users)
     */
    public void sortByAlphabetInverse(int option) {
        sortByAlphabet(option);
        Collections.reverse(myConversations);
    }

    /**
     * Set the myConversation list to store stores/customers in alphabetical order
     * @param conversation (an arraylist of string arrays)
     * @return count, # of messages sent
     */
    public int getMessageSent(ArrayList<String[]> conversation) {
        int count = 0;
        for (int i = 1; i < conversation.size(); i++) {
            if (!conversation.get(i)[0].equals(email)) {
                count++;
            }
        }
        return count;
    }

    /**
     * sort myConversation list in a way from highest # of messages received to lowest.
     */
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
                for (String emailString: map.keySet()) {
                    if (sent > map.get(emailString)) {
                        map.put(otherEmail, sent);
                        users.add(index, otherEmail);
                        break;
                    }
                    if (index == size - 1) {
                        map.put(otherEmail, sent);
                        users.add(otherEmail);
                    }
                    index++;
                }
            }
        }
        ArrayList<ArrayList<String[]>> temp = new ArrayList<>();
        for (String user: users) {
            user = getID(user);
            for (ArrayList<String[]> conversation: myConversations) {
                if (role == Role.Seller) {
                    if (conversation.get(0)[0].equals("No Customer")) {
                        if (!checkStoreExist(temp, conversation.get(0)[2])) {
                            temp.add(0, conversation);
                        } else {
                            continue;
                        }
                    } else {
                        if (conversation.get(0)[0].equals(user)) {
                            temp.add(conversation);
                            break;
                        }
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
    /**
     * reverses the myConversations array that is modified from sortByHighestReceived() method
     */
    public void sortByLowestReceived() {
        sortByHighestReceived();
        Collections.reverse(myConversations);
    }
}
