import java.lang.reflect.Array;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

//dashboard class
public class Dashboard {
    private final String DATABASE_SECTION_STRING = "---";
    private final String ARRAY_SPLITTER = ":::";
    private HashSet<SampleUser> statistic;
    private ArrayList<SampleStore> storesStatistic;
    private SampleUser user;

    public Dashboard(SampleUser user) {
        statistic = new HashSet<>();
        storesStatistic = new ArrayList<>();
        this.user = user;
    }

    public void setUser(String username) {
        user = searchForUserByName(username);
    }
    public void updateUserStoreData() {
        user = searchForUserByName(user.getUsername());
    }


    public ArrayList<SampleCustomer> getHighestSellerReceived(String storeName) {
        SampleStore store = searchForStoreByName(storeName);
        ArrayList<SampleCustomer> sortedChart = new ArrayList<>();
        HashMap<SampleCustomer, Integer> map = store.getMessageReceivedMap();
        for (SampleCustomer customer: map.keySet()) {
            if (sortedChart.size() == 0) {
                sortedChart.add(customer);
                continue;
            }
            int size = sortedChart.size();
            for (int i = 0; i < size; i++) {
                if (map.get(sortedChart.get(i)) > map.get(customer)) {
                    sortedChart.add(i, customer);
                    break;
                }
                if (i == size-1) {
                    sortedChart.add(customer);
                }
            }
        }
        return sortedChart;
    }
    public ArrayList<SampleCustomer> getLowestSellerReceived(String storeName) {
        SampleStore store = searchForStoreByName(storeName);
        ArrayList<SampleCustomer> sortedChart = new ArrayList<>();
        HashMap<SampleCustomer, Integer> map = store.getMessageReceivedMap();
        for (SampleCustomer customer: map.keySet()) {
            if (sortedChart.size() == 0) {
                sortedChart.add(customer);
                continue;
            }
            int size = sortedChart.size();
            for (int i = 0; i < size; i++) {
                if (map.get(sortedChart.get(i)) < map.get(customer)) {
                    sortedChart.add(i, customer);
                    break;
                }
                if (i == size-1) {
                    sortedChart.add(customer);
                }
            }
        }
        return sortedChart;
    }
    public void printMyStatistic(int option) {
        System.out.println("===================================");
        if (user instanceof SampleCustomer) {
            System.out.println("Customer Name: " + user.getUsername());
            ArrayList<String> userStores = user.getStores();
            if (option == 1) {
                Collections.sort(userStores);
            }
            if (option == 2) {
                Collections.sort(userStores, Collections.reverseOrder());
            }

            for (String store: userStores) {
                SampleCustomer customer = (SampleCustomer) user;
                System.out.println("Store Info:");
                System.out.printf("Store Name: %s\n", store);
                System.out.printf("Seller Name: %s\n", searchForStoreByName(store).getOwnerName());
                System.out.printf("Number of Message Sent: %s\n", searchForStoreByName(store).getUserMessageSent(customer));
                System.out.printf("Number of Message Received: %s\n\n", searchForStoreByName(store).getUserMessageReceived(customer));
            }
        } else if (user instanceof SampleSeller) {
            System.out.println("Seller Name: " + user.getUsername());
            ArrayList<String> userStores = user.getStores();
            if (option == 1) {
                Collections.sort(userStores);
            }
            if (option == 2) {
                Collections.sort(userStores, Collections.reverseOrder());
            }
            for (String storeStr: userStores) {
                SampleSeller seller = (SampleSeller) user;
                SampleStore store = searchForStoreByName(storeStr);
                String word = store.getMostCommonWord();
                System.out.println("Store Info:");
                System.out.printf("Store Name: %s\n", store.getName());
                if (option == 3) {
                    ArrayList<SampleCustomer> sortedCustomers = getHighestSellerReceived(storeStr);
                    for (SampleCustomer customer: sortedCustomers) {
                        System.out.printf("Customers Name: %s\n", customer.getUsername());
                        System.out.printf("Number of Message Sent: %s\n", store.getMessageReceivedMap().get(customer));
                    }
                    System.out.printf("Most Common Word: %s\n\n", word);
                } else if (option == 4) {
                    ArrayList<SampleCustomer> sortedCustomers = getLowestSellerReceived(storeStr);
                    for (SampleCustomer customer: sortedCustomers) {
                        System.out.printf("Customers Name: %s\n", customer.getUsername());
                        System.out.printf("Number of Message Sent: %s\n", store.getMessageReceivedMap().get(customer));
                    }
                    System.out.printf("Most Common Word: %s\n\n", word);
                } else {
                    for (SampleCustomer customer: store.getMessageReceivedMap().keySet()) {
                        System.out.printf("Customers Name: %s\n", customer.getUsername());
                        System.out.printf("Number of Message Sent: %s\n", store.getMessageReceivedMap().get(customer));
                    }
                    System.out.printf("Most Common Word: %s\n\n", word);
                }
            }
        }

    }

