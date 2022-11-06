import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Seller {
    String name;
    private Database db;
    public Seller(String name) {
        db = new Database("UserDatabase.txt");
        this.name = name;
    }
    public void addStore(String store) {
        try {
            File f = new File("stores.txt");
            FileOutputStream fos = new FileOutputStream(f, false);
            PrintWriter pw = new PrintWriter(fos);
            pw.write(store);
            pw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void viewCustomers() throws InvalidUserException {
        MessageManager manager = new MessageManager();
        ArrayList<String> customers = manager.getNames("Customer");
        for(String customer: customers) {
            System.out.println(customer);
        }
    }

    public void selectCustomer(String username, String message) throws InvalidUserException {
        MessageManager manager = new MessageManager();
        ArrayList<String> customers = manager.getNames("Customer");
        for (String customer : customers) {
            if (username.equals(customer)) {
                manager.messageUser(this.name, username, message);
            }
        }
    }
}
