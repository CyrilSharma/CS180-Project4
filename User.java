import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    //Instance fields
    private String email;
    private boolean hasAccount;
    private String password;
    private String role;
    private Database db;
    private ArrayList<String> stores;
    private MessageManager manager;

    private String id; //user's personal ID
    private String splitter = "-----";
    private int splitVal = 0;
    //creating an account
    public User(String email, String password, String role, MessageManager manager, Database db) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.hasAccount = true;
        this.manager = manager;
        this.db = db;
        stores = new ArrayList<>();
        this.id = db.get("email", this.email).get("id");
    }

    //deleting an account
    public void deleteAccount() {
        this.email = null;
        this.password = null;
        this.role = null;
        this.hasAccount = false;
    }

    //edit an account
    public void editAccount(String username, String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
    //checks if the account exists
    public boolean accountExists() {
        return this.hasAccount;
    }

    //This method allows the user, if customer, to view the list of stores
    //if the customer is not blocked, they can view the list of stores
    public void viewStores() {
        ArrayList<HashMap<String, String>> getSellers = db.getSelection("role", Role.Seller.toString());
        if (this.role.toLowerCase().equals(Role.Customer.toString().toLowerCase())) {
            System.out.println("Stores:");
            for (HashMap<String, String> sellerBlocked : getSellers) {
                if (!sellerBlocked.get("blocked").contains(id) && !db.get("id", id).get("blocked").contains(sellerBlocked.get("id")) && !db.get("id", id).get("invisible").contains(sellerBlocked.get("id"))) {
                    System.out.println(sellerBlocked.get("email"));
                } else if (!db.get("id", id).get("blocked").contains(sellerBlocked.get("id")) && !sellerBlocked.get("invisible").contains(id)){
                    System.out.println(sellerBlocked.get("email") + " (blocked)");
                } else if (db.get("id", id).get("blocked").contains(sellerBlocked.get("id")) && !sellerBlocked.get("invisible").contains(id)) {
                    System.out.println(sellerBlocked.get("email") + " (invisible)");
                }
            }
        }
    }
    //Customer can select a store and then send a message to the associated seller
    //the customer can go ahead and select the stores if they are not blocked
    //an InvalidUser message will be thrown if the user is blocked
    public void selectStore(String store, String message) throws InvalidUserException, IOException {
        boolean blocked = false;
        ArrayList<HashMap<String, String>> getSellers = db.getSelection("role", "customer");
        for (HashMap<String, String> sellerBlocked : getSellers) {
            if (sellerBlocked.get("blocked").contains(id)) {
                blocked = true;
            }
        }
        if (this.role.toLowerCase().equals("customer") && !blocked) {
            ArrayList<String> stores = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new FileReader("stores.txt"));
            String line = br.readLine();
            while (line != null) {
                stores.add(line);
                line = br.readLine();
            }
            for (int i = 0; i < stores.size(); i++) {
                if (stores.get(i).contains(store)) {
                    String seller = stores.get(i).split("-")[1];
                    manager.messageUser(db.get("email", this.email).get("id"), db.get("email", seller).get("id"), message);
                }
            }
        } else if (blocked) {
            throw new InvalidUserException(this.id + "has been blocked");
        }
    }

    //Implementing Seller
    //contains the blocked functionality where the user cannot input the store if the seller is blocked
    public void addStores(String store) throws InvalidUserException {
        if (this.role.toLowerCase().equals("seller")) {
            boolean blocked = false;
            ArrayList<HashMap<String, String>> getSellers = db.getSelection("role", "seller");
            for (HashMap<String, String> sellerBlocked : getSellers) {
                if (sellerBlocked.get("blocked").contains(id)) {
                    blocked = true;
                }
            }
            if (!blocked) {
                try {
                    File f = new File("stores.txt");
                    FileOutputStream fos = new FileOutputStream(f, false);
                    PrintWriter pw = new PrintWriter(fos);
                    String sellerID = db.get("email", this.email).get("id");
                    if (splitVal == 0) {
                        pw.write(sellerID + splitter + "\n");
                        splitVal += 1;
                    }
                    pw.write(store + "\n");
                    stores.add(store);
                    pw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (blocked) {
                throw new InvalidUserException(this.id + "has been blocked");
            }
        }
    }
    //Seller views list of customers that they can send messages to
    public void viewCustomers() {
        ArrayList<HashMap<String, String>> getCustomers = db.getSelection("role", Role.Customer.toString());
        if (this.role.toLowerCase().equals(Role.Seller.toString().toLowerCase())) {
            System.out.println("Customers:");
            HashMap<String, String> thisUser = db.get("id", id);
            for (HashMap<String, String> sellerBlocked : getCustomers) {
                if (!sellerBlocked.get("invisible").contains(id) && !thisUser.get("blocked").contains(sellerBlocked.get("id")) && !thisUser.get("invisible").contains(sellerBlocked.get("id"))) {
                    System.out.println(sellerBlocked.get("email"));
                } else if (!thisUser.get("blocked").contains(sellerBlocked.get("id")) && !sellerBlocked.get("invisible").contains(id)){
                    System.out.println(sellerBlocked.get("email") + " (invisible)");
                } else if (thisUser.get("blocked").contains(sellerBlocked.get("id")) && !sellerBlocked.get("invisible").contains(id)) {
                    System.out.println(sellerBlocked.get("email") + " (blocked)");
                }
            }
        }
    }
    //get list of stores
    public ArrayList<String> getStores() {
        return stores;
    }

    //Seller can choose a customer to send a message to
    public void selectCustomer(String recipient, String message) throws InvalidUserException, IOException {
        boolean blocked = false;
        ArrayList<HashMap<String, String>> getSellers = db.getSelection("role", "seller");
        for (HashMap<String, String> sellerBlocked : getSellers) {
            if (sellerBlocked.get("blocked").contains(id)) {
                blocked = true;
            }
        }
        if (this.role.toLowerCase().equals("seller") && !blocked) {
            ArrayList<String> customers = manager.getNames("Customer");
            for (String customer : customers) {
                if (email.equals(customer)) {
                    manager.messageUser(db.get("email", this.email).get("id"), db.get("email", recipient).get("id"), message);
                }
            }
        } else if (blocked) {
            throw new InvalidUserException(this.id + "has been blocked");
        }
    }
}
