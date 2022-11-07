import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Customer {
    private Database db;
    String name;
    public Customer(String name) {
        db = new Database("UserDatabase.txt");
        this.name = name;
    }
    //Customer can see who they can send messages to (list of stores)
    public void viewStores() throws InvalidUserException, IOException {
        //Will print store and seller
        MessageManager manager = new MessageManager();
        BufferedReader br = new BufferedReader(new FileReader("stores.txt"));
        String line = br.readLine();
        while(line != null) {
            System.out.println(line);
        }
        //In main interface, user will select the STORE, this should then lead to the user calling the selectSeller method
    }
    //Customer can choose a store and send a message to the seller
    public void selectSeller(String store, String message) throws InvalidUserException, IOException {
        MessageManager manager = new MessageManager();
        ArrayList<String> stores = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader("stores.txt"));
        String line = br.readLine();
        while(line != null) {
            stores.add(line);
            line = br.readLine();
        }
        for (int i = 0; i < stores.size(); i++) {
            if (stores.get(i).contains(store)) {
                String seller = Arrays.toString(stores.get(i).split("-", 1));
                manager.messageUser(this.name, seller, message);
            }
        }
    }

}
