import java.io.*;
import java.util.ArrayList;

public class Customer {
    private Database db;
    String name;
    public Customer(String name) {
        db = new Database("UserDatabase.txt");
        this.name = name;
    }
    public void viewSellers() throws InvalidUserException, IOException {
        MessageManager manager = new MessageManager();
        BufferedReader br = new BufferedReader(new FileReader("stores.txt"));
        String line = br.readLine();
        while(line != null) {
            System.out.println(line);
        }
    }
    public void selectSeller(String username, String message) throws InvalidUserException, IOException {
        MessageManager manager = new MessageManager();
        ArrayList<String> sellers = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader("stores.txt"));
        String line = br.readLine();
        while(line != null) {
            sellers.add(line);
        }
        for (String seller: sellers) {
            if (seller.equals(username)) {
                manager.messageUser(this.name, username, message);
            }
        }
    }

}