    public void printStatistic() {
        for (SampleUser user: statistic) {
            System.out.println("username: " + user.getUsername());
            if (user.getStores() == null) continue;
            if (user instanceof SampleCustomer) {
                for (String store: user.getStores()) {
                    SampleCustomer customer = (SampleCustomer) user;
                    System.out.println("store: " + searchForStoreByName(store).getName());
                    System.out.println("message sent: " + searchForStoreByName(store).getUserMessageSent(customer));
                    System.out.println("message received: " + searchForStoreByName(store).getUserMessageReceived(customer));
                }
            }

        }
    }

    public void sortByHighestMessage() {

    }
    public void sortByLowestMessage() {

    }

    public SampleUser searchForUserByName(String name) {
        for (SampleUser user: statistic) {
            if (user.getUsername().equals(name)) {
                return user;
            }

        }
        return null;
    }

    public SampleStore searchForStoreByName(String name) {
        for (SampleStore store: storesStatistic) {
            if (store.getName().equals(name)) {
                return store;
            }

        }
        return null;
    }

    public SampleStore makeStoreData(String line) {
        String[] data = line.split(DATABASE_SECTION_STRING);
        SampleSeller seller;
        HashMap<SampleCustomer, Integer> numReceivedMessage = null;
        HashMap<SampleCustomer, Integer> numSentMessage  = null;
        if (!data[2].equals("null")) {
            numReceivedMessage = new HashMap<>();
            String[] dataset = data[2].split(ARRAY_SPLITTER);
            for (String str: dataset) {
                String[] userdata = str.split(";");
                numReceivedMessage.put((SampleCustomer) searchForUserByName(userdata[0]),
                                        Integer.parseInt(userdata[1]));

            }
        }
        if (!data[3].equals("null")) {
            numSentMessage= new HashMap<>();
            String[] dataset = data[3].split(ARRAY_SPLITTER);
            for (String str: dataset) {
                String[] userdata = str.split(";");
                numSentMessage.put((SampleCustomer) searchForUserByName(userdata[0]),
                        Integer.parseInt(userdata[1]));

            }
        }
        return new SampleStore(data[4], data[0], (SampleSeller) searchForUserByName(data[1]),
                               numReceivedMessage, numSentMessage);

    }
    public SampleUser makeUserData(String line) {
        String[] data = line.split(DATABASE_SECTION_STRING);
        if (data[1].equals("seller")) {
            if (data[2].equals("null")) {
                return new SampleSeller(data[0], null);
            } else {
                ArrayList<String> stores;
                String[] storeData = data[2].split(ARRAY_SPLITTER);
                stores = new ArrayList<>(Arrays.asList(storeData));
                return new SampleSeller(data[0], stores);
            }
        } else {
            if (data[2].equals("null")) {
                return new SampleCustomer(data[0], null);
            } else {
                ArrayList<String> stores;
                String[] storeData = data[2].split(ARRAY_SPLITTER);
                stores = new ArrayList<>(Arrays.asList(storeData));
                return new SampleCustomer(data[0], stores);
            }
        }
    }
    public void readAndUpdate() {
        //process userDatabase
        File f = new File("SampleUserDatabase.txt");
        FileReader fr;
        BufferedReader bfr;
        try {
            fr = new FileReader(f);
            bfr = new BufferedReader(fr);
            while(true) {
                String line = bfr.readLine();
                if (line == null) {
                    break;
                }
                statistic.add(makeUserData(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database Error!");
        }

        //process storeDatabase
        File file = new File("SampleStoreDatabase.txt");
        FileReader frr;
        BufferedReader bfrr;
        try {
            frr = new FileReader(file);
            bfrr = new BufferedReader(frr);
            while(true) {
                String line = bfrr.readLine();
                if (line == null) {
                    break;
                }
                storesStatistic.add(makeStoreData(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database Error!");
        }


    }
}
