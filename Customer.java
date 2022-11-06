import java.io.*;
import java.util.ArrayList;

public class Customer {
    private Database db;
    String name;
    public Customer(String name) {
        db = new Database("UserDatabase.txt");
        this.name = name;
    }
    public void viewSellers() throws InvalidUserException {
        MessageManager manager = new MessageManager();
        ArrayList<String> sellers = manager.getNames("Seller");
        for (String seller: sellers) {
            System.out.println(seller);
        }
    }
    public void selectSeller(String username, String message) throws InvalidUserException {
        MessageManager manager = new MessageManager();
        ArrayList<String> sellers = manager.getNames("Seller");
        for (String seller: sellers) {
            if (seller.equals(username)) {
                manager.messageUser(this.name, username, message);
            }
        }
    }

}
